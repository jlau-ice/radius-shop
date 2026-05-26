-- ============================================
-- Radius Shop 数据库初始化脚本
-- ============================================

CREATE DATABASE IF NOT EXISTS radius_shop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE radius_shop;

-- 商品分类
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    icon VARCHAR(255) COMMENT '分类图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    parent_id BIGINT DEFAULT 0 COMMENT '父级ID',
    status TINYINT DEFAULT 1 COMMENT '状态 1启用 0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) COMMENT '商品分类';

-- 商品
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT NOT NULL COMMENT '分类ID',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    description TEXT COMMENT '商品描述',
    cover_image VARCHAR(500) COMMENT '封面图',
    images VARCHAR(2000) COMMENT '商品图片JSON数组',
    price DECIMAL(10,2) NOT NULL COMMENT '售价',
    origin_price DECIMAL(10,2) COMMENT '原价',
    unit VARCHAR(20) DEFAULT '份' COMMENT '单位',
    stock INT DEFAULT 0 COMMENT '库存',
    min_stock INT DEFAULT 0 COMMENT '库存预警阈值',
    sales INT DEFAULT 0 COMMENT '销量',
    status TINYINT DEFAULT 1 COMMENT '状态 1上架 0下架',
    sort_order INT DEFAULT 0 COMMENT '排序',
    is_recommend TINYINT DEFAULT 0 COMMENT '是否推荐',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_category (category_id),
    INDEX idx_status (status)
) COMMENT '商品';

-- 商品规格 (比如: 中杯/大杯, 正常糖/少糖/无糖)
CREATE TABLE product_sku (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL COMMENT '商品ID',
    spec_name VARCHAR(100) NOT NULL COMMENT '规格名称',
    spec_value VARCHAR(100) NOT NULL COMMENT '规格值',
    price_diff DECIMAL(10,2) DEFAULT 0 COMMENT '价格差',
    stock INT DEFAULT 0 COMMENT '规格库存',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_product (product_id)
) COMMENT '商品规格';

-- 预设配送地址 (后台录入，用户只能选择)
CREATE TABLE address_template (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '点位名称',
    address VARCHAR(300) NOT NULL COMMENT '详细地址',
    contact_name VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT '预设配送地址';

-- C端用户 (微信登录)
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    open_id VARCHAR(100) NOT NULL COMMENT '微信openid',
    union_id VARCHAR(100) COMMENT '微信unionid',
    nickname VARCHAR(100) COMMENT '昵称',
    avatar_url VARCHAR(500) COMMENT '头像',
    phone VARCHAR(20) COMMENT '手机号',
    default_address_id BIGINT COMMENT '默认地址',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    UNIQUE KEY uk_openid (open_id)
) COMMENT '用户';

-- 购物车
CREATE TABLE cart_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    sku_info VARCHAR(200) COMMENT '选择的规格JSON',
    quantity INT DEFAULT 1 COMMENT '数量',
    checked TINYINT DEFAULT 1 COMMENT '是否选中',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user (user_id)
) COMMENT '购物车';

-- 订单
CREATE TABLE `order` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(32) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '订单状态: pending待支付 paid已支付 preparing制作中 delivering配送中 delivered已送达 completed已完成 cancelled已取消',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    pay_amount DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    pay_time DATETIME COMMENT '支付时间',
    pay_transaction_id VARCHAR(64) COMMENT '微信支付交易号',
    delivery_address VARCHAR(300) COMMENT '配送地址',
    delivery_contact VARCHAR(50) COMMENT '收货人',
    delivery_phone VARCHAR(20) COMMENT '收货电话',
    delivery_person_id BIGINT COMMENT '配送员ID',
    delivery_time DATETIME COMMENT '送达时间',
    delivery_photo VARCHAR(500) COMMENT '送达照片',
    remark VARCHAR(500) COMMENT '备注',
    cancel_reason VARCHAR(200) COMMENT '取消原因',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    UNIQUE KEY uk_order_no (order_no),
    INDEX idx_user (user_id),
    INDEX idx_status (status)
) COMMENT '订单';

-- 订单明细
CREATE TABLE order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(100) COMMENT '商品名称',
    product_image VARCHAR(500) COMMENT '商品图片',
    sku_info VARCHAR(200) COMMENT '规格信息',
    price DECIMAL(10,2) NOT NULL COMMENT '单价',
    quantity INT NOT NULL COMMENT '数量',
    amount DECIMAL(10,2) NOT NULL COMMENT '小计',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_order (order_id)
) COMMENT '订单明细';

-- 配送员
CREATE TABLE delivery_person (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL COMMENT '账号',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    phone VARCHAR(20) COMMENT '手机号',
    status TINYINT DEFAULT 1 COMMENT '状态 1在职 0离职',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    UNIQUE KEY uk_username (username)
) COMMENT '配送员';

-- 系统管理员
CREATE TABLE admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL COMMENT '账号',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    name VARCHAR(50) COMMENT '姓名',
    role VARCHAR(20) DEFAULT 'admin' COMMENT '角色',
    status TINYINT DEFAULT 1 COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_username (username)
) COMMENT '管理员';

-- 初始化管理员账号 admin / 123456
INSERT INTO admin (username, password, name, role) VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5Eh', '系统管理员', 'admin');
