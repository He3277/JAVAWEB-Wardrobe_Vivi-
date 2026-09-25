package com.itheima.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.Order;
import com.itheima.service.OrderService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet(urlPatterns = {"/addOrder", "/getOrderByUser", "/payOrder", "/receiveOrder", "/delOrderData"})
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        String servletPath = request.getServletPath();

        switch (servletPath) {
            case "/addOrder": {
                Order[] orders = mapper.readValue(request.getReader(), Order[].class);
                String result = orderService.addOrder(Arrays.asList(orders));
                mapper.writeValue(writer, result);
                break;
            }
            case "/getOrderByUser": {
                int userId = Integer.parseInt(request.getParameter("userId"));
                String status = request.getParameter("status");
                mapper.writeValue(writer, orderService.getOrderByUser(userId, status));
                break;
            }
            case "/payOrder": {
                Order order = mapper.readValue(request.getReader(), Order.class);
                String result = orderService.payOrder(order.getId());
                mapper.writeValue(writer, result);
                break;
            }
            case "/receiveOrder": {
                Order order = mapper.readValue(request.getReader(), Order.class);
                String result = orderService.receiveOrder(order.getId());
                mapper.writeValue(writer, result);
                break;
            }
            case "/delOrderData": {
                Order order = mapper.readValue(request.getReader(), Order.class);
                String result = orderService.delOrderData(order.getId());
                mapper.writeValue(writer, result);
                break;
            }
        }
    }
}
