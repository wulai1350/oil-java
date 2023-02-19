
-- ----------------------------
-- Records of sys_mch
-- ----------------------------
INSERT INTO `sys_mch` (id,admin_id,name,short_name,status,del_flag,create_date,mch_type) VALUES ('10200', 'acfc0e9232f54732a5d9ffe9071bf572', '平台运营商', '运营商', '1',0,'2020-01-01 00:00:00',0);

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_office` (id,name,type,parent_id,tree_path,data_sort,status,mch_id,create_date,del_flag) VALUES ('2619a672e53811e7b983201a068c6482', '运营商',0,null,',', 0,1,'10200','2020-01-01 00:00:00',0);

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post`(id,name,parent_id,tree_path,data_sort,status,mch_id,create_date,del_flag) VALUES ('2619a672e53811e7b983201a068c6482', '管理员', null, ',', '0', '1', '10200','2020-01-01 00:00:00', 0);

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` (id,name,status,mch_id,create_date) VALUES ('2619a672e53811e7b983201a068c6482', '管理员', '1', '10200', '2020-01-01 00:00:00');

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`(id,username,password,mobile,user_type,status,nickname,mch_id,create_date,del_flag) VALUES ('acfc0e9232f54732a5d9ffe9071bf572', 'admin', 'c7122a1349c22cb3c009da3613d242ab', '13860431130', '1', '1', '超级管理员', '10200','2020-01-01 00:00:00', 0);

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_user_data_scope`(id,user_id,scope_type,office_id) VALUES ('acfc0e9232f54732a5d9ffe9071bf572', 'acfc0e9232f54732a5d9ffe9071bf572',1, '2619a672e53811e7b983201a068c6482');
-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES ('acfc0e9232f54732a5d9ffe9071bf572', 'admin', '2619a672e53811e7b983201a068c6482');

delete from sys_report where mch_id<>'10200';
insert into sys_report(id,`name`,report_category_id,`status`,`sql`,params,template,remark,mch_id,create_date,modify_date,del_flag)
select CONCAT(b.id,right(a.id,26)) as id,a.`name`,a.report_category_id,a.`status`,a.`sql`,a.params,a.template,a.remark,b.id as mch_id,a.create_date,a.modify_date,a.del_flag
from sys_report a,sys_mch b where b.id<>'10200';

INSERT INTO `sys_template` VALUES ('4941b6677709496cb6a51118d81e974c', 'mp.order.status', 'bYbL3Ee5f5yNOakDizKISSP6hDAI56YGrUJtac15i_Q', '下单成功通知', '2', '{{first.DATA}}\n下单时间：{{keyword1.DATA}}\n商品名称：{{keyword2.DATA}}\n订单编号：{{keyword3.DATA}}\n订单金额：{{keyword4.DATA}}\n{{remark.DATA}}', '', '10200', 'acfc0e9232f54732a5d9ffe9071bf572', '2020-07-07 16:44:04', 'acfc0e9232f54732a5d9ffe9071bf572', '2020-07-07 16:48:47'), ('ddd3b05ef1274bba9537713c5f2d18e5', 'msg.register', '', '豆讯短信验证', '0', '【望岳科技】您的验证码是${random}请不要把验证码泄露给其他人', '', '10200', 'acfc0e9232f54732a5d9ffe9071bf572', '2020-06-29 14:57:53', 'acfc0e9232f54732a5d9ffe9071bf572', '2020-09-03 18:10:46');


-- ----------------------------
-- Records of sys_employee
-- ----------------------------
INSERT INTO `sys_employee`(id,name,emp_no,mch_id,office_id,status,user_id,remark,create_date,del_flag) VALUES ('acfc0e9232f54732a5d9ffe9071bf572','管理员', 'admin','10200', '2619a672e53811e7b983201a068c6482', '1', 'acfc0e9232f54732a5d9ffe9071bf572', '管理员', '2020-01-01 00:00:00', 0);
