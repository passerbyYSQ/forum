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

 Date: 16/04/2022 21:36:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attendance
-- ----------------------------
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(10) UNSIGNED NOT NULL,
  `attend_date` date NOT NULL COMMENT '签到的日期，独立出来方便查询',
  `attend_time` time(0) NOT NULL COMMENT '签到的时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attendance
-- ----------------------------
INSERT INTO `attendance` VALUES (1, 21, '2021-07-04', '16:57:18');
INSERT INTO `attendance` VALUES (2, 22, '2021-07-04', '16:58:37');
INSERT INTO `attendance` VALUES (3, 21, '2021-07-03', '01:00:00');
INSERT INTO `attendance` VALUES (4, 23, '2021-07-04', '20:38:49');
INSERT INTO `attendance` VALUES (8, 21, '2021-07-05', '01:15:30');
INSERT INTO `attendance` VALUES (9, 22, '2021-07-05', '01:16:09');
INSERT INTO `attendance` VALUES (10, 23, '2021-07-05', '15:41:42');
INSERT INTO `attendance` VALUES (11, 26, '2021-07-05', '16:45:49');
INSERT INTO `attendance` VALUES (12, 27, '2021-07-06', '16:34:15');
INSERT INTO `attendance` VALUES (14, 26, '2021-07-10', '15:01:47');
INSERT INTO `attendance` VALUES (15, 22, '2021-07-10', '15:58:04');
INSERT INTO `attendance` VALUES (16, 26, '2021-07-12', '10:58:48');
INSERT INTO `attendance` VALUES (17, 21, '2021-10-10', '23:56:19');
INSERT INTO `attendance` VALUES (18, 21, '2021-10-17', '00:08:57');
INSERT INTO `attendance` VALUES (19, 26, '2021-10-18', '20:10:16');
INSERT INTO `attendance` VALUES (20, 21, '2021-10-18', '21:15:22');
INSERT INTO `attendance` VALUES (21, 21, '2021-10-19', '13:08:24');
INSERT INTO `attendance` VALUES (22, 26, '2021-10-19', '19:52:28');
INSERT INTO `attendance` VALUES (23, 21, '2021-10-20', '20:11:30');
INSERT INTO `attendance` VALUES (24, 26, '2021-10-20', '20:11:40');
INSERT INTO `attendance` VALUES (25, 26, '2021-10-28', '17:31:11');
INSERT INTO `attendance` VALUES (26, 21, '2021-12-05', '14:37:18');
INSERT INTO `attendance` VALUES (27, 26, '2021-12-05', '15:12:28');
INSERT INTO `attendance` VALUES (28, 26, '2022-01-20', '23:11:32');
INSERT INTO `attendance` VALUES (29, 26, '2022-01-25', '19:03:50');
INSERT INTO `attendance` VALUES (30, 21, '2022-02-03', '21:11:21');
INSERT INTO `attendance` VALUES (31, 26, '2022-02-10', '22:57:28');
INSERT INTO `attendance` VALUES (32, 26, '2022-02-11', '00:00:18');
INSERT INTO `attendance` VALUES (33, 26, '2022-02-17', '18:06:27');
INSERT INTO `attendance` VALUES (34, 22, '2022-02-17', '18:07:13');
INSERT INTO `attendance` VALUES (35, 26, '2022-02-18', '22:09:18');
INSERT INTO `attendance` VALUES (36, 26, '2022-02-19', '23:33:50');
INSERT INTO `attendance` VALUES (37, 26, '2022-02-20', '00:34:55');
INSERT INTO `attendance` VALUES (38, 22, '2022-02-20', '15:57:35');
INSERT INTO `attendance` VALUES (39, 22, '2022-02-21', '00:29:25');
INSERT INTO `attendance` VALUES (40, 26, '2022-02-22', '00:03:01');
INSERT INTO `attendance` VALUES (41, 26, '2022-03-24', '20:07:05');
INSERT INTO `attendance` VALUES (42, 26, '2022-03-25', '00:45:27');
INSERT INTO `attendance` VALUES (43, 28, '2022-03-28', '19:00:22');
INSERT INTO `attendance` VALUES (44, 26, '2022-03-28', '19:44:34');
INSERT INTO `attendance` VALUES (45, 26, '2022-03-30', '16:53:53');
INSERT INTO `attendance` VALUES (46, 21, '2022-04-03', '21:50:58');
INSERT INTO `attendance` VALUES (47, 21, '2022-04-05', '23:08:10');
INSERT INTO `attendance` VALUES (48, 21, '2022-04-09', '23:41:48');
INSERT INTO `attendance` VALUES (49, 26, '2022-04-09', '23:48:10');
INSERT INTO `attendance` VALUES (50, 21, '2022-04-10', '01:20:13');
INSERT INTO `attendance` VALUES (51, 26, '2022-04-10', '17:17:55');
INSERT INTO `attendance` VALUES (52, 26, '2022-04-14', '15:13:16');
INSERT INTO `attendance` VALUES (53, 21, '2022-04-14', '17:41:23');
INSERT INTO `attendance` VALUES (54, 26, '2022-04-15', '01:00:35');
INSERT INTO `attendance` VALUES (55, 21, '2022-04-15', '01:29:39');
INSERT INTO `attendance` VALUES (56, 21, '2022-04-16', '00:00:27');
INSERT INTO `attendance` VALUES (57, 26, '2022-04-16', '15:49:14');
INSERT INTO `attendance` VALUES (58, 22, '2022-04-16', '15:57:07');

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
-- Table structure for chat_friend
-- ----------------------------
DROP TABLE IF EXISTS `chat_friend`;
CREATE TABLE `chat_friend`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `my_id` int(10) UNSIGNED NOT NULL COMMENT '我的用户id',
  `friend_id` int(10) UNSIGNED NOT NULL COMMENT '好友的用户id',
  `friend_group_id` int(11) NULL DEFAULT NULL COMMENT '好友分组的id。如果为null表示未分组',
  `alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '好友的备注名',
  `create_time` datetime(0) NOT NULL COMMENT '成为好友额时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '好友关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_friend
-- ----------------------------
INSERT INTO `chat_friend` VALUES (9, 26, 23, NULL, NULL, '2022-04-02 15:41:02');
INSERT INTO `chat_friend` VALUES (10, 23, 26, NULL, NULL, '2022-04-02 15:42:16');
INSERT INTO `chat_friend` VALUES (13, 26, 21, 19, NULL, '2022-04-02 16:41:12');
INSERT INTO `chat_friend` VALUES (14, 21, 26, NULL, NULL, '2022-04-02 16:41:25');
INSERT INTO `chat_friend` VALUES (15, 26, 27, 19, NULL, '2022-04-02 16:42:05');
INSERT INTO `chat_friend` VALUES (16, 27, 16, NULL, NULL, '2022-04-02 16:42:15');
INSERT INTO `chat_friend` VALUES (25, 26, 28, 21, NULL, '2022-04-07 03:40:52');
INSERT INTO `chat_friend` VALUES (26, 28, 26, NULL, NULL, '2022-04-07 03:40:52');

-- ----------------------------
-- Table structure for chat_friend_apply
-- ----------------------------
DROP TABLE IF EXISTS `chat_friend_apply`;
CREATE TABLE `chat_friend_apply`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sender_id` int(10) UNSIGNED NOT NULL COMMENT '发送者的用户id',
  `receiver_id` int(10) UNSIGNED NOT NULL COMMENT '接收者的用户id',
  `friend_group_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '好友分组的id。如果为null表示未分组',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '好友申请的内容',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态。0：拒绝；1：同意',
  `apply_time` datetime(0) NOT NULL COMMENT '申请时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '好友申请表。好友同意和拒绝后，对方签收后才删除对应；忽略后，立即删除' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_friend_apply
-- ----------------------------
INSERT INTO `chat_friend_apply` VALUES (1, 26, 23, 1, '哈哈哈哈哈哈哈哈哈哈', 0, '2022-03-27 22:16:10');
INSERT INTO `chat_friend_apply` VALUES (2, 26, 28, 1, '你好啊', 1, '2022-03-29 21:06:00');
INSERT INTO `chat_friend_apply` VALUES (4, 22, 26, 1, '交个朋友呗！！！', NULL, '2022-03-29 21:08:03');
INSERT INTO `chat_friend_apply` VALUES (6, 23, 26, 3, '【有效测试数据】', 1, '2022-03-30 23:39:14');
INSERT INTO `chat_friend_apply` VALUES (8, 21, 26, NULL, '已经是好友', NULL, '2022-04-02 17:07:58');
INSERT INTO `chat_friend_apply` VALUES (12, 28, 26, NULL, '', 1, '2022-04-07 03:40:52');

-- ----------------------------
-- Table structure for chat_friend_group
-- ----------------------------
DROP TABLE IF EXISTS `chat_friend_group`;
CREATE TABLE `chat_friend_group`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '好友分组的名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '好友分组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_friend_group
-- ----------------------------
INSERT INTO `chat_friend_group` VALUES (3, 23, '分组3');
INSERT INTO `chat_friend_group` VALUES (19, 26, '随便');
INSERT INTO `chat_friend_group` VALUES (20, 26, '【测试分组】');
INSERT INTO `chat_friend_group` VALUES (21, 26, '右键');

-- ----------------------------
-- Table structure for chat_friend_msg
-- ----------------------------
DROP TABLE IF EXISTS `chat_friend_msg`;
CREATE TABLE `chat_friend_msg`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息的唯一id，由应用层生成',
  `sender_id` int(10) UNSIGNED NOT NULL COMMENT '发送者的用户id',
  `receiver_id` int(10) UNSIGNED NOT NULL COMMENT '接收者的用户id',
  `content` varchar(10240) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息的内容',
  `sign_flag` tinyint(4) NOT NULL COMMENT '是否已签收。0：未签收；1：已签收',
  `create_time` datetime(0) NOT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '单聊消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_friend_msg
-- ----------------------------
INSERT INTO `chat_friend_msg` VALUES ('087EE82189E042D08907B7D69FFFB287', 21, 26, '1111', 1, '2022-04-03 21:59:31');
INSERT INTO `chat_friend_msg` VALUES ('1700FFEB217449F48FF58A315E73369E', 26, 21, 'eeeee', 1, '2022-04-05 22:56:58');
INSERT INTO `chat_friend_msg` VALUES ('18B21B828B694A8882767617B91EC95E', 26, 21, 'face[嘻嘻] ', 1, '2022-04-03 22:03:57');
INSERT INTO `chat_friend_msg` VALUES ('1C0B31DE5730417AA75C6A483334E78F', 26, 21, '嗯嗯嗯额', 1, '2022-04-03 22:01:42');
INSERT INTO `chat_friend_msg` VALUES ('2616D070F8684F0480FF19489E25FADA', 21, 26, 'face[爱你] ', 1, '2022-04-05 23:17:21');
INSERT INTO `chat_friend_msg` VALUES ('2BFD6418C3B84C3297A7A2ED463F88AE', 26, 21, '呃呃呃呃呃', 1, '2022-04-03 22:07:59');
INSERT INTO `chat_friend_msg` VALUES ('35C7F0A4F5F247DA9DD2889BF5DF1646', 21, 26, 'face[右哼哼]  哈哈哈哈，你好可爱啊', 1, '2022-04-03 22:04:24');
INSERT INTO `chat_friend_msg` VALUES ('3EC8DBEA4E674359BBFFCD21842D70BC', 26, 21, '消息推送成功111111', 1, '2022-04-03 22:03:24');
INSERT INTO `chat_friend_msg` VALUES ('3F81F2D64EA0421895FF692392E4AA11', 26, 21, '有点慢啊', 1, '2022-04-08 17:06:03');
INSERT INTO `chat_friend_msg` VALUES ('472CC9287A31436F8E243E8D10597BA5', 21, 26, '额', 1, '2022-04-05 15:27:04');
INSERT INTO `chat_friend_msg` VALUES ('4F2399288EFE4C3081A081546991D90B', 21, 26, 'img[https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/2d57a30a5b02498c83abcc3b42fca418.png?timestamp=1649522441543]', 1, '2022-04-10 00:40:42');
INSERT INTO `chat_friend_msg` VALUES ('57A29CAB2B0F460F876DD3B432D0A2BB', 21, 26, '测试实时性', 1, '2022-04-07 21:26:12');
INSERT INTO `chat_friend_msg` VALUES ('5D4E688C166B4BBFAFDF778B4468D425', 21, 26, '[pre class=layui-code]http://localhost:8080/chathttp://localhost:8080/chathttp://localhost:8080/chat[/pre]', 1, '2022-04-05 23:17:08');
INSERT INTO `chat_friend_msg` VALUES ('5D51304D87724AEDB58F922C490AD7A1', 21, 26, '11111', 1, '2022-04-03 21:56:36');
INSERT INTO `chat_friend_msg` VALUES ('6D7AABE192A94AD694B031CB894BBD66', 26, 21, '嗯嗯嗯额', 1, '2022-04-03 22:01:51');
INSERT INTO `chat_friend_msg` VALUES ('6E03954D425144FDB4F460926DDBDA5E', 21, 26, '1111111', 1, '2022-04-03 21:55:01');
INSERT INTO `chat_friend_msg` VALUES ('8E83030CA17247928E1D159F4C9996EB', 26, 21, '好像还挺快啊', 1, '2022-04-07 21:26:25');
INSERT INTO `chat_friend_msg` VALUES ('A2F24A18C51D4C1498481D0FDA2689BD', 21, 26, '呃呃呃呃呃呃呃', 1, '2022-04-03 21:59:09');
INSERT INTO `chat_friend_msg` VALUES ('A814853759B9497CBBCEEB3E3F21942F', 21, 26, '111111111', 1, '2022-04-03 21:55:16');
INSERT INTO `chat_friend_msg` VALUES ('AB29C328CAA247CB9DC6D5D170A12DC3', 21, 26, '哈哈哈', 1, '2022-04-09 23:53:39');
INSERT INTO `chat_friend_msg` VALUES ('AB6CAAC8E5FE4774AD972B2DE828D007', 21, 26, 'img[https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/bd7c043444174116a9195352f348763c.png?timestamp=1649171810254]', 1, '2022-04-05 23:16:50');
INSERT INTO `chat_friend_msg` VALUES ('AD17D883C89640239BC7FE111522FAFF', 21, 26, '呃呃呃', 1, '2022-04-05 23:17:48');
INSERT INTO `chat_friend_msg` VALUES ('AD7E08B74E354CFFA0CAAD0C3CDF1C38', 26, 21, '呃呃呃呃呃', 1, '2022-04-05 23:17:52');
INSERT INTO `chat_friend_msg` VALUES ('B179ACBC6BF04EA09DD7646C7A12E644', 26, 21, '额，这铃声有点无语', 1, '2022-04-08 17:06:29');
INSERT INTO `chat_friend_msg` VALUES ('B44EA72282B84692AFDAD6414C485837', 26, 21, 'face[哈哈] ', 1, '2022-04-05 23:17:25');
INSERT INTO `chat_friend_msg` VALUES ('BDF359E633514D3CAAF899BCD8DBE8A5', 21, 26, '准备上线了有点小激动', 1, '2022-04-08 17:06:14');
INSERT INTO `chat_friend_msg` VALUES ('C2D03027938445ABBF0F72D1D2585040', 26, 21, '【】【】【】', 1, '2022-04-05 22:57:03');
INSERT INTO `chat_friend_msg` VALUES ('C6EEE02677604FAE947F2FD89E539AB8', 21, 26, 'eeeee', 1, '2022-04-05 22:56:55');
INSERT INTO `chat_friend_msg` VALUES ('C8FC3717648546EEA782BFDDA732F3F5', 26, 21, '哈哈哈哈', 1, '2022-04-03 22:01:47');
INSERT INTO `chat_friend_msg` VALUES ('C975FD2275EA4108BD53462F399557BD', 21, 26, '【消息签收】', 1, '2022-04-05 15:26:55');
INSERT INTO `chat_friend_msg` VALUES ('D0BD90157C69423B837145BB950CBB94', 21, 26, 'svsdvsdv', 1, '2022-04-05 22:57:06');
INSERT INTO `chat_friend_msg` VALUES ('E458364CE30741B68ABD76153240FDB4', 26, 21, '消息推送成功了', 1, '2022-04-03 22:03:05');
INSERT INTO `chat_friend_msg` VALUES ('EECE944126CC483AB097C439F6A7837B', 26, 21, '能聊天不', 1, '2022-04-09 23:53:46');

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
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '收藏\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES (7, 22, 2, '2021-06-30 10:44:06');
INSERT INTO `collect` VALUES (13, 26, 12, '2021-06-30 23:12:39');
INSERT INTO `collect` VALUES (14, 22, 12, '2021-07-02 11:56:50');
INSERT INTO `collect` VALUES (15, 21, 2, '2021-07-04 15:19:07');
INSERT INTO `collect` VALUES (16, 23, 8, '2021-07-05 15:43:24');
INSERT INTO `collect` VALUES (17, 26, 2, '2021-10-18 20:38:05');
INSERT INTO `collect` VALUES (18, 26, 17, '2021-10-19 23:33:25');
INSERT INTO `collect` VALUES (19, 21, 18, '2021-12-05 14:50:00');
INSERT INTO `collect` VALUES (20, 26, 11, '2021-12-05 15:04:50');
INSERT INTO `collect` VALUES (21, 26, 13, '2021-12-05 15:05:45');

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
) ENGINE = InnoDB AUTO_INCREMENT = 81 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论通知' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment_notification
-- ----------------------------
INSERT INTO `comment_notification` VALUES (1, 22, 1, 0, 2, 5, '2021-06-21 00:29:59', 0);
INSERT INTO `comment_notification` VALUES (2, 22, 1, 0, 2, 6, '2021-06-21 00:30:52', 0);
INSERT INTO `comment_notification` VALUES (3, 22, 1, 0, 2, 7, '2021-06-21 00:31:55', 0);
INSERT INTO `comment_notification` VALUES (4, 22, 1, 0, 2, 8, '2021-06-21 00:32:19', 0);
INSERT INTO `comment_notification` VALUES (5, 22, 1, 0, 2, 9, '2021-06-21 23:38:23', 0);
INSERT INTO `comment_notification` VALUES (6, 22, 1, 0, 2, 10, '2021-06-21 23:49:56', 0);
INSERT INTO `comment_notification` VALUES (7, 22, 1, 0, 2, 11, '2021-06-21 23:50:12', 0);
INSERT INTO `comment_notification` VALUES (8, 22, 1, 0, 2, 12, '2021-06-21 23:50:24', 0);
INSERT INTO `comment_notification` VALUES (9, 22, 1, 0, 2, 13, '2021-06-21 23:51:39', 0);
INSERT INTO `comment_notification` VALUES (10, 22, 1, 0, 2, 14, '2021-06-21 23:52:16', 0);
INSERT INTO `comment_notification` VALUES (11, 22, 1, 0, 2, 15, '2021-06-21 23:56:50', 0);
INSERT INTO `comment_notification` VALUES (12, 22, 1, 0, 2, 16, '2021-06-21 23:58:36', 0);
INSERT INTO `comment_notification` VALUES (13, 22, 1, 0, 2, 17, '2021-06-21 23:59:36', 0);
INSERT INTO `comment_notification` VALUES (14, 22, 1, 0, 2, 18, '2021-06-22 00:02:43', 0);
INSERT INTO `comment_notification` VALUES (15, 22, 1, 0, 2, 19, '2021-06-22 00:04:19', 0);
INSERT INTO `comment_notification` VALUES (16, 22, 1, 0, 2, 20, '2021-06-22 00:14:22', 0);
INSERT INTO `comment_notification` VALUES (17, 22, 1, 0, 2, 21, '2021-06-22 00:15:36', 0);
INSERT INTO `comment_notification` VALUES (18, 22, 1, 0, 2, 22, '2021-06-22 00:16:52', 0);
INSERT INTO `comment_notification` VALUES (19, 22, 1, 0, 2, 23, '2021-06-22 00:22:12', 0);
INSERT INTO `comment_notification` VALUES (20, 22, 1, 0, 2, 24, '2021-06-22 00:31:57', 0);
INSERT INTO `comment_notification` VALUES (21, 22, 1, 0, 2, 25, '2021-06-22 00:36:21', 0);
INSERT INTO `comment_notification` VALUES (22, 22, 1, 0, 2, 26, '2021-06-22 00:37:44', 0);
INSERT INTO `comment_notification` VALUES (23, 22, 1, 0, 2, 27, '2021-06-22 00:37:58', 0);
INSERT INTO `comment_notification` VALUES (24, 22, 1, 0, 2, 28, '2021-06-22 00:39:20', 0);
INSERT INTO `comment_notification` VALUES (25, 22, 1, 0, 2, 29, '2021-06-22 00:41:19', 0);
INSERT INTO `comment_notification` VALUES (26, 22, 1, 0, 2, 30, '2021-06-22 00:58:21', 0);
INSERT INTO `comment_notification` VALUES (27, 22, 1, 0, 2, 31, '2021-06-22 10:49:30', 0);
INSERT INTO `comment_notification` VALUES (28, 22, 1, 0, 2, 32, '2021-06-22 10:50:51', 0);
INSERT INTO `comment_notification` VALUES (29, 22, 1, 0, 2, 33, '2021-06-22 22:00:24', 0);
INSERT INTO `comment_notification` VALUES (30, 26, 1, 0, 2, 34, '2021-06-22 23:33:02', 0);
INSERT INTO `comment_notification` VALUES (31, 22, 26, 2, 2, 4, '2021-06-23 19:13:21', 0);
INSERT INTO `comment_notification` VALUES (32, 22, 26, 2, 2, 15, '2021-06-23 19:37:22', 0);
INSERT INTO `comment_notification` VALUES (33, 22, 26, 2, 2, 18, '2021-06-23 19:42:03', 0);
INSERT INTO `comment_notification` VALUES (34, 26, 22, 1, 5, 22, '2021-06-23 22:01:34', 0);
INSERT INTO `comment_notification` VALUES (35, 26, 22, 1, 6, 23, '2021-06-23 22:04:52', 0);
INSERT INTO `comment_notification` VALUES (36, 26, 22, 1, 5, 25, '2021-06-23 22:12:00', 0);
INSERT INTO `comment_notification` VALUES (37, 26, 22, 1, 8, 26, '2021-06-23 22:26:50', 0);
INSERT INTO `comment_notification` VALUES (38, 26, 22, 1, 7, 27, '2021-06-23 22:27:56', 0);
INSERT INTO `comment_notification` VALUES (39, 26, 22, 1, 5, 29, '2021-06-23 22:31:03', 0);
INSERT INTO `comment_notification` VALUES (40, 26, 22, 1, 5, 30, '2021-06-23 22:31:17', 0);
INSERT INTO `comment_notification` VALUES (41, 26, 22, 1, 5, 31, '2021-06-23 22:32:13', 0);
INSERT INTO `comment_notification` VALUES (42, 26, 22, 1, 9, 32, '2021-06-23 22:46:08', 0);
INSERT INTO `comment_notification` VALUES (43, 26, 22, 1, 10, 33, '2021-06-23 22:49:37', 0);
INSERT INTO `comment_notification` VALUES (44, 26, 22, 1, 10, 34, '2021-06-23 22:55:24', 0);
INSERT INTO `comment_notification` VALUES (45, 26, 22, 1, 10, 35, '2021-06-23 23:28:56', 0);
INSERT INTO `comment_notification` VALUES (46, 26, 22, 1, 9, 36, '2021-06-23 23:29:29', 0);
INSERT INTO `comment_notification` VALUES (47, 26, 1, 0, 2, 35, '2021-06-24 00:15:58', 0);
INSERT INTO `comment_notification` VALUES (48, 26, 22, 1, 5, 37, '2021-06-24 16:32:25', 0);
INSERT INTO `comment_notification` VALUES (49, 26, 22, 1, 6, 38, '2021-06-24 16:33:19', 0);
INSERT INTO `comment_notification` VALUES (50, 26, 1, 0, 2, 36, '2021-06-24 16:34:19', 0);
INSERT INTO `comment_notification` VALUES (51, 26, 1, 0, 2, 37, '2021-06-24 16:35:09', 0);
INSERT INTO `comment_notification` VALUES (52, 26, 1, 0, 2, 38, '2021-06-24 16:36:32', 0);
INSERT INTO `comment_notification` VALUES (53, 26, 1, 0, 2, 39, '2021-06-24 16:36:56', 0);
INSERT INTO `comment_notification` VALUES (54, 26, 1, 0, 2, 40, '2021-06-24 20:40:46', 0);
INSERT INTO `comment_notification` VALUES (55, 22, 26, 2, 41, 42, '2021-06-24 21:43:31', 0);
INSERT INTO `comment_notification` VALUES (56, 22, 26, 0, 13, 43, '2021-06-24 21:45:42', 0);
INSERT INTO `comment_notification` VALUES (57, 26, 22, 1, 5, 45, '2021-06-29 22:07:43', 0);
INSERT INTO `comment_notification` VALUES (58, 26, 22, 1, 5, 46, '2021-06-29 22:07:51', 0);
INSERT INTO `comment_notification` VALUES (59, 26, 22, 1, 5, 47, '2021-06-29 22:41:14', 0);
INSERT INTO `comment_notification` VALUES (60, 26, 22, 1, 43, 49, '2021-06-30 12:48:19', 0);
INSERT INTO `comment_notification` VALUES (61, 26, 22, 0, 12, 44, '2021-06-30 22:28:26', 0);
INSERT INTO `comment_notification` VALUES (62, 26, 22, 0, 12, 45, '2021-06-30 23:08:41', 0);
INSERT INTO `comment_notification` VALUES (63, 26, 22, 0, 12, 46, '2021-06-30 23:09:20', 0);
INSERT INTO `comment_notification` VALUES (64, 26, 22, 0, 12, 48, '2021-06-30 23:10:21', 0);
INSERT INTO `comment_notification` VALUES (65, 26, 22, 0, 12, 49, '2021-06-30 23:12:24', 0);
INSERT INTO `comment_notification` VALUES (66, 22, 26, 1, 40, 51, '2021-07-01 21:52:16', 0);
INSERT INTO `comment_notification` VALUES (67, 22, 26, 1, 44, 52, '2021-07-02 11:56:33', 0);
INSERT INTO `comment_notification` VALUES (68, 22, 1, 0, 7, 50, '2021-07-02 23:50:10', 0);
INSERT INTO `comment_notification` VALUES (69, 22, 1, 0, 8, 51, '2021-07-02 23:57:16', 0);
INSERT INTO `comment_notification` VALUES (70, 21, 22, 0, 11, 52, '2021-07-04 21:24:20', 0);
INSERT INTO `comment_notification` VALUES (71, 21, 22, 1, 53, 53, '2021-07-04 21:25:53', 0);
INSERT INTO `comment_notification` VALUES (72, 21, 22, 2, 54, 55, '2021-07-04 21:26:32', 0);
INSERT INTO `comment_notification` VALUES (73, 21, 22, 2, 54, 56, '2021-07-04 21:29:47', 0);
INSERT INTO `comment_notification` VALUES (74, 26, 21, 0, 16, 55, '2021-10-19 19:54:16', 0);
INSERT INTO `comment_notification` VALUES (75, 26, 21, 1, 54, 58, '2021-10-19 19:54:57', 0);
INSERT INTO `comment_notification` VALUES (76, 26, 21, 0, 17, 56, '2021-10-19 23:33:38', 0);
INSERT INTO `comment_notification` VALUES (77, 26, 21, 0, 17, 57, '2021-10-19 23:34:21', 0);
INSERT INTO `comment_notification` VALUES (78, 26, 21, 1, 58, 59, '2021-10-19 23:35:29', 0);
INSERT INTO `comment_notification` VALUES (79, 26, 21, 1, 58, 60, '2021-10-19 23:35:42', 0);
INSERT INTO `comment_notification` VALUES (80, 21, 26, 1, 74, 92, '2022-04-14 17:41:05', 0);

-- ----------------------------
-- Table structure for danmu_msg
-- ----------------------------
DROP TABLE IF EXISTS `danmu_msg`;
CREATE TABLE `danmu_msg`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '弹幕的唯一id，由应用层生成',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '弹幕的文本内容',
  `start_ms` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '弹幕在视频的哪一毫秒进入',
  `video_id` int(11) NOT NULL COMMENT '视频的id',
  `creator_id` int(11) NOT NULL COMMENT '发布者的id，发弹幕需要登录',
  `create_time` datetime(0) NOT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of danmu_msg
-- ----------------------------
INSERT INTO `danmu_msg` VALUES ('0A2938E7602F4706A019DFA0D592472F', '测试弹幕。。。', 6422, 1, 26, '2022-02-07 22:06:44');
INSERT INTO `danmu_msg` VALUES ('0C82A1672B5B4A7D846A72B89EBEE1DA', '有点慢啊', 1480, 1, 26, '2022-02-11 00:41:43');
INSERT INTO `danmu_msg` VALUES ('0EA7BE601D174C62AAC6820F5178E4F0', '测试', 25104, 1, 26, '2022-02-24 00:21:33');
INSERT INTO `danmu_msg` VALUES ('11C08606F704455FAB8F48ED04E9C8EF', '嗯嗯嗯额', 51759, 1, 28, '2022-04-03 00:29:16');
INSERT INTO `danmu_msg` VALUES ('14521513063A4ACA8198B0B90429731A', '你好啊', 5080, 1, 21, '2022-02-07 20:34:49');
INSERT INTO `danmu_msg` VALUES ('154CD86377B84A598803DE479C2F4A1C', 'hello', 558, 1, 22, '2022-03-01 01:17:40');
INSERT INTO `danmu_msg` VALUES ('1582B065AE4D42E9A7368C62CEC8576D', '一秒好像都不用啊', 1489, 1, 26, '2022-02-09 15:24:46');
INSERT INTO `danmu_msg` VALUES ('1E41CBCDB2484325B9793B01231BFE02', '怎么出不来', 24600, 1, 26, '2022-02-10 22:59:27');
INSERT INTO `danmu_msg` VALUES ('1FDCDF8D8CD94EC08E671BD87868EBE3', '呃呃呃呃呃', 41293, 1, 28, '2022-04-02 23:49:37');
INSERT INTO `danmu_msg` VALUES ('29F09E2BEF7D49AEB0AC71BAEDDCACB0', '11111111', 7711, 1, 28, '2022-04-03 00:02:28');
INSERT INTO `danmu_msg` VALUES ('2A9077067CBB4CD39780DEF7191D6EE2', '哈哈哈？', 5445, 1, 26, '2022-02-10 22:59:16');
INSERT INTO `danmu_msg` VALUES ('2BB7626D57F647AAA609EF1C738F71C0', '有了缓存还这么慢，主要是请求了3次redis', 0, 1, 26, '2022-02-17 22:15:56');
INSERT INTO `danmu_msg` VALUES ('2DB059CBFD35457FA26667D3C2388EC5', '嗯嗯嗯额', 41293, 1, 28, '2022-04-02 23:53:42');
INSERT INTO `danmu_msg` VALUES ('2DCD439C658F41CE9BB6B897A691215B', '缓存', 1253, 1, 26, '2022-02-17 22:15:25');
INSERT INTO `danmu_msg` VALUES ('2DD218EEA8CF4B1FB9FB9357EAFF683A', '测试。。。', 11891, 1, 26, '2022-02-07 20:34:36');
INSERT INTO `danmu_msg` VALUES ('383ADDE7409041509D24C177C508F778', 'eeeeee', 50951, 1, 28, '2022-04-03 00:24:18');
INSERT INTO `danmu_msg` VALUES ('3C23C8D93ACA472D82F7BCB298D5DD13', '我要抢沙发', 2586, 2, 21, '2022-02-07 20:36:19');
INSERT INTO `danmu_msg` VALUES ('405CE6BE98BE4B53970EA6FB22A11D85', 'efvv', 1942, 1, 22, '2022-03-02 17:34:00');
INSERT INTO `danmu_msg` VALUES ('466774F470A24478A8AEF667CF313369', '1111', 50951, 1, 28, '2022-04-03 00:24:46');
INSERT INTO `danmu_msg` VALUES ('4822327BF80C476EB15C15759228235D', '地方不方便的辅导班电饭煲电饭煲', 41293, 1, 28, '2022-04-02 23:58:27');
INSERT INTO `danmu_msg` VALUES ('49DB6F805F1D4F01824599B201697C82', '有了缓存还这么慢', 25104, 1, 22, '2022-02-17 22:15:36');
INSERT INTO `danmu_msg` VALUES ('4D8A98165BD7452EA6C19A8EE40F5683', '111', 41293, 1, 28, '2022-04-02 23:52:59');
INSERT INTO `danmu_msg` VALUES ('4ED66BEB8D9A4E71874AB169EC680D8E', '测试', 2282, 1, 26, '2022-02-11 00:41:31');
INSERT INTO `danmu_msg` VALUES ('4EF42B2EED674BD88A9BB61E47DCE391', '你好啊', 1489, 1, 26, '2022-02-09 15:23:11');
INSERT INTO `danmu_msg` VALUES ('4F1AE7BF3F484EB7AA61E986794CECE9', '延迟挺低的', 1489, 1, 26, '2022-02-09 15:24:11');
INSERT INTO `danmu_msg` VALUES ('4FBE832FB0B54E24B604BD5DFD597BD7', '由于转发会存在多次认证', 1159, 1, 26, '2022-02-11 22:04:12');
INSERT INTO `danmu_msg` VALUES ('58B46C8F8CF54BEDB47E4E30BE7A1565', '1111', 41293, 1, 28, '2022-04-02 23:50:07');
INSERT INTO `danmu_msg` VALUES ('59020307CC204F60850146B3B1267FAC', '代码迭代了好多次了', 1159, 1, 26, '2022-02-11 22:04:50');
INSERT INTO `danmu_msg` VALUES ('5D74266F14A34CBCB0583ADAFBE512D0', '靓仔', 51300, 1, 22, '2022-03-02 15:11:57');
INSERT INTO `danmu_msg` VALUES ('654AF4AF864F444E90A8A3E5B487BFEC', '怎么回事啊', 775, 1, 26, '2022-02-10 22:59:58');
INSERT INTO `danmu_msg` VALUES ('6632FDE18C1A4E6AB9B5532CAAB08A76', '没有缓存时有点慢啊', 1246, 1, 26, '2022-02-11 22:03:45');
INSERT INTO `danmu_msg` VALUES ('6647A8D5E40841B2B45FEEF286843F5B', '去除单张号登录', 2282, 1, 26, '2022-02-11 00:42:02');
INSERT INTO `danmu_msg` VALUES ('6C575C8A986248AAA7B917EDB2C6781B', 'dfbfbnrt', 2984, 1, 26, '2022-03-14 14:57:49');
INSERT INTO `danmu_msg` VALUES ('768D320AABA74879BB5C824F3EA05658', '哈哈哈，还挺快啊', 3984, 1, 21, '2022-02-09 15:24:02');
INSERT INTO `danmu_msg` VALUES ('7844333D8265428E9AA6116210CD6727', '？？？？', 47402, 1, 22, '2022-02-24 00:25:57');
INSERT INTO `danmu_msg` VALUES ('7DE3A5536DA04B82B49617952C3312A6', '离谱离谱', 5152, 1, 26, '2022-03-02 15:11:46');
INSERT INTO `danmu_msg` VALUES ('7E175F9D5F824E76BEEB1F5862DC6B3C', '哈哈哈哈?', 6139, 1, 22, '2022-02-24 00:23:41');
INSERT INTO `danmu_msg` VALUES ('7EACDBF6599B4B2C9BF5A8F83EEEFD73', '啊啊啊啊啊', 41293, 1, 28, '2022-04-02 23:56:19');
INSERT INTO `danmu_msg` VALUES ('8675384207E84B9A9FECCB1FA72753D8', '一直在演变迭代', 1246, 1, 26, '2022-02-11 22:05:07');
INSERT INTO `danmu_msg` VALUES ('8AD6F684193447E3A24030F2B98214CA', 'aaaa', 5391, 1, 28, '2022-04-03 00:01:08');
INSERT INTO `danmu_msg` VALUES ('8B2F22C6CD7B414E88560DEBD6C0FE87', '几次redis', 1159, 1, 26, '2022-02-11 22:05:39');
INSERT INTO `danmu_msg` VALUES ('8B9B9A751ADC4D97AAD92FE0D22381A5', '？？？？？', 17719, 1, 22, '2022-02-24 00:23:53');
INSERT INTO `danmu_msg` VALUES ('931A44C462E540B8937F8DAFCAB34A97', '代码演变迭代了好多次', 1489, 1, 26, '2022-02-09 15:25:35');
INSERT INTO `danmu_msg` VALUES ('93E682DA3DB141C586C998A021BE6565', '测试', 361, 1, 26, '2022-02-07 20:37:59');
INSERT INTO `danmu_msg` VALUES ('968B1DD08A4D4551B4749CD842C24661', '11', 41293, 1, 28, '2022-04-02 23:54:46');
INSERT INTO `danmu_msg` VALUES ('9DAE1F684F124BA5A00D80A17E810977', '怎么会断开呢？', 25769, 1, 26, '2022-02-24 00:24:02');
INSERT INTO `danmu_msg` VALUES ('A05551778D724ED38E81EC17814942FF', '你好啊', 1489, 1, 26, '2022-02-09 15:23:48');
INSERT INTO `danmu_msg` VALUES ('A4A68186F18B4F7384972D03E750A912', '【毫秒吗】', 41759, 1, 28, '2022-04-03 00:29:25');
INSERT INTO `danmu_msg` VALUES ('A56CEE5BC35846E8B617CAD7F1D1DA60', 'fgnmfgm', 44854, 1, 26, '2022-03-01 01:58:13');
INSERT INTO `danmu_msg` VALUES ('A7C7AB3431884CECB5B8D0AE24EC3DD9', '好了好了，散了吧。bug修完了', 3984, 1, 21, '2022-02-09 15:25:16');
INSERT INTO `danmu_msg` VALUES ('A9E0F953F5674D74A295F71FC266C556', '测试', 1253, 1, 26, '2022-02-17 22:15:07');
INSERT INTO `danmu_msg` VALUES ('ACD8059F6AD44887A012265030897D6F', '测试一下长文本测试一下长文本测试一下长文本测试一下长文本测试一下长文本', 0, 1, 26, '2022-02-09 15:25:54');
INSERT INTO `danmu_msg` VALUES ('B05B757EF17C4B7081F350A735C34954', '帅啊', 50951, 1, 28, '2022-04-03 00:25:22');
INSERT INTO `danmu_msg` VALUES ('B927D97DC7A44C9480BC7D7C519D991A', '测试测试', 6203, 1, 26, '2022-02-10 22:58:14');
INSERT INTO `danmu_msg` VALUES ('BDF689059F004288A88ACEA3BF77BEFA', '您是DVD', 5063, 1, 26, '2022-03-02 17:33:26');
INSERT INTO `danmu_msg` VALUES ('BEE94C8AB39743E593627FC597BD37E4', 'zhuanffa', 25104, 1, 22, '2022-02-17 22:15:14');
INSERT INTO `danmu_msg` VALUES ('C2F80D52763546E581804D99F0576081', '1秒左右吧', 3984, 1, 21, '2022-02-09 15:24:26');
INSERT INTO `danmu_msg` VALUES ('C93DD8C4D3B94F4EB912E08C07F8AE0C', '有了缓存之后快很多啊', 5080, 1, 21, '2022-02-07 20:35:07');
INSERT INTO `danmu_msg` VALUES ('CCE2F2A8B3B647D09A5B726B49BE4D21', '测试测试', 24600, 1, 26, '2022-02-10 23:00:04');
INSERT INTO `danmu_msg` VALUES ('D2FF3C41781041B58C4718DB22E3703F', '多次请求', 1246, 1, 26, '2022-02-11 22:04:24');
INSERT INTO `danmu_msg` VALUES ('D5493EDAC3524E7EBE2F4842FEFCBCEC', '怎么回事', 34435, 1, 26, '2022-02-24 00:26:08');
INSERT INTO `danmu_msg` VALUES ('DAA2757D4E70460FBD82D0A3AD1C49A0', '111', 41293, 1, 28, '2022-04-02 23:52:28');
INSERT INTO `danmu_msg` VALUES ('DAAEC6F63B734175BBDF0810BBA37F50', '你在干嘛啊', 11891, 1, 26, '2022-02-07 20:34:57');
INSERT INTO `danmu_msg` VALUES ('DAD4A7F6D17941209D7E42ED489964C7', '没有缓存会慢一些', 11891, 1, 26, '2022-02-07 20:35:19');
INSERT INTO `danmu_msg` VALUES ('DCC04B64C40E4B639D081658961193E5', '哈哈哈哈', 11891, 1, 26, '2022-02-07 20:38:17');
INSERT INTO `danmu_msg` VALUES ('DCD7D5C47CF748BC9C4CB3406C28F4D5', '测试测试', 25104, 1, 26, '2022-02-24 00:21:24');
INSERT INTO `danmu_msg` VALUES ('E5A4FCE09CD34BABBB9C9607A1F7AEC0', 'ceshi', 1159, 1, 26, '2022-02-11 22:03:30');
INSERT INTO `danmu_msg` VALUES ('E8FD70B445234AF4BF2A71D377F40901', 'fbe', 1320, 1, 26, '2022-03-24 20:51:34');
INSERT INTO `danmu_msg` VALUES ('F22F12AF30B74174A65FBE3338087C13', '你好啊', 10665, 1, 26, '2022-03-01 01:17:30');
INSERT INTO `danmu_msg` VALUES ('F747AAB0C1984326A181AFC34D29105B', '还行还行，延迟不是很高', 5080, 1, 21, '2022-02-07 20:35:33');
INSERT INTO `danmu_msg` VALUES ('F8A4657C46BA42B9BBE18EAE83401816', '机制勇敢的我', 17284, 1, 26, '2022-04-03 00:25:00');

-- ----------------------------
-- Table structure for db_file
-- ----------------------------
DROP TABLE IF EXISTS `db_file`;
CREATE TABLE `db_file`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `origin_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上传时的原始文件名',
  `rand_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上传后生成的随机文件名',
  `file_md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件的md5',
  `total_bytes` bigint(20) NOT NULL COMMENT '总字节数',
  `uploaded_bytes` bigint(20) NOT NULL COMMENT '已上传的字节数',
  `creator_id` int(11) NOT NULL COMMENT '第一次上传该文件的用户id',
  `local_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件在服务器本地的路径',
  `oss_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '云存储',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_file
-- ----------------------------
INSERT INTO `db_file` VALUES (1, '西瓜视频介绍', 'xigua_video', '123', 1024, 1024, 1, NULL, 'https://sf1-cdn-tos.huoshanstatic.com/obj/media-fe/xgplayer_doc_video/mp4/xgplayer-demo-720p.mp4', '2022-01-26 17:10:12', '2022-01-26 17:10:17');
INSERT INTO `db_file` VALUES (2, '思迈特2021-freshman', 'smartbi2021-freshman', '123', 1024, 1024, 1, NULL, 'http://v11-x.douyinvod.com/c82039c346a7657e8b6e0b981471fea0/6203861c/video/tos/cn/tos-cn-ve-15/87799cb5b76749d9b7f6f0f86e73f96b/?a=1128&br=1666&bt=1666&cd=0%7C0%7C0%7C0&ch=0&cr=0&cs=0&dr=0&ds=3&er=&ft=OR_LnaZZI02P1vTzWTh9Ixn_pbsdeR3ptqY&l=2022020916145501020208804908064975&lr=&mime_type=video_mp4&net=0&pl=0&qs=0&rc=M3R2ZTg6ZmkzNjMzNGkzM0ApM2g3aGg7NDs5NzxpMzQ3NmcpaGRqbGRoaGRmbGJpM3I0Z2ZsYC0tZC0wc3MvMjUvMjJeM2I2YC1iYzIuOmNwb2wrbStqdDo%3D&vl=&vr=', '2022-01-26 23:55:09', '2022-01-26 23:55:14');

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
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '一级评论' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of first_comment
-- ----------------------------
INSERT INTO `first_comment` VALUES (5, 22, '<p>测试测试</p>', 2, 1, 21, '2021-06-21 00:29:58');
INSERT INTO `first_comment` VALUES (6, 22, '<p>我是2楼。。。。。</p>', 2, 2, 6, '2021-06-21 00:30:51');
INSERT INTO `first_comment` VALUES (7, 22, '<p>我是3楼</p>', 2, 3, 3, '2021-06-21 00:31:54');
INSERT INTO `first_comment` VALUES (8, 22, '<p>我是4楼</p>\n<p>&nbsp;</p>\n<p>我是4楼</p>', 2, 4, 2, '2021-06-21 00:32:18');
INSERT INTO `first_comment` VALUES (9, 22, '<p>额额额额额</p>', 2, 5, 3, '2021-06-21 23:38:22');
INSERT INTO `first_comment` VALUES (10, 22, '<p>测试测试</p>', 2, 6, 8, '2021-06-21 23:49:56');
INSERT INTO `first_comment` VALUES (11, 22, '<p>测试测试</p>', 2, 7, 1, '2021-06-21 23:50:12');
INSERT INTO `first_comment` VALUES (12, 22, '<p>测试测试</p>', 2, 8, 1, '2021-06-21 23:50:24');
INSERT INTO `first_comment` VALUES (13, 22, '<p>啊啊啊啊啊啊</p>', 2, 9, 0, '2021-06-21 23:51:38');
INSERT INTO `first_comment` VALUES (14, 22, '<p>时间？？？？错误？？？？</p>', 2, 10, 0, '2021-06-21 23:52:15');
INSERT INTO `first_comment` VALUES (15, 22, '<p>发帖后所在页面</p>', 2, 11, 0, '2021-06-21 23:56:50');
INSERT INTO `first_comment` VALUES (16, 22, '<p>testdf</p>', 2, 12, 0, '2021-06-21 23:58:36');
INSERT INTO `first_comment` VALUES (17, 22, '<p>testsdbsb</p>', 2, 13, 0, '2021-06-21 23:59:36');
INSERT INTO `first_comment` VALUES (18, 22, '<p>测试asv</p>', 2, 14, 0, '2021-06-22 00:02:43');
INSERT INTO `first_comment` VALUES (19, 22, '<p>呃呃呃呃呃呃</p>', 2, 15, 0, '2021-06-22 00:04:18');
INSERT INTO `first_comment` VALUES (20, 22, '<p>asvsdbvksdvsd</p>', 2, 16, 0, '2021-06-22 00:14:22');
INSERT INTO `first_comment` VALUES (21, 22, '<p>dgmndgmgdf</p>', 2, 17, 0, '2021-06-22 00:15:36');
INSERT INTO `first_comment` VALUES (22, 22, '<p>svsdkjbvsdvs</p>', 2, 18, 0, '2021-06-22 00:16:51');
INSERT INTO `first_comment` VALUES (23, 22, '<p>svbksdvsdv</p>', 2, 19, 0, '2021-06-22 00:22:11');
INSERT INTO `first_comment` VALUES (24, 22, '<p>fddernbtrntr</p>', 2, 20, 0, '2021-06-22 00:31:56');
INSERT INTO `first_comment` VALUES (25, 22, '<p>v白色的空白VS定律</p>', 2, 21, 0, '2021-06-22 00:36:20');
INSERT INTO `first_comment` VALUES (26, 22, '<p>则必须执行在信标中学</p>', 2, 22, 0, '2021-06-22 00:37:44');
INSERT INTO `first_comment` VALUES (27, 22, '<p>三百VS打开VS豆瓣VS到了</p>', 2, 23, 0, '2021-06-22 00:37:58');
INSERT INTO `first_comment` VALUES (28, 22, '<p>？？？？？？？</p>', 2, 24, 0, '2021-06-22 00:39:19');
INSERT INTO `first_comment` VALUES (29, 22, '<p>奥斯本v卡刷包VS的</p>', 2, 25, 0, '2021-06-22 00:41:18');
INSERT INTO `first_comment` VALUES (30, 22, '<p>是代表所代表的</p>', 2, 26, 0, '2021-06-22 00:58:20');
INSERT INTO `first_comment` VALUES (31, 22, '<p>测试发帖后</p>', 2, 27, 0, '2021-06-22 10:49:29');
INSERT INTO `first_comment` VALUES (32, 22, '<p>牛逼啊🐂</p>\n<pre class=\"language-java\"><code>@GetMapping(\"/first/list\")\n    public ResultModel&lt;PageData&lt;FirstCommentDTO&gt;&gt; firstCommentList(\n            @RequestParam Integer postId, //  /detail 和 /detail/sdv 都会404\n            @RequestParam(defaultValue = \"1\") Integer page,\n            @RequestParam(defaultValue = \"10\") Integer count,\n            @RequestParam(defaultValue = \"true\") Boolean isTimeAsc) {\n        Post post = postService.getPostById(postId);\n        if (ObjectUtils.isEmpty(post)) {\n            return ResultModel.failed(StatusCode.POST_NOT_EXIST); // 帖子存在\n        }\n        if (count &lt; 1) {\n            count = 1;\n        }\n        PageData&lt;FirstCommentDTO&gt; commentList =\n                commentService.getCommentListByPostId(post, page, count, isTimeAsc);\n        return ResultModel.success(commentList);\n    }</code></pre>', 2, 28, 0, '2021-06-22 10:50:51');
INSERT INTO `first_comment` VALUES (33, 22, '<pre class=\"language-java\"><code> @GetMapping(\"/second/list\")\n    public ResultModel&lt;PageData&lt;SecondCommentDTO&gt;&gt; secondCommentList(\n            @RequestParam Integer firstCommentId,\n            @RequestParam(defaultValue = \"1\") Integer page,\n            @RequestParam(defaultValue = \"10\") Integer count) {\n        FirstComment firstComment = commentService.getFirstCommentById(firstCommentId);\n        if (ObjectUtils.isEmpty(firstComment)) {\n            return ResultModel.failed(StatusCode.FIRST_COMMENT_NOT_EXIST); // 一级评论不存在\n        }\n        if (count &lt; 1) {\n            count = 1;\n        }\n        PageData&lt;SecondCommentDTO&gt; secondCommentList =\n                commentService.getSecondCommentList(firstComment, page, count);\n        return ResultModel.success(secondCommentList);\n    }</code></pre>\n<p><img src=\"https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/df07c756ba794f8185e6f5fb56ddf47b.png?timestamp=1624370383211\" /></p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>', 2, 29, 0, '2021-06-22 22:00:23');
INSERT INTO `first_comment` VALUES (34, 26, '<p>第三方授权登录</p>', 2, 30, 0, '2021-06-22 23:33:01');
INSERT INTO `first_comment` VALUES (35, 26, '<p>&lt;script&gt;alert(\'123\');&lt;/script&gt;</p>', 2, 31, 0, '2021-06-24 00:15:57');
INSERT INTO `first_comment` VALUES (36, 26, '<h3 style=\"text-align: center;\"><span style=\"color: #e03e2d;\"><strong><a style=\"color: #e03e2d;\" title=\"哈哈哈哈\" href=\"https://www.bilibili.com/\" target=\"_blank\" rel=\"noopener\">b站</a></strong></span></h3>', 2, 32, 1, '2021-06-24 16:34:18');
INSERT INTO `first_comment` VALUES (37, 26, '<p><a title=\"bilibili\" href=\"https://www.bilibili.com/\" target=\"_blank\" rel=\"noopener\">https://www.bilibili.com/</a></p>', 2, 33, 0, '2021-06-24 16:35:08');
INSERT INTO `first_comment` VALUES (38, 26, '<p><img style=\"float: right;\" src=\"https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/73f3513af3fc42579ca3e7e509a0c9ce.png?timestamp=1624523750521\" width=\"635\" height=\"39\" /></p>\n<p>&nbsp;</p>\n<p>哈哈哈哈</p>\n<p><img src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/0b/tootha_thumb.gif\" alt=\"[嘻嘻]\" /></p>', 2, 34, 0, '2021-06-24 16:36:31');
INSERT INTO `first_comment` VALUES (39, 26, '<p>额额额额额额</p>', 2, 35, 0, '2021-06-24 16:36:56');
INSERT INTO `first_comment` VALUES (40, 26, '<pre class=\"language-java\"><code>    @GetMapping(\"/second/list\")\n    public ResultModel&lt;PageData&lt;SecondCommentDTO&gt;&gt; secondCommentList(\n            @RequestParam Integer firstCommentId,\n            @RequestParam(defaultValue = \"1\") Integer page,\n            @RequestParam(defaultValue = \"10\") Integer count) {\n        FirstComment firstComment = commentService.getFirstCommentById(firstCommentId);\n        if (ObjectUtils.isEmpty(firstComment)) {\n            return ResultModel.failed(StatusCode.FIRST_COMMENT_NOT_EXIST); // 一级评论不存在\n        }\n        if (count &lt; 1) {\n            count = 1;\n        }\n        PageData&lt;SecondCommentDTO&gt; secondCommentList =\n                commentService.getSecondCommentList(firstComment, page, count);\n        return ResultModel.success(secondCommentList);\n    }</code></pre>', 2, 36, 1, '2021-06-24 20:40:45');
INSERT INTO `first_comment` VALUES (41, 26, '<pre class=\"language-java\"><code>package top.ysqorz.forum.config;\n\nimport org.springframework.context.annotation.Configuration;\nimport org.springframework.format.FormatterRegistry;\nimport org.springframework.web.servlet.config.annotation.CorsRegistry;\nimport org.springframework.web.servlet.config.annotation.WebMvcConfigurer;\n\n/**\n * @author passerbyYSQ\n * @create 2021-01-29 14:41\n */\n@Configuration\npublic class WebMvcConfig implements WebMvcConfigurer {\n\n    /**\n     * 前后端分离需要解决跨域问题\n     */\n    @Override\n    public void addCorsMappings(CorsRegistry registry) {\n        registry.addMapping(\"/**\")\n                .allowedOrigins(\"*\")  // 放行哪些原始域\n                .allowCredentials(true) // 是否发送cookie\n                .allowedMethods(\"GET\", \"POST\", \"PUT\", \"OPTIONS\", \"DELETE\", \"PATCH\")\n                .exposedHeaders(\"*\")\n                .allowedHeaders(\"*\") // allowedHeaders是exposedHeaders的子集\n                .maxAge(3600); // 预检请求OPTIONS请求的缓存时间\n    }\n\n    /**\n     * 在参数绑定时，自定义String-&gt;String的转换器，\n     * 在转换逻辑中对参数值进行转义，从而达到防XSS的效果\n     */\n    @Override\n    public void addFormatters(FormatterRegistry registry) {\n        registry.addConverter(new EscapeStringConverter());\n        //registry.addFormatter(new LocalDateTimeFormatter());\n\n    }\n}</code></pre>', 13, 1, 1, '2021-06-24 20:56:59');
INSERT INTO `first_comment` VALUES (42, 26, '<p>测试测试</p>', 13, 2, 2, '2021-06-24 21:17:12');
INSERT INTO `first_comment` VALUES (43, 22, '<p>呃呃呃你</p>', 13, 3, 1, '2021-06-24 21:45:42');
INSERT INTO `first_comment` VALUES (44, 26, '<p>你好你好</p>', 12, 1, 1, '2021-06-30 22:28:26');
INSERT INTO `first_comment` VALUES (45, 26, '<p>esbvsdklg</p>', 12, 2, 0, '2021-06-30 23:08:40');
INSERT INTO `first_comment` VALUES (46, 26, '<p>vsddfbdf</p>', 12, 3, 0, '2021-06-30 23:09:19');
INSERT INTO `first_comment` VALUES (47, 22, '<p>呃呃呃呃呃</p>', 12, 4, 0, '2021-06-30 23:09:53');
INSERT INTO `first_comment` VALUES (48, 26, '<p>eeeee</p>', 12, 5, 0, '2021-06-30 23:10:21');
INSERT INTO `first_comment` VALUES (49, 26, '<p>esdvsdbv</p>', 12, 6, 0, '2021-06-30 23:12:24');
INSERT INTO `first_comment` VALUES (50, 22, '<p>给我顶上去</p>', 7, 1, 0, '2021-07-02 23:50:10');
INSERT INTO `first_comment` VALUES (51, 22, '<p>测试回复</p>', 8, 1, 0, '2021-07-02 23:57:16');
INSERT INTO `first_comment` VALUES (52, 21, '<p>【回复通知】</p>', 11, 1, 0, '2021-07-04 21:24:19');
INSERT INTO `first_comment` VALUES (53, 22, '<p>我是层主</p>', 11, 2, 5, '2021-07-04 21:25:09');
INSERT INTO `first_comment` VALUES (54, 21, '<p>测试评论</p>', 16, 1, 1, '2021-10-19 19:53:34');
INSERT INTO `first_comment` VALUES (55, 26, '<p>测试评论</p>', 16, 2, 0, '2021-10-19 19:54:15');
INSERT INTO `first_comment` VALUES (56, 26, '<p>一级评论1</p>', 17, 1, 0, '2021-10-19 23:33:38');
INSERT INTO `first_comment` VALUES (57, 26, '<p>一级评论2</p>', 17, 2, 0, '2021-10-19 23:34:20');
INSERT INTO `first_comment` VALUES (58, 21, '<p>一级评论3</p>', 17, 3, 2, '2021-10-19 23:35:04');
INSERT INTO `first_comment` VALUES (59, 21, '<p>是VS的绿色的你</p>', 18, 1, 7, '2021-10-20 21:27:08');
INSERT INTO `first_comment` VALUES (61, 21, '<p>DVSVWBVW</p>', 18, 2, 0, '2021-10-20 23:05:22');
INSERT INTO `first_comment` VALUES (62, 21, '<p>WEVWEV</p>', 18, 3, 0, '2021-10-20 23:05:43');
INSERT INTO `first_comment` VALUES (63, 21, '<p>收到VS的不是本人</p>', 18, 4, 0, '2021-10-20 23:09:00');
INSERT INTO `first_comment` VALUES (64, 21, '<p>xcbsdfb</p>', 18, 5, 0, '2021-10-20 23:09:34');
INSERT INTO `first_comment` VALUES (65, 21, '<p>sdbdsbdv</p>', 18, 6, 0, '2021-10-20 23:10:19');
INSERT INTO `first_comment` VALUES (66, 21, '<p>dbfdberb</p>', 18, 7, 1, '2021-10-20 23:12:31');
INSERT INTO `first_comment` VALUES (67, 21, '<p>你好sdvsd</p>', 18, 8, 1, '2021-10-20 23:12:59');
INSERT INTO `first_comment` VALUES (68, 21, '<p>sdbvsdbds</p>', 18, 9, 0, '2021-10-20 23:13:28');
INSERT INTO `first_comment` VALUES (70, 21, '<p>=====================</p>', 18, 11, 1, '2021-10-20 23:15:02');
INSERT INTO `first_comment` VALUES (71, 21, '<p>sdbsdbs</p>', 18, 12, 0, '2021-10-20 23:17:15');
INSERT INTO `first_comment` VALUES (72, 21, '<p>啊啊啊啊啊啊</p>', 18, 13, 2, '2021-10-20 23:17:43');
INSERT INTO `first_comment` VALUES (73, 26, '<p>我是发表最新评论的人</p>', 15, 1, 0, '2022-04-14 17:28:07');
INSERT INTO `first_comment` VALUES (74, 26, '<p>？？？？？</p>', 15, 2, 1, '2022-04-14 17:40:34');

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
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '关注' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of follow
-- ----------------------------
INSERT INTO `follow` VALUES (2, 26, 11, '2021-07-12 11:30:14');
INSERT INTO `follow` VALUES (4, 26, 22, '2021-07-12 12:45:24');
INSERT INTO `follow` VALUES (5, 26, 21, '2021-10-19 19:55:55');
INSERT INTO `follow` VALUES (7, 22, 1, '2022-02-20 16:07:52');
INSERT INTO `follow` VALUES (8, 22, 21, '2022-02-21 00:30:41');
INSERT INTO `follow` VALUES (9, 22, 26, '2022-02-21 00:32:57');
INSERT INTO `follow` VALUES (10, 21, 22, '2022-04-15 01:30:39');
INSERT INTO `follow` VALUES (11, 21, 1, '2022-04-15 01:30:46');

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
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of label
-- ----------------------------
INSERT INTO `label` VALUES (1, '大长腿', '', 1);
INSERT INTO `label` VALUES (2, '测试', '', 1);
INSERT INTO `label` VALUES (3, '辣妹子', '', 1);
INSERT INTO `label` VALUES (4, '帅哥', '', 1);
INSERT INTO `label` VALUES (5, 'a', '', 1);
INSERT INTO `label` VALUES (6, 'ab', '', 1);
INSERT INTO `label` VALUES (7, 'ac', '', 1);
INSERT INTO `label` VALUES (8, 'ad', '', 1);
INSERT INTO `label` VALUES (9, 'ae', '', 1);
INSERT INTO `label` VALUES (10, '你好', '', 1);
INSERT INTO `label` VALUES (11, 'YSQ', '', 1);
INSERT INTO `label` VALUES (12, '不能回复', '', 1);
INSERT INTO `label` VALUES (13, '锁定', '', 1);
INSERT INTO `label` VALUES (14, '验证码', '', 1);
INSERT INTO `label` VALUES (15, '发帖', '', 1);
INSERT INTO `label` VALUES (16, '代码', '', 1);
INSERT INTO `label` VALUES (17, '冲突', '', 1);
INSERT INTO `label` VALUES (18, '权限', '', 1);
INSERT INTO `label` VALUES (19, '标签2', '', 1);
INSERT INTO `label` VALUES (20, '标签1', '', 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '点赞' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of like
-- ----------------------------
INSERT INTO `like` VALUES (6, 22, 1, '2021-06-20 10:32:09', 0);
INSERT INTO `like` VALUES (19, 26, 13, '2021-06-24 20:57:33', 0);
INSERT INTO `like` VALUES (20, 26, 14, '2021-06-30 15:50:55', 0);
INSERT INTO `like` VALUES (21, 26, 12, '2021-06-30 23:12:38', 0);
INSERT INTO `like` VALUES (22, 22, 2, '2021-07-01 23:05:46', 0);
INSERT INTO `like` VALUES (23, 22, 9, '2021-07-02 00:19:33', 0);
INSERT INTO `like` VALUES (24, 22, 12, '2021-07-02 11:56:48', 0);
INSERT INTO `like` VALUES (25, 22, 8, '2021-07-02 15:09:21', 0);
INSERT INTO `like` VALUES (26, 22, 6, '2021-07-03 00:10:04', 0);
INSERT INTO `like` VALUES (27, 21, 11, '2021-07-04 15:10:53', 0);
INSERT INTO `like` VALUES (28, 21, 6, '2021-07-04 15:11:05', 0);
INSERT INTO `like` VALUES (29, 21, 2, '2021-07-04 15:11:44', 0);
INSERT INTO `like` VALUES (30, 21, 8, '2021-07-04 15:19:54', 0);
INSERT INTO `like` VALUES (31, 23, 8, '2021-07-05 15:43:22', 0);
INSERT INTO `like` VALUES (32, 26, 11, '2021-07-12 12:34:15', 0);
INSERT INTO `like` VALUES (36, 26, 16, '2021-10-19 19:53:56', 0);
INSERT INTO `like` VALUES (37, 21, 16, '2021-10-19 19:55:45', 0);
INSERT INTO `like` VALUES (38, 26, 17, '2021-10-19 23:33:24', 0);
INSERT INTO `like` VALUES (48, 21, 13, '2022-04-16 02:48:50', 0);
INSERT INTO `like` VALUES (58, 22, 11, '2022-04-16 16:20:39', 0);

-- ----------------------------
-- Table structure for points_record
-- ----------------------------
DROP TABLE IF EXISTS `points_record`;
CREATE TABLE `points_record`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(11) UNSIGNED NOT NULL COMMENT '用户id',
  `dif` int(11) NOT NULL COMMENT '积分变化的值',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '积分变化的描述',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '积分奖励记录\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of points_record
-- ----------------------------
INSERT INTO `points_record` VALUES (1, 26, 1, '签到', '2021-07-10 15:01:47');
INSERT INTO `points_record` VALUES (2, 26, 5, '精品帖', '2021-07-10 15:29:08');
INSERT INTO `points_record` VALUES (3, 26, 5, '精品帖', '2021-07-10 15:40:25');
INSERT INTO `points_record` VALUES (4, 26, 5, '精品帖', '2021-07-10 15:42:23');
INSERT INTO `points_record` VALUES (5, 22, 1, '签到', '2021-07-10 15:58:04');
INSERT INTO `points_record` VALUES (6, 26, 1, '签到', '2021-07-12 10:58:48');
INSERT INTO `points_record` VALUES (7, 26, 4, '发表主题帖', '2021-07-12 11:41:13');
INSERT INTO `points_record` VALUES (8, 21, 1, '签到', '2021-10-10 23:56:19');
INSERT INTO `points_record` VALUES (9, 21, 1, '签到', '2021-10-17 00:08:57');
INSERT INTO `points_record` VALUES (10, 26, 1, '签到', '2021-10-18 20:10:16');
INSERT INTO `points_record` VALUES (11, 1, 5, '精品帖', '2021-10-18 20:45:26');
INSERT INTO `points_record` VALUES (12, 21, 2, '签到', '2021-10-18 21:15:22');
INSERT INTO `points_record` VALUES (13, 21, 4, '发表主题帖', '2021-10-18 21:16:27');
INSERT INTO `points_record` VALUES (14, 21, 2, '签到', '2021-10-19 13:08:24');
INSERT INTO `points_record` VALUES (15, 26, 2, '签到', '2021-10-19 19:52:28');
INSERT INTO `points_record` VALUES (16, 26, 1, '发表评论或回复', '2021-10-19 19:54:16');
INSERT INTO `points_record` VALUES (17, 26, 1, '发表评论或回复', '2021-10-19 19:54:57');
INSERT INTO `points_record` VALUES (18, 21, 4, '发表主题帖', '2021-10-19 23:31:11');
INSERT INTO `points_record` VALUES (19, 26, 1, '发表评论或回复', '2021-10-19 23:33:38');
INSERT INTO `points_record` VALUES (20, 26, 1, '发表评论或回复', '2021-10-19 23:34:21');
INSERT INTO `points_record` VALUES (21, 26, 1, '发表评论或回复', '2021-10-19 23:35:29');
INSERT INTO `points_record` VALUES (22, 26, 1, '发表评论或回复', '2021-10-19 23:35:42');
INSERT INTO `points_record` VALUES (23, 21, 2, '签到', '2021-10-20 20:11:30');
INSERT INTO `points_record` VALUES (24, 26, 2, '签到', '2021-10-20 20:11:40');
INSERT INTO `points_record` VALUES (25, 21, 4, '发表主题帖', '2021-10-20 20:12:56');
INSERT INTO `points_record` VALUES (26, 26, 1, '签到', '2021-10-28 17:31:11');
INSERT INTO `points_record` VALUES (27, 21, 1, '签到', '2021-12-05 14:37:18');
INSERT INTO `points_record` VALUES (28, 26, 1, '签到', '2021-12-05 15:12:28');
INSERT INTO `points_record` VALUES (29, 26, 1, '签到', '2022-01-20 23:11:32');
INSERT INTO `points_record` VALUES (30, 26, 1, '签到', '2022-01-25 19:03:50');
INSERT INTO `points_record` VALUES (31, 21, 1, '签到', '2022-02-03 21:11:21');
INSERT INTO `points_record` VALUES (32, 26, 1, '签到', '2022-02-10 22:57:28');
INSERT INTO `points_record` VALUES (33, 26, 2, '签到', '2022-02-11 00:00:18');
INSERT INTO `points_record` VALUES (34, 26, 1, '签到', '2022-02-17 18:06:27');
INSERT INTO `points_record` VALUES (35, 22, 1, '签到', '2022-02-17 18:07:13');
INSERT INTO `points_record` VALUES (36, 26, 2, '签到', '2022-02-18 22:09:18');
INSERT INTO `points_record` VALUES (37, 26, 2, '签到', '2022-02-19 23:33:50');
INSERT INTO `points_record` VALUES (38, 26, 2, '签到', '2022-02-20 00:34:56');
INSERT INTO `points_record` VALUES (39, 22, 1, '签到', '2022-02-20 15:57:35');
INSERT INTO `points_record` VALUES (40, 22, 2, '签到', '2022-02-21 00:29:25');
INSERT INTO `points_record` VALUES (41, 26, 1, '签到', '2022-02-22 00:03:01');
INSERT INTO `points_record` VALUES (42, 26, 1, '签到', '2022-03-24 20:07:05');
INSERT INTO `points_record` VALUES (43, 26, 2, '签到', '2022-03-25 00:45:27');
INSERT INTO `points_record` VALUES (44, 28, 1, '签到', '2022-03-28 19:00:22');
INSERT INTO `points_record` VALUES (45, 26, 1, '签到', '2022-03-28 19:44:34');
INSERT INTO `points_record` VALUES (46, 26, 1, '签到', '2022-03-30 16:53:54');
INSERT INTO `points_record` VALUES (47, 21, 1, '签到', '2022-04-03 21:50:59');
INSERT INTO `points_record` VALUES (48, 21, 1, '签到', '2022-04-05 23:08:10');
INSERT INTO `points_record` VALUES (49, 21, 1, '签到', '2022-04-09 23:41:49');
INSERT INTO `points_record` VALUES (50, 26, 1, '签到', '2022-04-09 23:48:10');
INSERT INTO `points_record` VALUES (51, 21, 2, '签到', '2022-04-10 01:20:13');
INSERT INTO `points_record` VALUES (52, 26, 2, '签到', '2022-04-10 17:17:55');
INSERT INTO `points_record` VALUES (53, 26, 1, '签到', '2022-04-14 15:13:16');
INSERT INTO `points_record` VALUES (54, 21, 1, '发表评论或回复', '2022-04-14 17:41:05');
INSERT INTO `points_record` VALUES (55, 21, 1, '签到', '2022-04-14 17:41:23');
INSERT INTO `points_record` VALUES (56, 26, 2, '签到', '2022-04-15 01:00:35');
INSERT INTO `points_record` VALUES (57, 21, 2, '签到', '2022-04-15 01:29:39');
INSERT INTO `points_record` VALUES (58, 21, 5, '精品帖', '2022-04-15 23:29:21');
INSERT INTO `points_record` VALUES (59, 21, 2, '签到', '2022-04-16 00:00:27');
INSERT INTO `points_record` VALUES (60, 26, 2, '签到', '2022-04-16 15:49:14');
INSERT INTO `points_record` VALUES (61, 22, 1, '签到', '2022-04-16 15:57:07');

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
  `is_high_quality` tinyint(4) NOT NULL COMMENT '是否为精品',
  `is_locked` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否锁定。0：未锁定；1：锁定，锁定后不能评论，不能修改',
  `top_weight` int(11) NOT NULL COMMENT '置顶权重',
  `last_comment_time` datetime(0) NULL DEFAULT NULL COMMENT '最后一次评论时间',
  `visibility_type` tinyint(4) NULL DEFAULT NULL COMMENT '可见策略\r\n0：任何人可见\r\n1：粉丝可见\r\n2：点赞后可见\r\n>=3：积分购买后可见（积分就是visibility_type）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `creator_id`(`creator_id`) USING BTREE,
  INDEX `topic_id`(`topic_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主题帖' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES (1, '测试1111111', '<p>测试1111111说的不是吧微软</p>', 1, 1, '2021-05-25 21:13:29', '2021-05-25 21:13:29', 11, 1, 0, 0, 0, 0, 0, '2021-07-01 23:28:43', 0);
INSERT INTO `post` VALUES (2, '测试标题', '<p>asvdv</p>\n<p><a href=\"/video/1\" target=\"_blank\" rel=\"noopener\">视频弹幕</a></p>\n<p><a href=\"localhost:8080/video/1\" target=\"_blank\" rel=\"noopener\">补全</a></p>\n<p>&nbsp;</p>\n<p><a href=\"localhost:8080/video/1\">补全链接</a></p>\n<p>&nbsp;</p>\n<p><a href=\"/video/1\">绝对路径</a></p>', 1, 1, '2021-05-25 22:58:29', '2022-02-18 22:46:12', 232, 2, 3, 53, 1, 1, 34, '2021-07-01 21:52:16', 0);
INSERT INTO `post` VALUES (3, '这是一条非常重要的通知！！！', '<p><img src=\"https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/fc8ef26c62b94b9dbcad19570d9a6880.jpg?timestamp=1621955365165\" width=\"800\" /></p>\n<h1>欢迎。。。</h1>\n<p>&nbsp;</p>\n<table style=\"border-collapse: collapse; width: 100%;\" border=\"1\">\n<tbody>\n<tr>\n<td style=\"width: 50%;\">1</td>\n<td style=\"width: 50%;\">2</td>\n</tr>\n<tr>\n<td style=\"width: 50%;\">3</td>\n<td style=\"width: 50%;\">&nbsp;</td>\n</tr>\n</tbody>\n</table>\n<p>&nbsp;</p>\n<p><span style=\"font-size: 36pt;\">😂</span></p>\n<p><span style=\"font-size: 14pt;\">你好</span></p>', 1, 2, '2021-05-25 23:11:16', '2021-05-25 23:11:16', 9, 0, 0, 0, 0, 0, 0, '2021-07-01 23:28:47', 0);
INSERT INTO `post` VALUES (4, '我是真的帅', '<p>我是真的帅</p>', 1, 2, '2021-05-25 23:13:02', '2021-05-25 23:13:02', 2, 0, 0, 0, 0, 0, 0, '2021-07-01 23:28:50', 1);
INSERT INTO `post` VALUES (5, '测试标签提示', '<p>测试标签提示测试标签提示测试标签提示</p>', 1, 1, '2021-05-28 23:29:21', '2021-05-28 23:29:21', 1, 0, 0, 0, 0, 0, 0, '2021-07-01 23:28:53', 67);
INSERT INTO `post` VALUES (6, '【修改22】测试标签1123235', '<p>测试帖子修改！！！ 8888 <img src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/9d/sada_thumb.gif\" alt=\"[泪]\" /></p>', 1, 1, '2021-05-28 23:30:36', '2021-07-02 11:12:31', 16, 2, 0, 0, 0, 1, -1, '2021-07-01 23:28:56', 67);
INSERT INTO `post` VALUES (7, 'asvwev', '<p>erberb</p>', 1, 1, '2021-06-12 16:46:25', '2021-06-12 16:46:25', 4, 0, 0, 1, 0, 0, 0, '2021-07-02 23:50:10', 0);
INSERT INTO `post` VALUES (8, '【测试】锁定', '<blockquote>\n<p>【测试】锁定【测试】锁定【测试】锁定【测试】锁定</p>\n</blockquote>\n<p>&nbsp;</p>\n<h1><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/4206857946a94d639751220e1ba1036a.png?timestamp=1623502282211\" alt=\"Logo\" width=\"194\" height=\"146\" /><img src=\"https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/22d4b8bb504c447ca52b7b550180af0f.png?timestamp=1625211592797\" alt=\"dfnfd\" width=\"730\" height=\"781\" /></h1>\n<h1><img src=\"https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/e5914597f0d24ef9a3399cda535518a8.png?timestamp=1625211664578\" alt=\"ltlt\" width=\"800\" height=\"496\" /></h1>\n<h1>标题</h1>\n<p style=\"padding-left: 40px;\">你好</p>\n<p>&nbsp;</p>\n<p style=\"padding-left: 80px;\">&nbsp;</p>', 1, 1, '2021-06-12 16:53:28', '2021-07-02 23:55:29', 15, 3, 1, 1, 0, 0, 0, '2021-07-02 23:57:16', 23);
INSERT INTO `post` VALUES (9, '测试验证码测试验证码测试验证码', '<p>测试验证码测试验证码测试验证码</p>\n<p>&nbsp;</p>\n<p style=\"text-align: right;\">测试验证码测试验证码</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>测试验证码测试验证码</p>', 1, 1, '2021-06-14 23:56:31', '2021-06-14 23:56:31', 5, 1, 0, 0, 0, 1, 0, '2021-07-01 23:29:05', 2);
INSERT INTO `post` VALUES (10, '测试权限', '<p>测试权限</p>\n<p>&nbsp;</p>\n<p>测试权限</p>', 1, 1, '2021-06-18 21:36:33', '2021-06-18 21:36:33', 4, 0, 0, 0, 0, 1, 0, '2021-07-01 23:29:07', 1);
INSERT INTO `post` VALUES (11, '测试登录后发帖，测试修改时间', '<p><span style=\"color: #e03e2d;\">测试登录后发帖测试登录后发帖测试登录后发帖</span></p>\n<ol>\n<li>Sfgss</li>\n<li>sdvsd</li>\n<li>svsd</li>\n<li>&nbsp;</li>\n</ol>\n<p style=\"text-align: right;\">测试登录后发帖测试登录后发帖测试登录后发帖</p>', 22, 1, '2021-06-18 21:41:46', '2021-06-20 00:22:34', 75, 3, 1, 7, 1, 0, 99, '2021-07-04 21:31:25', 2);
INSERT INTO `post` VALUES (12, '测试发帖', '<p>测试发帖测试发帖测试发帖测试发帖</p>', 22, 1, '2021-06-18 23:27:03', '2021-06-18 23:27:03', 19, 2, 2, 7, 0, 0, 0, '2021-07-02 11:56:33', 0);
INSERT INTO `post` VALUES (13, '测试代码高亮', '<p>哈哈哈哈</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<pre class=\"language-java\"><code>package top.ysqorz.forum.config;\n\nimport org.springframework.context.annotation.Configuration;\nimport org.springframework.format.FormatterRegistry;\nimport org.springframework.web.servlet.config.annotation.CorsRegistry;\nimport org.springframework.web.servlet.config.annotation.WebMvcConfigurer;\n\n/**\n * @author passerbyYSQ\n * @create 2021-01-29 14:41\n */\n@Configuration\npublic class WebMvcConfig implements WebMvcConfigurer {\n\n    /**\n     * 前后端分离需要解决跨域问题\n     */\n    @Override\n    public void addCorsMappings(CorsRegistry registry) {\n        registry.addMapping(\"/**\")\n                .allowedOrigins(\"*\")  // 放行哪些原始域\n                .allowCredentials(true) // 是否发送cookie\n                .allowedMethods(\"GET\", \"POST\", \"PUT\", \"OPTIONS\", \"DELETE\", \"PATCH\")\n                .exposedHeaders(\"*\")\n                .allowedHeaders(\"*\") // allowedHeaders是exposedHeaders的子集\n                .maxAge(3600); // 预检请求OPTIONS请求的缓存时间\n    }\n\n    /**\n     * 在参数绑定时，自定义String-&gt;String的转换器，\n     * 在转换逻辑中对参数值进行转义，从而达到防XSS的效果\n     */\n    @Override\n    public void addFormatters(FormatterRegistry registry) {\n        registry.addConverter(new EscapeStringConverter());\n        //registry.addFormatter(new LocalDateTimeFormatter());\n\n    }\n}\n</code></pre>', 26, 1, '2021-06-24 20:55:07', '2021-06-30 15:49:17', 46, 2, 1, 7, 1, 0, 99, '2021-06-30 12:48:18', 0);
INSERT INTO `post` VALUES (14, 'sdbdsbsdb', '<p>sdvsdbsbvsdb</p>', 26, 1, '2021-06-30 15:50:50', '2021-06-30 15:50:50', 2, 1, 0, 0, 1, 0, 0, '2021-06-30 15:50:50', 0);
INSERT INTO `post` VALUES (15, '测试冲突测试冲突测试冲突测试冲突', '<p>测试冲突测试冲突测试冲突测试冲突测试冲突测试冲突测试冲突测试冲突测试冲突测试冲突</p>\n<p><img src=\"https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/00ca8c0bf69c4ce095a338c76c427954.jpg?timestamp=1626061510024\" width=\"800\" height=\"450\" /></p>', 26, 1, '2021-07-12 11:41:13', '2021-07-12 11:45:44', 7, 0, 0, 3, 0, 0, 0, '2022-04-14 17:41:04', 0);
INSERT INTO `post` VALUES (18, '测试删除帖子', '<p>为v为v我</p>', 21, 2, '2021-10-20 20:12:56', '2021-10-20 20:12:56', 29, 0, 1, 26, 0, 0, 0, '2021-10-20 23:30:40', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主题帖表和标签表的关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post_label
-- ----------------------------
INSERT INTO `post_label` VALUES (1, 1, 1);
INSERT INTO `post_label` VALUES (2, 1, 2);
INSERT INTO `post_label` VALUES (3, 1, 3);
INSERT INTO `post_label` VALUES (6, 3, 1);
INSERT INTO `post_label` VALUES (7, 3, 3);
INSERT INTO `post_label` VALUES (8, 4, 1);
INSERT INTO `post_label` VALUES (9, 4, 4);
INSERT INTO `post_label` VALUES (10, 4, 3);
INSERT INTO `post_label` VALUES (11, 5, 5);
INSERT INTO `post_label` VALUES (12, 5, 6);
INSERT INTO `post_label` VALUES (13, 5, 7);
INSERT INTO `post_label` VALUES (14, 5, 8);
INSERT INTO `post_label` VALUES (15, 5, 9);
INSERT INTO `post_label` VALUES (37, 7, 2);
INSERT INTO `post_label` VALUES (43, 9, 2);
INSERT INTO `post_label` VALUES (44, 9, 14);
INSERT INTO `post_label` VALUES (49, 12, 2);
INSERT INTO `post_label` VALUES (50, 11, 2);
INSERT INTO `post_label` VALUES (51, 11, 15);
INSERT INTO `post_label` VALUES (58, 13, 16);
INSERT INTO `post_label` VALUES (59, 13, 2);
INSERT INTO `post_label` VALUES (60, 14, 5);
INSERT INTO `post_label` VALUES (61, 14, 6);
INSERT INTO `post_label` VALUES (66, 6, 11);
INSERT INTO `post_label` VALUES (73, 8, 12);
INSERT INTO `post_label` VALUES (74, 8, 2);
INSERT INTO `post_label` VALUES (75, 8, 13);
INSERT INTO `post_label` VALUES (78, 15, 17);
INSERT INTO `post_label` VALUES (79, 15, 2);
INSERT INTO `post_label` VALUES (84, 18, 2);
INSERT INTO `post_label` VALUES (101, 2, 1);
INSERT INTO `post_label` VALUES (102, 2, 3);

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
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '涉及权限的资源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES (1, 0, '系统管理', 'layui-icon layui-icon-set', 'system', 'system', 0, 1);
INSERT INTO `resource` VALUES (16, 1, '帖子管理', 'layui-icon  layui-icon-form', 'post', 'post', 0, 101);
INSERT INTO `resource` VALUES (20, 1, '用户管理', 'layui-icon  layui-icon-user', 'user', 'user', 0, 998);
INSERT INTO `resource` VALUES (25, 1, '角色管理', 'layui-icon  layui-icon-auz', 'role', 'role', 0, 104);
INSERT INTO `resource` VALUES (30, 1, '话题管理', 'layui-icon  layui-icon-list', 'topic', 'topic', 0, 102);
INSERT INTO `resource` VALUES (43, 1, '权限管理', '', 'perm', 'perm', 0, 103);
INSERT INTO `resource` VALUES (48, 20, '查看', '', '', 'user:view', 1, 99);
INSERT INTO `resource` VALUES (49, 20, '分配角色', '', '', 'user:allotRole', 1, 98);
INSERT INTO `resource` VALUES (50, 20, '删除角色', '', '', 'user:deleteRole', 1, 97);
INSERT INTO `resource` VALUES (51, 20, '重置密码', '', '', 'user:resetPwd', 1, 96);
INSERT INTO `resource` VALUES (52, 20, '黑名单', '', '', 'user:blacklist', 1, 95);
INSERT INTO `resource` VALUES (53, 25, '查看', '', '', 'role:view', 1, 99);
INSERT INTO `resource` VALUES (54, 25, '添加角色', '', '', 'role:add', 1, 98);
INSERT INTO `resource` VALUES (55, 25, '修改角色', '', '', 'role:update', 1, 97);
INSERT INTO `resource` VALUES (56, 25, '删除角色', '', '', 'role:delete', 1, 96);
INSERT INTO `resource` VALUES (57, 25, '分配权限', '', '', 'role:allotPerm', 1, 95);
INSERT INTO `resource` VALUES (58, 43, '查看', '', '', 'perm:view', 1, 99);
INSERT INTO `resource` VALUES (59, 43, '添加权限', '', '', 'perm:add', 1, 98);
INSERT INTO `resource` VALUES (60, 43, '修改权限', '', '', 'perm:update', 1, 97);
INSERT INTO `resource` VALUES (61, 43, '删除权限', '', '', 'perm:delete', 1, 96);
INSERT INTO `resource` VALUES (62, 30, '查看', '', '', 'topic:view', 1, 99);
INSERT INTO `resource` VALUES (63, 30, '添加话题', '', '', 'topic:add', 1, 98);
INSERT INTO `resource` VALUES (64, 30, '修改话题', '', '', 'topic:update', 1, 97);
INSERT INTO `resource` VALUES (65, 30, '归档', '', '', 'topic:archive', 1, 96);
INSERT INTO `resource` VALUES (66, 16, '查看', '', '', 'post:view', 1, 99);
INSERT INTO `resource` VALUES (67, 16, '修改帖子', '', '', 'post:update', 1, 98);
INSERT INTO `resource` VALUES (68, 16, '删除帖子', '', '', 'post:delete', 1, 97);
INSERT INTO `resource` VALUES (69, 16, '置顶', '', '', 'post:top', 1, 96);
INSERT INTO `resource` VALUES (70, 16, '精品', '', '', 'post:quality', 1, 95);
INSERT INTO `resource` VALUES (71, 16, '锁定', '', '', 'post:lock', 1, 94);
INSERT INTO `resource` VALUES (72, 1, '访问后台', '', '', 'admin:access', 1, 999);

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
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (3, 'b', '123', '2021-05-19 16:54:43');
INSERT INTO `role` VALUES (17, '12333', '12', '2021-05-20 15:57:26');
INSERT INTO `role` VALUES (18, 'b', '132', '2021-05-20 16:59:53');
INSERT INTO `role` VALUES (19, 'b', '132', '2021-05-20 16:59:56');
INSERT INTO `role` VALUES (20, '123', '123', '2021-05-20 16:59:58');
INSERT INTO `role` VALUES (21, 'bfbb', '', '2021-06-04 20:38:01');
INSERT INTO `role` VALUES (23, '后台游客', '只包括查看权限，方便给其他用户游览后台', '2021-06-30 22:04:03');
INSERT INTO `role` VALUES (26, '普通管理员', '拥有除了角色管理和权限管理以外的所有权限', '2021-07-09 17:44:12');
INSERT INTO `role` VALUES (27, '超级管理员', '拥有后台所有权限，包括角色管理和权限管理', '2021-07-09 17:44:19');

-- ----------------------------
-- Table structure for role_resource
-- ----------------------------
DROP TABLE IF EXISTS `role_resource`;
CREATE TABLE `role_resource`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_id` int(11) UNSIGNED NOT NULL COMMENT '角色id',
  `resource_id` int(11) UNSIGNED NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `resource_id`(`resource_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  CONSTRAINT `role_resource_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 419 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表和资源表的关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_resource
-- ----------------------------
INSERT INTO `role_resource` VALUES (339, 27, 1);
INSERT INTO `role_resource` VALUES (340, 27, 72);
INSERT INTO `role_resource` VALUES (341, 27, 20);
INSERT INTO `role_resource` VALUES (342, 27, 48);
INSERT INTO `role_resource` VALUES (343, 27, 49);
INSERT INTO `role_resource` VALUES (344, 27, 50);
INSERT INTO `role_resource` VALUES (345, 27, 51);
INSERT INTO `role_resource` VALUES (346, 27, 52);
INSERT INTO `role_resource` VALUES (347, 27, 25);
INSERT INTO `role_resource` VALUES (348, 27, 53);
INSERT INTO `role_resource` VALUES (349, 27, 54);
INSERT INTO `role_resource` VALUES (350, 27, 55);
INSERT INTO `role_resource` VALUES (351, 27, 56);
INSERT INTO `role_resource` VALUES (352, 27, 57);
INSERT INTO `role_resource` VALUES (353, 27, 43);
INSERT INTO `role_resource` VALUES (354, 27, 58);
INSERT INTO `role_resource` VALUES (355, 27, 59);
INSERT INTO `role_resource` VALUES (356, 27, 60);
INSERT INTO `role_resource` VALUES (357, 27, 61);
INSERT INTO `role_resource` VALUES (358, 27, 30);
INSERT INTO `role_resource` VALUES (359, 27, 62);
INSERT INTO `role_resource` VALUES (360, 27, 63);
INSERT INTO `role_resource` VALUES (361, 27, 64);
INSERT INTO `role_resource` VALUES (362, 27, 65);
INSERT INTO `role_resource` VALUES (363, 27, 16);
INSERT INTO `role_resource` VALUES (364, 27, 66);
INSERT INTO `role_resource` VALUES (365, 27, 67);
INSERT INTO `role_resource` VALUES (366, 27, 68);
INSERT INTO `role_resource` VALUES (367, 27, 69);
INSERT INTO `role_resource` VALUES (368, 27, 70);
INSERT INTO `role_resource` VALUES (369, 27, 71);
INSERT INTO `role_resource` VALUES (370, 26, 1);
INSERT INTO `role_resource` VALUES (371, 26, 72);
INSERT INTO `role_resource` VALUES (372, 26, 20);
INSERT INTO `role_resource` VALUES (373, 26, 48);
INSERT INTO `role_resource` VALUES (374, 26, 51);
INSERT INTO `role_resource` VALUES (375, 26, 52);
INSERT INTO `role_resource` VALUES (376, 26, 25);
INSERT INTO `role_resource` VALUES (377, 26, 53);
INSERT INTO `role_resource` VALUES (378, 26, 43);
INSERT INTO `role_resource` VALUES (379, 26, 58);
INSERT INTO `role_resource` VALUES (380, 26, 30);
INSERT INTO `role_resource` VALUES (381, 26, 62);
INSERT INTO `role_resource` VALUES (382, 26, 63);
INSERT INTO `role_resource` VALUES (383, 26, 64);
INSERT INTO `role_resource` VALUES (384, 26, 65);
INSERT INTO `role_resource` VALUES (385, 26, 16);
INSERT INTO `role_resource` VALUES (386, 26, 66);
INSERT INTO `role_resource` VALUES (387, 26, 67);
INSERT INTO `role_resource` VALUES (388, 26, 68);
INSERT INTO `role_resource` VALUES (389, 26, 69);
INSERT INTO `role_resource` VALUES (390, 26, 70);
INSERT INTO `role_resource` VALUES (391, 26, 71);
INSERT INTO `role_resource` VALUES (392, 23, 1);
INSERT INTO `role_resource` VALUES (393, 23, 72);
INSERT INTO `role_resource` VALUES (394, 23, 20);
INSERT INTO `role_resource` VALUES (395, 23, 48);
INSERT INTO `role_resource` VALUES (396, 23, 25);
INSERT INTO `role_resource` VALUES (397, 23, 53);
INSERT INTO `role_resource` VALUES (398, 23, 43);
INSERT INTO `role_resource` VALUES (399, 23, 58);
INSERT INTO `role_resource` VALUES (400, 23, 30);
INSERT INTO `role_resource` VALUES (401, 23, 62);
INSERT INTO `role_resource` VALUES (402, 23, 16);
INSERT INTO `role_resource` VALUES (403, 23, 66);

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
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '二级评论' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of second_comment
-- ----------------------------
INSERT INTO `second_comment` VALUES (1, 22, '二级评论', 5, NULL, '2021-06-22 16:58:10');
INSERT INTO `second_comment` VALUES (2, 26, '二级评论1', 5, 1, '2021-06-22 16:58:33');
INSERT INTO `second_comment` VALUES (3, 22, ' 收到v白色的空白VS豆瓣', 5, NULL, '2021-06-23 19:13:00');
INSERT INTO `second_comment` VALUES (4, 22, ' 回复子华粉丝。。。', 5, 2, '2021-06-23 19:13:21');
INSERT INTO `second_comment` VALUES (5, 22, ' 哈哈哈哈哈', 6, NULL, '2021-06-23 19:14:19');
INSERT INTO `second_comment` VALUES (6, 22, ' 测试啊啊啊', 5, NULL, '2021-06-23 19:14:41');
INSERT INTO `second_comment` VALUES (7, 22, ' 测试', 6, 5, '2021-06-23 19:15:28');
INSERT INTO `second_comment` VALUES (8, 22, ' 开多个', 8, NULL, '2021-06-23 19:15:39');
INSERT INTO `second_comment` VALUES (9, 22, ' 二和然后', 7, NULL, '2021-06-23 19:15:59');
INSERT INTO `second_comment` VALUES (10, 22, '对付你的愤怒', 7, NULL, '2021-06-23 19:16:11');
INSERT INTO `second_comment` VALUES (11, 22, ' 说的不是豆瓣', 10, NULL, '2021-06-23 19:26:03');
INSERT INTO `second_comment` VALUES (12, 22, '测试', 10, 11, '2021-06-23 19:35:13');
INSERT INTO `second_comment` VALUES (13, 22, ' 测试', 12, NULL, '2021-06-23 19:35:34');
INSERT INTO `second_comment` VALUES (14, 22, ' 测试阿萨VS的v', 11, NULL, '2021-06-23 19:35:36');
INSERT INTO `second_comment` VALUES (15, 22, '回复子华', 5, 2, '2021-06-23 19:37:22');
INSERT INTO `second_comment` VALUES (16, 22, '哈哈哈哈', 5, NULL, '2021-06-23 19:37:31');
INSERT INTO `second_comment` VALUES (17, 22, ' ', 5, NULL, '2021-06-23 19:38:50');
INSERT INTO `second_comment` VALUES (18, 22, ' ', 5, 2, '2021-06-23 19:42:03');
INSERT INTO `second_comment` VALUES (19, 22, ' ', 9, NULL, '2021-06-23 19:44:55');
INSERT INTO `second_comment` VALUES (20, 22, '啊实打实部分当然不能', 5, 1, '2021-06-23 19:45:53');
INSERT INTO `second_comment` VALUES (21, 22, ' dndgnfgm', 5, 4, '2021-06-23 21:45:54');
INSERT INTO `second_comment` VALUES (22, 26, ' 测试', 5, NULL, '2021-06-23 22:01:34');
INSERT INTO `second_comment` VALUES (23, 26, ' 牛啊🐂', 6, NULL, '2021-06-23 22:04:52');
INSERT INTO `second_comment` VALUES (24, 26, ' 厉害厉害👍', 6, 23, '2021-06-23 22:05:36');
INSERT INTO `second_comment` VALUES (26, 26, 'face[微笑]  你好啊\n换行', 8, NULL, '2021-06-23 22:26:50');
INSERT INTO `second_comment` VALUES (27, 26, 'face[微笑]  ', 7, NULL, '2021-06-23 22:27:56');
INSERT INTO `second_comment` VALUES (28, 26, 'face[微笑] ', 5, 22, '2021-06-23 22:30:50');
INSERT INTO `second_comment` VALUES (29, 26, 'face[微笑   ] ', 5, NULL, '2021-06-23 22:31:03');
INSERT INTO `second_comment` VALUES (30, 26, '哈哈哈哈 face[good] ', 5, NULL, '2021-06-23 22:31:17');
INSERT INTO `second_comment` VALUES (31, 26, 'face[嘻嘻] 🐂啊\n\n是v比利时的vface[鄙视] ', 5, NULL, '2021-06-23 22:32:13');
INSERT INTO `second_comment` VALUES (32, 26, 'face[微笑]  ', 9, NULL, '2021-06-23 22:46:08');
INSERT INTO `second_comment` VALUES (33, 26, ' face[嘻嘻] ', 10, NULL, '2021-06-23 22:49:37');
INSERT INTO `second_comment` VALUES (34, 26, ' face[嘻嘻] ', 10, NULL, '2021-06-23 22:55:24');
INSERT INTO `second_comment` VALUES (35, 26, 'img[https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/e67343d1644f4374b1911b4fa44bb54b.jpg?timestamp=1624462133154]  ', 10, NULL, '2021-06-23 23:28:56');
INSERT INTO `second_comment` VALUES (36, 26, 'img[https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/4063c7b08c04432f95d79785dd628b27.png?timestamp=1624462153642]  你好啊按苏打绿扣税的v', 9, NULL, '2021-06-23 23:29:29');
INSERT INTO `second_comment` VALUES (37, 26, ' [pre]\npackage top.ysqorz.forum.config;\n\nimport org.springframework.context.annotation.Configuration;\nimport org.springframework.format.FormatterRegistry;\nimport org.springframework.web.servlet.config.annotation.CorsRegistry;\nimport org.springframework.web.servlet.config.annotation.WebMvcConfigurer;\n\n/**\n * @author passerbyYSQ\n * @create 2021-01-29 14:41\n */\n@Configuration\npublic class WebMvcConfig implements WebMvcConfigurer {\n\n    /**\n     * 前后端分离需要解决跨域问题\n     */\n    @Override\n    public void addCorsMappings(CorsRegistry registry) {\n        registry.addMapping(\"/**\")\n                .allowedOrigins(\"*\")  // 放行哪些原始域\n                .allowCredentials(true) // 是否发送cookie\n                .allowedMethods(\"GET\", \"POST\", \"PUT\", \"OPTIONS\", \"DELETE\", \"PATCH\")\n                .exposedHeaders(\"*\")\n                .allowedHeaders(\"*\") // allowedHeaders是exposedHeaders的子集\n                .maxAge(3600); // 预检请求OPTIONS请求的缓存时间\n    }\n\n    /**\n     * 在参数绑定时，自定义String->String的转换器，\n     * 在转换逻辑中对参数值进行转义，从而达到防XSS的效果\n     */\n    @Override\n    public void addFormatters(FormatterRegistry registry) {\n        registry.addConverter(new EscapeStringConverter());\n        //registry.addFormatter(new LocalDateTimeFormatter());\n\n    }\n}\n\n[/pre]', 5, NULL, '2021-06-24 16:32:25');
INSERT INTO `second_comment` VALUES (38, 26, '  a(https://www.bilibili.com/)[https://www.bilibili.com/] ', 6, NULL, '2021-06-24 16:33:19');
INSERT INTO `second_comment` VALUES (39, 26, 'face[嘻嘻]  ', 36, NULL, '2021-06-24 16:36:21');
INSERT INTO `second_comment` VALUES (40, 26, '[pre]\n<li class=\"layui-nav-item\">\n                <a class=\"fly-nav-avatar\" href=\"javascript:;\">\n                    <cite class=\"layui-hide-xs\">贤心</cite>\n                    <i class=\"iconfont icon-renzheng layui-hide-xs\" title=\"认证信息：layui 作者\"></i>\n                    <i class=\"layui-badge fly-badge-vip layui-hide-xs\">VIP3</i>\n                    <img src=\"https://tva1.sinaimg.cn/crop.0.0.118.118.180/5db11ff4gw1e77d3nqrv8j203b03cweg.jpg\">\n                </a>\n                <dl class=\"layui-nav-child\">\n                    <dd><a href=\"../user/set.html\"><i class=\"layui-icon\">&#xe620;</i>基本设置</a></dd>\n                    <dd><a href=\"../user/message.html\"><i class=\"iconfont icon-tongzhi\" style=\"top: 4px;\"></i>我的消息</a>\n                    </dd>\n                    <dd><a href=\"../user/home.html\"><i class=\"layui-icon\" style=\"margin-left: 2px; font-size: 22px;\">&#xe68e;</i>我的主页</a>\n                    </dd>\n                    <hr style=\"margin: 5px 0;\">\n                    <dd><a href=\"\" style=\"text-align: center;\">退出</a></dd>\n                </dl>\n            </li>\n[/pre] ', 41, NULL, '2021-06-24 21:30:59');
INSERT INTO `second_comment` VALUES (41, 26, ' [pre][code]\n/**\n     * 在参数绑定时，自定义String->String的转换器，\n     * 在转换逻辑中对参数值进行转义，从而达到防XSS的效果\n     */\n    @Override\n    public void addFormatters(FormatterRegistry registry) {\n        registry.addConverter(new EscapeStringConverter());\n        //registry.addFormatter(new LocalDateTimeFormatter());\n\n    }\n[code][/pre]', 42, NULL, '2021-06-24 21:35:10');
INSERT INTO `second_comment` VALUES (42, 22, '[pre]\n@Override\n    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {\n        //  AuthenticationInfo info 是我们在JwtRealm中doGetAuthenticationInfo()返回的那个\n        User user = (User) info.getCredentials();\n\n        //String tokenStr = (String) token.getPrincipal();\n        //Integer userId = (Integer) info.getPrincipals().getPrimaryPrincipal();\n\n        // 校验失败，会抛出异常，被shiro捕获\n        Map<String, String> claims = new HashMap<>();\n        claims.put(\"userId\", user.getId().toString());\n        try {\n            JwtUtils.verifyJwt((String) token.getCredentials(), user.getJwtSalt(), claims);\n            return true;\n        } catch (JWTVerificationException e) {\n            //e.printStackTrace();\n            return false;\n        }\n\n    }\n[/pre]', 42, 41, '2021-06-24 21:43:31');
INSERT INTO `second_comment` VALUES (43, 22, ' face[嘻嘻] ', 10, NULL, '2021-06-29 21:13:33');
INSERT INTO `second_comment` VALUES (44, 22, 'img[https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/e4187f806d0243a4bba879fd1cabb7ab.jpg?timestamp=1624972431311] ', 10, 43, '2021-06-29 21:13:54');
INSERT INTO `second_comment` VALUES (45, 26, ' 你好啊', 5, NULL, '2021-06-29 22:07:43');
INSERT INTO `second_comment` VALUES (46, 26, '汉化后', 5, NULL, '2021-06-29 22:07:51');
INSERT INTO `second_comment` VALUES (47, 26, ' face[泪] ', 5, NULL, '2021-06-29 22:41:14');
INSERT INTO `second_comment` VALUES (48, 26, ' 阿萨v产生的v各地v', 6, 24, '2021-06-29 22:42:32');
INSERT INTO `second_comment` VALUES (49, 26, ' 你好', 43, NULL, '2021-06-30 12:48:19');
INSERT INTO `second_comment` VALUES (50, 22, ' sbs', 10, NULL, '2021-07-01 12:37:45');
INSERT INTO `second_comment` VALUES (51, 22, ' 测试', 40, NULL, '2021-07-01 21:52:16');
INSERT INTO `second_comment` VALUES (52, 22, 'face[可怜]  ', 44, NULL, '2021-07-02 11:56:33');
INSERT INTO `second_comment` VALUES (53, 21, ' 【回复楼层】', 53, NULL, '2021-07-04 21:25:53');
INSERT INTO `second_comment` VALUES (54, 22, '哈哈哈哈', 53, NULL, '2021-07-04 21:26:12');
INSERT INTO `second_comment` VALUES (55, 21, '【回复通知】', 53, 54, '2021-07-04 21:26:32');
INSERT INTO `second_comment` VALUES (56, 21, '回复 哈哈哈哈', 53, 54, '2021-07-04 21:29:47');
INSERT INTO `second_comment` VALUES (57, 22, ' 自己回复自己', 53, NULL, '2021-07-04 21:31:25');
INSERT INTO `second_comment` VALUES (58, 26, '测试二级评论', 54, NULL, '2021-10-19 19:54:57');
INSERT INTO `second_comment` VALUES (59, 26, ' 二级评论1face[哈哈] ', 58, NULL, '2021-10-19 23:35:29');
INSERT INTO `second_comment` VALUES (60, 26, '二级评论2', 58, NULL, '2021-10-19 23:35:42');
INSERT INTO `second_comment` VALUES (74, 21, 'dvef ', 59, 73, '2021-10-20 22:45:21');
INSERT INTO `second_comment` VALUES (76, 21, 'edve', 59, 75, '2021-10-20 22:45:55');
INSERT INTO `second_comment` VALUES (77, 21, 'edvever', 59, 74, '2021-10-20 22:46:03');
INSERT INTO `second_comment` VALUES (78, 21, ' everv', 59, 74, '2021-10-20 22:46:26');
INSERT INTO `second_comment` VALUES (79, 21, 'everer', 59, 74, '2021-10-20 22:46:48');
INSERT INTO `second_comment` VALUES (81, 21, 'sdvwev你', 59, 80, '2021-10-20 22:49:50');
INSERT INTO `second_comment` VALUES (84, 21, 'asvasvadv', 70, 83, '2021-10-20 23:16:13');
INSERT INTO `second_comment` VALUES (86, 21, ' sdbsdbsd', 73, NULL, '2021-10-20 23:18:20');
INSERT INTO `second_comment` VALUES (87, 21, 'asvasv', 73, 86, '2021-10-20 23:18:28');
INSERT INTO `second_comment` VALUES (88, 21, ' dfbsfb', 72, NULL, '2021-10-20 23:22:52');
INSERT INTO `second_comment` VALUES (89, 21, 'vasdvdbva', 72, NULL, '2021-10-20 23:22:59');
INSERT INTO `second_comment` VALUES (90, 21, 'sdbvweb', 66, NULL, '2021-10-20 23:30:35');
INSERT INTO `second_comment` VALUES (91, 21, ' asvaebvew', 67, NULL, '2021-10-20 23:30:41');
INSERT INTO `second_comment` VALUES (92, 21, ' 测试二级评论最新回复\n\n\n', 74, NULL, '2022-04-14 17:41:05');

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
  `archive` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否归档。0：没有归档，1：已归档',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '话题\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of topic
-- ----------------------------
INSERT INTO `topic` VALUES (1, '测试', '测试专用', 12, 1, '2021-05-25 16:09:46', 0, 0);
INSERT INTO `topic` VALUES (2, '通知', '发通知', 4, 1, '2021-05-25 16:10:09', 0, 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
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
  `last_attend_time` datetime(0) NULL DEFAULT NULL COMMENT '上一次签到的时间',
  `consecutive_attend_days` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最近一次连续签到的天数',
  `reward_points` int(11) UNSIGNED NOT NULL COMMENT '积分',
  `fans_count` int(11) UNSIGNED NOT NULL COMMENT '粉丝数',
  `gitee_id` int(11) NULL DEFAULT NULL COMMENT 'gitee账号的唯一标识',
  `qq_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'qq的openid',
  `baidu_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '百度云账号的唯一标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '请问', '1', '1', '123456', '1', '1', 0, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-04-01 14:50:24', '2021-05-04 14:50:27', '2021-07-17 14:50:30', NULL, 0, 10, 4, NULL, NULL, NULL);
INSERT INTO `user` VALUES (2, '阿斯蒂', '2', '2', '123456', '2', '2', 1, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-04-03 14:51:34', '2021-05-11 14:51:36', '2021-07-16 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (3, '阿萨大', '3', '3', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-04-06 14:51:34', '2021-05-11 14:51:36', '2021-06-26 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (4, '分', '4', '4', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-04-23 14:51:34', '2021-05-11 14:51:36', '2021-06-20 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (5, '对', '5', '5', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-01 14:51:34', '2021-05-11 14:51:36', '2021-06-12 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (6, 'VS的', '6', '6', '123456', '1', '1', 1, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-05-08 14:50:24', '2021-05-04 14:50:27', '2021-06-22 14:50:30', NULL, 0, 1, 1, NULL, NULL, NULL);
INSERT INTO `user` VALUES (7, 'v萨维奇', '7', '7', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-05-28 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (8, '二维', '8', '8', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-15 14:51:34', '2021-05-11 14:51:36', '2021-05-22 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (9, '她', '9', '9', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-22 14:51:34', '2021-05-11 14:51:36', '2021-04-29 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (10, '和', '10', '10', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-28 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (11, '而', '11', '11', '1', '1', '1', 1, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-05-30 14:50:24', '2021-05-04 14:50:27', '2021-05-13 14:50:30', NULL, 0, 1, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (12, '广东佛山', '12', '12', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-01 14:51:34', '2021-05-11 14:51:36', '2021-05-26 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (13, '覆盖', '13', '13', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-10 14:51:34', '2021-05-11 14:51:36', '2021-04-29 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (14, '都发过', '14', '14', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-04 14:51:34', '2021-05-11 14:51:36', '2021-04-19 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (15, '分', '15', '15', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-25 14:51:34', '2021-05-11 14:51:36', '2021-04-13 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (16, '给对方', '16', '16', '1', '1', '1', 1, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-05-29 14:50:24', '2021-05-04 14:50:27', '2021-05-20 14:50:30', NULL, 0, 1, 1, NULL, NULL, NULL);
INSERT INTO `user` VALUES (17, '断分', '17', '17', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-27 14:51:34', '2021-05-11 14:51:36', '2021-03-11 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (18, '是个', '18', '18', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-07-17 14:51:34', '2021-05-11 14:51:36', '2021-05-03 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (19, '代国防', '19', '19', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-08-21 14:51:34', '2021-04-28 14:51:36', '2021-02-18 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (20, '三个地方', '20', '20', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-07-24 14:51:34', '2021-05-11 14:51:36', '2021-03-12 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (21, 'ysq', '1@qq.com', NULL, '3f54a72d01674b033327ab34ae5010e0', 'MOTciA8o', 'cAr!U-Bv', 3, '/admin/assets/images/defaultUserPhoto.jpg', '我是YSQ!!!!!', NULL, NULL, '2021-06-04 14:58:23', '2021-06-04 14:58:23', '2022-04-16 00:00:25', '2022-04-16 00:00:27', 3, 46, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (22, 'passerbyYSQ', '', NULL, '', 'ad^R9%UP', '(+&iC)e&', 0, 'https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/5151c990b20b470293232448371e4570.jpeg?timestamp=1650097921439', '', '2021-07-10', '广东省/肇庆市/封开县', '2021-06-14 15:54:53', '2021-06-14 15:54:53', '2022-04-16 19:55:02', '2022-04-16 15:57:07', 1, 12, 2, 7369646, NULL, NULL);
INSERT INTO `user` VALUES (23, '过路人', '', NULL, '', 'emNiOgun', 'l#xv3)Jw', 0, 'http://thirdqq.qlogo.cn/g?b=oidb&k=nMMFjagOIQXoHlwJUfHHsA&s=40&t=1582654446', '过路人。。。', NULL, NULL, '2021-06-17 21:31:40', '2021-06-17 21:31:40', '2021-07-05 15:40:05', '2021-07-05 15:41:42', 2, 4, 0, NULL, '1AF065CF5F865B4146F7F69A1AFCC60D', NULL);
INSERT INTO `user` VALUES (26, '子华粉丝', '233@qq.com', NULL, '3f6a37cc275a2c5b77f1b06920a6d5c7', 'Wyn5(thK', '^(=QAv6(', 0, 'https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/553fad416da24fc8aff1e41a3755a101.png?timestamp=1625904434361', '【我是帅锅】', '2021-07-10', '广东省/广州市/天河区', '2021-06-22 23:29:59', '2021-06-22 23:29:59', '2022-04-16 19:55:13', '2022-04-16 15:49:14', 3, 67, 1, NULL, NULL, '421602246');
INSERT INTO `user` VALUES (27, 'ligouzi', '2403298783@qq.com', NULL, '22bb34a6e77baab53ebf873afdfa34bd', 'KB%OApta', 'BNk$@YeY', 3, '/admin/assets/images/defaultUserPhoto.jpg', NULL, NULL, NULL, '2021-07-06 16:15:34', '2021-07-06 16:15:34', '2021-07-10 10:09:47', '2021-07-06 16:34:15', 1, 2, 0, NULL, NULL, '');
INSERT INTO `user` VALUES (28, 'test123', '123@qq.com', NULL, 'b2793335f43645fd8e00c7d18e14e05f', '123', '', 0, '/admin/assets/images/defaultUserPhoto.jpg', NULL, NULL, NULL, '2022-03-27 17:59:07', '2022-03-27 17:59:13', '2022-03-28 19:00:42', '2022-03-28 19:00:22', 1, 1, 0, NULL, NULL, '');

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
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员表和角色表的关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (69, 26, 27, '2022-04-16 19:52:37');

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '视频标题',
  `description` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频描述',
  `duration` bigint(20) NOT NULL COMMENT '视频时长，毫秒',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频封面',
  `author_id` int(10) UNSIGNED NOT NULL COMMENT '作者id',
  `file_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '文件id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of video
-- ----------------------------
INSERT INTO `video` VALUES (1, '《西瓜视频》', '西瓜视频。。。', 1024, NULL, 1, 1, '2022-01-26 17:15:19', '2022-01-26 17:15:23');
INSERT INTO `video` VALUES (2, '思迈特2021-freshman', '冲鸭！！！', 1024, NULL, 1, 2, '2022-01-26 23:58:41', '2022-01-26 23:58:45');

SET FOREIGN_KEY_CHECKS = 1;
