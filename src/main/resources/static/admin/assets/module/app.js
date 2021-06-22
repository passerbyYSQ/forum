layui.define(['jquery', 'layer', 'form', 'notice'], function (exports) {
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;
    var notice = layui.notice;

    var app = {
        /**
         * 判断字符串是否为空
         * @param {Object} str
         * true：不为空
         * false：为空c
         */
        isNotBlank: function (str) {
            return str !== null && str !== undefined && str.length > 0;
        },

        /**
         * 判断对象是否为null或者undefined
         * @param {Object} str
         * true：不为空
         * false：为空c
         */
        isNotNull: function (obj) {
            return obj !== null && obj !== undefined;
        },

        // 成功提示
        successNotice: function (msg) {
            notice.msg(msg, {icon: 1});
        },

        // 错误提示
        errorNotice: function (msg) {
            notice.msg(msg, {icon: 2});
        },

        // 信息提示
        infoNotice: function (msg) {
            notice.msg(msg, {icon: 5});
        },

        // loading
        showLoading: function () {
            notice.msg('加载中...', {icon: 4, close: true});
        },

        // 字符数
        charLen: function (val) {
            var arr = val.split(''), len = 0;
            for (var i = 0; i < val.length; i++) {
                arr[i].charCodeAt(0) < 299 ? len++ : len += 2;
            }
            return len;
        },

        // 取出url参数
        getUrlParam: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
            var r = window.location.search.substr(1).match(reg);  //匹配目标参数
            if (r != null) return unescape(r[2]);
            return null; //返回参数值
        },

        // 格式化时间
        formatDateTime: function (dateTimeStr) { // yyyy-MM-dd HH:mm:ss
            var timestamp = Date.parse(dateTimeStr.replace(/-/gi,"/"));
            var publishTime = timestamp / 1000,
                d_seconds,
                d_minutes,
                d_hours,
                d_days,
                timeNow = parseInt(new Date().getTime() / 1000),
                d,

                date = new Date(publishTime * 1000),
                Y = date.getFullYear(),
                M = date.getMonth() + 1,
                D = date.getDate(),
                H = date.getHours(),
                m = date.getMinutes(),
                s = date.getSeconds();

            //小于10的在前面补0
            if (M < 10) {
                M = '0' + M;
            }
            if (D < 10) {
                D = '0' + D;
            }
            if (H < 10) {
                H = '0' + H;
            }
            if (m < 10) {
                m = '0' + m;
            }
            if (s < 10) {
                s = '0' + s;
            }

            d = timeNow - publishTime;
            d_days = parseInt(d / 86400);
            d_hours = parseInt(d / 3600);
            d_minutes = parseInt(d / 60);
            d_seconds = parseInt(d);

            if (d_days > 0 && d_days < 3) {
                return d_days + '天前';
            } else if (d_days <= 0 && d_hours > 0) {
                return d_hours + '小时前';
            } else if (d_hours <= 0 && d_minutes > 0) {
                return d_minutes + '分钟前';
            } else if (d_seconds < 60) {
                if (d_seconds <= 0) {
                    return '刚刚';
                } else {
                    return d_seconds + '秒前';
                }
            } else if (d_days >= 3 && d_days < 30) {
                return M + '-' + D + ' ' + H + ':' + m;
            } else if (d_days >= 30) {
                return Y + '-' + M + '-' + D + ' ' + H + ':' + m;
            }
        },

        // 封装ajax
        ajax: function (url, data, successCallback = function (res) {
                        }, type = "post",
                        failedCallback = function (res) {
                        }) { // 业务失败

            //var loadIndex = layer.load(2); // 显示loading...
            app.showLoading();

            var user = localStorage.getItem("user");
            //var token;
            // if (app.isNotNull(user)) {
            //     token = user.token;
            // }

            $.ajax(url, {
                type: type,
                dataType: "json",
                // traditional:true, // value可以是数组
                data: data,
                timeout: 30000, // 超时时间为30秒
                // headers: {
                //     token: token// 携带token
                // },
                crossDomain: true,
                xhrFields: {
                    withCredentials: true // 携带cookie
                },
                success: function (res, textStatus, xhr) {
                    //layer.close(loadIndex); // 关闭 loading
                    notice.destroy();

                    if (res.code !== 2000) {
                        app.errorNotice(res.msg);
                        failedCallback(res); // 业务失败的回调，可通过传参自定义。默认实现以弹框形式输出错误信息
                    } else {
                        // 判断是否签发了新的token。如果是，更新token
                        var header = xhr.getAllResponseHeaders();
                        if (app.isNotBlank(header.token)) { // 如果签发了新的token
                            user.token = header.token;
                            localStorage.setItem("user", user);
                        }
                        successCallback(res); // 业务成功
                    }
                },
                // 请求发生错误
                error: function (xhr, type, errorThrown) {
                    //layer.close(loadIndex); // 关闭 loading
                    notice.destroy();
                    if (type === "timeout") {
                        app.errorNotice('请求超时');
                    } else if (type === "error") {
                        app.errorNotice('请求错误');
                    }
                }
            });
        }
    };

    exports('app', app);
});
