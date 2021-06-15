/** passerbyYSQ 前台需要的公共模块  */
layui.config({
    version: '318',
    base: '/front/res/mods/'
}).extend({
    notice: 'notice/notice',  // 第三方的通知组件
    tagsInput: 'tagsInput/tagsInput',
    app: 'common'
}).use(['app'], function () {
    var app = layui.app;

    // 判断url中是否
    var token = app.getUrlParam('token');
    console.log(token);
    if (app.isNotNull(token)) {
        var user = {};
        user.token = token;
        window.localStorage.setItem('user', JSON.stringify(user));
    }
});

