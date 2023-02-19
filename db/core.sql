
-- ----------------------------
-- Table structure for `sys_dict`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` varchar(32) NOT NULL COMMENT '字典id',
  `name` varchar(255) NOT NULL COMMENT '字典名称',
  `dict_type` varchar(32) NOT NULL COMMENT 'dict_type表主键',
  `description` varchar(255) NOT NULL COMMENT '描述',
  `status` int(11) NOT NULL COMMENT '状态(1启用 0停用)',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` bit NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_mch`
-- ----------------------------
DROP TABLE IF EXISTS `sys_mch`;
CREATE TABLE `sys_mch` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '商户编码',
  `admin_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '管理员',
  `logo` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT 'logo',
  `name` varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '商户名称',
  `short_name` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '商户简称',
  `mch_type` int(11) NOT NULL DEFAULT 0 COMMENT '商户类型(0.运营商 1.普通商户 2.特约商户 3.服务商(ISV)',
  `isv_id` varchar(32) DEFAULT NULL COMMENT '服务商',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '状态(0 待审 1启用 2停用)',
  `area_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '行政区编码',
  `area_name` varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '行政区名称',
  `address` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '详细地址',
  `linkman` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '商户联系人',
  `phone` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '商户联系人电话',
  `remark` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` bit NOT NULL DEFAULT 0 COMMENT '删除标志',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '商户档案';

-- ----------------------------
-- Table structure for `sys_office`
-- ----------------------------
DROP TABLE IF EXISTS `sys_office`;
CREATE TABLE `sys_office` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '组织编码',
  `name` varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '组织名称',
  `type` int(11) not null DEFAULT 0 NULL COMMENT '类型 (0公司 1部门)',
  `parent_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '父级编号',
  `tree_path` varchar(3000) CHARACTER SET utf8 DEFAULT NULL COMMENT '关系树',
  `data_sort` int(11) DEFAULT NULL COMMENT '排序',
  `leader` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '负责人',
  `phone` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '办公电话',
  `address` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '联系地址',
  `zip_code` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '邮政编码',
  `email` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '电子邮箱',
  `status` varchar(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '状态（1启用 0停用）',
  `remark` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注信息',
  `mch_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '商户号',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` bit NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织架构';


-- ----------------------------
-- Table structure for `sys_post`
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `id` varchar(32) NOT NULL COMMENT '岗位编码',
  `name` varchar(100) NOT NULL COMMENT '岗位名称',
  `parent_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '父级编号',
  `tree_path` varchar(3000) CHARACTER SET utf8 DEFAULT NULL COMMENT '关系树',
  `data_sort` int(11) DEFAULT NULL COMMENT '排序',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态（1启用 0停用）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `mch_id` varchar(32) NOT NULL COMMENT '商户号',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` bit NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工岗位表';

-- ----------------------------
-- Table structure for `sys_employee`
-- ----------------------------
DROP TABLE IF EXISTS `sys_employee`;
CREATE TABLE `sys_employee` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '员工编码',
  `name` varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '员工姓名',
  `emp_no` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '员工号',
  `phone` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '联系电话',
  `office_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '部门编码',
  `mch_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '商户号',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '状态（1在职 0离职）',
  `user_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '绑定用户',
  `remark` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注信息',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` bit NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工表';

-- ----------------------------
-- Records of sys_employee
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_employee_post`
-- ----------------------------
DROP TABLE IF EXISTS `sys_employee_post`;
CREATE TABLE `sys_employee_post` (
  `id` varchar(32) NOT NULL COMMENT '编码',
  `employee_id` varchar(32) NOT NULL COMMENT '员工编码',
  `post_id` varchar(32) NOT NULL COMMENT '岗位编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工组织与岗位关联表';

-- ----------------------------
-- Records of sys_employee_post
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_icon`
-- ----------------------------
DROP TABLE IF EXISTS `sys_icon`;
CREATE TABLE `sys_icon` (
  `id` varchar(32) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '图标名',
  `icon_type` varchar(255) NOT NULL COMMENT '分组(1.图标,2.素材)',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '队列描述',
  `data_sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图标库';


-- ----------------------------
-- Table structure for `sys_job`
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
  `id` varchar(32) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '描述任务',
  `cron` varchar(255) NOT NULL COMMENT '任务表达式',
  `status` int(11) NOT NULL COMMENT '状态:0禁用/1启用',
  `clazz_path` varchar(255) NOT NULL COMMENT '任务执行方法',
  `job_desc` varchar(255) DEFAULT NULL COMMENT '任务描述',
  `exec_addr` varchar(32)NOT NULL COMMENT '执行机IP地址',
  `exec_status` int(11) NOT NULL COMMENT '状态:0未启动/1运行中',
  `exec_err_desc` longtext COMMENT '错误描述',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='计划任务';

-- ----------------------------
-- Table structure for `sys_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` varchar(32) NOT NULL,
  `username` varchar(64) DEFAULT NULL COMMENT '用户账号',
	log_type varchar(50) NOT NULL COMMENT '日志类型',
	log_title varchar(500) NOT NULL COMMENT '日志标题',
	request_uri varchar(500) COMMENT '请求URI',
	request_method varchar(10) COMMENT '操作方式',
	request_params longtext COMMENT '操作提交的数据',
	diff_modify_data longtext COMMENT '新旧数据比较结果',
	remote_addr varchar(255) COMMENT '操作IP地址',
	server_addr varchar(255) COMMENT '请求服务器地址',
	is_exception char(1) COMMENT '是否异常',
	exception_info text COMMENT '异常信息',
	user_agent varchar(500) COMMENT '用户代理',
	device_name varchar(100) COMMENT '设备名称/操作系统',
	browser_name varchar(100) COMMENT '浏览器名称',
  `mch_id` varchar(32) DEFAULT NULL COMMENT '商户号',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志';

-- 行政区划
DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE sys_area
(
 `id` varchar(32) NOT NULL COMMENT '区域编码',
 `name` varchar(100) NOT NULL COMMENT '区域名称',
 `area_code` varchar(32) NOT NULL COMMENT '行政编码',
 `full_name` varchar(100) NOT NULL COMMENT '区域全名',
 `parent_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '父级编号',
 `data_sort` int(11) DEFAULT NULL COMMENT '排序',
 `tree_path` varchar(1000) NOT NULL COMMENT '关系树',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
 PRIMARY KEY (area_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '行政区划';

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` varchar(32) NOT NULL  COMMENT '菜单编码',
  `name` varchar(255) NOT NULL  COMMENT '菜单名称',
  `parent_id` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '父级编号',
  `data_sort` int(11) DEFAULT NULL COMMENT '排序',
  `tree_path` varchar(1000) NOT NULL COMMENT '关系树',
  `url` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限',
  `menu_type` int(11) NOT NULL DEFAULT '0' COMMENT '0菜单 1按钮',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '模块菜单';

-- ----------------------------
-- Table structure for `sys_mch_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_mch_menu`;
CREATE TABLE `sys_mch_menu` (
  `id` varchar(32) NOT NULL,
  `mch_id` varchar(32) NOT NULL,
  `menu_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '商户开通菜单';

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(32) NOT NULL,
  `name` varchar(128) NOT NULL COMMENT '角色名称',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '状态(1启用 0停用)',
  `mch_id` varchar(32) DEFAULT NULL COMMENT '商户号',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '角色管理';

-- ----------------------------
-- Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` varchar(32) NOT NULL,
  `role_id` varchar(32) NOT NULL,
  `menu_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '菜单权限';

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(32) NOT NULL,
  `username` varchar(64) NOT NULL unique COMMENT '账号',
  `password` varchar(128) NULL COMMENT '登录密码',
  `capital_password` varchar(128) NULL COMMENT '资金密码',
  `mobile` varchar(14) NULL COMMENT '绑定手机',
  `email` varchar(14) NULL COMMENT '绑定邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `user_type` int(11) NOT NULL DEFAULT 1 COMMENT '用户类型（1平台账号 2商户账号)',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '状态（1正常 2停用 3冻结)',
  `nickname` varchar(18) DEFAULT NULL COMMENT '用户昵称',
  `mch_id` varchar(32) NULL COMMENT '商户号',
  `wx_id` varchar(100) DEFAULT NULL COMMENT '微信id',
  `wxm_id` varchar(100) DEFAULT NULL COMMENT '小程序id',
  `wb_id` varchar(100) DEFAULT NULL COMMENT '微博id',
  `qq_id` varchar(100) DEFAULT NULL COMMENT 'QQID',
  `imei_id` varchar(100) DEFAULT NULL COMMENT '绑定的手机串号',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `last_login_ip` varchar(100) DEFAULT NULL COMMENT '最后登陆IP',
  `last_login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `freeze_date` datetime DEFAULT NULL COMMENT '冻结时间',
  `freeze_cause` varchar(200) DEFAULT NULL COMMENT '冻结原因',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` bit NOT NULL DEFAULT 0 COMMENT '删除标志',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '用户表';


ALTER TABLE `sys_user`
	add COLUMN  `referrer` varchar(32) DEFAULT NULL COMMENT '推荐人 userId';

-- ----------------------------
-- Table structure for `sys_role_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `role_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8  COMMENT = '用户角色关系表';


-- 用户数据权限表
DROP TABLE IF EXISTS `sys_user_data_scope`;
CREATE TABLE sys_user_data_scope
(
	id varchar(32) NOT NULL COMMENT '控制用户编码',
	user_id varchar(100) NOT NULL COMMENT '控制用户编码',
  scope_type int(11) NOT NULL DEFAULT 1 COMMENT '权限类型（1组织架构 2门店仓库..可扩展)',
	office_id varchar(32) NOT NULL COMMENT '控制组织架构',
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '用户数据权限表';

-- 文件表
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE sys_file
(
	`id` varchar(32) NOT NULL COMMENT '编号',
	`file_name` varchar(255) NOT NULL COMMENT '文件名称',
	`file_type` int(11) NOT NULL DEFAULT 0 COMMENT '文件分类（0image、1media、2music、3file）',
	`file_url` varchar(255) NOT NULL COMMENT '文件路径',
	`content` mediumblob COMMENT '文件内容(不能超过16M)',
	`order_type` varchar(50) NOT NULL COMMENT '单据类型',
	`order_id` varchar(50) DEFAULT NULL COMMENT '单据ID',
	`plugin_id` varchar(50) NOT NULL COMMENT '插件编码',
	`plugin_name` varchar(50) NOT NULL COMMENT '插件名称',
	`status` int(11) NOT NULL DEFAULT 0 NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	`remark` varchar(500) COMMENT '备注信息',
  `mch_id` varchar(32) DEFAULT NULL COMMENT '商户号',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` bit NOT NULL DEFAULT 0 COMMENT '删除标志',
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '文件上传表';


-- 消息模板
DROP TABLE IF EXISTS `sys_template`;
CREATE TABLE sys_template
(
	id varchar(32) NOT NULL COMMENT '编号',
	tpl_key varchar(100) NOT NULL COMMENT '模板键值',
	tpl_id varchar(100) NOT NULL COMMENT '模板消息id',
	tpl_name varchar(100) NOT NULL COMMENT '模板名称',
	tpl_type int(11) NOT NULL DEFAULT 0 NOT NULL COMMENT '状态（0短信 1邮箱 2通知）',
	tpl_content text NOT NULL COMMENT '模板内容',
 	remark varchar(500) COMMENT '备注信息',
  `mch_id` varchar(32) DEFAULT NULL COMMENT '商户号',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '消息模板';

-- 消息管理
DROP TABLE IF EXISTS `sys_msg`;
CREATE TABLE sys_msg
(
	id varchar(64) NOT NULL COMMENT '编号',
	msg_title varchar(200) NOT NULL COMMENT '消息标题',
	msg_level int(11) NOT NULL DEFAULT 0 COMMENT '内容级别（1普通 2一般 3紧急）',
	msg_type int(11) NOT NULL DEFAULT 0 COMMENT '内容类型（1公告 2新闻 3会议 4通知）',
	msg_content text NOT NULL COMMENT '消息内容（通知时是json串）',
	receive_type char(1) NOT NULL COMMENT '接受者类型（0全部 1用户 2部门 3角色 4岗位）',
	send_user_id varchar(32) COMMENT '发送者用户编码',
	send_user_name varchar(100) COMMENT '发送者用户姓名',
	send_date datetime COMMENT '发送时间',
	is_attac char(1) COMMENT '是否有附件',
	status int(11) NOT NULL DEFAULT 0 COMMENT '状态（0草稿 1审核 2驳回）',
  mch_id varchar(32) DEFAULT NULL COMMENT '商户号',
	`create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` bit NOT NULL DEFAULT 0 COMMENT '删除标志',
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '内部消息';


-- 消息推送表
DROP TABLE IF EXISTS `sys_msg_push`;
CREATE TABLE sys_msg_push
(
	id varchar(32) NOT NULL COMMENT '编号',
	msg_id varchar(32) NULL COMMENT '消息编码',
	msg_key varchar(32) NULL COMMENT '关联业务类型',
	msg_level int(11) NOT NULL DEFAULT 0 COMMENT '内容级别（1普通 2一般 3紧急）',
	msg_type int(11) NOT NULL DEFAULT 0 COMMENT '内容类型（1公告 2新闻 3会议 4通知）',
	msg_title varchar(200) NOT NULL COMMENT '消息标题',
	msg_content text NOT NULL COMMENT '消息内容（通知时是json串）',
	is_attac char(1) COMMENT '是否有附件',
	receive_user_id varchar(32) NOT NULL COMMENT '接受者用户编码',
	receive_user_name varchar(100) NOT NULL COMMENT '接受者用户姓名',
  send_user_id varchar(32) NOT NULL COMMENT '发送者用户编码',
	send_user_name varchar(100) NOT NULL COMMENT '发送者用户姓名',
	send_date datetime NOT NULL COMMENT '发送时间',
	is_push bit NOT NULL DEFAULT 0 COMMENT '是否推送',
	push_date datetime COMMENT '推送时间',
	push_status char(1) COMMENT '推送状态（0未推送 1成功  2失败）',
	read_status char(1) COMMENT '读取状态（0未送达 1已读 2未读）',
	read_date datetime COMMENT '读取时间',
  `mch_id` varchar(32) DEFAULT NULL COMMENT '商户号',
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '消息推送表';



-- 插件管理
DROP TABLE IF EXISTS `sys_plugin`;
CREATE TABLE sys_plugin
(
	id varchar(32) NOT NULL COMMENT '编号',
	name varchar(100) NOT NULL COMMENT '插件名称',
	version varchar(32) NOT NULL COMMENT '插件版本',
	plugin_id varchar(100) NOT NULL COMMENT '插件编号',
  plugin_type int(11) NOT NULL DEFAULT 0 COMMENT '插件类型（1存储插件 2支付插件 3手机短信 4分账插件）',
	is_installed bit NOT NULL DEFAULT 0 COMMENT '是否安装',
	is_enabled bit NOT NULL DEFAULT 0 COMMENT '是否启用',
  `mch_id` varchar(32) DEFAULT NULL COMMENT '商户号',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(32) DEFAULT NULL COMMENT '更新者',
  `modify_date` datetime DEFAULT NULL COMMENT '更新时间',
  	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '插件管理';

-- 插件属性
DROP TABLE IF EXISTS `sys_plugin_attribute`;
CREATE TABLE sys_plugin_attribute
(
	id varchar(32) NOT NULL COMMENT '编号',
	plugin_id varchar(32) NOT NULL COMMENT '插件编号',
	type int(11) NOT NULL DEFAULT 0 COMMENT '类型(1.String,2.Array,3.File)',
  name varchar(100) NOT NULL COMMENT '属性名称',
	attribute varchar(255) NOT NULL COMMENT '属性值',
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '插件属性';

-- ----------------------------
--  Table structure for `sys_sequence`
-- ----------------------------
DROP TABLE IF EXISTS `sys_sequence`;
CREATE TABLE `sys_sequence` (
  `id` varchar(32) NOT NULL COMMENT '编号',
  `last_value` bigint(20) NOT NULL COMMENT '序号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT = '序号表';

-- ----------------------------
--  Table structure for `sys_bind_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_bind_user`;
CREATE TABLE `sys_bind_user` (

  `user_id` varchar(32)  NOT NULL COMMENT '会员',
  `bind_type` int(11) NOT NULL DEFAULT '0' COMMENT '类型 {0.用户}',
  `bind_id` varchar(32)  NOT NULL COMMENT '绑定ID',

  KEY `FKuser_id` (`user_id`),
  KEY `FKbind_id` (`bind_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='绑定关系';
