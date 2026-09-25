package com.itheima.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.Order;
import com.itheima.service.OrderService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/allOrderData", "/deliveryOrder"})
public class ManageOrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        BufferedReader reader = request.getReader();
        String servletPath = request.getServletPath();

        switch (servletPath) {
            case "/allOrderData": {
                String userName = request.getParameter("userName");
                String status = request.getParameter("status");
                mapper.writeValue(writer, orderService.getAllOrders(userName, status));
                break;
            }
            case "/deliveryOrder": {
                Order order = mapper.readValue(reader, Order.class);
                String result = orderService.updateOrderStatus(order.getId());
                mapper.writeValue(writer, result);
                break;
            }
        }
    }
}
