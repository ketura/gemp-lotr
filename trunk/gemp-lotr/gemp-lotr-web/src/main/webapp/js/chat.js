var ChatBoxUI = Class.extend({
    name: null,
    div: null,
    communication: null,
    chatMessagesDiv: null,
    chatTalkDiv: null,
    chatListDiv: null,
    showTimestamps: false,
    talkBoxHeight: 25,
    chatUpdateInterval: 1000,
    unsentMessages: null,
    processingMessages: null,

    init: function(name, div, url, showList) {
        var that = this;
        this.name = name;
        this.div = div;
        this.unsentMessages = new Array();
        this.communication = new GempLotrCommunication(url, function(xhr, ajaxOptions, thrownError) {
            if (thrownError != "abort") {
                if (xhr != null) {
                    if (xhr.status == 401) {
                        that.appendMessage("You're not logged in, go to the <a href='index.html'>main page</a> to log in", "warningMessage");
                        return;
                    } else if (xhr.status == 404) {
                        that.appendMessage("Chat room was closed, please go to the Game Hall.", "warningMessage");
                        return;
                    } else if (xhr.status == 503) {
                        that.appendMessage("Server is being restarted, please wait for the restart to finish and try again later.", "warningMessage");
                        return;
                    } else {
                        that.appendMessage("Chat had a problem communicating with the server (error " + xhr.statis + "). Retrying...", "systemMessage");
                        setTimeout(function() {
                            that.updateChatMessages();
                        }, that.chatUpdateInterval);
                        return;
                    }
                }
                that.appendMessage("Chat had an unknown problem communicating with the server. Retrying...", "systemMessage");
                setTimeout(function() {
                    that.updateChatMessages();
                }, that.chatUpdateInterval);
            }
        });

        this.chatMessagesDiv = $("<div class='chatMessages'></div>");
        this.div.append(this.chatMessagesDiv);

        var that = this;

        if (this.name != null) {
            this.chatTalkDiv = $("<input class='chatTalk'>");
            if (showList) {
                this.chatListDiv = $("<div class='userList'></div>");
                this.div.append(this.chatListDiv);
            }
            this.div.append(this.chatTalkDiv);

            this.communication.startChat(this.name, function(xml) {
                that.processMessages(xml, true);
            });

            this.chatTalkDiv.bind("keypress", function(e) {
                var code = (e.keyCode ? e.keyCode : e.which);
                if (code == 13) {
                    var value = $(this).val();
                    if (value != "") {
                        that.sendMessage(value);
                        that.appendMessage("<b>Me:</b> " + that.escapeHtml(value));
                    }
                    $(this).val("");
                }
            });
        } else {
            this.talkBoxHeight = 0;
        }
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
        if (this.chatTalkDiv != null)
            this.chatTalkDiv.css({ position: "absolute", left: x + talkBoxPadding + "px", top: y - 2 * talkBoxPadding + (height - this.talkBoxHeight) + "px", width: width - 3 * talkBoxPadding , height: this.talkBoxHeight });
    },

    appendMessage: function(message, msgClass) {
        if (msgClass == undefined)
            msgClass = "chatMessage";
        this.chatMessagesDiv.append("<div class='message " + msgClass + "'>" + message + "</div>");
        if ($("div.message", this.chatMessagesDiv).length > 150) {
            $("div.message", this.chatMessagesDiv).first().remove();
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
            this.processingMessages = null;
            var messages = root.getElementsByTagName("message");
            for (var i = 0; i < messages.length; i++) {
                var message = messages[i];
                var from = message.getAttribute("from");
                var text = message.childNodes[0].nodeValue;

                var msgClass = "chatMessage";
                if (from == "System")
                    msgClass = "systemMessage";
                if (this.showTimestamps) {
                    var date = new Date(parseInt(message.getAttribute("date")));
                    var dateStr = this.monthNames[date.getMonth()] + " " + date.getDate() + " " + this.formatToTwoDigits(date.getHours()) + ":" + this.formatToTwoDigits(date.getMinutes()) + ":" + this.formatToTwoDigits(date.getSeconds());
                    this.appendMessage("<div class='timestamp'>[" + dateStr + "]</div> <b>" + from + ":</b> " + text, msgClass);
                } else {
                    this.appendMessage("<b>" + from + ":</b> " + text, msgClass);
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
                }, that.chatUpdateInterval);
        }
    },

    updateChatMessages: function(xml) {
        var that = this;

        if (this.processingMessages != null) {
            this.communication.sendChatMessage(this.name, this.processingMessages, function(xml) {
                that.processMessages(xml, true);
            });
        } else if (this.unsentMessages.length > 0) {
            this.communication.sendChatMessage(this.name, this.unsentMessages, function(xml) {
                that.processMessages(xml, true);
            });
            this.processingMessages = this.unsentMessages;
            this.unsentMessages = new Array();
        } else {
            this.communication.updateChat(this.name, function(xml) {
                that.processMessages(xml, true);
            });
        }
    },

    sendMessage: function(message) {
        var that = this;

        this.unsentMessages.push(message);
    }
});