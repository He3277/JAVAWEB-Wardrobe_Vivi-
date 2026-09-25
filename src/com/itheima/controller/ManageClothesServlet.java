package com.itheima.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.Clothes;
import com.itheima.service.ClothesService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/addClothes", "/delClothes", "/editClothes"})
public class ManageClothesServlet extends HttpServlet {

    private final ClothesService clothesService = new ClothesService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        BufferedReader reader = request.getReader();
        String servletPath = request.getServletPath();

        switch (servletPath) {
            case "/addClothes": {
                Clothes clothes = mapper.readValue(reader, Clothes.class);
                String result = clothesService.addClothes(clothes);
                mapper.writeValue(writer, result);
                break;
            }
            case "/delClothes": {
                Clothes clothes = mapper.readValue(reader, Clothes.class);
                String result = clothesService.delClothes(clothes.getId());
                mapper.writeValue(writer, result);
                break;
            }
            case "/editClothes": {
                Clothes clothes = mapper.readValue(reader, Clothes.class);
                String result = clothesService.editClothes(clothes);
                mapper.writeValue(writer, result);
                break;
            }
        }
    }
}
