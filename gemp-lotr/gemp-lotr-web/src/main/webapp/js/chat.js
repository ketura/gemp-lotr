var ChatBoxUI = Class.extend({
    name: null,
    div: null,
    communication: null,
    chatMessagesDiv: null,
    chatTalkDiv: null,
    chatListDiv: null,
    showTimestamps: false,
    talkBoxHeight: 25,

    init: function(name, div, url, showList) {
        var that = this;
        this.name = name;
        this.div = div;
        this.communication = new GempLotrCommunication(url, function(xhr, ajaxOptions, thrownError) {
            if (thrownError != "abort") {
                that.appendMessage("Chat had a problem communicating with the server, no new messages will be displayed, but your messages still might get sent.", "warningMessage");
                that.appendMessage("Reload the browser page (press F5) to resume the chat.", "warningMessage");
            }
        });

        this.chatMessagesDiv = $("<div class='chatMessages'></div>");
        this.chatTalkDiv = $("<input class='chatTalk'>");
        if (showList) {
            this.chatListDiv = $("<div class='userList'></div>");
            this.div.append(this.chatListDiv);
        }

        this.div.append(this.chatMessagesDiv);
        this.div.append(this.chatTalkDiv);

        var that = this;

        this.communication.startChat(this.name, function(xml) {
            that.processMessages(xml, true);
        });

        this.chatTalkDiv.bind("keypress", function(e) {
            var code = (e.keyCode ? e.keyCode : e.which);
            if (code == 13) {
                var value = $(this).val();
                that.sendMessage(value);
                that.appendMessage("<b>Me:</b> " + that.escapeHtml(value));
                $(this).val("");
            }
        });
    },

    escapeHtml: function(text) {
        return $('<div/>').text(text).html();
    },

    setBounds: function(x, y, width, height) {
        var talkBoxPadding = 3;

        var userListWidth = 150;
        if (this.chatListDiv == null)
            userListWidth = 0;

        if (this.chatListDiv != null)
            this.chatListDiv.css({ position: "absolute", left: x + width - userListWidth + "px", top: y + "px", width: userListWidth, height: height - this.talkBoxHeight - 3 * talkBoxPadding, overflow: "auto" });
        this.chatMessagesDiv.css({ position: "absolute", left: x + "px", top: y + "px", width: width - userListWidth, height: height - this.talkBoxHeight - 3 * talkBoxPadding, overflow: "auto" });
        this.chatTalkDiv.css({ position: "absolute", left: x + talkBoxPadding + "px", top: y - 2 * talkBoxPadding + (height - this.talkBoxHeight) + "px", width: width - 3 * talkBoxPadding , height: this.talkBoxHeight });
    },

    appendMessage: function(message, msgClass) {
        if (msgClass == undefined)
            msgClass = "chatMessage";
        this.chatMessagesDiv.append("<div class='" + msgClass + "'>" + message + "</div>");
        if ($("div", this.chatMessagesDiv).length > 100) {
            $("div", this.chatMessagesDiv).first().remove();
        }
        this.chatMessagesDiv.prop({ scrollTop: this.chatMessagesDiv.prop("scrollHeight") });
    },

    monthNames: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],

    formatToTwoDigits: function(no) {
        if (no < 10)
            return "0" + no;
        else
            return no;
    },

    processMessages: function(xml, processAgain) {
        var root = xml.documentElement;
        if (root.tagName == 'chat') {
            var messages = root.getElementsByTagName("message");
            for (var i = 0; i < messages.length; i++) {
                var message = messages[i];
                var from = message.getAttribute("from");
                var text = message.childNodes[0].nodeValue;
                if (this.showTimestamps) {
                    var date = new Date(parseInt(message.getAttribute("date")));
                    var dateStr = this.monthNames[date.getMonth()] + " " + date.getDate() + " " + this.formatToTwoDigits(date.getHours()) + ":" + this.formatToTwoDigits(date.getMinutes()) + ":" + this.formatToTwoDigits(date.getSeconds());
                    this.appendMessage("<div class='timestamp'>[" + dateStr + "]</div> <b>" + from + ":</b> " + text);
                } else {
                    this.appendMessage("<b>" + from + ":</b> " + text);
                }
            }

            if (this.chatListDiv != null) {
                this.chatListDiv.html("");
                var users = root.getElementsByTagName("user");
                for (var i = 0; i < users.length; i++) {
                    var user = users[i];
                    var userName = user.childNodes[0].nodeValue;
                    this.chatListDiv.append("<div class='chatUser'>" + userName + "</div>");
                }
            }

            var that = this;

            if (processAgain)
                setTimeout(function() {
                    that.updateChatMessages();
                }, 1000);
        }
    },

    updateChatMessages: function(xml) {
        var that = this;

        this.communication.updateChat(this.name, function(xml) {
            that.processMessages(xml, true);
        });
    },

    sendMessage: function(message) {
        var that = this;

        this.communication.sendChatMessage(this.name, message, function(xml) {
            that.processMessages(xml, false);
        });
    }
});