var GempLotrGameUI = Class.extend({
    padding: 5,

    bottomPlayerId: null,
    replayMode: null,
    spectatorMode: null,

    currentPlayerId: null,
    allPlayerIds: null,

    playerPositions: null,
    cardActionDialog: null,
    smallDialog: null,
    gameStateElem: null,
    alertBox: null,
    alertText: null,
    alertButtons: null,
    infoDialog: null,

    advPathGroup: null,
    supportOpponent: null,
    charactersOpponent: null,
    shadow: null,
    charactersPlayer: null,
    supportPlayer: null,
    hand: null,
    specialGroup: null,

    discardPileDialogs: null,
    discardPileGroups: null,
    deadPileDialogs: null,
    deadPileGroups: null,

    statsDiv: null,

    skirmishGroupDiv: null,
    fpStrengthDiv: null,
    fpDamageBonusDiv: null,
    shadowStrengthDiv: null,
    shadowDamageBonusDiv: null,
    skirmishShadowGroup: null,
    skirmishFellowshipGroup: null,

    assignGroupDivs: null,
    shadowAssignGroups: null,
    freePeopleAssignGroups: null,

    selectionFunction: null,

    chatBoxDiv: null,
    chatBox: null,
    communication: null,

    settingsAutoPass: false,
    settingsAutoAccept: false,

    windowWidth: null,
    windowHeight: null,

    tabPane: null,

    animations: null,

    init: function(url, replayMode) {
        this.replayMode = replayMode;

        log("ui initialized");
        var that = this;

        this.animations = new GameAnimations(this);

        this.communication = new GempLotrCommunication(url,
                function(xhr, ajaxOptions, thrownError) {
                    if (thrownError != "abort") {
                        if (xhr != null) {
                            if (xhr.status == 401) {
                                that.chatBox.appendMessage("Game problem - You're not logged in, go to the <a href='index.html'>main page</a> to log in", "warningMessage");
                                return;
                            } else {
                                that.chatBox.appendMessage("There was a problem communicating with the server (" + xhr.status + "), if the game is finished, it has been removed, otherwise you have lost connection to the server.", "warningMessage");
                                that.chatBox.appendMessage("Refresh the page (press F5) to resume the game, or press back on your browser to get back to the Game Hall.", "warningMessage");
                                return;
                            }
                        }
                        that.chatBox.appendMessage("There was a problem communicating with the server, if the game is finished, it has been removed, otherwise you have lost connection to the server.", "warningMessage");
                        that.chatBox.appendMessage("Refresh the page (press F5) to resume the game, or press back on your browser to get back to the Game Hall.", "warningMessage");
                    }
                });

        $.expr[':'].cardId = function(obj, index, meta, stack) {
            var cardIds = meta[3].split(",");
            var cardData = $(obj).data("card");
            return (cardData != null && ($.inArray(cardData.cardId, cardIds) > -1));
        };

        this.shadowAssignGroups = {};
        this.freePeopleAssignGroups = {};
        this.assignGroupDivs = new Array();

        this.discardPileDialogs = {};
        this.discardPileGroups = {};
        this.deadPileDialogs = {};
        this.deadPileGroups = {};

        this.skirmishShadowGroup = new NormalCardGroup($("#main"), function (card) {
            return card.zone == "SHADOW_CHARACTERS" && card.skirmish == true;
        }, false);
        this.skirmishFellowshipGroup = new NormalCardGroup($("#main"), function (card) {
            return (card.zone == "SUPPORT" || card.zone == "FREE_CHARACTERS") && card.skirmish == true;
        }, false);

        this.initializeDialogs();

        this.addBottomLeftTabPane();
    },

    layoutGroupWithCard: function(cardId) {
        var cardData = $(".card:cardId(" + cardId + ")").data("card");
        if (this.advPathGroup.cardBelongs(cardData)) {
            this.advPathGroup.layoutCards();
            return;
        }
        if (this.charactersPlayer.cardBelongs(cardData)) {
            this.charactersPlayer.layoutCards();
            return;
        }
        if (this.charactersOpponent.cardBelongs(cardData)) {
            this.charactersOpponent.layoutCards();
            return;
        }
        if (this.supportPlayer.cardBelongs(cardData)) {
            this.supportPlayer.layoutCards();
            return;
        }
        if (this.supportOpponent.cardBelongs(cardData)) {
            this.supportOpponent.layoutCards();
            return;
        }
        if (this.hand != null)
            if (this.hand.cardBelongs(cardData)) {
                this.hand.layoutCards();
                return;
            }
        if (this.shadow.cardBelongs(cardData)) {
            this.shadow.layoutCards();
            return;
        }

        if (this.skirmishFellowshipGroup.cardBelongs(cardData)) {
            this.skirmishFellowshipGroup.layoutCards();
            return;
        }
        if (this.skirmishShadowGroup.cardBelongs(cardData)) {
            this.skirmishShadowGroup.layoutCards();
            return;
        }

        for (var characterId in this.shadowAssignGroups) {
            if (this.shadowAssignGroups.hasOwnProperty(characterId)) {
                if (this.shadowAssignGroups[characterId].cardBelongs(cardData)) {
                    this.shadowAssignGroups[characterId].layoutCards();
                    return;
                }
                if (this.freePeopleAssignGroups[characterId].cardBelongs(cardData)) {
                    this.freePeopleAssignGroups[characterId].layoutCards();
                    return;
                }
            }
        }

        this.layoutUI(false);
    },

    initializeGameUI: function() {
        this.advPathGroup = new AdvPathCardGroup($("#main"));

        var that = this;

        this.supportOpponent = new NormalCardGroup($("#main"), function(card) {
            return (card.zone == "SUPPORT" && card.owner != that.bottomPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        this.charactersOpponent = new NormalCardGroup($("#main"), function(card) {
            return (card.zone == "FREE_CHARACTERS" && card.owner != that.bottomPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        this.shadow = new NormalCardGroup($("#main"), function(card) {
            return (card.zone == "SHADOW_CHARACTERS" && card.assign == null && card.skirmish == null);
        });
        this.charactersPlayer = new NormalCardGroup($("#main"), function(card) {
            return (card.zone == "FREE_CHARACTERS" && card.owner == that.bottomPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        this.supportPlayer = new NormalCardGroup($("#main"), function(card) {
            return (card.zone == "SUPPORT" && card.owner == that.bottomPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        if (!this.spectatorMode) {
            this.hand = new NormalCardGroup($("#main"), function(card) {
                return (card.zone == "HAND");
            });
        }

        this.specialGroup = new NormalCardGroup(this.cardActionDialog, function(card) {
            return (card.zone == "SPECIAL");
        }, false);
        this.specialGroup.setBounds(this.padding, this.padding, 580 - 2 * (this.padding), 250 - 2 * (this.padding));

        this.gameStateElem = $("<div class='ui-widget-content'></div>");
        this.gameStateElem.css({"border-radius": "7px"});

        for (var i = 0; i < this.allPlayerIds.length; i++) {
            this.gameStateElem.append("<div class='player'>" + this.allPlayerIds[i] + "<div id='clock" + i + "' class='clock'></div>"
                    + "<div class='playerStats'><div id='deck" + i + "' style='display: inline;'></div> <div id='hand" + i + "' style='display: inline;'></div> <div id='discard" + i + "' style='display: inline;'></div> <div id='deadPile" + i + "' style='display: inline;'></div></div></div>");
        }

        this.gameStateElem.append("<br>");

        this.gameStateElem.append("<div class='twilightPool'>0</div>");
        this.gameStateElem.append("<div class='phase' id='FELLOWSHIP'>Fellowship</div>");
        this.gameStateElem.append("<div class='phase' id='SHADOW'>Shadow</div>");
        this.gameStateElem.append("<div class='phase' id='MANEUVER'>Maneuver</div>");
        this.gameStateElem.append("<div class='phase' id='ARCHERY'>Archery</div>");
        this.gameStateElem.append("<div class='phase' id='ASSIGNMENT'>Assignment</div>");
        this.gameStateElem.append("<div class='phase' id='SKIRMISH'>Skirmish</div>");
        this.gameStateElem.append("<div class='phase' id='REGROUP'>Regroup</div>");

        $("#main").append(this.gameStateElem);

        if (!this.spectatorMode)
            $("#discard" + this.getPlayerIndex(this.bottomPlayerId)).addClass("clickable").click(
                    (function(index) {
                        return function() {
                            that.discardPileDialogs[that.bottomPlayerId].dialog("open");
                        };
                    })(i));

        for (var i = 0; i < this.allPlayerIds.length; i++) {
            $("#deadPile" + i).addClass("clickable").click(
                    (function(index) {
                        return function() {
                            that.deadPileDialogs[that.allPlayerIds[index]].dialog("open");
                        };
                    })(i));
        }

        this.alertBox = $("<div class='ui-widget-content'></div>");
        this.alertBox.css({"border-radius": "7px"});

        this.alertText = $("<div></div>");
        this.alertText.css({position: "absolute", left: "0px", top: "0px", width: "100%", height: "50px", scroll: "auto"});

        this.alertButtons = $("<div></div>");
        this.alertButtons.css({position: "absolute", left: "0px", top: "50px", width: "100%", height: "30px", scroll: "auto"});

        this.alertBox.append(this.alertText);
        this.alertBox.append(this.alertButtons);

        $("#main").append(this.alertBox);

        this.statsDiv = $("<div class='ui-widget-content stats'></div>");
        this.statsDiv.css({"border-radius": "7px"});
        this.statsDiv.append("<div class='fpArchery'></div> <div class='shadowArchery'></div> <div class='move'></div>");
        $("#main").append(this.statsDiv);

        $("body").click(
                function (event) {
                    return that.clickCardFunction(event);
                });
        $("body").mousedown(
                function (event) {
                    return that.dragStartCardFunction(event);
                });
        $("body").mouseup(
                function (event) {
                    return that.dragStopCardFunction(event);
                });
    },

    addBottomLeftTabPane: function() {
        var that = this;
        this.tabPane = $("<div><ul><li><a href='#chatBox'>Chat</a></li><li><a href='#settingsBox'>Settings</a></li><li><a href='#gameOptionsBox'>Game options</a></li></ul>"
                + "<div id='chatBox'></div><div id='settingsBox'></div><div id='gameOptionsBox'></div></div>").tabs();

        $("#main").append(this.tabPane);

        this.chatBoxDiv = $("#chatBox");

        $("#settingsBox").append("<input id='autoPass' type='checkbox' value='selected' /><label for='autoPass'>Auto-pass when there is no actions</label><br />");
        $("#settingsBox").append("<input id='autoAccept' type='checkbox' value='selected' /><label for='autoAccept'>Auto-accept after selecting action or card</label><br />");

        var autoPass = $.cookie("autoPass");
        if (autoPass == "true") {
            $("#autoPass").prop("checked", true);
            this.settingsAutoPass = true;
        }
        var autoAccept = $.cookie("autoAccept");
        if (autoAccept == "true") {
            $("#autoAccept").prop("checked", true);
            this.settingsAutoAccept = true;
        }

        $("#autoPass").bind("change", function() {
            var selected = $("#autoPass").prop("checked");
            that.settingsAutoPass = selected;
            $.cookie("autoPass", "" + selected, { expires: 365 });
        });

        $("#autoAccept").bind("change", function() {
            var selected = $("#autoAccept").prop("checked");
            that.settingsAutoAccept = selected;
            $.cookie("autoAccept", "" + selected, { expires: 365 });
        });

        var chatRoomName = (this.replayMode ? null : ("Game" + getUrlParam("gameId")));
        this.chatBox = new ChatBoxUI(chatRoomName, $("#chatBox"), this.communication.url);
        this.chatBox.chatUpdateInterval = 3000;

        if (!this.spectatorMode && !this.replayMode) {
            $("#gameOptionsBox").append("<button id='concedeGame'>Concede game</button>");
            $("#concedeGame").button().click(
                    function() {
                        that.communication.concede();
                    });
        }
    },

    clickCardFunction: function(event) {
        var tar = $(event.target);

        if (tar.hasClass("cardHint")) {
            var blueprintId = tar.attr("value");
            var card = new Card(blueprintId, "SPECIAL", "hint", "");
            this.displayCard(card, false);
            event.stopPropagation();
            return false;
        }

        if (!this.successfulDrag && this.infoDialog.dialog("isOpen")) {
            this.infoDialog.dialog("close");
            event.stopPropagation();
            return false;
        }

        if (tar.hasClass("actionArea")) {
            tar = tar.parent();
            if (tar.hasClass("borderOverlay")) {
                var selectedCardElem = tar.parent();
                if (event.which == 1) {
                    if (!this.successfulDrag) {
                        if (event.shiftKey) {
                            this.displayCardInfo(selectedCardElem.data("card"));
                        } else if (selectedCardElem.hasClass("selectableCard") && !this.replayMode)
                            this.selectionFunction(selectedCardElem.data("card").cardId, event);
                        event.stopPropagation();
                    }
                }
            }
            return false;
        }

        return true;
    },

    dragCardData: null,
    dragStartX: null,
    dragStartY: null,
    successfulDrag: null,

    dragStartCardFunction: function(event) {
        this.successfulDrag = false;
        var tar = $(event.target);
        if (tar.hasClass("actionArea")) {
            tar = tar.parent();
            if (tar.hasClass("borderOverlay")) {
                var selectedCardElem = tar.parent();
                if (event.which == 1) {
                    this.dragCardData = selectedCardElem.data("card");
                    this.dragStartX = event.clientX;
                    this.dragStartY = event.clientY;
                    return false;
                }
            }
        }
        return true;
    },

    dragStopCardFunction: function(event) {
        if (this.dragCardData != null) {
            if (this.dragStartY - event.clientY >= 20) {
                this.displayCardInfo(this.dragCardData);
                this.successfulDrag = true;
            }
            this.dragCardData = null;
            this.dragStartX = null;
            this.dragStartY = null;
            return false;
        }
        return true;
    },

    displayCard: function(card, extraSpace) {
        this.infoDialog.html("");
        this.infoDialog.html("<div style='scroll: auto'><div style='float: left;'><img src='" + card.imageUrl + "'></div><div id='cardEffects'></div></div>");
        var windowWidth = $(window).width();
        var windowHeight = $(window).height();

        var horSpace = (extraSpace ? 200 : 0) + 30;
        var vertSpace = 45;

        if (card.horizontal) {
            // 500x360
            this.infoDialog.dialog({width: Math.min(500 + horSpace, windowWidth), height: Math.min(360 + vertSpace, windowHeight)});
        } else {
            // 360x500
            this.infoDialog.dialog({width: Math.min(360 + horSpace, windowWidth), height: Math.min(500 + vertSpace, windowHeight)});
        }
        this.infoDialog.dialog("open");
    },

    displayCardInfo: function(card) {
        var showModifiers = false;
        var cardId = card.cardId;
        if (!this.replayMode && (cardId.length < 4 || cardId.substring(0, 4) != "temp"))
            showModifiers = true;

        this.displayCard(card, showModifiers);

        if (showModifiers)
            this.getCardModifiersFunction(cardId, this.setCardModifiers);
    },

    setCardModifiers: function(html) {
        $("#cardEffects").replaceWith(html);
    },

    initializeDialogs: function() {
        this.smallDialog = $("<div></div>")
                .dialog({
                    autoOpen: false,
                    closeOnEscape: false,
                    resizable: false,
                    width: 400,
                    height: 200
                });

        this.cardActionDialog = $("<div></div>")
                .dialog({
                    autoOpen: false,
                    closeOnEscape: false,
                    resizable: true,
                    width: 600,
                    height: 300
                });

        var that = this;

        this.cardActionDialog.bind("dialogresize", function() {
            that.arbitraryDialogResize();
        });

        $(".ui-dialog-titlebar-close").hide();

        var width = $(window).width();
        var height = $(window).height();

        this.infoDialog = $("<div></div>")
                .dialog({
                    autoOpen: false,
                    closeOnEscape: true,
                    resizable: false,
                    title: "Card information"
                });

        var swipeOptions = {
            threshold: 20,
            swipeUp: function (event) {
                that.infoDialog.prop({ scrollTop: that.infoDialog.prop("scrollHeight") });
                return false;
            },
            swipeDown: function (event) {
                that.infoDialog.prop({ scrollTop: 0 });
                return false;
            }
        };
        this.infoDialog.swipe(swipeOptions);
    },

    windowResized: function() {
        this.animations.windowResized();
    },

    layoutUI: function(sizeChanged) {
        var padding = this.padding;
        var width = $(window).width();
        var height = $(window).height();
        if (sizeChanged) {
            this.windowWidth = width;
            this.windowHeight = height;
        } else {
            width = this.windowWidth;
            height = this.windowHeight;
        }

        var heightScales;
        if (this.spectatorMode)
            heightScales = [6, 10, 10, 10, 6];
        else
            heightScales = [5, 9, 9, 10, 6, 10];
        var yScales = new Array();
        var scaleTotal = 0;
        for (var i = 0; i < heightScales.length; i++) {
            yScales[i] = scaleTotal;
            scaleTotal += heightScales[i];
        }

        var heightPerScale = (height - (padding * (heightScales.length + 1))) / scaleTotal;

        var advPathWidth = Math.min(150, width * 0.1);
        var specialUiWidth = 150;

        var alertHeight = 80;

        var chatHeight = 200;

        var assignmentsCount = this.assignGroupDivs.length + ((this.skirmishGroupDiv != null) ? 1 : 0);

        var charsWidth = width - (advPathWidth + specialUiWidth + padding * 3);
        var charsWidthWithAssignments = 2 * charsWidth / (2 + assignmentsCount);

        var currentPlayerTurn = (this.currentPlayerId == this.bottomPlayerId);

        if (this.advPathGroup != null) {
            this.statsDiv.css({position: "absolute", left: padding + "px", top: height - (padding * 2) - chatHeight - 34 + "px", width: advPathWidth - 4 , height: 30});

            this.advPathGroup.setBounds(padding, padding, advPathWidth, height - (padding * 3) - chatHeight - 34 - padding);
            this.supportOpponent.setBounds(advPathWidth + specialUiWidth + (padding * 2), padding + yScales[0] * heightPerScale, width - (advPathWidth + specialUiWidth + padding * 3), heightScales[0] * heightPerScale);

            this.charactersOpponent.setBounds(advPathWidth + specialUiWidth + (padding * 2), padding * 2 + yScales[1] * heightPerScale, currentPlayerTurn ? charsWidth : charsWidthWithAssignments, heightScales[1] * heightPerScale);
            this.shadow.setBounds(advPathWidth + specialUiWidth + (padding * 2), padding * 3 + yScales[2] * heightPerScale, charsWidthWithAssignments, heightScales[2] * heightPerScale);
            this.charactersPlayer.setBounds(advPathWidth + specialUiWidth + (padding * 2), padding * 4 + yScales[3] * heightPerScale, currentPlayerTurn ? charsWidthWithAssignments : charsWidth, heightScales[3] * heightPerScale);

            var i = 0;

            if (this.skirmishGroupDiv != null) {
                var groupWidth = (charsWidth - charsWidthWithAssignments) / assignmentsCount - padding;
                var groupHeight = currentPlayerTurn ? (heightScales[2] * heightPerScale + heightScales[3] * heightPerScale + padding) : (heightScales[1] * heightPerScale + heightScales[2] * heightPerScale + padding);
                var x = advPathWidth + specialUiWidth + (padding * 2) + charsWidthWithAssignments + padding + i * (groupWidth + padding);
                var y = currentPlayerTurn ? (padding * 3 + yScales[2] * heightPerScale) : (padding * 2 + yScales[1] * heightPerScale);
                this.skirmishGroupDiv.css({left:x + "px", top:y + "px", width: groupWidth, height: groupHeight, position: "absolute"});
                var strengthBoxSize = 40;
                var dmgBoxSize = 30;
                if (currentPlayerTurn) {
                    this.skirmishShadowGroup.setBounds(x + 3, y + 3, groupWidth - 6, heightScales[2] * heightPerScale - 6);
                    this.skirmishFellowshipGroup.setBounds(x + 3, y + heightScales[2] * heightPerScale + padding + 3, groupWidth - 6, heightScales[3] * heightPerScale - 6);
                    this.fpStrengthDiv.css({position: "absolute", left: groupWidth - strengthBoxSize - 2 + "px", top: groupHeight - strengthBoxSize - 2 + "px", width: strengthBoxSize, height: strengthBoxSize, "z-index": 50});
                    this.fpDamageBonusDiv.css({position: "absolute", left: groupWidth - strengthBoxSize - dmgBoxSize - 2 + "px", top: groupHeight - dmgBoxSize - 2 + "px", width: dmgBoxSize, height: dmgBoxSize, "z-index": 50});
                    this.shadowStrengthDiv.css({position: "absolute", left: groupWidth - strengthBoxSize - 2 + "px", top: 2 + "px", width: strengthBoxSize, height: strengthBoxSize, "z-index": 50});
                    this.shadowDamageBonusDiv.css({position: "absolute", left: groupWidth - strengthBoxSize - dmgBoxSize - 2 + "px", top: 2 + "px", width: dmgBoxSize, height: dmgBoxSize, "z-index": 50});
                } else {
                    this.skirmishFellowshipGroup.setBounds(x + 3, y + 3, groupWidth - 6, heightScales[1] * heightPerScale - 6);
                    this.skirmishShadowGroup.setBounds(x + 3, y + heightScales[1] * heightPerScale + padding + 3, groupWidth - 6, heightScales[2] * heightPerScale - 6);
                    this.shadowStrengthDiv.css({position: "absolute", left: groupWidth - strengthBoxSize - 2 + "px", top: groupHeight - strengthBoxSize - 2 + "px", width: strengthBoxSize, height: strengthBoxSize, "z-index": 50});
                    this.shadowDamageBonusDiv.css({position: "absolute", left: groupWidth - strengthBoxSize - dmgBoxSize - 2 + "px", top: groupHeight - dmgBoxSize - 2 + "px", width: dmgBoxSize, height: dmgBoxSize, "z-index": 50});
                    this.fpStrengthDiv.css({position: "absolute", left: groupWidth - strengthBoxSize - 2 + "px", top: 2 + "px", width: strengthBoxSize, height: strengthBoxSize, "z-index": 50});
                    this.fpDamageBonusDiv.css({position: "absolute", left: groupWidth - strengthBoxSize - dmgBoxSize - 2 + "px", top: 2 + "px", width: dmgBoxSize, height: dmgBoxSize, "z-index": 50});
                }
                i++;
            }

            var assignIndex = 0;
            for (var characterId in this.shadowAssignGroups) {
                if (this.shadowAssignGroups.hasOwnProperty(characterId)) {
                    var groupWidth = (charsWidth - charsWidthWithAssignments) / assignmentsCount - padding;
                    var groupHeight = currentPlayerTurn ? (heightScales[2] * heightPerScale + heightScales[3] * heightPerScale + padding) : (heightScales[1] * heightPerScale + heightScales[2] * heightPerScale + padding);
                    var x = advPathWidth + specialUiWidth + (padding * 2) + charsWidthWithAssignments + padding + i * (groupWidth + padding);
                    var y = currentPlayerTurn ? (padding * 3 + yScales[2] * heightPerScale) : (padding * 2 + yScales[1] * heightPerScale);
                    this.assignGroupDivs[assignIndex].css({left:x + "px", top:y + "px", width: groupWidth, height: groupHeight, position: "absolute"});
                    if (currentPlayerTurn) {
                        this.shadowAssignGroups[characterId].setBounds(x + 3, y + 3, groupWidth - 6, heightScales[2] * heightPerScale - 6);
                        this.freePeopleAssignGroups[characterId].setBounds(x + 3, y + heightScales[2] * heightPerScale + padding + 3, groupWidth - 6, heightScales[3] * heightPerScale - 6);
                    } else {
                        this.freePeopleAssignGroups[characterId].setBounds(x + 3, y + 3, groupWidth - 6, heightScales[1] * heightPerScale - 6);
                        this.shadowAssignGroups[characterId].setBounds(x + 3, y + heightScales[1] * heightPerScale + padding + 3, groupWidth - 6, heightScales[2] * heightPerScale - 6);
                    }
                    i++;
                    assignIndex++;
                }
            }

            this.supportPlayer.setBounds(advPathWidth + specialUiWidth + (padding * 2), padding * 5 + yScales[4] * heightPerScale, width - (advPathWidth + specialUiWidth + padding * 3), heightScales[4] * heightPerScale);
            if (!this.spectatorMode)
                this.hand.setBounds(advPathWidth + specialUiWidth + (padding * 2), padding * 6 + yScales[5] * heightPerScale, width - (advPathWidth + specialUiWidth + padding * 3), heightScales[5] * heightPerScale);

            this.gameStateElem.css({ position: "absolute", left: padding * 2 + advPathWidth, top: padding, width: specialUiWidth - padding, height: height - padding * 4 - alertHeight - chatHeight});
            this.alertBox.css({ position: "absolute", left: padding * 2 + advPathWidth, top: height - (padding * 2) - alertHeight - chatHeight, width: specialUiWidth - padding, height: alertHeight });


            for (var playerId in this.discardPileGroups)
                if (this.discardPileGroups.hasOwnProperty(playerId))
                    this.discardPileGroups[playerId].layoutCards();

            for (var playerId in this.deadPileGroups)
                if (this.deadPileGroups.hasOwnProperty(playerId))
                    this.deadPileGroups[playerId].layoutCards();
        }
        this.tabPane.css({ position: "absolute", left: padding, top: height - padding - chatHeight, width: specialUiWidth + advPathWidth - padding, height: chatHeight - padding});
        this.chatBox.setBounds(4, 4 + 25, specialUiWidth + advPathWidth - 8, chatHeight - 8 - 25);
    },

    startGameSession: function() {
        var that = this;
        this.communication.startGameSession(
                function(xml) {
                    that.processXml(xml, false);
                });
    },

    startReplaySession: function(replayId) {
        var that = this;
        this.communication.getReplay(replayId,
                function(xml) {
                    that.processXml(xml, true);
                });
    },

    updateGameState: function() {
        var that = this;
        this.communication.updateGameState(
                function(xml) {
                    that.processXml(xml, true);
                });
    },

    decisionFunction: function(decisionId, result) {
        var that = this;
        this.communication.gameDecisionMade(decisionId, result,
                function(xml) {
                    that.processXml(xml, true);
                });
    },

    getCardModifiersFunction: function(cardId, func) {
        var that = this;
        this.communication.getGameCardModifiers(cardId,
                function(html) {
                    that.setCardModifiers(html);
                });
    },

    processXml: function(xml, animate) {
        log(xml);
        var root = xml.documentElement;
        if (root.tagName == 'gameState' || root.tagName == 'update' || root.tagName == 'gameReplay')
            this.processGameEventsXml(root, animate);
    },

    processGameEventsXml: function(element, animate) {
        var gameEvents = element.getElementsByTagName("ge");

        var hasDecision = false;

        // Go through all the events
        for (var i = 0; i < gameEvents.length; i++) {
            var gameEvent = gameEvents[i];
            var eventType = gameEvent.getAttribute("type");
            if (eventType == "PCIP") {
                this.animations.putCardInPlay(gameEvent, animate);
            } else if (eventType == "MCIP") {
                this.animations.moveCardInPlay(gameEvent, animate);
            } else if (eventType == "P") {
                this.participant(gameEvent);
            } else if (eventType == "RCFP") {
                this.animations.removeCardFromPlay(gameEvent, animate);
            } else if (eventType == "GPC") {
                this.animations.gamePhaseChange(gameEvent, animate);
            } else if (eventType == "TP") {
                this.animations.twilightPool(gameEvent, animate);
            } else if (eventType == "TC") {
                this.animations.turnChange(gameEvent, animate);
            } else if (eventType == "AA") {
                this.animations.addAssignment(gameEvent, animate);
            } else if (eventType == "RA") {
                this.animations.removeAssignment(gameEvent, animate);
            } else if (eventType == "SS") {
                this.animations.startSkirmish(gameEvent, animate);
            } else if (eventType == "RFS") {
                this.animations.removeFromSkirmish(gameEvent, animate);
            } else if (eventType == "ES") {
                this.animations.endSkirmish(animate);
            } else if (eventType == "AT") {
                this.animations.addTokens(gameEvent, animate);
            } else if (eventType == "RT") {
                this.animations.removeTokens(gameEvent, animate);
            } else if (eventType == "PP") {
                this.animations.playerPosition(gameEvent, animate);
            } else if (eventType == "GS") {
                this.animations.gameStats(gameEvent, animate);
            } else if (eventType == "M") {
                this.animations.message(gameEvent, animate);
            } else if (eventType == "W") {
                this.animations.warning(gameEvent, animate);
            } else if (eventType == "CAC") {
                this.animations.cardAffectsCard(gameEvent, animate);
            } else if (eventType == "EP") {
                this.animations.eventPlayed(gameEvent, animate);
            } else if (eventType == "CA") {
                this.animations.cardActivated(gameEvent, animate);
            } else if (eventType == "D") {
                hasDecision = true;
                this.animations.processDecision(gameEvent, animate);
            }
        }

        if (this.allPlayerIds != null) {
            var clocksXml = element.getElementsByTagName("clocks");
            if (clocksXml.length > 0) {
                var clocks = clocksXml[0].getElementsByTagName("clock");
                for (var i = 0; i < clocks.length; i++) {
                    var clock = clocks[i];
                    var participantId = clock.getAttribute("participantId");
                    var index = this.getPlayerIndex(participantId);

                    var value = parseInt(clock.childNodes[0].nodeValue);

                    var sign = (value < 0) ? "-" : "";
                    value = Math.abs(value);
                    var minutes = Math.floor(value / 60);
                    var seconds = value % 60;

                    $("#clock" + index).text(sign + minutes + ":" + ((seconds < 10) ? ("0" + seconds) : seconds));
                }
            }
        }

        if (!hasDecision && !this.replayMode)
            this.animations.updateGameState(animate);
    },

    getPlayerIndex: function(playerId) {
        for (var plId = 0; plId < this.allPlayerIds.length; plId++)
            if (this.allPlayerIds[plId] == playerId)
                return plId;
        return -1;
    },

    layoutZones: function() {
        this.advPathGroup.layoutCards();
        this.charactersPlayer.layoutCards();
        this.charactersOpponent.layoutCards();
        this.supportPlayer.layoutCards();
        this.supportOpponent.layoutCards();
        if (!this.spectatorMode)
            this.hand.layoutCards();
        this.shadow.layoutCards();

        this.skirmishFellowshipGroup.layoutCards();
        this.skirmishShadowGroup.layoutCards();

        for (var characterId in this.shadowAssignGroups) {
            if (this.shadowAssignGroups.hasOwnProperty(characterId)) {
                this.shadowAssignGroups[characterId].layoutCards();
                this.freePeopleAssignGroups[characterId].layoutCards();
            }
        }
    },

    participant: function(element) {
        var participantId = element.getAttribute("participantId");
        this.allPlayerIds = element.getAttribute("allParticipantIds").split(",");

        this.bottomPlayerId = participantId;

        var that = this;

        var index = this.getPlayerIndex(this.bottomPlayerId);
        if (index == -1) {
            this.bottomPlayerId = this.allPlayerIds[0];
            this.spectatorMode = true;
        } else {
            this.spectatorMode = false;

            var discardPileDialog = $("<div></div>").dialog({
                autoOpen: false,
                closeOnEscape: true,
                resizable: true,
                title: "Discard - " + participantId,
                minHeight: 80,
                minWidth: 200,
                width: 600,
                height: 300
            });

            this.discardPileDialogs[participantId] = discardPileDialog;
            this.discardPileGroups[participantId] = new NormalCardGroup(discardPileDialog, function(card) {
                return true;
            }, false);

            this.discardPileGroups[participantId].setBounds(this.padding, this.padding, 580 - 2 * (this.padding), 250 - 2 * (this.padding));

            discardPileDialog.bind("dialogresize", function() {
                that.dialogResize(discardPileDialog, that.discardPileGroups[participantId]);
            });
        }

        for (var i = 0; i < this.allPlayerIds.length; i++) {
            var deadPileDialog = $("<div></div>").dialog({
                autoOpen: false,
                closeOnEscape: true,
                resizable: true,
                title: "Dead pile - " + this.allPlayerIds[i],
                minHeight: 80,
                minWidth: 200,
                width: 600,
                height: 300
            });
            this.deadPileDialogs[this.allPlayerIds[i]] = deadPileDialog;
            this.deadPileGroups[this.allPlayerIds[i]] = new NormalCardGroup(deadPileDialog, function(card) {
                return true;
            }, false);

            this.deadPileGroups[this.allPlayerIds[i]].setBounds(this.padding, this.padding, 580 - 2 * (this.padding), 250 - 2 * (this.padding));

            deadPileDialog.bind("dialogresize", (function(dialog, index) {
                return function() {
                    that.dialogResize(dialog, that.deadPileGroups[that.allPlayerIds[index]]);
                }
            })(deadPileDialog, i));
        }

        this.initializeGameUI();
        this.layoutUI(true);
    },

    getDecisionParameter: function(decision, name) {
        var parameters = decision.getElementsByTagName("parameter");
        for (var i = 0; i < parameters.length; i++)
            if (parameters[i].getAttribute("name") == name)
                return parameters[i].getAttribute("value");

        return null;
    },

    getDecisionParameters: function(decision, name) {
        var result = new Array();
        var parameters = decision.getElementsByTagName("parameter");
        for (var i = 0; i < parameters.length; i++)
            if (parameters[i].getAttribute("name") == name)
                result.push(parameters[i].getAttribute("value"));

        return result;
    },

    cleanupDecision: function() {
        this.smallDialog.dialog("close");
        this.cardActionDialog.dialog("close");
        this.clearSelection();
        this.alertText.html("");
        this.alertButtons.html("");
    },

    integerDecision: function(decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");
        var val = 0;

        var min = this.getDecisionParameter(decision, "min");
        if (min == null)
            min = 0;
        var max = this.getDecisionParameter(decision, "max");
        if (max == null)
            max = 1000;

        var defaultValue = this.getDecisionParameter(decision, "defaultValue");
        if (defaultValue != null)
            val = parseInt(defaultValue);

        var that = this;
        this.smallDialog
                .html(text + "<br /><input id='integerDecision' type='text' value='0'>");

        if (!this.replayMode) {
            this.smallDialog.dialog("option", "buttons",
                    {
                        "OK": function() {
                            $(this).dialog("close");
                            that.decisionFunction(id, $("#integerDecision").val());
                        }
                    });
        }

        $("#integerDecision").SpinnerControl({ type: 'range',
            typedata: {
                min: min,
                max: max,
                interval: 1,
                decimalplaces: 0
            },
            defaultVal: val,
            width: '50px',
            backColor: "#000000"
        });

        this.smallDialog.dialog("open");
    },

    multipleChoiceDecision: function(decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var results = this.getDecisionParameters(decision, "results");

        var html = text + "<br /><select id='multipleChoiceDecision' selectedIndex='0'>";
        for (var i = 0; i < results.length; i++)
            html += "<option value='" + results[i] + "'>" + results[i] + "</option>";
        html += "</select>";

        var that = this;
        this.smallDialog
                .html(html);

        if (!this.replayMode) {
            this.smallDialog.dialog("option", "buttons",
                    {
                        "OK": function() {
                            $(this).dialog("close");
                            that.decisionFunction(id, $("#multipleChoiceDecision").val());
                        }
                    });
        }

        this.smallDialog.dialog("open");
    },

    createCardDiv: function(card, text, foil) {
        var cardDiv = createCardDiv(card.imageUrl, text, foil);

        var tokenOverlay = $(".tokenOverlay", cardDiv);

        var cardStrengthDiv = $("<div class='cardStrength'></div>");
        cardStrengthDiv.css({display: "none"});
        tokenOverlay.append(cardStrengthDiv);

        var cardVitalityDiv = $("<div class='cardVitality'></div>");
        cardVitalityDiv.css({display: "none"});
        tokenOverlay.append(cardVitalityDiv);

        var cardSiteNumberDiv = $("<div class='cardSiteNumber'></div>");
        cardSiteNumberDiv.css({display: "none"});
        tokenOverlay.append(cardSiteNumberDiv);

        cardDiv.data("card", card);

        var sizeListeners = new Array();
        sizeListeners[0] = {
            sizeChanged: function(cardElem, width, height) {
                var maxDimension = Math.max(width, height);

                var size = 0.0865 * maxDimension;

                var x = 0.09255 * maxDimension - size / 2;
                var strengthY = 0.67203 * maxDimension - size / 2;
                var vitalityY = 0.79074 * maxDimension - size / 2;
                var minionSiteNumberY = 0.89135 * maxDimension - size / 2;

                var fontPerc = (size * 7) + "%";
                var borderRadius = Math.ceil(size / 5) + "px";

                $(".cardStrength", cardElem).css({position: "absolute", "border-radius": borderRadius, "font-size": fontPerc, left: x + "px", top: strengthY + "px", width: size, height: size});
                $(".cardVitality", cardElem).css({position: "absolute", "border-radius": borderRadius, "font-size": fontPerc, left: x + "px", top: vitalityY + "px", width: size, height: size});
                $(".cardSiteNumber", cardElem).css({position: "absolute", "border-radius": borderRadius, "font-size": fontPerc, left: x + "px", top: minionSiteNumberY + "px", width: size, height: size});
            }
        };

        cardDiv.data("sizeListeners", sizeListeners);

        var that = this;
        var swipeOptions = {
            threshold: 20,
            swipeUp: function (event) {
                var tar = $(event.target);
                if (tar.hasClass("actionArea")) {
                    tar = tar.parent();
                    if (tar.hasClass("borderOverlay")) {
                        var selectedCardElem = tar.parent();
                        that.displayCardInfo(selectedCardElem.data("card"));
                    }
                }
                return false;
            }
        };
        cardDiv.swipe(swipeOptions);


        return cardDiv;
    },

    attachSelectionFunctions: function(cardIds) {
        if (cardIds.length > 0)
            $(".card:cardId(" + cardIds + ")").addClass("selectableCard");
    },

    // Choosing cards from a predefined selection (for example stating fellowship)
    arbitraryCardsDecision: function(decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var min = this.getDecisionParameter(decision, "min");
        var max = this.getDecisionParameter(decision, "max");
        var cardIds = this.getDecisionParameters(decision, "cardId");
        var blueprintIds = this.getDecisionParameters(decision, "blueprintId");
        var selectable = this.getDecisionParameters(decision, "selectable");

        var that = this;

        var selectedCardIds = new Array();

        var selectableCardIds = new Array();

        this.cardActionDialog
                .html("<div id='arbitraryChoice'></div>")
                .dialog("option", "title", text);

        // Create the action cards and fill the dialog with them
        for (var i = 0; i < blueprintIds.length; i++) {
            var cardId = cardIds[i];
            var blueprintId = blueprintIds[i];

            if (selectable[i] == "true")
                selectableCardIds.push(cardId);

            var card = new Card(blueprintId, "SPECIAL", cardId, this.selfParticipantId);

            var cardDiv = this.createCardDiv(card);

            $("#arbitraryChoice").append(cardDiv);
        }

        var finishChoice = function() {
            that.cardActionDialog.dialog("close");
            $("#arbitraryChoice").html("");
            that.clearSelection();
            that.decisionFunction(id, "" + selectedCardIds);
        };

        var resetChoice = function() {
            selectedCardIds = new Array();
            that.clearSelection();
            allowSelection();
            processButtons();
        };

        var processButtons = function() {
            var buttons = {};
            if (selectedCardIds.length > 0)
                buttons["Clear selection"] = function() {
                    resetChoice();
                    processButtons();
                };
            if (selectedCardIds.length >= min)
                buttons["Done"] = function() {
                    finishChoice();
                };
            that.cardActionDialog.dialog("option", "buttons", buttons);
        };

        var allowSelection = function() {
            that.selectionFunction = function(cardId) {
                selectedCardIds.push(cardId);

                if (selectedCardIds.length == max) {
                    if (that.settingsAutoAccept) {
                        finishChoice();
                        return;
                    } else {
                        that.clearSelection();
                        $(".card:cardId(" + selectedCardIds + ")").addClass("selectedCard");
                    }
                } else {
                    $(".card:cardId(" + cardId + ")").removeClass("selectableCard").addClass("selectedCard");
                }

                processButtons();
            };

            that.attachSelectionFunctions(selectableCardIds);
        };

        allowSelection();
        if (!this.replayMode)
            processButtons();

        this.cardActionDialog.dialog("open");
        this.arbitraryDialogResize(false);
    },

    // Choosing one action to resolve, for example phase actions
    cardActionChoiceDecision: function (decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var cardIds = this.getDecisionParameters(decision, "cardId");
        var actionIds = this.getDecisionParameters(decision, "actionId");
        var actionTexts = this.getDecisionParameters(decision, "actionText");

        var that = this;

        if (cardIds.length == 0 && this.settingsAutoPass) {
            that.decisionFunction(id, "");
            return;
        }

        var selectedCardIds = new Array();

        this.alertText.html(text);

        var processButtons = function() {
            that.alertButtons.html("");
            if (selectedCardIds.length == 0) {
                that.alertButtons.append("<button id='Pass'>Pass</button>");
                $("#Pass").button().click(function() {
                    finishChoice();
                });
            }
            if (selectedCardIds.length > 0) {
                that.alertButtons.append("<button id='ClearSelection'>Reset choice</button>");
                that.alertButtons.append("<button id='Done'>Done</button>");
                $("#Done").button().click(function() {
                    finishChoice();
                });
                $("#ClearSelection").button().click(function() {
                    resetChoice();
                });
            }
        };

        var finishChoice = function() {
            that.alertText.html("");
            that.alertButtons.html("");
            that.clearSelection();
            that.decisionFunction(id, "" + selectedCardIds);
        };

        var resetChoice = function() {
            selectedCardIds = new Array();
            that.clearSelection();
            allowSelection();
            processButtons();
        };

        var allowSelection = function() {
            for (var i = 0; i < cardIds.length; i++) {
                var cardId = cardIds[i];
                var actionId = actionIds[i];
                var actionText = actionTexts[i];


                var cardIdElem = $(".card:cardId(" + cardId + ")");
                if (cardIdElem.data("action") == null) {
                    cardIdElem.data("action", new Array());
                }

                var actions = cardIdElem.data("action");
                actions.push({ actionId: actionId, actionText: actionText });
            }

            that.selectionFunction = function(cardId, event) {
                var cardIdElem = $(".card:cardId(" + cardId + ")");
                var actions = cardIdElem.data("action");

                var selectActionFunction = function(actionId) {
                    selectedCardIds.push(actionId);
                    if (that.settingsAutoAccept) {
                        finishChoice();
                    } else {
                        that.clearSelection();
                        $(".card:cardId(" + cardId + ")").addClass("selectedCard");
                        processButtons();
                    }
                };

                if (actions.length == 1) {
                    var action = actions[0];
                    selectActionFunction(action.actionId);
                } else {
                    that.createActionChoiceContextMenu(actions, event, selectActionFunction);
                }
            };

            that.attachSelectionFunctions(cardIds);
        };

        allowSelection();
        if (!this.replayMode)
            processButtons();
    },

    createActionChoiceContextMenu: function(actions, event, selectActionFunction) {
        // Remove context menus that may be showing
        $(".contextMenu").remove();

        var div = $("<ul class='contextMenu'></ul>");
        for (var i = 0; i < actions.length; i++) {
            var action = actions[i];
            var text = action.actionText;
            div.append("<li><a href='#" + action.actionId + "'>" + text + "</a></li>");
        }

        $("#main").append(div);

        var x = event.pageX;
        var y = event.pageY;
        $(div).css({left: x, top: y}).fadeIn(150);

        $(div).find('A').mouseover(
                function() {
                    $(div).find('LI.hover').removeClass('hover');
                    $(this).parent().addClass('hover');
                }).mouseout(function() {
                    $(div).find('LI.hover').removeClass('hover');
                });

        var getRidOfContextMenu = function() {
            $(div).remove();
            $(document).unbind("click", getRidOfContextMenu);
            return false;
        };

        // When items are selected
        $(div).find('A').unbind('click');
        $(div).find('LI:not(.disabled) A').click(function() {
            $(document).unbind('click', getRidOfContextMenu);
            $(".contextMenu").remove();

            var actionId = $(this).attr('href').substr(1);
            selectActionFunction(actionId);
            return false;
        });

        // Hide bindings
        setTimeout(function() { // Delay for Mozilla
            $(document).click(getRidOfContextMenu);
        }, 0);
    },

    // Choosing one action to resolve, for example required triggered actions
    actionChoiceDecision: function (decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var blueprintIds = this.getDecisionParameters(decision, "blueprintId");
        var actionIds = this.getDecisionParameters(decision, "actionId");
        var actionTexts = this.getDecisionParameters(decision, "actionText");

        var that = this;

        var selectedActionIds = new Array();

        this.cardActionDialog
                .html("<div id='arbitraryChoice'></div>")
                .dialog("option", "title", text);

        var cardIds = new Array();

        for (var i = 0; i < blueprintIds.length; i++) {
            var blueprintId = blueprintIds[i];

            cardIds.push("temp" + i);
            var card = new Card(blueprintId, "SPECIAL", "temp" + i, this.selfParticipantId);

            var cardDiv = this.createCardDiv(card, actionTexts[i]);

            $("#arbitraryChoice").append(cardDiv);
        }

        var finishChoice = function() {
            that.cardActionDialog.dialog("close");
            $("#arbitraryChoice").html("");
            that.clearSelection();
            that.decisionFunction(id, "" + selectedActionIds);
        };

        var resetChoice = function() {
            selectedActionIds = new Array();
            that.clearSelection();
            allowSelection();
            processButtons();
        };

        var processButtons = function() {
            var buttons = {};
            if (selectedActionIds.length > 0) {
                buttons["Clear selection"] = function() {
                    resetChoice();
                    processButtons();
                };
                buttons["Done"] = function() {
                    finishChoice();
                };
            }
            that.cardActionDialog.dialog("option", "buttons", buttons);
        };

        var allowSelection = function() {
            that.selectionFunction = function(cardId) {
                var actionId = actionIds[parseInt(cardId.substring(4))];
                selectedActionIds.push(actionId);

                that.clearSelection();

                if (this.settingsAutoAccept) {
                    finishChoice();
                } else {
                    processButtons();
                    $(".card:cardId(" + cardId + ")").addClass("selectedCard");
                }
            };

            that.attachSelectionFunctions(cardIds);
        };

        allowSelection();
        if (!this.replayMode)
            processButtons();

        this.cardActionDialog.dialog("open");
        this.arbitraryDialogResize(false);
    },

    // Choosing some number of cards, for example to character
    cardSelectionDecision: function(decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var min = this.getDecisionParameter(decision, "min");
        var max = this.getDecisionParameter(decision, "max");
        var cardIds = this.getDecisionParameters(decision, "cardId");

        var that = this;

        this.alertText.html(text);

        var selectedCardIds = new Array();

        var finishChoice = function() {
            that.alertText.html("");
            that.alertButtons.html("");
            that.clearSelection();
            that.decisionFunction(id, "" + selectedCardIds);
        };

        var resetChoice = function() {
            selectedCardIds = new Array();
            that.clearSelection();
            allowSelection();
            processButtons();
        };

        var processButtons = function() {
            that.alertButtons.html("");
            if (selectedCardIds.length > 0) {
                that.alertButtons.append("<button id='ClearSelection'>Reset choice</button>");
                $("#ClearSelection").button().click(function() {
                    resetChoice();
                });
            }
            if (selectedCardIds.length >= min) {
                that.alertButtons.append("<button id='Done'>Done</button>");
                $("#Done").button().click(function() {
                    finishChoice();
                });
            }
        };

        var allowSelection = function() {
            that.selectionFunction = function(cardId) {
                selectedCardIds.push(cardId);
                if (selectedCardIds.length == max) {
                    if (this.settingsAutoAccept) {
                        finishChoice();
                        return;
                    } else {
                        that.clearSelection();
                        $(".card:cardId(" + selectedCardIds + ")").addClass("selectedCard");
                    }
                } else {
                    $(".card:cardId(" + cardId + ")").removeClass("selectableCard").addClass("selectedCard");
                }

                processButtons();
            };

            that.attachSelectionFunctions(cardIds);
        };

        allowSelection();
        if (!this.replayMode)
            processButtons();
    },

    assignMinionsDecision: function(decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var freeCharacters = this.getDecisionParameters(decision, "freeCharacters");
        var minions = this.getDecisionParameters(decision, "minions");

        var that = this;

        this.alertText.html(text);
        if (!this.replayMode) {
            this.alertButtons.html("<button id='Done'>Done</button>");
            $("#Done").button().click(function() {
                that.alertText.html("");
                that.alertButtons.html("");
                that.clearSelection();

                var assignmentMap = {};
                for (var i = 0; i < freeCharacters.length; i++) {
                    assignmentMap[freeCharacters[i]] = freeCharacters[i];
                }

                $(".card:cardId(" + minions + ")").each(function() {
                    var card = $(this).data("card");
                    if (card.assign != null)
                        assignmentMap[card.assign] += " " + card.cardId;
                });

                var assignmentArray = new Array();
                for (var i = 0; i < freeCharacters.length; i++) {
                    assignmentArray.push(assignmentMap[freeCharacters[i]]);
                }

                that.decisionFunction(id, "" + assignmentArray);
            });
        }

        this.doAssignments(freeCharacters, minions);
    },

    unassignMinion: function(minionId) {
        var previousCharacterId = $(".card:cardId(" + minionId + ")").data("card").assign;
        delete $(".card:cardId(" + minionId + ")").data("card").assign;

        var characterHasMinion = false;
        $(".card").each(function () {
            if ($(this).data("card").assign == previousCharacterId) characterHasMinion = true;
        });

        if (!characterHasMinion) {
            delete this.shadowAssignGroups[previousCharacterId];
            delete this.freePeopleAssignGroups[previousCharacterId];

            this.assignGroupDivs[0].remove();
            this.assignGroupDivs.splice(0, 1);
        }
    },

    assignMinion: function(minionId, characterId) {
        if ($(".card:cardId(" + minionId + ")").data("card").assign != null)
            this.unassignMinion(minionId);

        var that = this;

        if (this.shadowAssignGroups[characterId] == null) {
            this.shadowAssignGroups[characterId] = new NormalCardGroup($("#main"), function (card) {
                return (card.zone == "SHADOW_CHARACTERS" && card.assign == characterId);
            }, false);
            this.freePeopleAssignGroups[characterId] = new NormalCardGroup($("#main"), function (card) {
                return (card.cardId == characterId);
            }, false);

            var newDiv = $("<div class='ui-widget-content'></div>");
            newDiv.css({"border-radius": "7px"});
            $("#main").append(newDiv);
            this.assignGroupDivs.push(newDiv);
        }

        $(".card:cardId(" + minionId + ")").data("card").assign = characterId;
    },

    doAssignments: function(freeCharacters, minions) {
        var that = this;
        this.selectionFunction = function(cardId) {
            that.clearSelection();

            that.selectionFunction = function(secondCardId) {
                that.clearSelection();
                if (cardId != secondCardId) {
                    that.assignMinion(cardId, secondCardId);
                } else {
                    that.unassignMinion(cardId);
                }
                that.layoutUI(false);
                that.doAssignments(freeCharacters, minions);
            };

            that.attachSelectionFunctions(freeCharacters);
            that.attachSelectionFunctions([cardId]);
        };

        this.attachSelectionFunctions(minions);
    },

    clearSelection: function() {
        $(".selectableCard").removeClass("selectableCard").data("action", null);
        $(".selectedCard").removeClass("selectedCard");
        this.selectionFunction = null;
    },

    dialogResize: function(dialog, group) {
        var width = dialog.width() + 10;
        var height = dialog.height() + 10;
        group.setBounds(this.padding, this.padding, width - 2 * this.padding, height - 2 * this.padding);
    },

    arbitraryDialogResize: function(texts) {
        if (texts) {
            var width = this.cardActionDialog.width() + 10;
            var height = this.cardActionDialog.height() - 10;
            this.specialGroup.setBounds(this.padding, this.padding, width - 2 * this.padding, height - 2 * this.padding);
        } else
            this.dialogResize(this.cardActionDialog, this.specialGroup);
    }
});
