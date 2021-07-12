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

 Date: 10/07/2021 15:23:06
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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '收藏\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES (7, 22, 2, '2021-06-30 10:44:06');
INSERT INTO `collect` VALUES (12, 26, 2, '2021-06-30 12:15:51');
INSERT INTO `collect` VALUES (13, 26, 12, '2021-06-30 23:12:39');
INSERT INTO `collect` VALUES (14, 22, 12, '2021-07-02 11:56:50');
INSERT INTO `collect` VALUES (15, 21, 2, '2021-07-04 15:19:07');
INSERT INTO `collect` VALUES (16, 23, 8, '2021-07-05 15:43:24');

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
) ENGINE = InnoDB AUTO_INCREMENT = 74 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论通知' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '一级评论' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签\r\n' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '点赞' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of like
-- ----------------------------
INSERT INTO `like` VALUES (6, 22, 1, '2021-06-20 10:32:09', 0);
INSERT INTO `like` VALUES (13, 22, 11, '2021-06-22 09:42:41', 0);
INSERT INTO `like` VALUES (18, 26, 2, '2021-06-24 20:48:52', 0);
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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '积分奖励记录\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of points_record
-- ----------------------------
INSERT INTO `points_record` VALUES (1, 26, 1, '签到', '2021-07-10 15:01:47');

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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主题帖' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES (1, '测试1111111', '<p>测试1111111说的不是吧微软</p>', 1, 1, '2021-05-25 21:13:29', '2021-05-25 21:13:29', 11, 1, 0, 0, 0, 0, 0, '2021-07-01 23:28:43', 0);
INSERT INTO `post` VALUES (2, '测试标题', '<p>asvdv</p>', 1, 1, '2021-05-25 22:58:29', '2021-07-01 16:41:01', 198, 3, 3, 53, 1, 1, 56, '2021-07-01 21:52:16', 0);
INSERT INTO `post` VALUES (3, '这是一条非常重要的通知！！！', '<p><img src=\"https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/fc8ef26c62b94b9dbcad19570d9a6880.jpg?timestamp=1621955365165\" width=\"800\" /></p>\n<h1>欢迎。。。</h1>\n<p>&nbsp;</p>\n<table style=\"border-collapse: collapse; width: 100%;\" border=\"1\">\n<tbody>\n<tr>\n<td style=\"width: 50%;\">1</td>\n<td style=\"width: 50%;\">2</td>\n</tr>\n<tr>\n<td style=\"width: 50%;\">3</td>\n<td style=\"width: 50%;\">&nbsp;</td>\n</tr>\n</tbody>\n</table>\n<p>&nbsp;</p>\n<p><span style=\"font-size: 36pt;\">😂</span></p>\n<p><span style=\"font-size: 14pt;\">你好</span></p>', 1, 2, '2021-05-25 23:11:16', '2021-05-25 23:11:16', 9, 0, 0, 0, 0, 0, 0, '2021-07-01 23:28:47', 0);
INSERT INTO `post` VALUES (4, '我是真的帅', '<p>我是真的帅</p>', 1, 2, '2021-05-25 23:13:02', '2021-05-25 23:13:02', 2, 0, 0, 0, 0, 0, 0, '2021-07-01 23:28:50', 1);
INSERT INTO `post` VALUES (5, '测试标签提示', '<p>测试标签提示测试标签提示测试标签提示</p>', 1, 1, '2021-05-28 23:29:21', '2021-05-28 23:29:21', 1, 0, 0, 0, 0, 0, 0, '2021-07-01 23:28:53', 67);
INSERT INTO `post` VALUES (6, '【修改22】测试标签1123235', '<p>测试帖子修改！！！ 8888 <img src=\"http://img.t.sinajs.cn/t4/appstyle/expression/ext/normal/9d/sada_thumb.gif\" alt=\"[泪]\" /></p>', 1, 1, '2021-05-28 23:30:36', '2021-07-02 11:12:31', 16, 2, 0, 0, 0, 1, -1, '2021-07-01 23:28:56', 67);
INSERT INTO `post` VALUES (7, 'asvwev', '<p>erberb</p>', 1, 1, '2021-06-12 16:46:25', '2021-06-12 16:46:25', 3, 0, 0, 1, 0, 0, 0, '2021-07-02 23:50:10', 0);
INSERT INTO `post` VALUES (8, '【测试】锁定', '<blockquote>\n<p>【测试】锁定【测试】锁定【测试】锁定【测试】锁定</p>\n</blockquote>\n<p>&nbsp;</p>\n<h1><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/4206857946a94d639751220e1ba1036a.png?timestamp=1623502282211\" alt=\"Logo\" width=\"194\" height=\"146\" /><img src=\"https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/22d4b8bb504c447ca52b7b550180af0f.png?timestamp=1625211592797\" alt=\"dfnfd\" width=\"730\" height=\"781\" /></h1>\n<h1><img src=\"https://webchat-ysq.oss-cn-shenzhen.aliyuncs.com/images/e5914597f0d24ef9a3399cda535518a8.png?timestamp=1625211664578\" alt=\"ltlt\" width=\"800\" height=\"496\" /></h1>\n<h1>标题</h1>\n<p style=\"padding-left: 40px;\">你好</p>\n<p>&nbsp;</p>\n<p style=\"padding-left: 80px;\">&nbsp;</p>', 1, 1, '2021-06-12 16:53:28', '2021-07-02 23:55:29', 12, 3, 1, 1, 0, 0, 0, '2021-07-02 23:57:16', 23);
INSERT INTO `post` VALUES (9, '测试验证码测试验证码测试验证码', '<p>测试验证码测试验证码测试验证码</p>\n<p>&nbsp;</p>\n<p style=\"text-align: right;\">测试验证码测试验证码</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>测试验证码测试验证码</p>', 1, 1, '2021-06-14 23:56:31', '2021-06-14 23:56:31', 5, 1, 0, 0, 0, 1, 0, '2021-07-01 23:29:05', 2);
INSERT INTO `post` VALUES (10, '测试权限', '<p>测试权限</p>\n<p>&nbsp;</p>\n<p>测试权限</p>', 1, 1, '2021-06-18 21:36:33', '2021-06-18 21:36:33', 2, 0, 0, 0, 0, 1, 0, '2021-07-01 23:29:07', 1);
INSERT INTO `post` VALUES (11, '测试登录后发帖，测试修改时间', '<p><span style=\"color: #e03e2d;\">测试登录后发帖测试登录后发帖测试登录后发帖</span></p>\n<ol>\n<li>Sfgss</li>\n<li>sdvsd</li>\n<li>svsd</li>\n<li>&nbsp;</li>\n</ol>\n<p style=\"text-align: right;\">测试登录后发帖测试登录后发帖测试登录后发帖</p>', 22, 1, '2021-06-18 21:41:46', '2021-06-20 00:22:34', 19, 2, 0, 7, 1, 0, 99, '2021-07-04 21:31:25', 2);
INSERT INTO `post` VALUES (12, '测试发帖', '<p>测试发帖测试发帖测试发帖测试发帖</p>', 22, 1, '2021-06-18 23:27:03', '2021-06-18 23:27:03', 10, 2, 2, 7, 0, 0, 0, '2021-07-02 11:56:33', 0);
INSERT INTO `post` VALUES (13, '测试代码高亮', '<p>哈哈哈哈</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<pre class=\"language-java\"><code>package top.ysqorz.forum.config;\n\nimport org.springframework.context.annotation.Configuration;\nimport org.springframework.format.FormatterRegistry;\nimport org.springframework.web.servlet.config.annotation.CorsRegistry;\nimport org.springframework.web.servlet.config.annotation.WebMvcConfigurer;\n\n/**\n * @author passerbyYSQ\n * @create 2021-01-29 14:41\n */\n@Configuration\npublic class WebMvcConfig implements WebMvcConfigurer {\n\n    /**\n     * 前后端分离需要解决跨域问题\n     */\n    @Override\n    public void addCorsMappings(CorsRegistry registry) {\n        registry.addMapping(\"/**\")\n                .allowedOrigins(\"*\")  // 放行哪些原始域\n                .allowCredentials(true) // 是否发送cookie\n                .allowedMethods(\"GET\", \"POST\", \"PUT\", \"OPTIONS\", \"DELETE\", \"PATCH\")\n                .exposedHeaders(\"*\")\n                .allowedHeaders(\"*\") // allowedHeaders是exposedHeaders的子集\n                .maxAge(3600); // 预检请求OPTIONS请求的缓存时间\n    }\n\n    /**\n     * 在参数绑定时，自定义String-&gt;String的转换器，\n     * 在转换逻辑中对参数值进行转义，从而达到防XSS的效果\n     */\n    @Override\n    public void addFormatters(FormatterRegistry registry) {\n        registry.addConverter(new EscapeStringConverter());\n        //registry.addFormatter(new LocalDateTimeFormatter());\n\n    }\n}\n</code></pre>', 26, 1, '2021-06-24 20:55:07', '2021-06-30 15:49:17', 15, 1, 0, 7, 0, 0, 0, '2021-06-30 12:48:18', 0);
INSERT INTO `post` VALUES (14, 'sdbdsbsdb', '<p>sdvsdbsbvsdb</p>', 26, 1, '2021-06-30 15:50:50', '2021-06-30 15:50:50', 2, 1, 0, 0, 1, 0, 0, '2021-06-30 15:50:50', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主题帖表和标签表的关联表' ROW_FORMAT = Dynamic;

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
INSERT INTO `post_label` VALUES (64, 2, 1);
INSERT INTO `post_label` VALUES (65, 2, 3);
INSERT INTO `post_label` VALUES (66, 6, 11);
INSERT INTO `post_label` VALUES (73, 8, 12);
INSERT INTO `post_label` VALUES (74, 8, 2);
INSERT INTO `post_label` VALUES (75, 8, 13);

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
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '涉及权限的资源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES (1, 0, '系统管理', 'layui-icon layui-icon-set', '', 'system', 0, 1);
INSERT INTO `resource` VALUES (16, 1, '帖子管理', '', '', 'post', 0, 101);
INSERT INTO `resource` VALUES (17, 16, '修改帖子', '', '', 'post:update', 1, 91);
INSERT INTO `resource` VALUES (18, 16, '置顶', '', '', 'post:top', 1, 71);
INSERT INTO `resource` VALUES (19, 16, '精品', '', '', 'post:quality', 1, 81);
INSERT INTO `resource` VALUES (20, 1, '用户管理', '', '', 'user', 0, 105);
INSERT INTO `resource` VALUES (21, 20, '角色分配', '', '', 'user:allot', 1, 95);
INSERT INTO `resource` VALUES (22, 20, '重置密码', '', '', 'user:reset', 1, 85);
INSERT INTO `resource` VALUES (23, 20, '删除角色', '', '', 'user:del', 1, 75);
INSERT INTO `resource` VALUES (24, 20, '黑名单设置', '', '', 'user:blacklist', 1, 65);
INSERT INTO `resource` VALUES (25, 1, '角色管理', '', '', 'role', 0, 104);
INSERT INTO `resource` VALUES (26, 25, '角色添加', '', '', 'role:add', 1, 94);
INSERT INTO `resource` VALUES (27, 25, '角色删除', '', '', 'role:del', 1, 84);
INSERT INTO `resource` VALUES (28, 25, '角色修改', '', '', 'role:update', 1, 74);
INSERT INTO `resource` VALUES (29, 25, '权限分配', '', '', 'role:allot', 1, 64);
INSERT INTO `resource` VALUES (30, 1, '话题管理', '', '', 'topic', 0, 102);
INSERT INTO `resource` VALUES (31, 30, '添加话题', '', '', 'topic:add', 1, 92);
INSERT INTO `resource` VALUES (32, 30, '归档', '', '', 'topic:file', 1, 82);
INSERT INTO `resource` VALUES (33, 30, '查看帖子', '', '', 'topic:check', 1, 72);
INSERT INTO `resource` VALUES (34, 30, '修改话题', '', '', 'topic:update', 1, 62);
INSERT INTO `resource` VALUES (35, 16, '锁定帖子', '', '', 'post:lock', 1, 61);
INSERT INTO `resource` VALUES (36, 16, '删除帖子', '', '', 'post:del', 1, 51);
INSERT INTO `resource` VALUES (37, 1, '后台准入', '', '', 'admin:access', 1, 106);

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
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (3, 'b', '123', '2021-05-19 16:54:43');
INSERT INTO `role` VALUES (17, '12333', '12', '2021-05-20 15:57:26');
INSERT INTO `role` VALUES (18, 'b', '132', '2021-05-20 16:59:53');
INSERT INTO `role` VALUES (19, 'b', '132', '2021-05-20 16:59:56');
INSERT INTO `role` VALUES (20, '123', '123', '2021-05-20 16:59:58');
INSERT INTO `role` VALUES (21, 'bfbb', '', '2021-06-04 20:38:01');
INSERT INTO `role` VALUES (23, 'admin', '', '2021-06-30 22:04:03');
INSERT INTO `role` VALUES (26, '普通管理员', '', '2021-07-09 17:44:12');
INSERT INTO `role` VALUES (27, '超级管理员', '', '2021-07-09 17:44:19');

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
) ENGINE = InnoDB AUTO_INCREMENT = 141 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表和资源表的关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_resource
-- ----------------------------
INSERT INTO `role_resource` VALUES (1, 20, 15);
INSERT INTO `role_resource` VALUES (47, 23, 1);
INSERT INTO `role_resource` VALUES (48, 23, 16);
INSERT INTO `role_resource` VALUES (49, 23, 17);
INSERT INTO `role_resource` VALUES (50, 23, 19);
INSERT INTO `role_resource` VALUES (51, 23, 18);
INSERT INTO `role_resource` VALUES (88, 27, 1);
INSERT INTO `role_resource` VALUES (89, 27, 37);
INSERT INTO `role_resource` VALUES (90, 27, 20);
INSERT INTO `role_resource` VALUES (91, 27, 21);
INSERT INTO `role_resource` VALUES (92, 27, 22);
INSERT INTO `role_resource` VALUES (93, 27, 23);
INSERT INTO `role_resource` VALUES (94, 27, 24);
INSERT INTO `role_resource` VALUES (95, 27, 25);
INSERT INTO `role_resource` VALUES (96, 27, 26);
INSERT INTO `role_resource` VALUES (97, 27, 27);
INSERT INTO `role_resource` VALUES (98, 27, 28);
INSERT INTO `role_resource` VALUES (99, 27, 29);
INSERT INTO `role_resource` VALUES (100, 27, 30);
INSERT INTO `role_resource` VALUES (101, 27, 31);
INSERT INTO `role_resource` VALUES (102, 27, 32);
INSERT INTO `role_resource` VALUES (103, 27, 33);
INSERT INTO `role_resource` VALUES (104, 27, 34);
INSERT INTO `role_resource` VALUES (105, 27, 16);
INSERT INTO `role_resource` VALUES (106, 27, 17);
INSERT INTO `role_resource` VALUES (107, 27, 19);
INSERT INTO `role_resource` VALUES (108, 27, 18);
INSERT INTO `role_resource` VALUES (109, 27, 35);
INSERT INTO `role_resource` VALUES (110, 27, 36);
INSERT INTO `role_resource` VALUES (126, 26, 1);
INSERT INTO `role_resource` VALUES (127, 26, 37);
INSERT INTO `role_resource` VALUES (128, 26, 20);
INSERT INTO `role_resource` VALUES (129, 26, 24);
INSERT INTO `role_resource` VALUES (130, 26, 30);
INSERT INTO `role_resource` VALUES (131, 26, 31);
INSERT INTO `role_resource` VALUES (132, 26, 32);
INSERT INTO `role_resource` VALUES (133, 26, 33);
INSERT INTO `role_resource` VALUES (134, 26, 34);
INSERT INTO `role_resource` VALUES (135, 26, 16);
INSERT INTO `role_resource` VALUES (136, 26, 17);
INSERT INTO `role_resource` VALUES (137, 26, 19);
INSERT INTO `role_resource` VALUES (138, 26, 18);
INSERT INTO `role_resource` VALUES (139, 26, 35);
INSERT INTO `role_resource` VALUES (140, 26, 36);

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
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '二级评论' ROW_FORMAT = Dynamic;

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
INSERT INTO `topic` VALUES (1, '测试', '测试专用', 11, 1, '2021-05-25 16:09:46', 0, 0);
INSERT INTO `topic` VALUES (2, '通知', '发通知', 3, 1, '2021-05-25 16:10:09', 0, 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '请问', '1', '1', '123456', '1', '1', 0, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-04-01 14:50:24', '2021-05-04 14:50:27', '2021-07-17 14:50:30', NULL, 0, 5, 1, NULL, NULL, NULL);
INSERT INTO `user` VALUES (2, '阿斯蒂', '2', '2', '123456', '2', '2', 1, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-04-03 14:51:34', '2021-05-11 14:51:36', '2021-07-16 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (3, '阿萨大', '3', '3', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-04-06 14:51:34', '2021-05-11 14:51:36', '2021-06-26 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (4, '分', '4', '4', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-04-23 14:51:34', '2021-05-11 14:51:36', '2021-06-20 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (5, '对', '5', '5', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-01 14:51:34', '2021-05-11 14:51:36', '2021-06-12 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (6, 'VS的', '6', '6', '123456', '1', '1', 1, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-05-08 14:50:24', '2021-05-04 14:50:27', '2021-06-22 14:50:30', NULL, 0, 1, 1, NULL, NULL, NULL);
INSERT INTO `user` VALUES (7, 'v萨维奇', '7', '7', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-13 14:51:34', '2021-05-11 14:51:36', '2021-05-28 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (8, '二维', '8', '8', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-15 14:51:34', '2021-05-11 14:51:36', '2021-05-22 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (9, '她', '9', '9', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-22 14:51:34', '2021-05-11 14:51:36', '2021-04-29 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (10, '和', '10', '10', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-05-28 14:51:34', '2021-05-11 14:51:36', '2021-05-12 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (11, '而', '11', '11', '1', '1', '1', 1, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-05-30 14:50:24', '2021-05-04 14:50:27', '2021-05-13 14:50:30', NULL, 0, 1, 1, NULL, NULL, NULL);
INSERT INTO `user` VALUES (12, '广东佛山', '12', '12', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-01 14:51:34', '2021-05-11 14:51:36', '2021-05-26 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (13, '覆盖', '13', '13', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-10 14:51:34', '2021-05-11 14:51:36', '2021-04-29 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (14, '都发过', '14', '14', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-04 14:51:34', '2021-05-11 14:51:36', '2021-04-19 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (15, '分', '15', '15', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-25 14:51:34', '2021-05-11 14:51:36', '2021-04-13 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (16, '给对方', '16', '16', '1', '1', '1', 1, '/admin/assets/images/logo.png', '1', '2021-04-07', '1', '2021-05-29 14:50:24', '2021-05-04 14:50:27', '2021-05-20 14:50:30', NULL, 0, 1, 1, NULL, NULL, NULL);
INSERT INTO `user` VALUES (17, '断分', '17', '17', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-06-27 14:51:34', '2021-05-11 14:51:36', '2021-03-11 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (18, '是个', '18', '18', '2', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-07-17 14:51:34', '2021-05-11 14:51:36', '2021-05-03 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (19, '代国防', '19', '19', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-08-21 14:51:34', '2021-04-28 14:51:36', '2021-02-18 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (20, '三个地方', '20', '20', '123456', '2', '2', 2, '/admin/assets/images/logo.png', '2', '2021-05-18', '2', '2021-07-24 14:51:34', '2021-05-11 14:51:36', '2021-03-12 14:51:39', NULL, 0, 2, 2, NULL, NULL, NULL);
INSERT INTO `user` VALUES (21, 'ysq', '1@qq.com', NULL, '3f54a72d01674b033327ab34ae5010e0', 'MOTciA8o', 'IoRji@9r', 3, 'https://gitee.com/assets/no_portrait.png', NULL, NULL, NULL, '2021-06-04 14:58:23', '2021-06-04 14:58:23', '2021-07-05 00:57:14', '2021-07-05 01:15:30', 3, 8, 0, NULL, NULL, NULL);
INSERT INTO `user` VALUES (22, 'passerbyYSQ', '', NULL, '', 'ad^R9%UP', 'bkDZ@ot)', 3, 'https://gitee.com/assets/no_portrait.png', NULL, NULL, NULL, '2021-06-14 15:54:53', '2021-06-14 15:54:53', '2021-07-05 00:57:35', '2021-07-05 01:16:09', 2, 6, 0, 7369646, NULL, NULL);
INSERT INTO `user` VALUES (23, '过路人', '', NULL, '', 'emNiOgun', 'l#xv3)Jw', 0, 'http://thirdqq.qlogo.cn/g?b=oidb&k=nMMFjagOIQXoHlwJUfHHsA&s=40&t=1582654446', NULL, NULL, NULL, '2021-06-17 21:31:40', '2021-06-17 21:31:40', '2021-07-05 15:40:05', '2021-07-05 15:41:42', 2, 4, 0, NULL, '1AF065CF5F865B4146F7F69A1AFCC60D', NULL);
INSERT INTO `user` VALUES (26, '子华粉丝', '', NULL, '', 'Wyn5(thK', 'PYOiUdVx', 3, 'https://dss0.bdstatic.com/7Ls0a8Sm1A5BphGlnYG/sys/portrait/item/netdisk.1.da7f3d21.-moaj1fMJ9CaPQczTz98eg.jpg', NULL, NULL, NULL, '2021-06-22 23:29:59', '2021-06-22 23:29:59', '2021-07-10 14:58:11', '2021-07-10 15:01:47', 1, 8, 0, NULL, NULL, '421602246');
INSERT INTO `user` VALUES (27, 'ligouzi', '2403298783@qq.com', NULL, '22bb34a6e77baab53ebf873afdfa34bd', 'KB%OApta', 'BNk$@YeY', 3, '/admin/assets/images/defaultUserPhoto.jpg', NULL, NULL, NULL, '2021-07-06 16:15:34', '2021-07-06 16:15:34', '2021-07-10 10:09:47', '2021-07-06 16:34:15', 1, 2, 0, NULL, NULL, '');

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
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员表和角色表的关联表' ROW_FORMAT = Dynamic;

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
INSERT INTO `user_role` VALUES (62, 19, 3, '2021-05-22 00:31:10');
INSERT INTO `user_role` VALUES (64, 19, 2, '2021-05-27 23:42:43');
INSERT INTO `user_role` VALUES (65, 26, 27, '2021-06-30 22:04:40');
INSERT INTO `user_role` VALUES (66, 22, 23, '2021-07-01 12:16:58');
INSERT INTO `user_role` VALUES (67, 27, 27, '2021-07-09 17:55:04');

SET FOREIGN_KEY_CHECKS = 1;
