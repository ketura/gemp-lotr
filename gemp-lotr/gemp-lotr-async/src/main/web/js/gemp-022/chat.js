var ChatBoxUI = Class.extend({
    name:null,
    userName:null,
    div:null,
    comm:null,

    chatMessagesDiv:null,
    chatTalkDiv:null,
    chatListDiv:null,

    showTimestamps:false,
    maxMessageCount:500,
    talkBoxHeight:25,

    chatUpdateInterval:100,

    playerListener:null,
    hiddenClasses:null,

    hideSystemButton:null,
    lockButton:null,

    lockChat:false,
    stopUpdates: false,
    
    dialogListener: null,
    
    enableDiscord: false,
    discordDiv:null,
    discordWidget:null,
    chatEmbed:null,
    displayDiscord:true,
    
    toggleChatButton:null,

    init:function (name, div, url, showList, playerListener, showHideSystemButton, showLockButton, displayChatListener, allowDiscord=false) {
        var that = this;
             
        this.hiddenClasses = new Array();
        this.playerListener = playerListener;
        this.dialogListener = displayChatListener;
        this.name = name;
        this.div = div;
        this.comm = new GempLotrCommunication(url, function (xhr, ajaxOptions, thrownError) {
            that.appendMessage("Unknown chat problem occured (error=" + xhr.status + ")", "warningMessage");
        });
        this.enableDiscord = allowDiscord;

        this.chatMessagesDiv = $("#chatMessages");

        if (this.name != null) {
            
            this.discordDiv = $("#discordChat");
            this.comm.getPlayerInfo(function(json)
            { 
                that.userName = json; 
            }, this.chatErrorMap());

            this.chatTalkDiv = $("#chatTalk");

            this.hideSystemButton = $("#showSystemButton");
            if (showHideSystemButton) {
                hideSystemButton.button({icons:{
                     primary:"ui-icon-zoomin"
                 }, text:false});

                this.hideSystemButton.click(
                        function () {
                            if (that.isShowingMessageClass("systemMessage")) {
                                $('#showSystemMessages').button("option", "icons", {primary:'ui-icon-zoomin'});
                                that.hideMessageClass("systemMessage");
                            } else {
                                $('#showSystemMessages').button("option", "icons", {primary:'ui-icon-zoomout'});
                                that.showMessageClass("systemMessage");
                            }
                        });
                this.hideMessageClass("systemMessage");
            }
            else
            {
                this.hideSystemButton.hide();
                this.hideSystemButton = null;
            }

            this.lockButton = $("#lockChatButton");
            if (showLockButton) {
                this.lockButton.button({icons:{
                     primary:"ui-icon-locked"
                 }, text:false});
                
                this.lockButton.click(
                    function () {
                        if (that.lockChat) {
                            $('#lockChatButton').button("option", "icons", {primary:'ui-icon-locked'});
                            that.lockChat = false;
                        } else {
                            $('#lockChatButton').button("option", "icons", {primary:'ui-icon-unlocked'});
                            that.lockChat = true;
                    }
                });
            }
            else
            {
                this.lockButton.hide();
                this.lockButton = null;
            }

            this.comm.startChat(this.name,
                    function (xml) {
                        that.processMessages(xml, true);
                    }, this.chatErrorMap());

            this.chatTalkDiv.bind("keypress", function (e) {
                var code = (e.keyCode ? e.keyCode : e.which);
                if (code == 13) {
                    var value = $(this).val();
                    if (value != "")
                        that.sendMessage(value);
                    $(this).val("");
                }
            });

            
            if (showList) {
                this.chatListDiv = $("#userList");
                this.toggleChatButton = $("#toggleChatButt");

                this.toggleChatButton.button();
                this.toggleChatButton.click( function() {
                    that.toggleChat();
                });
            }
            
            this.setDiscordVisible(false);
            
        } else {
            this.talkBoxHeight = 0;
        }
    },


    hideMessageClass:function (msgClass) {
        this.hiddenClasses.push(msgClass);
        $("div.message." + msgClass, this.chatMessagesDiv).hide();
    },

    isShowingMessageClass:function (msgClass) {
        var index = $.inArray(msgClass, this.hiddenClasses);
        return index == -1;
    },

    showMessageClass:function (msgClass) {
        var index = $.inArray(msgClass, this.hiddenClasses);
        if (index > -1) {
            this.hiddenClasses.splice(index, 1);
            $("div.message." + msgClass, this.chatMessagesDiv).show();
        }
    },

    escapeHtml:function (text) {
        return $('<div/>').text(text).html();
    },

    setBounds:function (x, y, width, height) {

        this.handleChatVisibility();       
    },
    
    handleChatVisibility:function() {
        
        if(this.enableDiscord)
        {
            if(this.displayDiscord)
            {
                this.toggleChatButton.text("Switch to Legacy");
                
                if(this.chatEmbed == null)
                {
                    this.discordDiv.show();
                    this.chatEmbed = $("<widgetbot server='699957633121255515' channel='873065954609881140' shard='https://e.widgetbot.co' width='100%' height='100%' username='" + this.userName + "'></widgetbot>");
                    var script = $("<script src='https://cdn.jsdelivr.net/npm/@widgetbot/html-embed'></script>");
                    this.discordDiv.append(script);
                    this.discordDiv.append(this.chatEmbed);
                }
            }
            else
            {
                this.toggleChatButton.text("Switch to Discord");
            } 
        }
        
        if(this.enableDiscord && this.displayDiscord)
        {

            this.discordDiv.show();
            
            if(this.chatMessagesDiv != null)
                this.chatMessagesDiv.hide();
            if(this.chatTalkDiv != null)
                this.chatTalkDiv.hide();
            if(this.hideSystemButton != null)
                this.hideSystemButton.hide();
            if(this.lockButton != null)
                this.lockButton.hide();
        }
        else
        {

            this.discordDiv.hide();
            
            if(this.chatMessagesDiv != null)
                this.chatMessagesDiv.show();
            if(this.chatTalkDiv != null)
                this.chatTalkDiv.show();
            if(this.hideSystemButton != null)
                this.hideSystemButton.show();
            if(this.lockButton != null)
                this.lockButton.show(); 
        }
        
    },
    
    toggleChat:function() {
        this.setDiscordVisible(!this.displayDiscord);
    },
    
    setDiscordVisible:function(visible)
    {
      this.displayDiscord = visible;
      
      this.handleChatVisibility();
        
    },
    
    checkForEnd:function (message, msgClass) {
        // if(msgClass != "systemMessage")
        // {
        //     return;
        // }
        
        if(message.includes("Thank you for playtesting!")) {
            if (this.dialogListener != null) {
                this.dialogListener("Give us feedback!", message);
            }
        }
    },

    appendMessage:function (message, msgClass) {
        if (msgClass == undefined)
            msgClass = "chatMessage";
        var messageDiv = $("<div class='message " + msgClass + "'>" + message + "</div>");

        this.chatMessagesDiv.append(messageDiv);
        var index = $.inArray(msgClass, this.hiddenClasses);
        if (!this.isShowingMessageClass(msgClass)) {
            messageDiv.hide();
        }

        if ($("div.message", this.chatMessagesDiv).length > this.maxMessageCount) {
            $("div.message", this.chatMessagesDiv).first().remove();
        }
        if (!this.lockChat)
            this.chatMessagesDiv.prop({ scrollTop:this.chatMessagesDiv.prop("scrollHeight") });
        
        this.checkForEnd(message, msgClass);
    },

    monthNames:["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],

    formatToTwoDigits:function (no) {
        if (no < 10)
            return "0" + no;
        else
            return no;
    },

    processMessages:function (xml, processAgain) {
        var root = xml.documentElement;
        if (root.tagName == 'chat') {
            this.retryCount = 0;
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
                for (var i = 0; i < users.length; i++) {
                    var user = users[i];
                    var userName = user.childNodes[0].nodeValue;
                    this.chatListDiv.append("<div class='chatUser'>" + userName + "</div>");
                }
            }

            var that = this;

            if (processAgain)
                setTimeout(function () {
                    that.updateChatMessages();
                }, that.chatUpdateInterval);
        }
    },

    updateChatMessages:function () {
        var that = this;

        this.comm.updateChat(this.name, function (xml) {
            that.processMessages(xml, true);
        }, this.chatErrorMap());
    },

    sendMessage:function (message) {
        var that = this;
        this.comm.sendChatMessage(this.name, message, this.chatErrorMap());
        
        //this.chatEmbed.emit("sendMessage", message);
    },

    chatMalfunction: function() {
        this.stopUpdates = true;
        this.chatTalkDiv.prop('disabled', true);
        this.chatTalkDiv.css({"background-color": "#ff9999"});
        
        this.discordDiv.prop('disabled', true);
        this.discordDiv.css({"background-color": "#ff9999"});
    },

    chatErrorMap:function() {
        var that = this;
        return {
            "0":function() {
                that.chatMalfunction();
                that.appendMessage("Chat server has been closed or there was a problem with your internet connection.", "warningMessage");
            },
            "401":function() {
                that.chatMalfunction();
                that.appendMessage("You are not logged in.", "warningMessage");
            },
            "403": function() {
                that.chatMalfunction();
                that.appendMessage("You have no permission to participate in this chat.", "warningMessage");
            },
            "404": function() {
                that.chatMalfunction();
                that.appendMessage("Chat room is closed.", "warningMessage");
            },
            "410": function() {
                that.chatMalfunction();
                that.appendMessage("You have been inactive for too long and were removed from the chat room. Refresh the page if you wish to re-enter.", "warningMessage");
            }
        };
    },
    
    
});
