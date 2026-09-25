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

@WebServlet(urlPatterns = {"/getAllUser", "/delUser", "/addUser", "/editUser"})
public class ManageUserServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        BufferedReader reader = request.getReader();
        String servletPath = request.getServletPath();

        switch (servletPath) {
            case "/getAllUser": {
                String searchKey = request.getParameter("searchKey");
                mapper.writeValue(writer, userService.getUserByParam(searchKey));
                break;
            }
            case "/delUser": {
                User user = mapper.readValue(reader, User.class);
                String result = userService.delUser(user.getId());
                mapper.writeValue(writer, result);
                break;
            }
            case "/addUser": {
                User user = mapper.readValue(reader, User.class);
                String result = userService.register(user);
                mapper.writeValue(writer, result);
                break;
            }
            case "/editUser": {
                User user = mapper.readValue(reader, User.class);
                // Simple update via UserDao
                try {
                    com.itheima.dao.UserDao userDao = new com.itheima.dao.UserDao();
                    int rows = userDao.updateUser(user);
                    mapper.writeValue(writer, rows > 0 ? "修改成功！" : "修改失败！");
                } catch (Exception e) {
                    e.printStackTrace();
                    mapper.writeValue(writer, "修改失败！");
                }
                break;
            }
        }
    }
}
