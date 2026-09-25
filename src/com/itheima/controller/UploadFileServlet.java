package com.itheima.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "UploadFileServlet", urlPatterns = {"/uploadFile"})
public class UploadFileServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();

        String realPath = request.getServletContext().getRealPath("/images");
        Part part = request.getPart("clothesImage");
        String fileName = part.getSubmittedFileName();

        // Generate unique filename with timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String prefix = LocalDateTime.now().format(formatter);
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = prefix + suffix;

        File uploadDir = new File(realPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        part.write(realPath + File.separator + newFileName);
        mapper.writeValue(writer, newFileName);
    }
}
