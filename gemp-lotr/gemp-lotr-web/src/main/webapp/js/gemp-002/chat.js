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
    retryCount: null,
    maxRetryCount: 5,
    playerListener: null,
    hiddenClasses: null,
    hideSystemButton: null,

    init: function(name, div, url, showList, playerListener, showHideSystemButton) {
        var that = this;
        this.hiddenClasses = new Array();
        this.playerListener = playerListener;
        this.retryCount = 0;
        this.name = name;
        this.div = div;
        this.unsentMessages = new Array();
        this.communication = new GempLotrCommunication(url, function(xhr, ajaxOptions, thrownError) {
            if (thrownError != "abort") {
                if (xhr != null) {
                    that.retryCount++;
                    if (that.retryCount <= that.maxRetryCount) {
                        that.appendMessage("Chat had a problem communicating with the server (error " + xhr.status + "). Retrying (" + that.retryCount + " of " + that.maxRetryCount + ")...", "systemMessage");
                        setTimeout(function() {
                            that.updateChatMessages();
                        }, that.chatUpdateInterval);
                    } else {
                        that.appendMessage("Chat has given up on connection retry (tried " + that.maxRetryCount + "), make sure your connection with the Internet is working.", "warningMessage");
                    }
                    return;
                }
                that.retryCount++;
                if (that.retryCount <= that.maxRetryCount) {
                    that.appendMessage("Chat had an unkown problem communicating with the server. Retrying (" + that.retryCount + " of " + that.maxRetryCount + ")...", "systemMessage");
                    setTimeout(function() {
                        that.updateChatMessages();
                    }, that.chatUpdateInterval);
                } else {
                    that.appendMessage("Chat has given up on connection retry (tried " + that.maxRetryCount + "), make sure your connection with the Internet is working.", "warningMessage");
                }
            }
        });

        this.chatMessagesDiv = $("<div class='chatMessages'></div>");
        this.div.append(this.chatMessagesDiv);

        if (this.name != null) {
            this.chatTalkDiv = $("<input type='text' class='chatTalk'>");

            if (showHideSystemButton) {
                this.hideSystemButton = $("<button id='showSystemMessages'>Toggle system messages</button>").button(
                {icons: {
                    primary: "ui-icon-zoomin"
                }, text: false});
                this.hideSystemButton.click(
                        function() {
                            if (that.isShowingMessageClass("systemMessage")) {
                                $('#showSystemMessages').button("option", "icons", {primary:'ui-icon-zoomin'})
                                that.hideMessageClass("systemMessage");
                            } else {
                                $('#showSystemMessages').button("option", "icons", {primary:'ui-icon-zoomout'})
                                that.showMessageClass("systemMessage");
                            }
                        });
                this.hideMessageClass("systemMessage");
            }

            if (showList) {
                this.chatListDiv = $("<div class='userList'></div>");
                this.div.append(this.chatListDiv);
            }
            this.div.append(this.hideSystemButton);
            this.div.append(this.chatTalkDiv);

            this.communication.startChat(this.name,
                    function(xml) {
                        that.processMessages(xml, true);
                    }, {
                "401": function() {
                    that.appendMessage("You are not logged in, go to the main page to log in.", "warningMessage");
                },
                "403": function() {
                    that.appendMessage("You have no permission to join this chat.", "warningMessage");
                },
                "404": function() {
                    that.appendMessage("Chat room was closed, please go to the Game Hall.", "warningMessage");
                }
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

    hideMessageClass: function(msgClass) {
        this.hiddenClasses.push(msgClass);
        $("div.message." + msgClass, this.chatMessagesDiv).hide();
    },

    isShowingMessageClass: function(msgClass) {
        var index = $.inArray(msgClass, this.hiddenClasses);
        return index == -1;
    },

    showMessageClass: function(msgClass) {
        var index = $.inArray(msgClass, this.hiddenClasses);
        if (index > -1) {
            this.hiddenClasses.splice(index, 1);
            $("div.message." + msgClass, this.chatMessagesDiv).show();
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
        if (this.chatTalkDiv != null) {
            if (this.hideSystemButton != null) {
                this.hideSystemButton.css({position: "absolute", left: x + width - talkBoxPadding - this.talkBoxHeight + "px", top: y - 2 * talkBoxPadding + (height - this.talkBoxHeight) + "px", width: this.talkBoxHeight, height: this.talkBoxHeight});
                this.chatTalkDiv.css({ position: "absolute", left: x + talkBoxPadding + "px", top: y - 2 * talkBoxPadding + (height - this.talkBoxHeight) + "px", width: width - 4 * talkBoxPadding - this.talkBoxHeight, height: this.talkBoxHeight });
            } else {
                this.chatTalkDiv.css({ position: "absolute", left: x + talkBoxPadding + "px", top: y - 2 * talkBoxPadding + (height - this.talkBoxHeight) + "px", width: width - 3 * talkBoxPadding , height: this.talkBoxHeight });
            }
        }
    },

    appendMessage: function(message, msgClass) {
        if (msgClass == undefined)
            msgClass = "chatMessage";
        var messageDiv = $("<div class='message " + msgClass + "'>" + message + "</div>");

        this.chatMessagesDiv.append(messageDiv);
        var index = $.inArray(msgClass, this.hiddenClasses);
        if (!this.isShowingMessageClass(msgClass)) {
            messageDiv.hide();
        }

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
            this.retryCount = 0;
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

            var users = root.getElementsByTagName("user");
            if (this.playerListener != null) {
                var players = new Array();
                for (var i = 0; i < users.length; i++) {
                    var user = users[i];
                    var userName = user.childNodes[0].nodeValue;
                    players.push(userName);
                }
                this.playerListener(players);
            }

            if (this.chatListDiv != null) {
                this.chatListDiv.html("");
                var players = new Array();
                for (var i = 0; i < users.length; i++) {
                    var user = users[i];
                    var userName = user.childNodes[0].nodeValue;
                    players.push(userName);
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
            }, {
                "401": function() {
                    that.appendMessage("You are not logged in, go to the main page to log in.", "warningMessage");
                },
                "403": function() {
                    that.appendMessage("You have no permission to send messages to this chat.", "warningMessage");
                },
                "404": function() {
                    that.appendMessage("Chat room was closed, or you were inactive for too long, please go to the Game Hall.", "warningMessage");
                }
            });
        } else if (this.unsentMessages.length > 0) {
            this.communication.sendChatMessage(this.name, this.unsentMessages, function(xml) {
                that.processMessages(xml, true);
            }, {
                "401": function() {
                    that.appendMessage("You are not logged in, go to the main page to log in.", "warningMessage");
                },
                "403": function() {
                    that.appendMessage("You have no permission to send messages to this chat.", "warningMessage");
                },
                "404": function() {
                    that.appendMessage("Chat room was closed, or you were inactive for too long, please go to the Game Hall.", "warningMessage");
                }
            });
            this.processingMessages = this.unsentMessages;
            this.unsentMessages = new Array();
        } else {
            this.communication.updateChat(this.name, function(xml) {
                that.processMessages(xml, true);
            }, {
                "401": function() {
                    that.appendMessage("You are not logged in, go to the main page to log in.", "warningMessage");
                },
                "403": function() {
                    that.appendMessage("You have no permission to get message from this chat.", "warningMessage");
                },
                "404": function() {
                    that.appendMessage("Chat room was closed, or you were inactive for too long, please go to the Game Hall.", "warningMessage");
                }
            });
        }
    },

    sendMessage: function(message) {
        var that = this;

        this.unsentMessages.push(message);
    }
});