
-- ----------------------------
--  Table structure for `wx_article_category`
-- ----------------------------
DROP TABLE IF EXISTS `wx_article_category`;
CREATE TABLE `wx_article_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `orders` int(11) DEFAULT NULL COMMENT '排序',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `tree_path` varchar(255) NOT NULL,
  `parent` bigint(20) DEFAULT NULL,
  `thumbnail` varchar(255) DEFAULT NULL COMMENT '缩略图',
  PRIMARY KEY (`id`),
  KEY `FKparent` (`parent`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='文章分类';

-- ----------------------------
--  Table structure for `wx_article`
-- ----------------------------
DROP TABLE IF EXISTS `wx_article`;
CREATE TABLE `wx_article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `orders` int(11) DEFAULT NULL COMMENT '排序',
  `authority` int(11) DEFAULT NULL COMMENT '谁可见{0:公开,1:不公开,2:加密,3:私秘}',
  `content` longtext COMMENT '文章内容',
  `hits` bigint(20) DEFAULT '0' COMMENT '阅读数',
  `read_time` int(20) DEFAULT '0' COMMENT '阅读数',
  `article_type` int(11) DEFAULT NULL COMMENT '类型 {0:图文,1:模板,2:商品}',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `article_category_id` bigint(20) DEFAULT NULL COMMENT '分类',
  `deleted` bit(1) DEFAULT NULL COMMENT '是否删除',
  `thumbnail` varchar(255) DEFAULT NULL COMMENT '缩略图',
  `is_draft` bit(1) DEFAULT NULL COMMENT '是否草稿',
  `meta` longtext DEFAULT NULL COMMENT '其他资源',
  PRIMARY KEY (`id`),
  KEY `FKarticle_category_id` (`article_category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24278 DEFAULT CHARSET=utf8mb4 COMMENT='文章管理';


-- ----------------------------
--  Table structure for `wx_article_tag`
-- ----------------------------
DROP TABLE IF EXISTS `wx_article_tag`;
CREATE TABLE `wx_article_tag` (
  `articles` bigint(20) NOT NULL,
  `tags` bigint(20) NOT NULL,
  KEY `FKtags` (`tags`),
  KEY `FKarticles` (`articles`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='文章标签';

-- ----------------------------
--  Table structure for `wx_article_category`
-- ----------------------------
DROP TABLE IF EXISTS `wx_article_category`;
CREATE TABLE `wx_article_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `orders` int(11) DEFAULT NULL COMMENT '排序',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `tree_path` varchar(255) NOT NULL,
  `parent` bigint(20) DEFAULT NULL,
  `thumbnail` varchar(255) DEFAULT NULL COMMENT '缩略图'
  PRIMARY KEY (`id`),
  KEY `FKparent` (`parent`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='文章分类';

-- ----------------------------
--  Table structure for `wx_warehouse
-- ----------------------------
DROP TABLE IF EXISTS `wx_warehouse`;
CREATE TABLE `wx_warehouse` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `name` varchar(255) NOT NULL COMMENT '油库名称',
  `code` varchar(255) NOT NULL COMMENT '油库编码',
  `company_id` bigint(20) DEFAULT NOT null COMMENT '所属公司',
  `customer_id` varchar(32) DEFAULT NOT null COMMENT '供应商ID',
  `area_id` int(11) NOT NULL COMMENT '所属城市',
  `address` varchar(255) NULL COMMENT '详情地址',
  `phone` varchar(255) NULL COMMENT '联系电话',
  `linkman` varchar(255) NULL COMMENT '联系人',
 PRIMARY KEY (`id`),
  KEY `FKcompany` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='油库资料';

-- ----------------------------
--  Table structure for `wx_goods_categroy`
-- ----------------------------
DROP TABLE IF EXISTS `wx_goods_categroy`;
CREATE TABLE `wx_goods_categroy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `name` varchar(255) NOT NULL COMMENT '商品名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='商品分类';

-- ----------------------------
--  Table structure for `wx_goods`
-- ----------------------------
DROP TABLE IF EXISTS `wx_goods`;
CREATE TABLE `wx_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `goods_categroy_id` bigint(20) NOT null COMMENT '所属商品',
  `name` varchar(255) NOT NULL COMMENT '商品名称',
  `sn` varchar(255) NOT NULL COMMENT '商品编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='商品资料';

-- ----------------------------
--  Table structure for `wx_product`
-- ----------------------------
DROP TABLE IF EXISTS `wx_product`;
CREATE TABLE `wx_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `sn` varchar(255) NOT NULL COMMENT '产品编码',
  `name` varchar(255) NOT NULL COMMENT '产品名称',
  `price` decimal(21,2) NOT NULL COMMENT '预售价格',
  `goods_id` bigint(20) NOT null COMMENT '所属商品',
  `company_id` bigint(20) NOT null COMMENT '所属公司',
  `customer_id` bigint(32) NOT null COMMENT '供应商ID',
  `minimum` int(11) NOT NULL COMMENT '最小起订量',
  `limit_day` int(11) NOT NULL COMMENT '锁油期限(天)',
  `order_method` int(11) NOT NULL COMMENT '交易模式(1.锁涨不跌,2.锁涨追跌)',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='产品资料';


-- ----------------------------
--  Table structure for `wx_group`
-- ----------------------------
DROP TABLE IF EXISTS `wx_group`;
CREATE TABLE `wx_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `name` varchar(255) NOT NULL COMMENT '集团名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='集团公司';


-- ----------------------------
--  Table structure for `wx_company`
-- ----------------------------
DROP TABLE IF EXISTS `wx_company`;
CREATE TABLE `wx_company` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `name` varchar(255)  NULL COMMENT '公司名称',
  `type` int(11) NOT NULL COMMENT '公司类型(1.经纪人(个人),2.法人企业)',
  `logo` varchar(255) NULL COMMENT '公司Logo',
  `phone` varchar(255)  NULL COMMENT '联系电话',
  `linkman` varchar(255)  NULL COMMENT '联系人',
  `status` int(11) NOT NULL COMMENT '状态(0.未认证,1.已认证)',
  `msg` varchar(255)  NULL COMMENT '审核说明',
  `address` varchar(255)  NULL COMMENT '详情地址',
  `user_id` varchar(32) DEFAULT null COMMENT '管理员',
  `meta` longtext COMMENT '审核资料',
  PRIMARY KEY (`id`),
  KEY `FKuser` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='账户资料';


-- ----------------------------
--  Table structure for `wx_customer`
-- ----------------------------
DROP TABLE IF EXISTS `wx_customer`;
CREATE TABLE `wx_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `name` varchar(255) NOT NULL COMMENT '公司名称',
  `type` int(11) NOT NULL COMMENT '客户类型(1.供应商,2.采购方)',
  `nature` int(11)  NULL COMMENT '公司类型(1.贸易公司,2.零售终端,3.用油企业)',
  `logo` varchar(255)  NULL COMMENT '公司Logo',
  `phone` varchar(255)  NULL COMMENT '联系电话',
  `linkman` varchar(255)  NULL COMMENT '联系人',
  `status` int(11) NOT NULL COMMENT '状态(0.未认证,1.已认证)',
  `msg` varchar(255)  NULL COMMENT '审核说明',
  `address` varchar(255)  NULL COMMENT '详情地址',
  `group_id` bigint(32) DEFAULT null COMMENT '所属集团',
  `company_id` bigint(32) DEFAULT null COMMENT '所属账户',
  `meta` longtext COMMENT '审核资料',
  PRIMARY KEY (`id`),
  KEY `FKcompany` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='客户管理';

-- ----------------------------
--  Table structure for `wx_order`
-- ----------------------------
DROP TABLE IF EXISTS `wx_order`;
CREATE TABLE `wx_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `company_id` bigint(32) DEFAULT null COMMENT '所属公司',
  `supplier_id` bigint(32) DEFAULT null COMMENT '供应商',
  `customer_id` bigint(32) DEFAULT null COMMENT '采购商',
  `product_id` bigint(32) DEFAULT null COMMENT '产品',
  `warehouse_id` bigint(32) DEFAULT null COMMENT '油库',
  `status` int(11) NOT NULL COMMENT '订单状态(0.制单,1.审核,2.复核,3.出单,4.下定,4.付款,9.完成,10.关闭)',
  `price` decimal(21,2) NOT NULL COMMENT '单价',
  `add_price` decimal(21,2) NOT NULL COMMENT '加价',
  `quantity` decimal(21,2) NOT NULL COMMENT '购买吨数',
  `amount_payable` decimal(21,2) NOT NULL COMMENT '应该金额',
  `amount_paid` decimal(21,2) NOT NULL COMMENT '已付金额',
  `delivery` int(11) NOT NULL COMMENT '提供方式(1.公路,2.船提,3.火车)',
  `limit_day` int(11) NOT NULL COMMENT '锁油期限(天)',
  `order_method` int(11) NOT NULL COMMENT '交易模式(1.锁涨不跌,2.锁涨追跌)',
  `linkman` varchar(255)  NULL COMMENT '客户经理',
  `phone` varchar(255)  NULL COMMENT '联系电话',
  `sn` varchar(255)  NULL COMMENT '订单号',
  `memo` longtext COMMENT '备注',
PRIMARY KEY (`id`),
  KEY `FKcompany` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='交易订单';


insert into wx_goods_categroy(id,create_date,name) values(1,'2023-01-01 00:00:00','车用汽油');
insert into wx_goods_categroy(id,create_date,name) values(2,'2023-01-01 00:00:00','车用柴油');
insert into wx_goods_categroy(id,create_date,name) values(3,'2023-01-01 00:00:00','燃料油');


insert into wx_goods(id,create_date,goods_categroy_id,name) values(1,'2023-01-01 00:00:00',1,'车用汽油-92#-国际VIA');
insert into wx_goods(id,create_date,goods_categroy_id,name) values(2,'2023-01-01 00:00:00',1,'车用汽油-95#-国际VIA');
insert into wx_goods(id,create_date,goods_categroy_id,name) values(3,'2023-01-01 00:00:00',1,'车用汽油-92#-国际VIB');
insert into wx_goods(id,create_date,goods_categroy_id,name) values(4,'2023-01-01 00:00:00',1,'车用汽油-95#-国际VIB');


insert into wx_goods(id,create_date,goods_categroy_id,name) values(5,'2023-01-01 00:00:00',2,'车用柴油-0#-国际VI');

insert into wx_goods(id,create_date,goods_categroy_id,name) values(6,'2023-01-01 00:00:00',3,'燃料油');

-- ----------------------------
--  Table structure for `wx_manager
-- ----------------------------
DROP TABLE IF EXISTS `wx_manager`;
CREATE TABLE `wx_manager` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `name` varchar(255) NOT NULL COMMENT '客户经理',
  `phone` varchar(255) NOT NULL COMMENT '联系电话',
  `status` int(11) NOT NULL COMMENT '状态(0.待审核,1.已审核,2.已驳回)',
  `msg` varchar(255)  NULL COMMENT '审核说明',
  `customer_id` bigint(20) NOT NULL COMMENT '供应商ID',
 PRIMARY KEY (`id`),
  KEY `FKcustomer_id` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='客户经理';


-- ----------------------------
--  Table structure for `wx_log
-- ----------------------------
DROP TABLE IF EXISTS `wx_log`;
CREATE TABLE `wx_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `content` varchar(255) NOT NULL COMMENT '操作说明',
  `log_type` varchar(255) NOT NULL COMMENT '文章日志 article 订单日志 order 客户日志 customer 账户日志 company 油库 warehouse 产品 product 客户经理 manager',
  `log_id` varchar(255) NOT NULL COMMENT '数据对像id',
  `user_id` varchar(255) NOT NULL COMMENT '用户ID',
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
 PRIMARY KEY (`id`),
  KEY `FKlog_id` (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';

-- ----------------------------
--  Table structure for `wx_car
-- ----------------------------
DROP TABLE IF EXISTS `wx_car`;
CREATE TABLE `wx_car` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `plate` varchar(255) NOT NULL COMMENT '车牌号',
  `id_no` varchar(255) NOT NULL COMMENT '身份证号',
  `meta` longtext COMMENT '审核资料',
  `name` varchar(255) NOT NULL COMMENT '司机姓名',
  `phone` varchar(255) NOT NULL COMMENT '联系电话',
  `status` int(11) NOT NULL COMMENT '状态(0.待审核,1.已审核,2.已驳回)',
  `msg` varchar(255)  NULL COMMENT '审核说明',
  `company_id` bigint(20) NOT NULL COMMENT '所属账户',
 PRIMARY KEY (`id`),
  KEY `FKcompany_id` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='车辆管理';

-- ----------------------------
--  Table structure for `wx_warehouse_stock
-- ----------------------------
DROP TABLE IF EXISTS `wx_warehouse_stock`;
CREATE TABLE `wx_warehouse_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `company_id` bigint(32) DEFAULT null COMMENT '所属公司',
  `supplier_id` bigint(32) DEFAULT null COMMENT '供应商',
  `customer_id` bigint(32) DEFAULT null COMMENT '采购商',
  `warehouse_id` varchar(32) NOT NULL COMMENT '油库',
  `product_id` varchar(32) NOT NULL COMMENT '商品',
  `stock` decimal(21,2) NOT NULL COMMENT '实物库存',
  `allocate_stock` decimal(21,2) NOT NULL COMMENT '下单库存',
 PRIMARY KEY (`id`),
  KEY `FKcustomer_id` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='采购库存';

-- ----------------------------
--  Table structure for `wx_shipping
-- ----------------------------
DROP TABLE IF EXISTS `wx_shipping`;
CREATE TABLE `wx_shipping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `modify_date` datetime COMMENT '修改日期',
  `company_id` bigint(32) DEFAULT null COMMENT '所属公司',
  `supplier_id` bigint(32) DEFAULT null COMMENT '供应商',
  `customer_id` bigint(32) DEFAULT null COMMENT '采购商',
  `warehouse_id` varchar(32) NOT NULL COMMENT '油库',
  `product_id` varchar(32) NOT NULL COMMENT '商品',
  `car_id` varchar(32) NOT NULL COMMENT '车辆id',
  `quantity` decimal(21,2) NOT NULL COMMENT '提货数量',
  `delivery` int(11) NOT NULL COMMENT '提供方式(1.公路,2.船提,3.火车)',
  `status` int(11) NOT NULL COMMENT '提货状态(1.已预约,2.已提货)',
  `memo` longtext COMMENT '备注',
 PRIMARY KEY (`id`),
  KEY `FKcustomer_id` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='提货单';
