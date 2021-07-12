layui.define(['jquery', 'layer', 'form', 'notice', 'element', 'upload', 'util'], function (exports) {
    var $ = layui.jquery,
        layer = layui.layer,
        form = layui.form,
        notice = layui.notice,
        element = layui.element,
        upload = layui.upload,
        util = layui.util,
        device = layui.device();

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

            // base64 转 blob
            base64ToBlob: function () {
                var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
                    bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
                while(n--){
                    u8arr[n] = bstr.charCodeAt(n);
                }
                return new File([u8arr], filename, {type:mime});
            },

            // 取出url参数
            getUrlParam: function (name) {
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
                var r = decodeURI(window.location.search).substr(1).match(reg);  //匹配目标参数
                if (r != null) return unescape(r[2]);
                return null; //返回参数值
            },

            // 滚动到指定位置
            scrollIntoView: function (selector) {
                document.querySelector(selector).scrollIntoView({
                    behavior: "smooth", // 定义动画过渡效果， "auto"或 "smooth" 之一。默认为 "auto"
                    block: "center", // 定义垂直方向的对齐， "start", "center", "end", 或 "nearest"之一。默认为 "start"
                    inline: "nearest" // 定义水平方向的对齐， "start", "center", "end", 或 "nearest"之一。默认为 "nearest"
                });
                console.log(selector);
            },

            // 格式化时间
            formatDateTime: function (dateTimeStr) { // yyyy-MM-dd HH:mm:ss
                var timestamp = Date.parse(dateTimeStr.replace(/-/gi, "/"));
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
                        } else if (type === "error") { // 请求错误
                            // console.log(xhr);
                            // console.log(typeof xhr.responseJSON);
                            var res = xhr.responseJSON;
                            app.errorNotice(res.code + "：" + res.msg);
                        }
                    }
                });
            },

            focusInsert: function (obj, str) {
                var result, val = obj.value;
                obj.focus();
                if (document.selection) { //ie
                    result = document.selection.createRange();
                    document.selection.empty();
                    result.text = str;
                } else {
                    result = [val.substring(0, obj.selectionStart), str, val.substr(obj.selectionEnd)];
                    obj.focus();
                    obj.value = result.join('');
                }
            },


            // 简易编辑器
            layEditor: function (options) {
                var html = ['<div class="layui-unselect fly-edit">'
                    , '<span type="face" title="插入表情"><i class="iconfont icon-yxj-expression" style="top: 1px;"></i></span>'
                    , '<span type="picture" title="插入图片：img[src]"><i class="iconfont icon-tupian"></i></span>'
                    , '<span type="href" title="超链接格式：a(href)[text]"><i class="iconfont icon-lianjie"></i></span>'
                    , '<span type="code" title="插入代码或引用"><i class="iconfont icon-emwdaima" style="top: 1px;"></i></span>'
                    , '<span type="hr" title="插入水平线">hr</span>'
                    , '<span type="yulan" title="预览"><i class="iconfont icon-yulan1"></i></span>'
                    , '</div>'].join('');

                var log = {}, mod = {
                    face: function (editor, self) { //插入表情
                        var str = '', ul, face = app.faces;
                        for (var key in face) {
                            str += '<li title="' + key + '"><img src="' + face[key] + '"></li>';
                        }
                        str = '<ul id="LAY-editface" class="layui-clear">' + str + '</ul>';
                        layer.tips(str, self, {
                            tips: 3
                            , time: 0
                            , skin: 'layui-edit-face'
                        });
                        $(document).on('click', function () {
                            layer.closeAll('tips');
                        });
                        $('#LAY-editface li').on('click', function () {
                            var title = $(this).attr('title') + ' ';
                            app.focusInsert(editor[0], 'face' + title);
                        });
                    }
                    , picture: function (editor) { //插入图片
                        layer.open({
                            type: 1
                            , id: 'fly-jie-upload'
                            , title: '插入图片'
                            , area: 'auto'
                            , shade: false
                            , area: '465px'
                            , fixed: false
                            , offset: [
                                editor.offset().top - $(window).scrollTop() + 'px'
                                , editor.offset().left + 'px'
                            ]
                            , skin: 'layui-layer-border'
                            , content: ['<form method="post"><ul class="layui-form layui-form-pane" style="margin: 20px;">'
                                , '<li class="layui-form-item">'
                                , '<label class="layui-form-label">URL</label>'
                                , '<div class="layui-input-inline">'
                                , '<input required name="image" placeholder="支持直接粘贴远程图片地址" value="" class="layui-input">'
                                , '</div>'
                                , '<button type="button" class="layui-btn layui-btn-primary" id="uploadImg"><i class="layui-icon">&#xe67c;</i>上传图片</button>'
                                , '</li>'
                                , '<li class="layui-form-item" style="text-align: center;">'
                                , '<button type="button" lay-submit lay-filter="uploadImages" class="layui-btn">确认</button>'
                                , '</li>'
                                , '</ul></form>'].join('')
                            , success: function (layero, index) {
                                var image = layero.find('input[name="image"]');
                                console.log(image);

                                //执行上传实例
                                upload.render({
                                    elem: '#uploadImg',
                                    accept: 'images',
                                    field: 'image'
                                    , url: '/upload/image'
                                    , size: 1024
                                    , done: function (res) {
                                        if (res.code === 2000) {
                                            image.val(res.data.url[0]);
                                        } else {
                                            layer.msg(res.msg, {icon: 5});
                                        }
                                    }
                                });

                                form.on('submit(uploadImages)', function (data) {
                                    // var field = data.field; // 为空，不知道为什么...
                                    //console.log(image.val());
                                    if (!image.val()) return image.focus();
                                    app.focusInsert(editor[0], 'img[' + image.val() + '] ');
                                    layer.close(index);
                                });
                            }
                        });
                    }
                    , href: function (editor) { //超链接
                        layer.prompt({
                            title: '请输入合法链接'
                            , shade: false
                            , fixed: false
                            , id: 'LAY_flyedit_href'
                            , offset: [
                                editor.offset().top - $(window).scrollTop() + 'px'
                                , editor.offset().left + 'px'
                            ]
                        }, function (val, index, elem) {
                            if (!/^http(s*):\/\/[\S]/.test(val)) {
                                layer.tips('这根本不是个链接，不要骗我。', elem, {tips: 1})
                                return;
                            }
                            app.focusInsert(editor[0], ' a(' + val + ')[' + val + '] ');
                            layer.close(index);
                        });
                    }
                    , code: function (editor) { //插入代码
                        layer.prompt({
                            title: '请贴入代码或任意文本'
                            , formType: 2
                            , maxlength: 10000
                            , shade: false
                            , id: 'LAY_flyedit_code'
                            , area: ['800px', '360px']
                        }, function (val, index, elem) {
                            app.focusInsert(editor[0], '[pre]\n' + val + '\n[/pre]');
                            layer.close(index);
                        });
                    }
                    , hr: function (editor) { //插入水平分割线
                        app.focusInsert(editor[0], '[hr]');
                    }
                    , yulan: function (editor) { //预览
                        var content = editor.val();

                        content = /^\{html\}/.test(content)
                            ? content.replace(/^\{html\}/, '')
                            : app.content(content);

                        layer.open({
                            type: 1
                            , title: '预览'
                            , shade: false
                            , area: ['100%', '100%']
                            , scrollbar: false
                            , content: '<div class="detail-body" style="margin:20px;">' + content + '</div>'
                        });
                    }
                };

                layui.use('face', function (face) {
                    options = options || {};
                    app.faces = face;
                    $(options.elem).each(function (index) {
                        var that = this, othis = $(that), parent = othis.parent();
                        parent.prepend(html);
                        parent.find('.fly-edit span').on('click', function (event) {
                            var type = $(this).attr('type');
                            mod[type].call(that, othis, this);
                            if (type === 'face') {
                                event.stopPropagation()
                            }
                        });
                    });
                });

            }

            ,
            escape: function (html) {
                return String(html || '').replace(/&(?!#?[a-zA-Z0-9]+;)/g, '&amp;')
                    .replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/'/g, '&#39;').replace(/"/g, '&quot;');
            }

            //内容转义
            ,
            content: function (content) {
                //支持的html标签
                var html = function (end) {
                    return new RegExp('\\n*\\[' + (end || '') + '(pre|hr|div|span|p|table|thead|th|tbody|tr|td|ul|li|ol|li|dl|dt|dd|h2|h3|h4|h5)([\\s\\S]*?)\\]\\n*', 'g');
                };
                content = app.escape(content || '') //XSS
                    .replace(/img\[([^\s]+?)\]/g, function (img) {  //转义图片
                        return '<img src="' + img.replace(/(^img\[)|(\]$)/g, '') + '">';
                    }).replace(/@(\S+)(\s+?|$)/g, '@<a href="javascript:;" class="fly-aite">$1</a>$2') //转义@
                    .replace(/face\[([^\s\[\]]+?)\]/g, function (face) {  //转义表情
                        var alt = face.replace(/^face/g, '');
                        return '<img alt="' + alt + '" title="' + alt + '" src="' + app.faces[alt] + '">';
                    }).replace(/a\([\s\S]+?\)\[[\s\S]*?\]/g, function (str) { //转义链接
                        var href = (str.match(/a\(([\s\S]+?)\)\[/) || [])[1];
                        var text = (str.match(/\)\[([\s\S]*?)\]/) || [])[1];
                        if (!href) return str;
                        var rel = /^(http(s)*:\/\/)\b(?!(\w+\.)*(sentsin.com|layui.com))\b/.test(href.replace(/\s/g, ''));
                        return '<a href="' + href + '" target="_blank"' + (rel ? ' rel="nofollow"' : '') + '>' + (text || href) + '</a>';
                    }).replace(html(), '\<$1 $2\>').replace(html('/'), '\</$1\>') //转移HTML代码
                    .replace(/\n/g, '<br>') //转义换行
                return content;
            }

        };

    exports('app', app);
});
