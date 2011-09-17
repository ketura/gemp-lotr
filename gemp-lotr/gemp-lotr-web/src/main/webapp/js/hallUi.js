var GempLotrHallUI = Class.extend({
    div : null,
    comm: null,
    chat: null,
    supportedFormatsInitialized: false,
    supportedFormatsSelect: null,
    createTableButton: null,
    leaveTableButton: null,

    tablesDiv: null,

    init: function(div, url, chat) {
        this.div = div;
        this.comm = new GempLotrCommunication(url, function() {
            chat.appendMessage("The game hall had a problem communicating with the server, no new updates will be displayed.", "warningMessage");
            chat.appendMessage("Reload the browser page (press F5) to resume the game hall functionality.", "warningMessage");
        });
        this.chat = chat;

        var width = $(div).width();
        var height = $(div).height();

        this.tablesDiv = $("<div></div>");
        this.tablesDiv.css({scroll: "auto", left: "0px", top: "0px", width: width + "px", height: (height - 40) + "px"});
        this.div.append(this.tablesDiv);

        var buttonsDiv = $("<div></div>");
        buttonsDiv.css({left: "0px", top: (height - 40) + "px", width: width + "px", height: 40 + "px", align: "right"});

        var that = this;

        var editDeck = $("<button>Edit deck</button>");
        editDeck.button().click(
                function() {
                    location.href = 'deckBuild.html';
                });

        buttonsDiv.append(editDeck);

        this.supportedFormatsSelect = $("<select></select>");
        this.supportedFormatsSelect.hide();

        this.createTableButton = $("<button>Create table</button>");
        $(this.createTableButton).button().click(
                function() {
                    that.supportedFormatsSelect.hide();
                    that.createTableButton.hide();
                    var format = that.supportedFormatsSelect.val();
                    that.comm.createTable(format, function(xml) {
                        that.processResponse(xml);
                    });
                });
        this.createTableButton.hide();

        buttonsDiv.append(this.supportedFormatsSelect);
        buttonsDiv.append(this.createTableButton);

        this.leaveTableButton = $("<button>Leave table</button>");
        $(this.leaveTableButton).button().click(
                function() {
                    that.leaveTableButton.hide();
                    that.comm.leaveTable();
                });
        this.leaveTableButton.hide();

        buttonsDiv.append(this.leaveTableButton);

        this.div.append(buttonsDiv);

        this.updateHall();
    },

    updateHall: function() {
        var that = this;

        this.comm.getHall(function(xml) {
            that.processHall(xml);
        });
    },

    processResponse: function(xml) {
        if (xml != null) {
            var root = xml.documentElement;
            if (root.tagName == "error") {
                var message = root.getAttribute("message");
                this.chat.appendMessage(message, "warningMessage");
            }
        }
    },

    processHall: function(xml) {
        var root = xml.documentElement;
        if (root.tagName == "hall") {
            this.tablesDiv.html("");

            var waiting = root.getAttribute("waiting") == "true";

            var tables = root.getElementsByTagName("table");
            for (var i = 0; i < tables.length; i++) {
                var table = tables[i];
                var id = table.getAttribute("id");
                var status = table.getAttribute("status");
                var playersAttr = table.getAttribute("players");
                var players = new Array();
                if (playersAttr.length > 0)
                    players = playersAttr.split(",");

                var tableDiv = this.createTableDiv(id, status, players, waiting);
                this.tablesDiv.append(tableDiv);
            }

            var games = root.getElementsByTagName("game");
            if (games.length > 0) {
                var waitingGameId = games[0].getAttribute("id");
                var participantId = getUrlParam("participantId");
                var participantIdAppend = "";
                if (participantId != null)
                    participantIdAppend = "&participantId=" + participantId;
                location.href = "/gemp-lotr/game.html?gameId=" + waitingGameId + participantIdAppend;
            }

            if (!this.supportedFormatsInitialized) {
                var formats = root.getElementsByTagName("format");
                for (var i = 0; i < formats.length; i++) {
                    var format = formats[i].childNodes[0].nodeValue;
                    this.supportedFormatsSelect.append("<option value='" + format + "'>" + format + "</option>");
                }
                this.supportedFormatsInitialized = true;
            }

            if (waiting) {
                this.supportedFormatsSelect.hide();
                this.createTableButton.hide();
                this.leaveTableButton.css("display", "");
            } else {
                this.supportedFormatsSelect.css("display", "");
                this.createTableButton.css("display", "");
                this.leaveTableButton.hide();
            }

            var that = this;

            setTimeout(function() {
                that.updateHall();
            }, 1000);
        }
    },

    createTableDiv: function(id, status, players, waiting) {
        var tableDiv = $("<div></div>");
        tableDiv.css({ display: "inline-block", width: "120px", height: "120px", margin: "5px", "background-color": "#333300", color: "#ffffff"});
        tableDiv.append("<div class='tableStatus'>" + status + "</div>");
        tableDiv.append("Players:<br/>");
        for (var i = 0; i < players.length; i++)
            tableDiv.append("<div class='tablePlayer'>" + players[i] + "</div>");

        if (players.length < 2) {
            var that = this;

            if (!waiting) {
                var but = $("<button>Join table</button>");
                $(but).button().click(
                        function(event) {
                            that.comm.joinTable(id, function(xml) {
                                that.processResponse(xml);
                            });
                        });
                tableDiv.append(but);
            }
        }

        return tableDiv;
    }
});