var ChatBoxUI = Class.extend({
    name: null,
    div: null,
    communication: null,
    chatMessagesDiv: null,
    chatTalkDiv: null,

    init: function(name, div, communication) {
        this.name = name;
        this.div = div;
        this.communication = communication;

        this.chatMessagesDiv = $("<div class='chatMessages'></div>");
        this.chatTalkDiv = $("<input class='chatTalk'>");

        this.div.append(this.chatMessagesDiv);
        this.div.append(this.chatTalkDiv);

        var that = this;

        this.communication.startChat(this.name, function(xml) {
            that.processMessages(xml);
        })
    },

    setSize: function(width, height) {
        this.chatMessagesDiv.css({ position: "absolute", left: 0 + "px", top: 0 + "px", width: width, height: height - 30, overflow: "scroll" });
        this.chatTalkDiv.css({ position: "absolute", left: 0 + "px", top: (height - 30) + "px", width: width, height: 30 });
    },

    appendMessage: function(message) {
        this.chatMessagesDiv.append("<p>" + message + "</p>");
        if ($("p", this.chatMessagesDiv).length > 50) {
            $("p", this.chatMessagesDiv).first().remove();
        }
    },

    processMessages: function(xml) {
        var root = xml.documentElement;
        if (root.tagName == 'chat') {
            var messages = element.getElementsByTagName("message");
            for (var i = 0; i < messages.length; i++) {
                var message = messages[i];
                var from = message.getAttribute("from");
                var text = message.childNodes[0].nodeValue;
                this.appendMessage("<b>" + from + ":</b>" + text);
            }

            setTimeout(this.updateChatMessages, 1000);
        }
    },

    updateChatMessages: function(xml) {
        var that = this;

        this.communication.updateChat(this.name, function(xml) {
            that.processMessages(xml);
        });
    }
});