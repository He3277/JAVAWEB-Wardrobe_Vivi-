package com.itheima.service;

import com.itheima.dao.UserDao;
import com.itheima.model.User;
import com.itheima.model.UserVo;
import com.itheima.utils.TokenUtils;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserDao userDao = new UserDao();

    /**
     * 用户登录
     */
    public User login(String userInfo, String password) {
        User user = null;
        try {
            user = userDao.login(userInfo, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (user != null) {
            String token = TokenUtils.generateToken(user.getId(), user.getPassword());
            user.setToken(token);
        }

        return user;
    }

    /**
     * 用户注册
     */
    public String register(User user) {
        try {
            if (userDao.isUserNameExist(user.getUserName()) != null) {
                return "用户名已存在，请更换";
            }
            if (userDao.isPhoneExist(user.getPhone()) != null) {
                return "手机号已存在，请确认！";
            }
            user.setRole(2);
            userDao.addUser(user);
            return "注册成功，请登录！";
        } catch (SQLException e) {
            e.printStackTrace();
            return "注册失败！";
        }
    }

    /**
     * 搜索用户
     */
    public List<User> getUserByParam(String searchKey) {
        List<User> result = null;
        try {
            result = userDao.getUserByParam(searchKey);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除用户
     */
    public String delUser(int id) {
        try {
            int rows = userDao.delUser(id);
            return rows > 0 ? "删除成功！" : "删除失败！";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前用户
     */
    public User getCurrentUser(int id) {
        try {
            return userDao.getUserById(id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 更新用户信息
     */
    public String updateUser(UserVo userVo) {
        try {
            User currentUser = getCurrentUser(userVo.getId());
            if (!currentUser.getPassword().equals(userVo.getPassword())) {
                return "原始密码不正确，请输入正确的密码！";
            }

            User existUser = userDao.isUserNameExist(userVo.getUserName());
            if (existUser != null && !existUser.getUserName().equals(userVo.getUserName())) {
                return "用户名已存在，请更换其他用户名！";
            }

            User existPhone = userDao.isPhoneExist(userVo.getPhone());
            if (existPhone != null && !existPhone.getPhone().equals(userVo.getPhone())) {
                return "手机号已存在，请确认！";
            }

            userVo.setPassword(userVo.getNewpsw());
            int rows = userDao.updateUser(userVo);
            return rows > 0 ? "修改成功！" : "修改失败！";
        } catch (SQLException e) {
            e.printStackTrace();
            return "修改失败！";
        }
    }
}
