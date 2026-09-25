package com.itheima.dao;

import com.itheima.model.User;
import com.itheima.utils.DruidUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    /**
     * 用户登录 - 先按用户名匹配，失败再按手机号匹配
     */
    public User login(String userInfo, String password) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id, user_name AS userName, password, phone, address, role FROM t_user WHERE user_name = ? AND password = ?";
        Object[] params = {userInfo, password};

        User user = qr.query(sql, new BeanHandler<>(User.class), params);

        if (user == null) {
            sql = "SELECT id, user_name AS userName, password, phone, address, role FROM t_user WHERE phone = ? AND password = ?";
            user = qr.query(sql, new BeanHandler<>(User.class), params);
        }

        return user;
    }

    /**
     * 获取用户列表（role=2 普通用户），支持用户名模糊搜索
     */
    public List<User> getUserByParam(String searchKey) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT id, user_name AS userName, password, phone, address, role FROM t_user WHERE role=2"
        );

        List<User> result = null;

        if (searchKey != null && !searchKey.isEmpty()) {
            sqlBuilder.append(" AND user_name LIKE ?");
            result = qr.query(sqlBuilder.toString(), new BeanListHandler<>(User.class),
                    "%" + searchKey + "%");

            if (result == null || result.isEmpty()) {
                // 回退：去掉 LIKE 条件，查询全部用户
                String fallbackSql = sqlBuilder.toString()
                        .replace(" AND user_name LIKE ?", "");
                fallbackSql = fallbackSql + " AND user_name LIKE ?";
                result = qr.query(fallbackSql, new BeanListHandler<>(User.class),
                        "%" + searchKey + "%");
            }
        } else {
            result = qr.query(sqlBuilder.toString(), new BeanListHandler<>(User.class));
        }

        return result;
    }

    /**
     * 根据 ID 获取用户
     */
    public User getUserById(int id) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id, user_name AS userName, password, phone, address, role FROM t_user WHERE id = ?";
        return qr.query(sql, new BeanHandler<>(User.class), id);
    }

    /**
     * 添加用户
     */
    public int addUser(User user) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "INSERT INTO t_user(user_name, password, phone, address, role) VALUES(?,?,?,?,?)";
        return qr.update(sql,
                user.getUserName(),
                user.getPassword(),
                user.getPhone(),
                user.getAddress(),
                user.getRole());
    }

    /**
     * 更新用户
     */
    public int updateUser(User user) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "UPDATE t_user SET user_name = ?, password = ?, phone = ?, address = ? WHERE id = ?";
        return qr.update(sql,
                user.getUserName(),
                user.getPassword(),
                user.getPhone(),
                user.getAddress(),
                user.getId());
    }

    /**
     * 删除用户
     */
    public int delUser(int id) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "DELETE FROM t_user WHERE id = ?";
        return qr.update(sql, id);
    }

    /**
     * 检查用户名是否存在
     */
    public User isUserNameExist(String userName) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id, user_name AS userName FROM t_user WHERE user_name = ?";
        return qr.query(sql, new BeanHandler<>(User.class), userName);
    }

    /**
     * 检查手机号是否存在
     */
    public User isPhoneExist(String phone) throws SQLException {
        QueryRunner qr = new QueryRunner(DruidUtils.getDataSource());
        String sql = "SELECT id, phone FROM t_user WHERE phone = ?";
        return qr.query(sql, new BeanHandler<>(User.class), phone);
    }
}
