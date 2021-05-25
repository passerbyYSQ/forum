/*
 Navicat Premium Data Transfer

 Source Server         : 本地MySQL-5.7
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : forum

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 25/05/2021 23:20:31
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
) ENGINE = InnoDB AUTO_INCREMENT = 82 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '黑名单（小黑屋）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blacklist
-- ----------------------------
INSERT INTO `blacklist` VALUES (63, 4, 1, '2021-05-19 16:06:53', '2021-05-19 16:07:57', '2021-05-19 16:06:53', '123', 0);
INSERT INTO `blacklist` VALUES (64, 1, 1, '2021-05-19 16:08:52', '2021-05-19 16:07:55', '2021-05-19 16:08:52', '123', 0);
INSERT INTO `blacklist` VALUES (65, 2, 1, '2021-05-19 16:09:09', '2021-05-19 16:33:03', '2021-05-19 16:09:09', '123', 0);
INSERT INTO `blacklist` VALUES (66, 3, 1, '2021-05-19 16:32:20', '2021-05-19 16:36:14', '2021-05-19 16:32:20', '123', 0);
INSERT INTO `blacklist` VALUES (67, 5, 1, '2021-05-19 16:37:22', '2021-05-19 16:36:35', '2021-05-19 16:37:22', '1324', 0);
INSERT INTO `blacklist` VALUES (68, 1, 1, '2021-05-19 16:37:28', '2021-05-19 16:36:34', '2021-05-19 16:37:28', '123123', 0);
INSERT INTO `blacklist` VALUES (69, 4, 1, '2021-05-19 16:40:34', '2021-05-19 16:39:53', '2021-05-19 16:40:34', '123', 0);
INSERT INTO `blacklist` VALUES (70, 9, 1, '2021-05-19 16:40:40', '2021-05-19 16:39:49', '2021-05-19 16:40:40', '123', 0);
INSERT INTO `blacklist` VALUES (71, 2, 1, '2021-05-19 16:52:49', '2021-05-19 16:52:26', '2021-05-19 16:52:49', '123', 0);
INSERT INTO `blacklist` VALUES (72, 4, 1, '2021-05-19 16:52:53', '2021-05-19 16:52:22', '2021-05-19 16:52:53', '123', 0);
INSERT INTO `blacklist` VALUES (73, 6, 1, '2021-05-19 16:53:03', '2021-05-19 16:52:20', '2021-05-19 16:53:03', '312', 0);
INSERT INTO `blacklist` VALUES (74, 2, 1, '2021-05-19 22:19:16', '2021-05-19 22:18:41', '2021-05-19 22:19:16', '123123', 0);
INSERT INTO `blacklist` VALUES (75, 4, 1, '2021-05-19 22:19:21', '2021-05-19 22:18:38', '2021-05-19 22:19:21', '123123', 0);
INSERT INTO `blacklist` VALUES (76, 1, 1, '2021-05-20 16:03:52', '2021-05-20 16:56:04', '2021-05-20 16:03:52', '123', 0);
INSERT INTO `blacklist` VALUES (77, 13, 1, '2021-05-20 16:56:56', '2021-05-20 16:56:00', '2021-05-20 16:56:56', '123123', 0);
INSERT INTO `blacklist` VALUES (78, 4, 1, '2021-05-20 16:57:31', '2021-05-20 16:56:47', '2021-05-20 16:57:31', '123', 0);
INSERT INTO `blacklist` VALUES (79, 9, 1, '2021-05-20 16:57:35', '2021-05-20 16:56:45', '2021-05-20 16:57:35', '123', 0);
INSERT INTO `blacklist` VALUES (80, 7, 1, '2021-05-20 16:57:39', '2021-05-20 16:56:44', '2021-05-20 16:57:39', '123', 0);
INSERT INTO `blacklist` VALUES (81, 2, 1, '2021-05-20 17:01:59', '2021-05-23 23:32:12', '2021-05-20 17:01:59', '123', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of label
-- ----------------------------
INSERT INTO `label` VALUES (1, '大长腿', '', 1);
INSERT INTO `label` VALUES (2, '测试', '', 1);
INSERT INTO `label` VALUES (3, '辣妹子', '', 1);
INSERT INTO `label` VALUES (4, '帅哥', '', 1);

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
  `visibility_type` tinyint(4) NULL DEFAULT NULL COMMENT '可见策略\r\n0：任何人可见\r\n1：粉丝可见\r\n2：点赞后可见\r\n>=3：积分购买后可见（积分就是visibility_type）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `creator_id`(`creator_id`) USING BTREE,
  INDEX `topic_id`(`topic_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主题帖' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES (1, '测试1111111', '<p>测试1111111说的不是吧微软</p>', 1, 1, '2021-05-25 21:13:29', '2021-05-25 21:13:29', 0, 0, 0, 0, 0, 0, NULL, 0);
INSERT INTO `post` VALUES (2, '235236236', '<p>asvdv</p>', 1, 1, '2021-05-25 22:58:29', '2021-05-25 22:58:29', 0, 0, 0, 0, 0, 0, NULL, 0);
INSERT INTO `post` VALUES (3, '这是一条非常重要的通知！！！', '<p><img src=\"https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/fc8ef26c62b94b9dbcad19570d9a6880.jpg?timestamp=1621955365165\" width=\"800\" /></p>\n<h1>欢迎。。。</h1>\n<p>&nbsp;</p>\n<table style=\"border-collapse: collapse; width: 100%;\" border=\"1\">\n<tbody>\n<tr>\n<td style=\"width: 50%;\">1</td>\n<td style=\"width: 50%;\">2</td>\n</tr>\n<tr>\n<td style=\"width: 50%;\">3</td>\n<td style=\"width: 50%;\">&nbsp;</td>\n</tr>\n</tbody>\n</table>\n<p>&nbsp;</p>\n<p><span style=\"font-size: 36pt;\">😂</span></p>\n<p><span style=\"font-size: 14pt;\">你好</span></p>', 1, 2, '2021-05-25 23:11:16', '2021-05-25 23:11:16', 0, 0, 0, 0, 0, 0, NULL, 0);
INSERT INTO `post` VALUES (4, '我是真的帅', '<p>我是真的帅</p>', 1, 2, '2021-05-25 23:13:02', '2021-05-25 23:13:02', 0, 0, 0, 0, 0, 0, NULL, 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主题帖表和标签表的关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post_label
-- ----------------------------
INSERT INTO `post_label` VALUES (1, 1, 1);
INSERT INTO `post_label` VALUES (2, 1, 2);
INSERT INTO `post_label` VALUES (3, 1, 3);
INSERT INTO `post_label` VALUES (4, 2, 1);
INSERT INTO `post_label` VALUES (5, 2, 3);
INSERT INTO `post_label` VALUES (6, 3, 1);
INSERT INTO `post_label` VALUES (7, 3, 3);
INSERT INTO `post_label` VALUES (8, 4, 1);
INSERT INTO `post_label` VALUES (9, 4, 4);
INSERT INTO `post_label` VALUES (10, 4, 3);

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
  `sort_weight` int(11) NULL DEFAULT 0 COMMENT '排序权重',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '涉及权限的资源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES (1, 0, '系统管理', 'layui-icon layui-icon-set', '', '', 0, 1);
INSERT INTO `resource` VALUES (2, 1, '用户管理', '', 'system/user', '', 0, 2);
INSERT INTO `resource` VALUES (5, 1, '角色管理', '', 'system/role', '', 0, 5);
INSERT INTO `resource` VALUES (8, 1, '权限管理', '', 'system/authorities', '', 0, 8);
INSERT INTO `resource` VALUES (11, 1, '登录日志', '', 'system/loginRecord', 'loginRecord:view', 0, 11);
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
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (2, 'acan', '123', '2021-05-16 18:05:49');
INSERT INTO `role` VALUES (3, 'b', '123', '2021-05-19 16:54:43');
INSERT INTO `role` VALUES (17, '12333', '12', '2021-05-20 15:57:26');
INSERT INTO `role` VALUES (18, 'b', '132', '2021-05-20 16:59:53');
INSERT INTO `role` VALUES (19, 'b', '132', '2021-05-20 16:59:56');
INSERT INTO `role` VALUES (20, '123', '123', '2021-05-20 16:59:58');

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
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表和资源表的关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_resource
-- ----------------------------
INSERT INTO `role_resource` VALUES (1, 20, 15);
INSERT INTO `role_resource` VALUES (2, 2, 1);
INSERT INTO `role_resource` VALUES (3, 2, 11);
INSERT INTO `role_resource` VALUES (4, 2, 14);
INSERT INTO `role_resource` VALUES (5, 2, 8);
INSERT INTO `role_resource` VALUES (6, 2, 12);
INSERT INTO `role_resource` VALUES (7, 2, 13);
INSERT INTO `role_resource` VALUES (8, 2, 10);
INSERT INTO `role_resource` VALUES (9, 2, 5);
INSERT INTO `role_resource` VALUES (10, 2, 7);
INSERT INTO `role_resource` VALUES (11, 2, 6);
INSERT INTO `role_resource` VALUES (12, 2, 2);

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
  `sort_weight` int(11) NULL DEFAULT 0 COMMENT '排序权重',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '话题\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of topic
-- ----------------------------
INSERT INTO `topic` VALUES (1, '测试', '测试专用', 2, 1, '2021-05-25 16:09:46', 0);
INSERT INTO `topic` VALUES (2, '通知', '发通知', 2, 1, '2021-05-25 16:10:09', 0);

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
INSERT INTO `user` VALUES (1, '请问', '1', '1', '123456', '1', '1', 0, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-04-01 14:50:24', '2021-05-04 14:50:27', '2021-07-17 14:50:30', 1, 1);
INSERT INTO `user` VALUES (2, '阿斯蒂', '2', '2', '123456', '2', '2', 1, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-04-03 14:51:34', '2021-05-11 14:51:36', '2021-07-16 14:51:39', 2, 2);
INSERT INTO `user` VALUES (3, '阿萨大', '3', '3', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-04-06 14:51:34', '2021-05-11 14:51:36', '2021-06-26 14:51:39', 2, 2);
INSERT INTO `user` VALUES (4, '分', '4', '4', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-04-23 14:51:34', '2021-05-11 14:51:36', '2021-06-20 14:51:39', 2, 2);
INSERT INTO `user` VALUES (5, '对', '5', '5', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-01 14:51:34', '2021-05-11 14:51:36', '2021-06-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (6, 'VS的', '6', '6', '123456', '1', '1', 1, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-05-08 14:50:24', '2021-05-04 14:50:27', '2021-06-22 14:50:30', 1, 1);
INSERT INTO `user` VALUES (7, 'v萨维奇', '7', '7', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-05-28 14:51:39', 2, 2);
INSERT INTO `user` VALUES (8, '二维', '8', '8', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-15 14:51:34', '2021-05-11 14:51:36', '2021-05-22 14:51:39', 2, 2);
INSERT INTO `user` VALUES (9, '她', '9', '9', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-22 14:51:34', '2021-05-11 14:51:36', '2021-04-29 14:51:39', 2, 2);
INSERT INTO `user` VALUES (10, '和', '10', '10', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-28 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', 2, 2);
INSERT INTO `user` VALUES (11, '而', '11', '11', '1', '1', '1', 1, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-05-30 14:50:24', '2021-05-04 14:50:27', '2021-05-13 14:50:30', 1, 1);
INSERT INTO `user` VALUES (12, '广东佛山', '12', '12', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-01 14:51:34', '2021-05-11 14:51:36', '2021-05-26 14:51:39', 2, 2);
INSERT INTO `user` VALUES (13, '覆盖', '13', '13', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-10 14:51:34', '2021-05-11 14:51:36', '2021-04-29 14:51:39', 2, 2);
INSERT INTO `user` VALUES (14, '都发过', '14', '14', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-04 14:51:34', '2021-05-11 14:51:36', '2021-04-19 14:51:39', 2, 2);
INSERT INTO `user` VALUES (15, '分', '15', '15', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-25 14:51:34', '2021-05-11 14:51:36', '2021-04-13 14:51:39', 2, 2);
INSERT INTO `user` VALUES (16, '给对方', '16', '16', '1', '1', '1', 1, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-05-29 14:50:24', '2021-05-04 14:50:27', '2021-05-20 14:50:30', 1, 1);
INSERT INTO `user` VALUES (17, '断分', '17', '17', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-27 14:51:34', '2021-05-11 14:51:36', '2021-03-11 14:51:39', 2, 2);
INSERT INTO `user` VALUES (18, '是个', '18', '18', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-07-17 14:51:34', '2021-05-11 14:51:36', '2021-05-03 14:51:39', 2, 2);
INSERT INTO `user` VALUES (19, '代国防', '19', '19', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-08-21 14:51:34', '2021-04-28 14:51:36', '2021-02-18 14:51:39', 2, 2);
INSERT INTO `user` VALUES (20, '三个地方', '20', '20', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-07-24 14:51:34', '2021-05-11 14:51:36', '2021-03-12 14:51:39', 2, 2);

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
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员表和角色表的关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (25, 3, 2, '2021-05-20 15:55:17');
INSERT INTO `user_role` VALUES (26, 3, 3, '2021-05-20 15:55:17');
INSERT INTO `user_role` VALUES (27, 4, 3, '2021-05-20 15:56:35');
INSERT INTO `user_role` VALUES (28, 5, 2, '2021-05-20 15:56:39');
INSERT INTO `user_role` VALUES (29, 5, 3, '2021-05-20 15:56:39');
INSERT INTO `user_role` VALUES (32, 1, 17, '2021-05-20 15:57:35');
INSERT INTO `user_role` VALUES (33, 4, 2, '2021-05-20 16:57:10');
INSERT INTO `user_role` VALUES (34, 5, 17, '2021-05-20 16:57:17');
INSERT INTO `user_role` VALUES (35, 1, 18, '2021-05-20 17:00:07');
INSERT INTO `user_role` VALUES (36, 1, 19, '2021-05-20 17:00:07');
INSERT INTO `user_role` VALUES (37, 1, 20, '2021-05-20 17:00:07');
INSERT INTO `user_role` VALUES (39, 2, 3, '2021-05-20 17:28:18');
INSERT INTO `user_role` VALUES (40, 2, 17, '2021-05-20 17:28:18');
INSERT INTO `user_role` VALUES (42, 2, 19, '2021-05-20 17:28:18');
INSERT INTO `user_role` VALUES (44, 7, 2, '2021-05-20 17:28:40');
INSERT INTO `user_role` VALUES (45, 7, 3, '2021-05-20 17:28:40');
INSERT INTO `user_role` VALUES (46, 7, 17, '2021-05-20 17:28:40');
INSERT INTO `user_role` VALUES (47, 7, 18, '2021-05-20 17:28:40');
INSERT INTO `user_role` VALUES (48, 7, 19, '2021-05-20 17:28:40');
INSERT INTO `user_role` VALUES (49, 7, 20, '2021-05-20 17:28:40');
INSERT INTO `user_role` VALUES (50, 8, 2, '2021-05-20 17:29:35');
INSERT INTO `user_role` VALUES (52, 11, 2, '2021-05-21 22:11:54');
INSERT INTO `user_role` VALUES (53, 11, 3, '2021-05-21 22:11:54');
INSERT INTO `user_role` VALUES (54, 11, 17, '2021-05-21 22:11:54');
INSERT INTO `user_role` VALUES (55, 11, 18, '2021-05-21 22:11:54');
INSERT INTO `user_role` VALUES (56, 11, 19, '2021-05-21 22:11:54');
INSERT INTO `user_role` VALUES (57, 11, 20, '2021-05-21 22:11:54');
INSERT INTO `user_role` VALUES (58, 10, 2, '2021-05-22 00:30:39');
INSERT INTO `user_role` VALUES (59, 10, 3, '2021-05-22 00:30:39');
INSERT INTO `user_role` VALUES (60, 10, 17, '2021-05-22 00:30:39');
INSERT INTO `user_role` VALUES (61, 19, 2, '2021-05-22 00:31:10');
INSERT INTO `user_role` VALUES (62, 19, 3, '2021-05-22 00:31:10');
INSERT INTO `user_role` VALUES (63, 19, 17, '2021-05-22 00:31:10');

SET FOREIGN_KEY_CHECKS = 1;
