/*
 Navicat Premium Data Transfer

 Source Server         : acan
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : localhost:3306
 Source Schema         : forum

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 16/05/2021 12:21:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blacklist
-- ----------------------------
DROP TABLE IF EXISTS `blacklist`;
CREATE TABLE `blacklist`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(11) UNSIGNED NOT NULL COMMENT '被封禁用户id',
  `admin_id` int(11) UNSIGNED NOT NULL COMMENT '操作人',
  `start_time` datetime(0) NOT NULL COMMENT '起始时间',
  `end_time` datetime(0) NOT NULL COMMENT '结束时间',
  `create_time` datetime(0) NOT NULL COMMENT '操作时间',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封禁原因',
  `is_read` tinyint(4) NOT NULL COMMENT '是否已被用户阅读',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '黑名单（小黑屋）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blacklist
-- ----------------------------
INSERT INTO `blacklist` VALUES (8, 4, 1, '2021-05-28 00:00:00', '2021-05-28 00:00:00', '2021-05-15 20:58:13', '123123', 0);
INSERT INTO `blacklist` VALUES (9, 6, 1, '2021-05-16 00:00:00', '2021-05-16 00:00:00', '2021-05-15 20:59:44', '阿八八八', 0);
INSERT INTO `blacklist` VALUES (10, 5, 1, '2021-05-20 00:00:00', '2021-06-22 00:00:00', '2021-05-15 21:00:49', '才说的', 0);
INSERT INTO `blacklist` VALUES (13, 9, 1, '2021-05-25 00:00:00', '2021-06-17 00:00:00', '2021-05-15 21:05:01', '12312', 0);

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(11) UNSIGNED NOT NULL COMMENT '用户id',
  `post_id` int(11) UNSIGNED NOT NULL COMMENT '帖子id',
  `create_time` datetime(0) NOT NULL COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '收藏\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collect
-- ----------------------------

-- ----------------------------
-- Table structure for comment_notification
-- ----------------------------
DROP TABLE IF EXISTS `comment_notification`;
CREATE TABLE `comment_notification`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '通知id',
  `sender_id` int(11) UNSIGNED NOT NULL COMMENT '发送者id',
  `receiver_id` int(11) UNSIGNED NOT NULL COMMENT '接收者id',
  `comment_type` tinyint(4) NOT NULL COMMENT '通知类型。0：主题帖被回复，1：一级评论被回复，2：二级评论被回复',
  `replied_id` int(11) UNSIGNED NOT NULL COMMENT '被回复的id（可能是主题帖、一级评论、二级评论，根据评论类型来判断）',
  `comment_id` int(11) UNSIGNED NOT NULL COMMENT '来自于哪条评论（可能是一级评论、二级评论）',
  `create_time` datetime(0) NOT NULL COMMENT '时间',
  `is_read` tinyint(4) NOT NULL COMMENT '是否已读',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `quote_comment_id`(`replied_id`) USING BTREE,
  INDEX `comment_id`(`comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论通知' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment_notification
-- ----------------------------

-- ----------------------------
-- Table structure for first_comment
-- ----------------------------
DROP TABLE IF EXISTS `first_comment`;
CREATE TABLE `first_comment`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `user_id` int(11) UNSIGNED NOT NULL COMMENT '发送者id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `post_id` int(11) UNSIGNED NOT NULL COMMENT '所属帖子',
  `floor_num` int(11) UNSIGNED NOT NULL COMMENT '楼层编号',
  `second_comment_count` int(11) UNSIGNED NOT NULL COMMENT '该一级评论下二级评论的数量',
  `create_time` datetime(0) NOT NULL COMMENT '发布的时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '一级评论' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of first_comment
-- ----------------------------

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `from_user_id` int(11) UNSIGNED NOT NULL COMMENT '发起者id',
  `to_user_id` int(11) UNSIGNED NOT NULL COMMENT '被关注者',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '关注' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of follow
-- ----------------------------

-- ----------------------------
-- Table structure for follow_notification
-- ----------------------------
DROP TABLE IF EXISTS `follow_notification`;
CREATE TABLE `follow_notification`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `follow_id` int(11) UNSIGNED NOT NULL COMMENT '关注id',
  `is_read` tinyint(4) NOT NULL COMMENT '是否已读',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '关注通知' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of follow_notification
-- ----------------------------

-- ----------------------------
-- Table structure for label
-- ----------------------------
DROP TABLE IF EXISTS `label`;
CREATE TABLE `label`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `label_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签描述',
  `post_count` int(10) UNSIGNED NOT NULL COMMENT '帖子数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of label
-- ----------------------------

-- ----------------------------
-- Table structure for like
-- ----------------------------
DROP TABLE IF EXISTS `like`;
CREATE TABLE `like`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(11) UNSIGNED NOT NULL COMMENT '点赞的用户id',
  `post_id` int(11) UNSIGNED NOT NULL COMMENT '帖子id',
  `create_time` datetime(0) NOT NULL COMMENT '点赞时间',
  `is_read` tinyint(4) NOT NULL COMMENT '是否已读',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `post_id`(`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '点赞' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of like
-- ----------------------------

-- ----------------------------
-- Table structure for points_record
-- ----------------------------
DROP TABLE IF EXISTS `points_record`;
CREATE TABLE `points_record`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(11) UNSIGNED NOT NULL COMMENT '用户id',
  `points_type_id` int(11) UNSIGNED NOT NULL COMMENT '类型id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '积分奖励记录\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of points_record
-- ----------------------------

-- ----------------------------
-- Table structure for points_type
-- ----------------------------
DROP TABLE IF EXISTS `points_type`;
CREATE TABLE `points_type`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `points` int(11) NOT NULL COMMENT '加减操作的分值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '积分行为\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of points_type
-- ----------------------------

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '帖子id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `creator_id` int(11) UNSIGNED NOT NULL COMMENT '发布者id',
  `topic_id` int(11) UNSIGNED NOT NULL COMMENT '所属话题',
  `create_time` datetime(0) NOT NULL COMMENT '发布时间',
  `last_modify_time` datetime(0) NOT NULL COMMENT '上一次修改时间',
  `visit_count` int(10) UNSIGNED NOT NULL COMMENT '访问数',
  `like_count` int(10) UNSIGNED NOT NULL COMMENT '点赞数',
  `collect_count` int(10) UNSIGNED NOT NULL COMMENT '收藏数',
  `comment_count` int(10) UNSIGNED NOT NULL COMMENT '总评论数',
  `is_hight_quality` tinyint(4) NOT NULL COMMENT '是否为精品',
  `top_weight` int(11) NOT NULL COMMENT '置顶权重',
  `last_comment_time` datetime(0) NULL DEFAULT NULL COMMENT '最后一次评论时间',
  `visibility_type` tinyint(4) NULL DEFAULT NULL COMMENT '可见策略（之后再考虑规划）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `creator_id`(`creator_id`) USING BTREE,
  INDEX `topic_id`(`topic_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主题帖' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post
-- ----------------------------

-- ----------------------------
-- Table structure for post_label
-- ----------------------------
DROP TABLE IF EXISTS `post_label`;
CREATE TABLE `post_label`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `post_id` int(11) UNSIGNED NOT NULL COMMENT '帖子id',
  `label_id` int(11) UNSIGNED NOT NULL COMMENT '标签id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `label_id`(`label_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主题帖表和标签表的关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post_label
-- ----------------------------

-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '权限名称',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '图标',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路径',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '权限表达式',
  `type` tinyint(4) NOT NULL COMMENT '类型。0：菜单，1：按钮',
  `sort_weight` int(11) NULL DEFAULT NULL COMMENT '排序权重',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '涉及权限的资源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES (1, 0, '系统管理', 'layui-icon layui-icon-set', '', '', 0, 1);
INSERT INTO `resource` VALUES (2, 1, '用户管理', '', 'system/user', '', 0, 2);
INSERT INTO `resource` VALUES (5, 1, '角色管理', '', 'system/role', '', 0, 5);
INSERT INTO `resource` VALUES (6, 5, '查看角色', '', '', 'role:view', 1, 6);
INSERT INTO `resource` VALUES (7, 5, '更新角色', '', '', 'role:update', 1, 7);
INSERT INTO `resource` VALUES (8, 1, '权限管理', '', 'system/authorities', '', 0, 8);
INSERT INTO `resource` VALUES (10, 8, '更新权限', '', '', 'authorities:update', 1, 10);
INSERT INTO `resource` VALUES (11, 1, '登录日志', '', 'system/loginRecord', 'loginRecord:view', 0, 11);
INSERT INTO `resource` VALUES (12, 8, '系统监控', 'layui-icon layui-icon-engine', '', '', 0, 12);
INSERT INTO `resource` VALUES (13, 12, 'Druid监控', '', 'druid', '', 0, 133);
INSERT INTO `resource` VALUES (14, 11, 'ddddddddddddd', 'asdf', '123', '', 0, 5);
INSERT INTO `resource` VALUES (15, 0, 'avcsdf', 'asdf', '', '', 0, -2);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------

-- ----------------------------
-- Table structure for role_resource
-- ----------------------------
DROP TABLE IF EXISTS `role_resource`;
CREATE TABLE `role_resource`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_id` int(11) UNSIGNED NOT NULL COMMENT '角色id',
  `resource_id` int(11) UNSIGNED NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `resource_id`(`resource_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表和资源表的关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_resource
-- ----------------------------

-- ----------------------------
-- Table structure for second_comment
-- ----------------------------
DROP TABLE IF EXISTS `second_comment`;
CREATE TABLE `second_comment`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(11) UNSIGNED NOT NULL COMMENT '发送者的用户id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论的内容',
  `first_comment_id` int(11) UNSIGNED NOT NULL COMMENT '所属的一级评论id',
  `quote_second_comment_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '引用（回复）的二级评论id。如果为空，说明回复的是一级评论',
  `create_time` datetime(0) NOT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '二级评论' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of second_comment
-- ----------------------------

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数名',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置参数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_config
-- ----------------------------

-- ----------------------------
-- Table structure for system_notification
-- ----------------------------
DROP TABLE IF EXISTS `system_notification`;
CREATE TABLE `system_notification`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '通知id',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `is_urgent` tinyint(4) NOT NULL COMMENT '是否紧急',
  `create_time` datetime(0) NOT NULL COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统通知' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_notification
-- ----------------------------

-- ----------------------------
-- Table structure for topic
-- ----------------------------
DROP TABLE IF EXISTS `topic`;
CREATE TABLE `topic`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '话题id',
  `topic_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '话题名',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '话题描述',
  `post_count` int(10) UNSIGNED NOT NULL COMMENT '帖子数',
  `create_id` int(11) UNSIGNED NOT NULL COMMENT '创建者',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '话题\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of topic
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机',
  `passsword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `login_salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码的随机盐',
  `jwt_salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '生成jwt的随机盐',
  `gender` tinyint(4) NOT NULL COMMENT '性别。0：男；1：女；3：保密',
  `photo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个性签名',
  `birth` date NULL DEFAULT NULL COMMENT '生日',
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置',
  `register_time` datetime(0) NOT NULL COMMENT '注册时间',
  `modify_time` datetime(0) NOT NULL COMMENT '上一次更新时间',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '上一次登录时间',
  `reward_points` int(11) UNSIGNED NOT NULL COMMENT '积分',
  `fans_count` int(11) UNSIGNED NOT NULL COMMENT '粉丝数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '1', '1', '1', '123456', '1', '1', 1, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-05-12 14:50:24', '2021-05-04 14:50:27', '2021-05-13 14:50:30', 1, 1);
INSERT INTO `user` VALUES (2, '2', '2', '2', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (3, '3', '3', '2', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (4, '4', '4', '2', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-04-22 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (5, '5', '2', '2', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (6, '6', '6', '1', '123456', '1', '1', 1, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-05-12 14:50:24', '2021-05-04 14:50:27', '2021-04-22 14:50:30', 1, 1);
INSERT INTO `user` VALUES (7, '7', '2', '2', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (8, '8', '2', '2', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (9, '9', '2', '2', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-04-29 14:51:39', 2, 2);
INSERT INTO `user` VALUES (10, '10', '2', '2', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (11, '11', '1', '1', '1', '1', '1', 1, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-05-12 14:50:24', '2021-05-04 14:50:27', '2021-05-13 14:50:30', 1, 1);
INSERT INTO `user` VALUES (12, '12', '2', '2', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (13, '13', '2', '2', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (14, '14', '2', '2', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-10 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (15, '15', '2', '2', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-06-17 14:51:39', 2, 2);
INSERT INTO `user` VALUES (16, '16', '1', '1', '1', '1', '1', 1, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-05-12 14:50:24', '2021-05-04 14:50:27', '2021-05-13 14:50:30', 1, 1);
INSERT INTO `user` VALUES (17, '17', '2', '2', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (18, '18', '2', '2', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (19, '19', '2', '2', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-04-28 14:51:36', '2021-05-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (20, '20', '2', '2', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', 2, 2);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(11) UNSIGNED NOT NULL COMMENT '管理员id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `create_time` datetime(0) NOT NULL COMMENT '分配角色的时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `admin_id`(`user_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员表和角色表的关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
