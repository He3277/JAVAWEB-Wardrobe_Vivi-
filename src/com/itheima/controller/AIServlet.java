package com.itheima.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itheima.dao.ClothesDao;
import com.itheima.model.Clothes;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/aiChat")
public class AIServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String OLLAMA_URL = "http://localhost:11434/api/chat";
    private static final String MODEL_NAME = "qwen3:1.7b";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/event-stream;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Connection", "keep-alive");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        // 显式按UTF-8读取请求体，避免依赖容器默认编码
        String body;
        try (InputStream is = req.getInputStream()) {
            body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        JsonNode requestJson = mapper.readTree(body);

        String userMessage = requestJson.get("message").asText();
        JsonNode historyNode = requestJson.has("history") ? requestJson.get("history") : mapper.createArrayNode();

        String productContext = buildProductContext();
        String systemPrompt = buildSystemPrompt(productContext);

        ObjectNode ollamaRequest = mapper.createObjectNode();
        ollamaRequest.put("model", MODEL_NAME);
        ollamaRequest.put("stream", true);

        ArrayNode messages = mapper.createArrayNode();

        ObjectNode systemMsg = mapper.createObjectNode();
        systemMsg.put("role", "system");
        systemMsg.put("content", systemPrompt);
        messages.add(systemMsg);

        for (JsonNode histMsg : historyNode) {
            ObjectNode msg = mapper.createObjectNode();
            msg.put("role", histMsg.get("role").asText());
            msg.put("content", histMsg.get("content").asText());
            messages.add(msg);
        }

        ObjectNode userMsg = mapper.createObjectNode();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messages.add(userMsg);

        ollamaRequest.set("messages", messages);

        PrintWriter writer = resp.getWriter();
        HttpURLConnection conn = null;

        try {
            URL url = new URL(OLLAMA_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(180000);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] requestBytes = ollamaRequest.toString().getBytes(StandardCharsets.UTF_8);
                os.write(requestBytes);
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                ObjectNode errorResponse = mapper.createObjectNode();
                errorResponse.put("error", "AI服务暂时不可用，请稍后再试");
                writer.write("data: " + errorResponse.toString() + "\n\n");
                writer.flush();
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            StringBuilder fullResponse = new StringBuilder();
            boolean inThinkBlock = false;

            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;

                try {
                    JsonNode ollamaResp = mapper.readTree(line);
                    if (ollamaResp.has("message")) {
                        String content = ollamaResp.get("message").get("content").asText();

                        // Handle qwen3 think tags
                        if (content.contains("<think>")) {
                            inThinkBlock = true;
                            content = content.substring(0, content.indexOf("<think>"));
                        }
                        if (content.contains("</think>")) {
                            inThinkBlock = false;
                            content = content.substring(content.indexOf("</think>") + 8);
                        }
                        if (inThinkBlock) {
                            content = "";
                        }

                        if (!content.isEmpty()) {
                            fullResponse.append(content);
                            ObjectNode sseData = mapper.createObjectNode();
                            sseData.put("content", content);
                            sseData.put("done", false);
                            writer.write("data: " + sseData.toString() + "\n\n");
                            writer.flush();
                        }
                    }

                    if (ollamaResp.has("done") && ollamaResp.get("done").asBoolean()) {
                        ObjectNode doneData = mapper.createObjectNode();
                        doneData.put("done", true);
                        doneData.put("fullResponse", fullResponse.toString());
                        writer.write("data: " + doneData.toString() + "\n\n");
                        writer.flush();
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            }

            br.close();

        } catch (Exception e) {
            ObjectNode errorResponse = mapper.createObjectNode();
            errorResponse.put("error", "无法连接到AI服务，请确认Ollama正在运行");
            writer.write("data: " + errorResponse.toString() + "\n\n");
            writer.flush();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        ObjectNode result = mapper.createObjectNode();

        try {
            URL url = new URL("http://localhost:11434/api/tags");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            if (conn.getResponseCode() == 200) {
                result.put("status", "ok");
                result.put("model", MODEL_NAME);
            } else {
                result.put("status", "error");
            }
            conn.disconnect();
        } catch (Exception e) {
            result.put("status", "offline");
        }

        resp.getWriter().write(result.toString());
    }

    private String buildProductContext() {
        try {
            ClothesDao clothesDao = new ClothesDao();
            List<Clothes> allClothes = clothesDao.getAllClothes(null, null);

            if (allClothes == null || allClothes.isEmpty()) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            int idx = 1;
            for (Clothes c : allClothes) {
                sb.append(idx).append(". 「").append(c.getClothName()).append("」");
                sb.append(" | 类型:").append(c.getTypeName());
                sb.append(" | 风格:").append(c.getStyle());
                sb.append(" | 价格:").append(c.getPrice()).append("元");
                sb.append(" | ID:").append(c.getId());
                sb.append("\n");
                idx++;
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private String buildSystemPrompt(String productContext) {
        return "你是「小V」，Vivi Style网上衣橱的AI穿搭顾问。\n\n" +
                "你的职责：\n" +
                "1. 根据用户的喜好、场景、季节推荐穿搭方案\n" +
                "2. 基于商城内的实际商品推荐具体的购买方案\n" +
                "3. 分析用户的穿搭风格偏好，给出个性化建议\n\n" +
                "当前商城在售商品列表：\n" +
                productContext + "\n" +
                "【重要规则】\n" +
                "- 用中文回答，语气友好专业，适当使用emoji表情增加亲切感（如👗👔👠👜🧥✨💕等）\n" +
                "- 推荐商品时，必须使用格式：[商品:商品名:ID]\n" +
                "  商品名和ID必须严格对应上面列表中的数据，不要编造不存在的商品或ID\n" +
                "  例如：列表中「夏季碎花连衣裙」的ID是5，就写 [商品:夏季碎花连衣裙:5]\n" +
                "- 每次推荐可以附上价格和推荐理由\n" +
                "- 回答简洁明了，不要过于冗长\n" +
                "- 不要使用markdown格式，使用纯文本\n" +
                "- 如果用户问的不是穿搭相关问题，礼貌地引导回穿搭话题";
    }
}
