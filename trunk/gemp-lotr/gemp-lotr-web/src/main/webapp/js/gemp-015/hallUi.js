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
        this.addTournamentsTable();
        this.addWaitingTablesTable();
        this.addPlayingTablesTable();
        this.addFinishedTablesTable();

        this.div.append(this.tablesDiv);

        this.buttonsDiv = $("<div></div>");
        this.buttonsDiv.css({left:"0px", top:(height - 30) + "px", width:width + "px", height:29 + "px", align:"right", backgroundColor:"#000000", "border-top-width":"1px", "border-top-color":"#ffffff", "border-top-style":"solid"});

        var that = this;

        this.buttonsDiv.append("<a href='deckBuild.html'>Deck builder</a>");
        this.buttonsDiv.append(" | ");

        this.buttonsDiv.append("<a href='merchant.html'>Merchant</a>");
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
        var header = $("<div class='eventHeader queues'></div>");

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
        header.append(" <span class='count'>(0)</span>");

        var table = $("<table class='tables queues'></table>");
        table.append("<tr><th width='10%'>Format</th><th width='8%'>Collection</th><th width='20%'>Queue name</th><th width='16%'>Starts</th><th width='10%'>System</th><th width='6%'>Players</th><th width='8%'>Cost</th><th width='12%'>Prizes</th><th width='10%'>Actions</th></tr>");
        content.append(table);

        this.tablesDiv.append(header);
        this.tablesDiv.append(content);
    },

    addTournamentsTable: function() {
        var header = $("<div class='eventHeader tournaments'></div>");

        var content = $("<div></div>");

        var toggleContent = $("<div>Toggle tournaments in progress</div>").button({
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
        header.append(" Tournaments in progress");
        header.append(" <span class='count'>(0)</span>");

        var table = $("<table class='tables tournaments'></table>");
        table.append("<tr><th width='10%'>Format</th><th width='10%'>Collection</th><th width='25%'>Tournament name</th><th width='15%'>System</th><th width='10%'>Stage</th><th width='10%'>Round</th><th width='10%'>Players</th><th width='10%'>Actions</th></tr>");
        content.append(table);

        this.tablesDiv.append(header);
        this.tablesDiv.append(content);
    },

    addWaitingTablesTable: function() {
        var header = $("<div class='eventHeader waitingTables'></div>");

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
        header.append(" <span class='count'>(0)</span>");

        var table = $("<table class='tables waitingTables'></table>");
        table.append("<tr><th width='20%'>Format</th><th width='40%'>Tournament</th><th width='10%'>Status</th><th width='20%'>Players</th><th width='10%'>Actions</th></tr>");
        content.append(table);

        this.tablesDiv.append(header);
        this.tablesDiv.append(content);
    },

    addPlayingTablesTable: function() {
        var header = $("<div class='eventHeader playingTables'></div>");

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
        header.append(" <span class='count'>(0)</span>");

        var table = $("<table class='tables playingTables'></table>");
        table.append("<tr><th width='20%'>Format</th><th width='40%'>Tournament</th><th width='10%'>Status</th><th width='20%'>Players</th><th width='10%'>Actions</th></tr>");
        content.append(table);

        this.tablesDiv.append(header);
        this.tablesDiv.append(content);
    },

    addFinishedTablesTable: function() {
        var header = $("<div class='eventHeader finishedTables'></div>");

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
        header.append(" <span class='count'>(0)</span>");

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

        this.comm.getHall(
                function(xml) {
                    that.processHall(xml);
                }, this.hallErrorMap());
    },

    updateHall:function () {
        var that = this;

        this.comm.updateHall(
                function (xml) {
                    that.processHall(xml);
                }, this.hallChannelId, this.hallErrorMap());
    },

    hallErrorMap:function() {
        var that = this;
        return {
            "0": function() {
                that.showErrorDialog("Server connection error", "Unable to connect to server. Either server is down or there is a problem with your internet connection.", true, false);
            },
            "401":function() {
                that.showErrorDialog("Authentication error", "You are not logged in", false, true);
            },
            "409":function() {
                that.showErrorDialog("Concurrent access error", "You are accessing Game Hall from another browser or window. Close this window or if you wish to access Game Hall from here, click \"Refresh page\".", true, false);
            },
            "410":function() {
                that.showErrorDialog("Inactivity error", "You were inactive for too long and have been removed from the Game Hall. If you wish to re-enter, click \"Refresh page\".", true, false);
            }
        };
    },

    showErrorDialog:function(title, text, reloadButton, mainPageButton) {
        var buttons = {};
        if (reloadButton) {
            buttons["Refresh page"] =
            function () {
                location.reload(true);
            };
        }
        if (mainPageButton) {
            buttons["Go to main page"] =
            function() {
                location.href = "/gemp-lotr/";
            };
        }

        var dialog = $("<div></div>").dialog({
            title: title,
            resizable: false,
            height: 160,
            modal: true,
            buttons: buttons
        }).text(text);
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

            var serverTime = root.getAttribute("serverTime");
            if (serverTime != null)
                $(".serverTime").text("Server time: " + serverTime);

            var busy = root.getAttribute("busy") == "true";

            var queues = root.getElementsByTagName("queue");
            for (var i = 0; i < queues.length; i++) {
                var queue = queues[i];
                var id = queue.getAttribute("id");
                var action = queue.getAttribute("action");
                if (action == "add" || action == "update") {
                    var actionsField = $("<td></td>");

                    var joined = queue.getAttribute("signedUp");
                    if (joined != "true" && queue.getAttribute("joinable") == "true") {
                        var but = $("<button>Join queue</button>");
                        $(but).button().click((
                                function(queueId) {
                                    return function () {
                                        var deck = that.decksSelect.val();
                                        if (deck != null)
                                            that.comm.joinQueue(queueId, deck, function (xml) {
                                                that.processResponse(xml);
                                            });
                                    };
                                }
                                )(id));
                        actionsField.append(but);
                    } else if (joined == "true") {
                        var but = $("<button>Leave queue</button>");
                        $(but).button().click((
                                function(queueId) {
                                    return function() {
                                        that.comm.leaveQueue(queueId, function (xml) {
                                            that.processResponse(xml);
                                        });
                                    }
                                })(id));
                        actionsField.append(but);
                    }

                    var row = $("<tr class='queue" + id + "'><td>" + queue.getAttribute("format") + "</td>" +
                                "<td>" + queue.getAttribute("collection") + "</td>" +
                                "<td>" + queue.getAttribute("queue") + "</td>" +
                                "<td>" + queue.getAttribute("start") + "</td>" +
                                "<td>" + queue.getAttribute("system") + "</td>" +
                                "<td>" + queue.getAttribute("playerCount") + "</td>" +
                                "<td align='right'>" + formatPrice(queue.getAttribute("cost")) + "</td>" +
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

            var tournaments = root.getElementsByTagName("tournament");
            for (var i = 0; i < tournaments.length; i++) {
                var tournament = tournaments[i];
                var id = tournament.getAttribute("id");
                var action = tournament.getAttribute("action");
                if (action == "add" || action == "update") {
                    var actionsField = $("<td></td>");

                    var joined = tournament.getAttribute("signedUp");
                    if (joined == "true") {
                        var but = $("<button>Drop from tournament</button>");
                        $(but).button().click((
                                function(tournamentId) {
                                    return function () {
                                        that.comm.dropFromTournament(tournamentId, function (xml) {
                                            that.processResponse(xml);
                                        });
                                    };
                                }
                                )(id));
                        actionsField.append(but);
                    }

                    var row = $("<tr class='tournament" + id + "'><td>" + tournament.getAttribute("format") + "</td>" +
                                "<td>" + tournament.getAttribute("collection") + "</td>" +
                                "<td>" + tournament.getAttribute("name") + "</td>" +
                                "<td>" + tournament.getAttribute("system") + "</td>" +
                                "<td>" + tournament.getAttribute("stage") + "</td>" +
                                "<td>" + tournament.getAttribute("round") + "</td>" +
                                "<td>" + tournament.getAttribute("playerCount") + "</td>" +
                                "</tr>");

                    row.append(actionsField);

                    if (action == "add") {
                        $("table.tournaments", this.tablesDiv)
                                .append(row);
                    } else if (action == "update") {
                        $(".tournament" + id, this.tablesDiv).replaceWith(row);
                    }
                } else if (action == "remove") {
                    $(".tournament" + id, this.tablesDiv).remove();
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
                    for (var playerI = 0; playerI < players.length; playerI++) {
                        if (playerI > 0)
                            playersStr += ", ";
                        playersStr += players[playerI];
                    }
                    row.append("<td>" + playersStr + "</td>");

                    var lastField = $("<td></td>");
                    if (status == "WAITING" && !busy) {
                        var that = this;

                        var but = $("<button>Join table</button>");
                        $(but).button().click((
                                function(tableId) {
                                    return function() {
                                        var deck = that.decksSelect.val();
                                        if (deck != null)
                                            that.comm.joinTable(tableId, deck, function (xml) {
                                                that.processResponse(xml);
                                            });
                                    };
                                })(id));
                        lastField.append(but);
                    }

                    if (status == "PLAYING" && watchable == "true") {
                        var participantId = getUrlParam("participantId");
                        var participantIdAppend = "";
                        if (participantId != null)
                            participantIdAppend = "&participantId=" + participantId;

                        lastField.append("<a href='game.html?gameId=" + gameId + participantIdAppend + "'>Watch game</a>");
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
                            if ($(".table" + id, $("table.waitingTables")).length > 0) {
                                $(".table" + id, this.tablesDiv).replaceWith(row);
                            } else {
                                $(".table" + id, this.tablesDiv).remove();
                                $("table.waitingTables", this.tablesDiv)
                                        .append(row);
                            }
                        } else if (status == "PLAYING") {
                            if ($(".table" + id, $("table.playingTables")).length > 0) {
                                $(".table" + id, this.tablesDiv).replaceWith(row);
                            } else {
                                $(".table" + id, this.tablesDiv).remove();
                                $("table.playingTables", this.tablesDiv)
                                        .append(row);
                            }
                        } else if (status == "FINISHED") {
                            if ($(".table" + id, $("table.finishedTables")).length > 0) {
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

            $(".count", $(".eventHeader.queues")).html("(" + ($("tr", $("table.queues")).length - 1) + ")");
            $(".count", $(".eventHeader.tournaments")).html("(" + ($("tr", $("table.tournaments")).length - 1) + ")");
            $(".count", $(".eventHeader.waitingTables")).html("(" + ($("tr", $("table.waitingTables")).length - 1) + ")");
            $(".count", $(".eventHeader.playingTables")).html("(" + ($("tr", $("table.playingTables")).length - 1) + ")");
            $(".count", $(".eventHeader.finishedTables")).html("(" + ($("tr", $("table.finishedTables")).length - 1) + ")");

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
});