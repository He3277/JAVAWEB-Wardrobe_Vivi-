package com.itheima.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.Cart;
import com.itheima.service.CartService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/addToCart", "/getCartDataByUser", "/updateCartData", "/delCartData"})
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        String servletPath = request.getServletPath();

        switch (servletPath) {
            case "/addToCart": {
                Cart cart = new Cart(
                    Integer.parseInt(request.getParameter("clothId")),
                    request.getParameter("clothSize"),
                    Integer.parseInt(request.getParameter("userId"))
                );
                String result = cartService.addClothesToCart(cart);
                mapper.writeValue(writer, result);
                break;
            }
            case "/getCartDataByUser": {
                int userId = Integer.parseInt(request.getParameter("userId"));
                mapper.writeValue(writer, cartService.allCartData(userId));
                break;
            }
            case "/updateCartData": {
                int id = Integer.parseInt(request.getParameter("id"));
                int amount = Integer.parseInt(request.getParameter("amount"));
                String result = cartService.updateCartData(id, amount);
                mapper.writeValue(writer, result);
                break;
            }
            case "/delCartData": {
                Cart cart = mapper.readValue(request.getReader(), Cart.class);
                String result = cartService.delCartData(cart.getId());
                mapper.writeValue(writer, result);
                break;
            }
        }
    }
}
