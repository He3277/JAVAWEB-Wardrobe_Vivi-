package com.itheima.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.dao.UserDao;
import com.itheima.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter("/*")
public class AccessFilter implements Filter {

    private final UserDao userDao = new UserDao();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        req.setCharacterEncoding("UTF-8");

        String token = request.getHeader("token");

        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            DecodedJWT jwt = JWT.decode(token);
            int userId = Integer.parseInt(jwt.getAudience().get(0));
            User user = userDao.getUserById(userId);
            if (user != null) {
                chain.doFilter(request, response);
            } else {
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                mapper.writeValue(writer, "用户不存在");
            }
        } catch (Exception e) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            mapper.writeValue(writer, "没有权限！");
        }
    }
}
