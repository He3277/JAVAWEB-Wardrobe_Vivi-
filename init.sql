-- ============================================
-- Vivi Style 网上衣橱 - 数据库初始化脚本
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS wardrobe DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE wardrobe;

-- ============================================
-- 1. 用户表
-- ============================================
CREATE TABLE IF NOT EXISTS t_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255) DEFAULT '',
    role INT NOT NULL DEFAULT 2 COMMENT '1=管理员, 2=普通用户'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 2. 服装类型表
-- ============================================
CREATE TABLE IF NOT EXISTS t_type (
    id INT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 3. 服装尺寸表
-- ============================================
CREATE TABLE IF NOT EXISTS t_size (
    id INT PRIMARY KEY,
    type_id INT NOT NULL,
    size_name VARCHAR(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 4. 服装表
-- ============================================
CREATE TABLE IF NOT EXISTS t_clothes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cloth_name VARCHAR(100) NOT NULL,
    image VARCHAR(255) DEFAULT '',
    type_id INT NOT NULL,
    style VARCHAR(50) DEFAULT '',
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 5. 购物车表
-- ============================================
CREATE TABLE IF NOT EXISTS t_cart (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cloth_id INT NOT NULL,
    cloth_size VARCHAR(20) DEFAULT '',
    amount INT NOT NULL DEFAULT 1,
    user_id INT NOT NULL,
    date VARCHAR(50) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 6. 订单表
-- ============================================
CREATE TABLE IF NOT EXISTS t_order (
    id INT AUTO_INCREMENT PRIMARY KEY,
    clothes_details VARCHAR(500) DEFAULT '',
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(10) NOT NULL DEFAULT '0' COMMENT '0=待付款, 1=待发货, 2=待收货, 3=已完成',
    user_id INT NOT NULL,
    address VARCHAR(255) DEFAULT '',
    time VARCHAR(50) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 7. 种子数据 - 管理员和测试用户
-- ============================================
INSERT INTO t_user (user_name, password, phone, address, role) VALUES
('admin', 'admin123', '13800000001', '系统管理员', 1),
('user', 'user123', '13800000002', '测试地址', 2);

-- ============================================
-- 8. 种子数据 - 服装类型
-- ============================================
INSERT INTO t_type (id, type_name) VALUES
(1, '鞋子'),
(2, '上衣'),
(3, '裤子'),
(4, '裙子'),
(5, '配饰');

-- ============================================
-- 9. 种子数据 - 服装尺寸
-- ============================================
-- 鞋子尺寸
INSERT INTO t_size (id, type_id, size_name) VALUES
(1, 1, '38'), (2, 1, '39'), (3, 1, '40'),
(4, 1, '41'), (5, 1, '42'), (6, 1, '43');

-- 上衣尺寸
INSERT INTO t_size (id, type_id, size_name) VALUES
(7, 2, 'S'), (8, 2, 'M'), (9, 2, 'L'),
(10, 2, 'XL'), (11, 2, 'XXL');

-- 裤子尺寸
INSERT INTO t_size (id, type_id, size_name) VALUES
(12, 3, '28'), (13, 3, '29'), (14, 3, '30'),
(15, 3, '31'), (16, 3, '32'), (17, 3, '33'), (18, 3, '34');

-- 裙子尺寸
INSERT INTO t_size (id, type_id, size_name) VALUES
(19, 4, 'S'), (20, 4, 'M'), (21, 4, 'L'), (22, 4, 'XL');

-- 配饰尺寸
INSERT INTO t_size (id, type_id, size_name) VALUES
(23, 5, '均码');

-- ============================================
-- 10. 种子数据 - 示例服装
-- ============================================
INSERT INTO t_clothes (cloth_name, image, type_id, style, price) VALUES
('简约纯白运动鞋', '0.jpg', 1, '简约', 299.00),
('复古经典帆布鞋', '1.jpg', 1, '复古', 199.00),
('韩版厚底增高鞋', '2.jpg', 1, '韩版', 269.00),
('街头潮流老爹鞋', '3.jpg', 1, '街头', 349.00),
('休闲透气网面鞋', '4.jpg', 1, '休闲', 259.00),
('商务正装牛津鞋', '5.jpg', 1, '商务', 459.00),
('运动减震跑步鞋', '6.jpg', 1, '运动', 399.00),
('简约百搭小白鞋', '7.jpg', 1, '简约', 239.00),
('复古真皮马丁靴', '8.jpg', 1, '复古', 369.00),
('韩版时尚滑板鞋', '9.jpg', 1, '韩版', 219.00),
('街头涂鸦板鞋', '10.jpg', 1, '街头', 289.00),
('休闲轻便乐福鞋', '11.jpg', 1, '休闲', 279.00),
('运动气垫篮球鞋', '12.jpg', 1, '运动', 329.00);

SELECT '数据库初始化完成！' AS message;
