package com.itheima.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.UserVo;
import com.itheima.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/getCurrentUser", "/updateUser"})
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        BufferedReader reader = request.getReader();
        String servletPath = request.getServletPath();

        switch (servletPath) {
            case "/getCurrentUser": {
                int id = Integer.parseInt(request.getParameter("id"));
                mapper.writeValue(writer, userService.getCurrentUser(id));
                break;
            }
            case "/updateUser": {
                UserVo userVo = mapper.readValue(reader, UserVo.class);
                String result = userService.updateUser(userVo);
                mapper.writeValue(writer, result);
                break;
            }
        }
    }
}
