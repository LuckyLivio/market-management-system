-- 小型超市管理系统数据库脚本
CREATE DATABASE IF NOT EXISTS supermarket_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE supermarket_db;

-- 商品信息表
CREATE TABLE products (
    product_id VARCHAR(20) PRIMARY KEY COMMENT '商品号',
    product_name VARCHAR(100) NOT NULL COMMENT '商品名',
    manufacturer VARCHAR(100) COMMENT '生产厂家',
    unit_price DECIMAL(10,2) NOT NULL COMMENT '单价',
    stock_quantity INT DEFAULT 0 COMMENT '库存量',
    min_stock INT DEFAULT 0 COMMENT '最小库存量',
    stock_in_date DATE COMMENT '入库日期',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 销售记录表
CREATE TABLE sales (
    receipt_id VARCHAR(20) PRIMARY KEY COMMENT '票号',
    sale_date DATE NOT NULL COMMENT '销售日期',
    sale_time TIME NOT NULL COMMENT '销售时间',
    cashier_id VARCHAR(20) COMMENT '收银台',
    member_id VARCHAR(20) COMMENT '会员号',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    discount DECIMAL(5,2) DEFAULT 0 COMMENT '折扣',
    cash_received DECIMAL(10,2) COMMENT '现金',
    change_amount DECIMAL(10,2) COMMENT '找零',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 销售明细表
CREATE TABLE sale_details (
    detail_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '明细ID',
    receipt_id VARCHAR(20) NOT NULL COMMENT '票号',
    product_id VARCHAR(20) NOT NULL COMMENT '商品号',
    quantity INT NOT NULL COMMENT '数量',
    unit_price DECIMAL(10,2) NOT NULL COMMENT '单价',
    subtotal DECIMAL(10,2) NOT NULL COMMENT '小计',
    FOREIGN KEY (receipt_id) REFERENCES sales(receipt_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- 库存变动表
CREATE TABLE inventory_changes (
    change_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '变动ID',
    product_id VARCHAR(20) NOT NULL COMMENT '商品号',
    change_type ENUM('入库', '出库') NOT NULL COMMENT '变动类型',
    change_quantity INT NOT NULL COMMENT '变动数量',
    change_date DATE NOT NULL COMMENT '变动日期',
    operator VARCHAR(50) COMMENT '操作员',
    remarks TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- 资金占用表
CREATE TABLE fund_occupation (
    record_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '单号',
    product_category VARCHAR(50) COMMENT '商品类型',
    occupied_amount DECIMAL(15,2) NOT NULL COMMENT '占用金额',
    total_occupied DECIMAL(15,2) NOT NULL COMMENT '总占用金额',
    occupation_ratio DECIMAL(5,2) COMMENT '占用比例',
    statistics_date DATE NOT NULL COMMENT '统计日期',
    accountant VARCHAR(50) COMMENT '会计',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户表（用于登录管理）
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    role ENUM('销售员', '库管员', '财务员', '管理员') NOT NULL COMMENT '角色',
    real_name VARCHAR(50) COMMENT '真实姓名',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入测试数据
INSERT INTO users (username, password, role, real_name) VALUES
('admin', 'admin123', '管理员', '系统管理员'),
('cashier1', 'cash123', '销售员', '张三'),
('warehouse1', 'ware123', '库管员', '李四'),
('finance1', 'fin123', '财务员', '王五');

INSERT INTO products (product_id, product_name, manufacturer, unit_price, stock_quantity, min_stock, stock_in_date) VALUES
('P001', '可口可乐330ml', '可口可乐公司', 3.50, 100, 20, '2024-01-01'),
('P002', '康师傅方便面', '康师傅', 4.50, 80, 15, '2024-01-01'),
('P003', '农夫山泉550ml', '农夫山泉', 2.00, 150, 30, '2024-01-01'),
('P004', '奥利奥饼干', '奥利奥', 8.90, 60, 10, '2024-01-01'),
('P005', '德芙巧克力', '德芙', 15.80, 40, 8, '2024-01-01');