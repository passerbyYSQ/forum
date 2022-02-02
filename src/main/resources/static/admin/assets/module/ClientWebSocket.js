layui.define(['app'], function (exports) {
    var app = layui.app;
    const MSG_TYPE = app.MSG_TYPE;
    var Msg = app.Msg;
    const WS_SERVER = 'ws://localhost:8081/ws';

    /**
     * 封装WebSocket
     */
    class ClientWebSocket {
        SocketClazz;    // 浏览器的WebSocket类
        socket;         // 长连接
        heartBeatTimer; // 心跳定时器
        options;
        channelId;
        server;

        constructor(opt) {
            this.options = opt || {};
            var _that = this;
            let callback = function (res) {
                _that.server = res.data;
                _that.SocketClazz = window.WebSocket || window.MozWebSocket;
                _that._initSocket();
            };
            app.ajax('/im/server', {}, callback, 'get', null, false);
        }

        _initSocket() {
            this.socket = new this.SocketClazz(this.server);
            if (this.socket) {
                this.socket.onopen = this._onOpenCallback.bind(this);
                this.socket.onclose = this._onCloseCallback.bind(this);
                this.socket.onerror = this._onErrorCallback.bind(this);
                this.socket.onmessage = this._onMsgReceived.bind(this);
            }
        }

        sendByHttp(msg, failedCallback) { // msgObj
            app.ajax('/im/send', {
                msgJson: JSON.stringify(msg),
                channelId: this.channelId
            }, null, 'post', failedCallback, false);
        }

        send(msg) { // MsgObj
            if (!this.channelId) { // 未绑定不发送
                return;
            }
            this._trySend(msg, 1);
        }

        _trySend(msg, cnt) {
            if (cnt > 3) {
                console.log('重试3次发送消息失败，丢弃消息', msg);
                app.errorNotice('重试3次发送消息失败，丢弃消息：' + JSON.stringify(msg));
                return;
            }
            if (this.socket && this.socket.readyState === this.SocketClazz.OPEN) {
                console.log('发送消息成功', msg);
                this.socket.send(JSON.stringify(msg));
                return;
            }
            this._clearHeartBeatTimer();
            // 尝试重连，成功会发送执行_onOpenCallback，里面也有个超时重试
            // 但不存在trySend第2次之后，第一次重连的bind消息才回来。因为在trySend第2次之前的_initSocket会把this.socket重置掉
            // 因为_initSocket中重置了this.socket，第一次重连成功的_onOpenCallback自然也不会执行
            this._initSocket();
            var _that = this;
            setTimeout(function () {
                console.log(`第${cnt}次尝试发送`, msg);
                _that._trySend(msg, cnt + 1); // 延时1秒，递归调用
            }, 1000);
        }

        _onOpenCallback() {
            if (this.channelId || !this.socket || this.socket.readyState !== this.SocketClazz.OPEN) {
                return;
            }
            console.log('长连接成功建立');
            // 发送绑定类型的消息
            var data = {
                token: app.getToken(),
                groupId: this.options.groupId
            };
            if (!data.token || !data.groupId) {
                app.errorNotice('缺少参数token或groupId');
                throw new Error('缺少参数token或groupId');
            }
            this.socket.send(this._createMsg(MSG_TYPE.BIND, data));
            console.log('成功发送绑定类型的消息');
            setTimeout(this._onOpenCallback.bind(this), 1000); // 超时重试，直到后端绑定成功或者后端关闭了通道才放弃
        }

        _initHeartBeatTimer() {
            if (this.heartBeatTimer) {
                return;
            }
            var heartBeatJob = () => {
                if (this.socket && this.socket.readyState === this.SocketClazz.OPEN) {
                    this.socket.send(this._createMsg(MSG_TYPE.PING));
                    console.log('发送心跳包', new Date().format());
                } else {
                    this._clearHeartBeatTimer(); // 清除当前心跳定时器
                    this._initSocket(); // 尝试重连，成功会发送执行_onOpenCallback，发送绑定消息和开启新的心跳定时器
                }
            };
            // 每隔10秒（必须小与后端定义的超时时间）发送一个心跳包
            this.heartBeatTimer = setInterval(heartBeatJob.bind(this), 1000 * 10);
        }

        _clearHeartBeatTimer() {
            // 清除定时器
            clearInterval(this.heartBeatTimer);
            this.heartBeatTimer = null;
        }

        _onCloseCallback() {
            this._clearHeartBeatTimer();
            this.socket = null;
            console.log('长连接关闭，成功清除心跳定时器，并且置空socket');
            app.errorNotice('长连接关闭，成功清除心跳定时器');
        }

        _onErrorCallback() {
            console.log('发生异常');
            app.errorNotice('发生异常');
            if (this.socket) {
                this.socket.close(); // 关闭后会执行_onCloseCallback
            }
        }

        _onMsgReceived(ev) {
            var msg = JSON.parse(ev.data);
            console.log('接收到消息', msg);
            if (msg.msgType === MSG_TYPE.BIND) {
                this.channelId = msg.data.channelId;
                // 开启心跳
                this._initHeartBeatTimer();
                console.log('绑定成功，开启心跳定时器');
            } else if (msg.msgType !== MSG_TYPE.PONG && this.options.onMsgReceived) {
                this.options.onMsgReceived(msg);
            }
        }

        _createMsg(msgType, data) {
            var msg = new Msg(msgType, this.options.channelType, data);
            return JSON.stringify(msg);
        }
    }

    exports('ClientWebSocket', ClientWebSocket);
});
