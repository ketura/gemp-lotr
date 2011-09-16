var ChatBoxUI = Class.extend({
    name: null,
    div: null,
    communication: null,
    chatMessagesDiv: null,
    chatTalkDiv: null,
    talkBoxHeight: 25,

    init: function(name, div, url) {
        this.name = name;
        this.div = div;
        this.communication = new GempLotrCommunication(url, function() {
            alert("Chat had a problem communicating with the server, reload the page to continue");
        });

        this.chatMessagesDiv = $("<div class='chatMessages'></div>");
        this.chatTalkDiv = $("<input class='chatTalk'>");

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

        this.chatMessagesDiv.css({ position: "absolute", left: x + "px", top: y + "px", width: width, height: height - this.talkBoxHeight - 3 * talkBoxPadding, overflow: "auto" });
        this.chatTalkDiv.css({ position: "absolute", left: x + talkBoxPadding + "px", top: y - 2 * talkBoxPadding + (height - this.talkBoxHeight) + "px", width: width - 3 * talkBoxPadding , height: this.talkBoxHeight });
    },

    appendMessage: function(message, msgClass) {
        if (msgClass == undefined)
            msgClass = "chatMessage";
        this.chatMessagesDiv.append("<div class='" + msgClass + "'>" + message + "</div>");
        if ($("div", this.chatMessagesDiv).length > 50) {
            $("div", this.chatMessagesDiv).first().remove();
        }
        this.chatMessagesDiv.prop({ scrollTop: this.chatMessagesDiv.prop("scrollHeight") });
    },

    processMessages: function(xml, processAgain) {
        var root = xml.documentElement;
        if (root.tagName == 'chat') {
            var messages = root.getElementsByTagName("message");
            for (var i = 0; i < messages.length; i++) {
                var message = messages[i];
                var from = message.getAttribute("from");
                var text = message.childNodes[0].nodeValue;
                this.appendMessage("<b>" + from + ":</b> " + text);
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