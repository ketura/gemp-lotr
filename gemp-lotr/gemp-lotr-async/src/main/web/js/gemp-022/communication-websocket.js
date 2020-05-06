var GempLotrWebsocketCommunication = Class.extend({
    url:null,
    failure:null,
    websocket:null,

    init:function (url, failure) {
        this.url = "ws://" + location.host + "/gemp-lotr-websocket";
        this.failure = failure;
    },

    errorCheck:function (errorMap) {
        var that = this;
        return function (xhr, status, request) {
            var errorStatus = "" + xhr.status;
            if (errorMap != null && errorMap[errorStatus] != null)
                errorMap[errorStatus](xhr, status, request);
            else if (""+xhr.status != "200")
                that.failure(xhr, status, request);
        };
    },

    startChat:function (room, callback, errorMap) {
        var that = this;
        // Let us open a web socket
        var ws = new WebSocket(this.url+"/chat/"+room);

        ws.onopen = function() {
            that.websocket = ws;
        };

        ws.onmessage = function (evt) {
            var parser = new DOMParser();
            var xmlDoc = parser.parseFromString(evt.data,"text/xml");
            callback(xmlDoc);
        };

        ws.onclose = function() {
            errorMap[0]();
            that.websocket = null;
        };
    },
    sendChatMessage:function (room, messages, errorMap) {
        if (this.websocket != null) {
            this.websocket.send(messages);
        }
    },
    isContinuous:function() {
        return false;
    }
});
