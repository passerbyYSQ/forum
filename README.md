## CI/CD
在master分支commit或者通过pr合并代码到master分支，只要标题不以draft开头，就会触发CI/CD流水线，自动编译和打包，部署到演示服务器

## idea设置

1. 设置自动去除无效导包
2. 安装lombok插件
3. MyBatisX插件
4. MyBatis Generator插件

## 坑
1. PageHelper联表查询分页时total错误
2. Controller类上加@Validated和shiro的注解冲突：https://zhuanlan.zhihu.com/p/143126919

## 编码注意事项

1. 注意代码分层，不要将过多逻辑堆积到controller层
    + cotroller：参数接收和判断，结果返回
    + service：核心业务逻辑
    + dao：数据访问层
2. 注意写注释
3. 常用的idea快捷键
    + 格式化代码快捷键：ctrl + L
    + 查看类中所有方法：ctrl + f12
    + 大小写转换：ctrl + shift + u
    + 查看继承树：选中类名，ctrl + h
    + 跳转到实现类：ctrl + 鼠标左键
    + 查看类图：选中类名，ctrl + alt + u
    + 追踪源码，光标后退和前进：ctrl + alt + 左箭头 | 右箭头
    + 重写方法：ctrl + o
    + surround with： ctrl + alt + t
    + generate：alt + insert
    + 重命名所有引用的变量：shift + f6
    + 删除一行：ctrl + y
    + 全局替换：ctrl + shift + r
    + 全局搜索：ctrl + shift + f，需要关闭win10默认的快捷键
4. 【新增】代码中不要使用System.out.println。使用lombok提供的@slf注解，打印调试日志


## 第三方类库使用

1. tk.mapper：https://github.com/abel533/Mapper/
2. 后端分页插件PageHelper：https://github.com/pagehelper/Mybatis-PageHelper
3. 模板引擎thymeleaf中文文档
4. 前端树形表格组件treetable-lay：https://gitee.com/ele-admin/treetable-lay
5. Hibernate Validator中文文档：http://docs.jboss.org/hibernate/validator/4.2/reference/zh-CN/html/validator-usingvalidator.html
6. layui官方文档：https://www.layui.com/doc/
7. jquery zTree官方文档：http://www.treejs.cn/v3/main.php#_zTreeInfo
8. + 后台管理静态资源easy-web-iframe：https://github.com/lianxiansen/easy-web-iframe
   + 开发文档：https://www.kancloud.cn/sansui/easyweb/#/catalog
9. 论坛前台静态资源fly：https://github.com/layui/fly
10. TinyMCE富文本编辑器中文文档：http://tinymce.ax-z.cn/
11. 前端标签输入组件jquery tagsInput：https://github.com/xoxco/jQuery-Tags-Input
12. jwt：https://jwt.io/
13. jquery：https://jquery.cuishifeng.cn/
14. shiro：http://shiro.apache.org/
15. jsoup处理html富文本 https://jsoup.org/，https://blog.csdn.net/qq_24549805/article/details/52833463
16. 百度授权登录官方文档：https://pan.baidu.com/union/document/entrance#%E6%8E%A5%E5%85%A5%E6%B5%81%E7%A8%8B
17. thymeleaf-shiro：https://github.com/theborakompanioni/thymeleaf-extras-shiro
18. + mybatis3中文文档：https://mybatis.org/mybatis-3/zh/index.html
    + 动态SQL if：https://blog.csdn.net/xu1916659422/article/details/78104976/
19. + LayIM 3.9.1：https://github.com/passerbyYSQ/LayIM-3.9.1
    + 开发文档：http://www.51xuediannao.com/js/jquery/layim.html
20. 西瓜播放器：https://v2.h5player.bytedance.com/
21. Redis命令中文文档：http://doc.redisfans.com/
