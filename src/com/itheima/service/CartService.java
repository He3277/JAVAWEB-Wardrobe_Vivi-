package com.itheima.service;

import com.itheima.dao.CartDao;
import com.itheima.model.Cart;

import java.sql.SQLException;
import java.util.List;

public class CartService {

    private final CartDao cartDao = new CartDao();

    public List<Cart> allCartData(int userId) {
        try {
            return cartDao.allCartData(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String addClothesToCart(Cart cart) {
        try {
            return cartDao.addClothesToCart(cart);
        } catch (SQLException e) {
            e.printStackTrace();
            return "添加失败";
        }
    }

    public String updateCartData(int id, int amount) {
        try {
            int rows = cartDao.updateClothData(id, amount);
            return rows > 0 ? "修改成功！" : "修改失败！";
        } catch (SQLException e) {
            e.printStackTrace();
            return "修改失败！";
        }
    }

    public String delCartData(int id) {
        try {
            return cartDao.delCartData(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return "删除失败！";
        }
    }
}
