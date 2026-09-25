package com.itheima.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.service.ClothesService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/allClothes", "/allStyles", "/allTypes", "/clothesByName", "/clothDetails", "/searchClothes", "/getAllClothesData"})
public class ClothesServlet extends HttpServlet {

    private final ClothesService clothesService = new ClothesService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        String servletPath = request.getServletPath();

        switch (servletPath) {
            case "/getAllClothesData":
                mapper.writeValue(writer, clothesService.getAllClothesData());
                break;
            case "/allClothes":
                String style = request.getParameter("style");
                String typeName = request.getParameter("type");
                mapper.writeValue(writer, clothesService.getAllClothes(style, typeName));
                break;
            case "/allStyles":
                mapper.writeValue(writer, clothesService.getAllStyles());
                break;
            case "/allTypes":
                mapper.writeValue(writer, clothesService.getAllTypes());
                break;
            case "/clothesByName":
                String name = request.getParameter("clothesName");
                mapper.writeValue(writer, clothesService.getClothesByName(name));
                break;
            case "/clothDetails":
                int clothId = Integer.parseInt(request.getParameter("clothId"));
                mapper.writeValue(writer, clothesService.getClothDetails(clothId));
                break;
            case "/searchClothes":
                String keyword = request.getParameter("clothName");
                String xstyle = request.getParameter("style");
                String xtype = request.getParameter("typeName");
                mapper.writeValue(writer, clothesService.getClothesByParams(keyword, xstyle, xtype));
                break;
        }
    }
}
