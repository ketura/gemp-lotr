var GempLotrHallUI = Class.extend({
    div : null,
    comm: null,

    tablesDiv: null,

    init: function(div, url) {
        this.div = div;
        this.comm = new GempLotrCommunication(url, function() {
            alert("There was a problem communicating with the server, reload this window to continue.");
        });

        var width = $(div).width();
        var height = $(div).height();

        this.tablesDiv = $("<div></div>");
        this.tablesDiv.css({scroll: "auto", left: "0px", top: "0px", width: width + "px", height: (height - 40) + "px"});
        this.div.append(this.tablesDiv);

        var buttonsDiv = $("<div></div>");
        buttonsDiv.css({left: "0px", top: (height - 40) + "px", width: width + "px", height: 40 + "px", align: "right"});

        var that = this;

        var createTableButton = $("<button>Create table</button>");
        $(createTableButton).button().click(
                function() {
                    that.comm.createTable();
                });

        buttonsDiv.append(createTableButton);

        this.div.append(buttonsDiv);

        this.updateHall();
    },

    updateHall: function() {
        var that = this;

        this.comm.getHall(function(xml) {
            that.processHall(xml);
        });
    },

    processHall: function(xml) {
        var root = xml.documentElement;
        if (root.tagName == 'hall') {
            this.tablesDiv.html("");

            var tables = root.getElementsByTagName("table");
            for (var i = 0; i < tables.length; i++) {
                var table = tables[i];
                var id = table.getAttribute("id");
                var status = table.getAttribute("status");
                var playersAttr = table.getAttribute("players");
                var players = new Array();
                if (playersAttr.length > 0)
                    players = playersAttr.split(",");

                var tableDiv = this.createTableDiv(id, status, players);
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

            var that = this;

            setTimeout(function() {
                that.updateHall();
            }, 1000);
        }
    },

    createTableDiv: function(id, status, players) {
        var tableDiv = $("<div></div>");
        tableDiv.css({ display: "inline", width: "120px", height: "120px", margin: "5px", "background-color": "#333300", color: "#ffffff"});
        tableDiv.append("<i>" + status + "</i><br/>");
        tableDiv.append("Players:<br/>");
        for (var i = 0; i < players.length; i++)
            tableDiv.append(players[i] + "<br/>");

        if (players.length < 2) {
            var that = this;

            var but = $("<button>Join game</button>");
            $(but).button().click(
                    function(event) {
                        that.comm.joinTable(id);
                    });
            tableDiv.append(but);
        }

        return tableDiv;
    }
});