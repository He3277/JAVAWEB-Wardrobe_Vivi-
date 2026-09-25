package com.itheima.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.User;
import com.itheima.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/login", "/register"})
public class LoginOrRegServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        String servletPath = request.getServletPath();
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter writer = response.getWriter();
        BufferedReader reader = request.getReader();

        if ("/register".equals(servletPath)) {
            // 注册处理
            User user = mapper.readValue(reader, User.class);
            String result = userService.register(user);
            mapper.writeValue(writer, result);

        } else if ("/login".equals(servletPath)) {
            // 登录处理
            String userInfo = request.getParameter("userInfo");
            String password = request.getParameter("password");
            boolean isAdminLogin = Boolean.parseBoolean(request.getParameter("isAdminLogin"));

            User user = userService.login(userInfo, password);

            // FIX: 如果是管理员登录但用户无管理员权限，立即返回错误，不再继续写入 user 对象
            if (isAdminLogin && user != null && user.getRole() != 1) {
                mapper.writeValue(writer, "没有权限！");
                return;  // <-- BUG FIX: 添加 return，防止继续写入 user 对象导致 JSON 格式错误
            }

            mapper.writeValue(writer, user);
        }
    }
}
