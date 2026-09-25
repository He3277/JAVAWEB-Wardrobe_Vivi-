package com.itheima.service;

import com.itheima.dao.OrderDao;
import com.itheima.model.Order;

import java.sql.SQLException;
import java.util.List;

public class OrderService {

    private final OrderDao orderDao = new OrderDao();

    public List<Order> getAllOrders(String userName, String status) {
        try {
            return orderDao.getOrderByParams(userName, status);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Order> getOrderByUser(int userId, String status) {
        try {
            return orderDao.getOrderByUser(userId, status);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String addOrder(List<Order> orderList) {
        try {
            for (Order order : orderList) {
                orderDao.addOrder(order);
            }
            return "下单成功！";
        } catch (SQLException e) {
            e.printStackTrace();
            return "下单失败！";
        }
    }

    public String updateOrderStatus(int id) {
        try {
            int rows = orderDao.updateOrderStatus(id, 1, 2);
            return rows > 0 ? "发货成功！" : "发货失败！";
        } catch (SQLException e) {
            e.printStackTrace();
            return "发货失败！";
        }
    }

    public String payOrder(int id) {
        try {
            int rows = orderDao.updateOrderStatus(id, 0, 1);
            return rows > 0 ? "支付成功！" : "支付失败！";
        } catch (SQLException e) {
            e.printStackTrace();
            return "支付失败！";
        }
    }

    public String receiveOrder(int id) {
        try {
            int rows = orderDao.updateOrderStatus(id, 2, 3);
            return rows > 0 ? "收货成功！" : "收货失败！";
        } catch (SQLException e) {
            e.printStackTrace();
            return "收货失败！";
        }
    }

    public String delOrderData(int id) {
        try {
            int rows = orderDao.delOrderData(id);
            return rows > 0 ? "删除成功！" : "删除失败！";
        } catch (SQLException e) {
            e.printStackTrace();
            return "删除失败！";
        }
    }
}
