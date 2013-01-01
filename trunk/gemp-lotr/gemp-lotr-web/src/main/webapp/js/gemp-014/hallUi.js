var GempLotrHallUI = Class.extend({
    div:null,
    comm:null,
    chat:null,
    supportedFormatsInitialized:false,
    supportedFormatsSelect:null,
    decksSelect:null,
    createTableButton:null,
    leaveTableButton:null,

    tablesDiv:null,
    buttonsDiv:null,

    pocketDiv:null,
    hallChannelId: null,

    init:function (div, url, chat) {
        this.div = div;
        this.comm = new GempLotrCommunication(url, function (xhr, ajaxOptions, thrownError) {
            if (thrownError != "abort") {
                if (xhr != null) {
                    if (xhr.status == 401) {
                        chat.appendMessage("Game hall problem - You're not logged in, go to the <a href='index.html'>main page</a> to log in", "warningMessage");
                        return;
                    } else {
                        chat.appendMessage("The game hall had a problem communicating with the server (" + xhr.status + "), no new updates will be displayed.", "warningMessage");
                        chat.appendMessage("Reload the browser page (press F5) to resume the game hall functionality.", "warningMessage");
                        return;
                    }
                }
                chat.appendMessage("The game hall had a problem communicating with the server, no new updates will be displayed.", "warningMessage");
                chat.appendMessage("Reload the browser page (press F5) to resume the game hall functionality.", "warningMessage");
            }
        });
        this.chat = chat;

        var width = $(div).width();
        var height = $(div).height();

        this.tablesDiv = $("<div></div>");
        this.tablesDiv.css({overflow:"auto", left:"0px", top:"0px", width:width + "px", height:(height - 30) + "px"});

        this.addQueuesTable();
        this.addWaitingTablesTable();
        this.addPlayingTablesTable();
        this.addFinishedTablesTable();

        this.div.append(this.tablesDiv);

        this.buttonsDiv = $("<div></div>");
        this.buttonsDiv.css({left:"0px", top:(height - 30) + "px", width:width + "px", height:29 + "px", align:"right", backgroundColor:"#000000", "border-top-width":"1px", "border-top-color":"#ffffff", "border-top-style":"solid"});

        var that = this;

        var editDeck = $("<button>Deck builder</button>");
        editDeck.button().click(
                function () {
                    location.href = 'deckBuild.html';
                });

        var merchant = $("<button>Merchant</button>");
        merchant.button().click(
                function () {
                    location.href = 'merchant.html';
                });

        this.buttonsDiv.append(editDeck);
        this.buttonsDiv.append(" | ");

        this.buttonsDiv.append(merchant);
        this.buttonsDiv.append(" | ");

        this.pocketDiv = $("<div class='pocket'></div>");
        this.pocketDiv.css({"float":"right", width:95, height:18});
        this.buttonsDiv.append(this.pocketDiv);

        this.supportedFormatsSelect = $("<select style='width: 220px'></select>");
        this.supportedFormatsSelect.hide();

        this.createTableButton = $("<button>Create table</button>");
        $(this.createTableButton).button().click(
                function () {
                    that.supportedFormatsSelect.hide();
                    that.decksSelect.hide();
                    that.createTableButton.hide();
                    var format = that.supportedFormatsSelect.val();
                    var deck = that.decksSelect.val();
                    if (deck != null)
                        that.comm.createTable(format, deck, function (xml) {
                            that.processResponse(xml);
                        });
                });
        this.createTableButton.hide();

        this.decksSelect = $("<select style='width: 220px'></select>");
        this.decksSelect.hide();

        this.buttonsDiv.append(this.supportedFormatsSelect);
        this.buttonsDiv.append(this.decksSelect);
        this.buttonsDiv.append(this.createTableButton);

        this.leaveTableButton = $("<button>Leave table</button>");
        $(this.leaveTableButton).button().click(
                function () {
                    that.leaveTableButton.hide();
                    that.comm.leaveTable();
                });
        this.leaveTableButton.hide();

        this.buttonsDiv.append(this.leaveTableButton);

        this.div.append(this.buttonsDiv);

        this.getHall();
        this.updateDecks();
    },

    addQueuesTable: function() {
        var header = $("<div class='eventHeader'></div>");

        var content = $("<div></div>");

        var toggleContent = $("<div>Toggle tournament queues</div>").button({
            icons: {
                primary: "ui-icon-circlesmall-minus"
            },
            text: false
        });
        toggleContent.css({width: "13px", height: "15px"});
        toggleContent.click(
                function() {
                    if (toggleContent.button("option", "icons")["primary"] == "ui-icon-circlesmall-minus")
                        toggleContent.button("option", "icons", {primary: "ui-icon-circlesmall-plus"});
                    else
                        toggleContent.button("option", "icons", {primary: "ui-icon-circlesmall-minus"});
                    content.toggle("blind", {}, 200);
                });
        header.append(toggleContent);
        header.append(" Tournament queues");

        var table = $("<table class='tables queues'></table>");
        table.append("<tr><th width='20%'>Format</th><th width='10%'>Collection</th><th width='20%'>Queue name</th><th width='10%'>Players</th><th width='10%'>Cost</th><th width='20%'>Prizes</th><th width='10%'>Actions</th></tr>");
        content.append(table);

        this.tablesDiv.append(header);
        this.tablesDiv.append(content);
    },

    addWaitingTablesTable: function() {
        var header = $("<div class='eventHeader'></div>");

        var content = $("<div></div>");

        var toggleContent = $("<div>Toggle waiting tables</div>").button({
            icons: {
                primary: "ui-icon-circlesmall-minus"
            },
            text: false
        });
        toggleContent.css({width: "13px", height: "15px"});
        toggleContent.click(
                function() {
                    if (toggleContent.button("option", "icons")["primary"] == "ui-icon-circlesmall-minus")
                        toggleContent.button("option", "icons", {primary: "ui-icon-circlesmall-plus"});
                    else
                        toggleContent.button("option", "icons", {primary: "ui-icon-circlesmall-minus"});
                    content.toggle("blind", {}, 200);
                });
        header.append(toggleContent);
        header.append(" Waiting tables");

        var table = $("<table class='tables waitingTables'></table>");
        table.append("<tr><th width='20%'>Format</th><th width='40%'>Tournament</th><th width='10%'>Status</th><th width='20%'>Players</th><th width='10%'>Actions</th></tr>");
        content.append(table);

        this.tablesDiv.append(header);
        this.tablesDiv.append(content);
    },

    addPlayingTablesTable: function() {
        var header = $("<div class='eventHeader'></div>");

        var content = $("<div></div>");

        var toggleContent = $("<div>Toggle playing tables</div>").button({
            icons: {
                primary: "ui-icon-circlesmall-minus"
            },
            text: false
        });
        toggleContent.css({width: "13px", height: "15px"});
        toggleContent.click(
                function() {
                    if (toggleContent.button("option", "icons")["primary"] == "ui-icon-circlesmall-minus")
                        toggleContent.button("option", "icons", {primary: "ui-icon-circlesmall-plus"});
                    else
                        toggleContent.button("option", "icons", {primary: "ui-icon-circlesmall-minus"});
                    content.toggle("blind", {}, 200);
                });
        header.append(toggleContent);
        header.append(" Playing tables");

        var table = $("<table class='tables playingTables'></table>");
        table.append("<tr><th width='20%'>Format</th><th width='40%'>Tournament</th><th width='10%'>Status</th><th width='20%'>Players</th><th width='10%'>Actions</th></tr>");
        content.append(table);

        this.tablesDiv.append(header);
        this.tablesDiv.append(content);
    },

    addFinishedTablesTable: function() {
        var header = $("<div class='eventHeader'></div>");

        var content = $("<div></div>");

        var toggleContent = $("<div>Toggle finished tables</div>").button({
            icons: {
                primary: "ui-icon-circlesmall-minus"
            },
            text: false
        });
        toggleContent.css({width: "13px", height: "15px"});
        toggleContent.click(
                function() {
                    if (toggleContent.button("option", "icons")["primary"] == "ui-icon-circlesmall-minus")
                        toggleContent.button("option", "icons", {primary: "ui-icon-circlesmall-plus"});
                    else
                        toggleContent.button("option", "icons", {primary: "ui-icon-circlesmall-minus"});
                    content.toggle("blind", {}, 200);
                });
        header.append(toggleContent);
        header.append(" Finished tables");

        var table = $("<table class='tables finishedTables'></table>");
        table.append("<tr><th width='20%'>Format</th><th width='40%'>Tournament</th><th width='10%'>Status</th><th width='20%'>Players</th><th width='10%'>Winner</th></tr>");
        content.append(table);

        this.tablesDiv.append(header);
        this.tablesDiv.append(content);
    },

    hallResized:function (width, height) {
        this.tablesDiv.css({overflow:"auto", left:"0px", top:"0px", width:width + "px", height:(height - 30) + "px"});
        this.buttonsDiv.css({left:"0px", top:(height - 30) + "px", width:width + "px", height:29 + "px", align:"right", backgroundColor:"#000000", "border-top-width":"1px", "border-top-color":"#ffffff", "border-top-style":"solid"});
    },

    getHall: function() {
        var that = this;

        this.comm.getHall(function(xml) {
            that.processHall(xml);
        });
    },

    updateHall:function () {
        var that = this;

        this.comm.updateHall(
                function (xml) {
                    that.processHall(xml);
                }, this.hallChannelId,
        {
            "409":function() {
                that.chat.appendMessage("You have accessed Game Hall in another browser, press F5 (refresh) to regain access in this window");
            },
            "410":function() {
                that.chat.appendMessage("You have been inactive for too long, press F5 (refresh) to enter Game Hall again");
            }
        });
    },

    updateDecks:function () {
        var that = this;
        this.comm.getDecks(function (xml) {
            that.processDecks(xml);
        });
    },

    processResponse:function (xml) {
        if (xml != null) {
            var root = xml.documentElement;
            if (root.tagName == "error") {
                var message = root.getAttribute("message");
                this.chat.appendMessage(message, "warningMessage");
            }
        }
    },

    processDecks:function (xml) {
        var root = xml.documentElement;
        if (root.tagName == "decks") {
            this.decksSelect.html("");
            var decks = root.getElementsByTagName("deck");
            for (var i = 0; i < decks.length; i++) {
                var deck = decks[i];
                var deckName = decks[i].childNodes[0].nodeValue;
                var deckElem = $("<option></option>");
                deckElem.attr("value", deckName);
                deckElem.html(deckName);
                this.decksSelect.append(deckElem);
            }
            this.decksSelect.css("display", "");
        }
    },

    processHall:function (xml) {
        var that = this;

        var root = xml.documentElement;
        if (root.tagName == "hall") {
            this.hallChannelId = root.getAttribute("channelNumber");

            var currency = root.getAttribute("currency");
            this.pocketDiv.html(formatPrice(currency));

            var motd = root.getAttribute("motd");
            if (motd != null)
                $("#motd").html("<b>MOTD:</b> " + motd);

            var busy = root.getAttribute("busy") == "true";

            var queues = root.getElementsByTagName("queue");
            for (var i = 0; i < queues.length; i++) {
                var queue = queues[i];
                var id = queue.getAttribute("id");
                var action = queue.getAttribute("action");
                if (action == "add" || action == "update") {
                    var actionsField = $("<td></td>");

                    var joined = queue.getAttribute("signedUp");
                    if (joined != "true") {
                        var but = $("<button>Join queue</button>");
                        $(but).button().click(
                                function (event) {
                                    var deck = that.decksSelect.val();
                                    if (deck != null)
                                        that.comm.joinQueue(id, deck, function (xml) {
                                            that.processResponse(xml);
                                        });
                                });
                        actionsField.append(but);
                    } else {
                        var but = $("<button>Leave queue</button>");
                        $(but).button().click(
                                function (event) {
                                    var deck = that.decksSelect.val();
                                    if (deck != null)
                                        that.comm.leaveQueue(id, deck, function (xml) {
                                            that.processResponse(xml);
                                        });
                                });
                        actionsField.append(but);
                    }

                    var row = $("<tr class='queue" + id + "'><td>" + queue.getAttribute("format") + "</td>" +
                                "<td>" + queue.getAttribute("collection") + "</td>" +
                                "<td>" + queue.getAttribute("queue") + "</td>" +
                                "<td>" + queue.getAttribute("playerCount") + "</td>" +
                                "<td>" + formatPrice(queue.getAttribute("cost")) + "</td>" +
                                "<td>" + queue.getAttribute("prizes") + "</td>" +
                                "</tr>");

                    row.append(actionsField);

                    if (action == "add") {
                        $("table.queues", this.tablesDiv)
                                .append(row);
                    } else if (action == "update") {
                        $(".queue" + id, this.tablesDiv).replaceWith(row);
                    }
                } else if (action == "remove") {
                    $(".queue" + id, this.tablesDiv).remove();
                }
            }

            var tables = root.getElementsByTagName("table");
            for (var i = 0; i < tables.length; i++) {
                var table = tables[i];
                var id = table.getAttribute("id");
                var action = table.getAttribute("action");
                if (action == "add" || action == "update") {
                    var status = table.getAttribute("status");

                    var gameId = table.getAttribute("gameId");
                    var statusDescription = table.getAttribute("statusDescription");
                    var watchable = table.getAttribute("watchable");
                    var playersAttr = table.getAttribute("players");
                    var formatName = table.getAttribute("format");
                    var tournamentName = table.getAttribute("tournament");
                    var players = new Array();
                    if (playersAttr.length > 0)
                        players = playersAttr.split(",");
                    var winner = table.getAttribute("winner");

                    var row = $("<tr class='table" + id + "'></tr>");

                    row.append("<td>" + formatName + "</td>");
                    row.append("<td>" + tournamentName + "</td>");
                    row.append("<td>" + statusDescription + "</td>");

                    var playersStr = "";
                    for (var i = 0; i < players.length; i++) {
                        if (i > 0)
                            playersStr += ", ";
                        playersStr += players[i];
                    }
                    row.append("<td>" + playersStr + "</td>");

                    var lastField = $("<td></td>");
                    if (status == "WAITING" && !busy) {
                        var that = this;

                        var but = $("<button>Join table</button>");
                        $(but).button().click(
                                function (event) {
                                    var deck = that.decksSelect.val();
                                    if (deck != null)
                                        that.comm.joinTable(id, deck, function (xml) {
                                            that.processResponse(xml);
                                        });
                                });
                        lastField.append(but);
                    }

                    if (status == "PLAYING" && watchable == "true") {
                        var but = $("<button>Watch game</button>");
                        $(but).button().click(
                                function (event) {
                                    var participantId = getUrlParam("participantId");
                                    var participantIdAppend = "";
                                    if (participantId != null)
                                        participantIdAppend = "&participantId=" + participantId;
                                    location.href = "/gemp-lotr/game.html?gameId=" + gameId + participantIdAppend;
                                });
                        lastField.append(but);
                    }

                    if (status == "FINISHED" && winner != null) {
                        lastField.append(winner);
                    }

                    row.append(lastField);

                    if (action == "add") {
                        if (status == "WAITING") {
                            $("table.waitingTables", this.tablesDiv)
                                    .append(row);
                        } else if (status == "PLAYING") {
                            $("table.playingTables", this.tablesDiv)
                                    .append(row);
                        } else if (status == "FINISHED") {
                            $("table.finishedTables", this.tablesDiv)
                                    .append(row);
                        }
                    } else if (action == "update") {
                        if (status == "WAITING") {
                            if ($(".table"+id, $("table.waitingTables")).length > 0) {
                                $(".table" + id, this.tablesDiv).replaceWith(row);
                            } else {
                                $(".table" + id, this.tablesDiv).remove();
                                $("table.waitingTables", this.tablesDiv)
                                        .append(row);
                            }
                        } else if (status == "PLAYING") {
                            if ($(".table"+id, $("table.playingTables")).length > 0) {
                                $(".table" + id, this.tablesDiv).replaceWith(row);
                            } else {
                                $(".table" + id, this.tablesDiv).remove();
                                $("table.playingTables", this.tablesDiv)
                                        .append(row);
                            }
                        } else if (status == "FINISHED") {
                            if ($(".table"+id, $("table.finishedTables")).length > 0) {
                                $(".table" + id, this.tablesDiv).replaceWith(row);
                            } else {
                                $(".table" + id, this.tablesDiv).remove();
                                $("table.finishedTables", this.tablesDiv)
                                        .append(row);
                            }
                        }
                    }
                } else if (action == "remove") {
                    $(".table" + id, this.tablesDiv).remove();
                }
            }

            var skipReload = false;
            var games = root.getElementsByTagName("game");
            if (games.length > 0) {
                var waitingGameId = games[0].getAttribute("id");
                var participantId = getUrlParam("participantId");
                var participantIdAppend = "";
                if (participantId != null)
                    participantIdAppend = "&participantId=" + participantId;
                this.tablesDiv.append("<embed src='/gemp-lotr/fanfare_x.wav' hidden=true autostart=true loop=false>");
                skipReload = true;
                setTimeout("location.href = '/gemp-lotr/game.html?gameId=" + waitingGameId + participantIdAppend + "'", 3000);
            }

            if (!this.supportedFormatsInitialized) {
                var formats = root.getElementsByTagName("format");
                for (var i = 0; i < formats.length; i++) {
                    var format = formats[i].childNodes[0].nodeValue;
                    var type = formats[i].getAttribute("type");
                    this.supportedFormatsSelect.append("<option value='" + type + "'>" + format + "</option>");
                }
                this.supportedFormatsInitialized = true;
            }

            if (busy) {
                this.supportedFormatsSelect.hide();
                this.decksSelect.hide();
                this.createTableButton.hide();
                if (this.leaveTableButton.css("display") == "none")
                    this.leaveTableButton.css("display", "");
            } else {
                if (this.supportedFormatsSelect.css("display") == "none")
                    this.supportedFormatsSelect.css("display", "");
                if (this.decksSelect.css("display") == "none")
                    this.decksSelect.css("display", "");
                if (this.createTableButton.css("display") == "none")
                    this.createTableButton.css("display", "");
                this.leaveTableButton.hide();
            }

            var that = this;

            if (!skipReload) {
                setTimeout(function () {
                    that.updateHall();
                }, 1000);
            }
        }
    }
    ,

    appendTournamentQueue:function (container, id, formatName, tournamentName, players, joined) {
        var row = $("<tr></tr>");

        row.append("<td>" + formatName + "</td>");
        row.append("<td>" + tournamentName + "</td>");
        row.append("<td>" + "Accepting players" + "</td>");
        row.append("<td>" + "Players: " + players + "</td>");

        var actionsField = $("<td></td>");
        var that = this;

        if (joined != "true") {
            var but = $("<button>Join queue</button>");
            $(but).button().click(
                    function (event) {
                        var deck = that.decksSelect.val();
                        if (deck != null)
                            that.comm.joinQueue(id, deck, function (xml) {
                                that.processResponse(xml);
                            });
                    });
            actionsField.append(but);
        } else {
            var but = $("<button>Leave queue</button>");
            $(but).button().click(
                    function (event) {
                        var deck = that.decksSelect.val();
                        if (deck != null)
                            that.comm.leaveQueue(id, deck, function (xml) {
                                that.processResponse(xml);
                            });
                    });
            actionsField.append(but);
        }

        row.append(actionsField);

        container.append(row);

    }
    ,

    appendTable:function (container, id, gameId, watchable, status, formatName, tournamentName, players, waiting, winner) {
        var row = $("<tr></tr>");

        row.append("<td>" + formatName + "</td>");
        row.append("<td>" + tournamentName + "</td>");
        row.append("<td>" + status + "</td>");

        var playersStr = "";
        for (var i = 0; i < players.length; i++) {
            if (i > 0)
                playersStr += ", ";
            if (winner == players[i])
                playersStr += "<b>" + players[i] + "</b>";
            else
                playersStr += players[i];
        }
        row.append("<td>" + playersStr + "</td>");

        var actionsField = $("<td></td>");
        if (players.length < 2) {
            var that = this;

            if (!waiting) {
                var but = $("<button>Join table</button>");
                $(but).button().click(
                        function (event) {
                            var deck = that.decksSelect.val();
                            if (deck != null)
                                that.comm.joinTable(id, deck, function (xml) {
                                    that.processResponse(xml);
                                });
                        });
                actionsField.append(but);
            }
        }

        if (watchable == "true") {
            var but = $("<button>Watch game</button>");
            $(but).button().click(
                    function (event) {
                        var participantId = getUrlParam("participantId");
                        var participantIdAppend = "";
                        if (participantId != null)
                            participantIdAppend = "&participantId=" + participantId;
                        location.href = "/gemp-lotr/game.html?gameId=" + gameId + participantIdAppend;
                    });
            actionsField.append(but);
        }

        row.append(actionsField);

        container.append(row);
    }
});