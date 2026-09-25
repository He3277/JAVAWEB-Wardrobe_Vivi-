package com.itheima.dao;

import com.itheima.model.Cart;
import com.itheima.model.Clothes;
import com.itheima.model.User;
import com.itheima.utils.DruidUtils;
import com.itheima.utils.LocalDateTimeUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class CartDao {

    public List<Cart> allCartData(int userId) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id, cloth_id AS clothId, cloth_size AS clothSize, amount, user_id AS userId, date FROM t_cart WHERE user_id = ?";
        List<Cart> cartList = qr.query(sql, new BeanListHandler<>(Cart.class), userId);

        ClothesDao clothesDao = new ClothesDao();
        UserDao userDao = new UserDao();
        for (Cart cart : cartList) {
            Clothes clothes = clothesDao.getClothById(cart.getClothId());
            cart.setClothes(clothes);
            User user = userDao.getUserById(cart.getUserId());
            cart.setUser(user);
        }
        return cartList;
    }

    public String addClothesToCart(Cart cart) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "INSERT INTO t_cart (cloth_id, cloth_size, amount, user_id, date) VALUES (?,?,?,?,?)";

        Cart existing = clothesAmount(cart.getUserId(), cart.getClothId(), cart.getClothSize());
        if (existing == null) {
            String date = LocalDateTimeUtil.formatLocalDateTime(LocalDateTime.now());
            Object[] params = {cart.getClothId(), cart.getClothSize(), 1, cart.getUserId(), date};
            int rows = qr.update(sql, params);
            return rows > 0 ? "添加成功！" : "添加失败";
        } else {
            int rows = updateClothData(existing.getId(), existing.getAmount() + 1);
            return rows > 0 ? "添加成功！" : "添加失败";
        }
    }

    public Cart clothesAmount(int userId, int clothId, String clothSize) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id, cloth_id AS clothId, cloth_size AS clothSize, amount, user_id AS userId, date FROM t_cart WHERE user_id = ? AND cloth_id = ? AND cloth_size = ?";
        return qr.query(sql, new BeanHandler<>(Cart.class), userId, clothId, clothSize);
    }

    public int updateClothData(int id, int amount) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "UPDATE t_cart SET amount = ? WHERE id = ?";
        return qr.update(sql, amount, id);
    }

    public String delCartData(int id) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "DELETE FROM t_cart WHERE id = ?";
        int rows = qr.update(sql, id);
        return rows >= 1 ? "删除成功！" : "删除失败！";
    }

    public int delUserCartData(int userId) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "DELETE FROM t_cart WHERE user_id = ?";
        return qr.update(sql, userId);
    }
}
