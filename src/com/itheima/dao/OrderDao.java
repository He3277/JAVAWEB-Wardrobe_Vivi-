package com.itheima.dao;

import com.itheima.model.Order;
import com.itheima.utils.DruidUtils;
import com.itheima.utils.LocalDateTimeUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    public List<Order> getOrderByParams(String userName, String status) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT t_order.id, clothes_details AS clothesDetails, price, status, user_id AS userId, user_name, time FROM t_order JOIN t_user ON user_id = t_user.id"
        );
        List<Object> params = new ArrayList<>();

        if (userName != null && !userName.isEmpty()) {
            sqlBuilder.append(" AND user_name LIKE ?");
            params.add("%" + userName + "%");
        }
        if (status != null && !status.isEmpty()) {
            sqlBuilder.append(" AND status = ?");
            params.add(status);
        }

        return qr.query(sqlBuilder.toString(), new BeanListHandler<>(Order.class), params.toArray());
    }

    public List<Order> getOrderByUser(int userId, String status) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id, clothes_details AS clothesDetails, price, status, time FROM t_order WHERE user_id = ? AND status = ?";
        return qr.query(sql, new BeanListHandler<>(Order.class), userId, status);
    }

    public int addOrder(Order order) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "INSERT INTO t_order (clothes_details, price, status, user_id, address, time) VALUES (?,?,?,?,?,?)";
        String time = LocalDateTimeUtil.formatLocalDateTime(LocalDateTime.now());
        return qr.update(sql,
                order.getClothesDetails(), order.getPrice(), order.getStatus(),
                order.getUserId(), order.getAddress(), time);
    }

    public int updateOrderStatus(int id, int oldStatus, int newStatus) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "UPDATE t_order SET status = ? WHERE status = ? AND id = ?";
        return qr.update(sql, newStatus, oldStatus, id);
    }

    public int delOrderData(int id) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "DELETE FROM t_order WHERE id = ? AND status NOT IN (1, 2)";
        return qr.update(sql, id);
    }
}
