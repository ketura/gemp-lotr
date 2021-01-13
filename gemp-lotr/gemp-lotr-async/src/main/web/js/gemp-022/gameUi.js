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
    adventureDeckDialogs: null,
    adventureDeckGroups: null,
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
    channelNumber: null,

    settingsFoilPresentation: "static",
    settingsAutoPass: false,
    settingsAutoAccept: false,
    settingsAlwaysDropDown: false,

    windowWidth: null,
    windowHeight: null,

    tabPane: null,

    animations: null,
    replayPlay: false,

    init: function (url, replayMode) {
        this.replayMode = replayMode;

        log("ui initialized");
        var that = this;

        this.animations = new GameAnimations(this);

        this.communication = new GempLotrCommunication(url,
            function (xhr, ajaxOptions, thrownError) {
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

        $.expr[':'].cardId = function (obj, index, meta, stack) {
            var cardIds = meta[3].split(",");
            var cardData = $(obj).data("card");
            return (cardData != null && ($.inArray(cardData.cardId, cardIds) > -1));
        };

        if (this.replayMode) {
            var replayDiv = $("<div class='replay' style='position:absolute'></div>");
            var slowerBut = $("<button id='slowerButton'>Slower</button>").button({
                icons: {primary: 'ui-icon-triangle-1-w'},
                text: false
            });
            var fasterBut = $("<button id='fasterButton'>Faster</button>").button({
                icons: {primary: 'ui-icon-triangle-1-e'},
                text: false
            });
            slowerBut.click(
                function () {
                    that.animations.replaySpeed = Math.min(2, that.animations.replaySpeed + 0.2);
                });
            fasterBut.click(
                function () {
                    that.animations.replaySpeed = Math.max(0.2, that.animations.replaySpeed - 0.2);
                });
            replayDiv.append(slowerBut);
            replayDiv.append(fasterBut);
            replayDiv.append("<br/>");

            var replayBut = $("<img id='replayButton' src='images/play.png' width='64' height='64'>").button();
            replayDiv.append(replayBut);

            $("#main").append(replayDiv);
            replayDiv.css({"z-index": 1000});
        }

        this.shadowAssignGroups = {};
        this.freePeopleAssignGroups = {};
        this.assignGroupDivs = new Array();

        this.discardPileDialogs = {};
        this.discardPileGroups = {};
        this.adventureDeckDialogs = {};
        this.adventureDeckGroups = {};
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

    getReorganizableCardGroupForCardData: function (cardData) {
        if (this.charactersPlayer.cardBelongs(cardData)) {
            return this.charactersPlayer;
        }
        if (this.charactersOpponent.cardBelongs(cardData)) {
            return this.charactersOpponent;
        }
        if (this.supportPlayer.cardBelongs(cardData)) {
            return this.supportPlayer;
        }
        if (this.supportOpponent.cardBelongs(cardData)) {
            return this.supportOpponent;
        }
        if (this.hand != null)
            if (this.hand.cardBelongs(cardData)) {
                return this.hand;
            }
        if (this.shadow.cardBelongs(cardData)) {
            return this.shadow;
        }

        if (this.skirmishFellowshipGroup.cardBelongs(cardData)) {
            return this.skirmishFellowshipGroup;
        }
        if (this.skirmishShadowGroup.cardBelongs(cardData)) {
            return this.skirmishShadowGroup;
        }

        for (var characterId in this.shadowAssignGroups) {
            if (this.shadowAssignGroups.hasOwnProperty(characterId)) {
                if (this.shadowAssignGroups[characterId].cardBelongs(cardData)) {
                    return this.shadowAssignGroups[characterId];
                }
                if (this.freePeopleAssignGroups[characterId].cardBelongs(cardData)) {
                    return this.freePeopleAssignGroups[characterId];
                }
            }
        }

        return null;
    },

    layoutGroupWithCard: function (cardId) {
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

    initializeGameUI: function () {
        this.advPathGroup = new AdvPathCardGroup($("#main"));

        var that = this;

        this.supportOpponent = new NormalCardGroup($("#main"), function (card) {
            return (card.zone == "SUPPORT" && card.owner != that.bottomPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        this.charactersOpponent = new NormalCardGroup($("#main"), function (card) {
            return (card.zone == "FREE_CHARACTERS" && card.owner != that.bottomPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        this.shadow = new NormalCardGroup($("#main"), function (card) {
            return (card.zone == "SHADOW_CHARACTERS" && card.assign == null && card.skirmish == null);
        });
        this.charactersPlayer = new NormalCardGroup($("#main"), function (card) {
            return (card.zone == "FREE_CHARACTERS" && card.owner == that.bottomPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        this.supportPlayer = new NormalCardGroup($("#main"), function (card) {
            return (card.zone == "SUPPORT" && card.owner == that.bottomPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        if (!this.spectatorMode) {
            this.hand = new NormalCardGroup($("#main"), function (card) {
                return (card.zone == "HAND") || (card.zone == "EXTRA");
            });
        }

        this.specialGroup = new NormalCardGroup(this.cardActionDialog, function (card) {
            return (card.zone == "SPECIAL");
        }, false);
        this.specialGroup.setBounds(this.padding, this.padding, 580 - 2 * (this.padding), 250 - 2 * (this.padding));

        this.gameStateElem = $("<div class='ui-widget-content'></div>");
        this.gameStateElem.css({"border-radius": "7px"});

        for (var i = 0; i < this.allPlayerIds.length; i++) {
            this.gameStateElem.append("<div class='player'>" + (i + 1) + ". " + this.allPlayerIds[i] + "<div id='clock" + i + "' class='clock'></div>"
                + "<div class='playerStats'><div id='deck" + i + "' class='deckSize'></div><div id='hand" + i + "' class='handSize'></div><div id='threats" + i + "' class='threatsSize'></div><div id='showStats" + i + "' class='showStats'></div><div id='discard" + i + "' class='discardSize'></div><div id='deadPile" + i + "' class='deadPileSize'></div><div id='adventureDeck" + i + "' class='adventureDeckSize'></div></div></div>");
        }

        this.gameStateElem.append("<div class='twilightPool'>0</div>");
        this.gameStateElem.append("<div class='phase'></div>");

        $("#main").append(this.gameStateElem);

        for (var i = 0; i < this.allPlayerIds.length; i++) {
            var showBut = $("<div class='slimButton'>+</div>").button().click(
                (function (playerIndex) {
                    return function () {
                        $(".player").each(
                            function (index) {
                                if (index == playerIndex) {
                                    if ($(this).hasClass("opened")) {
                                        $(this).removeClass("opened").css({width: 150 - that.padding});
                                        $("#discard" + playerIndex).css({display: "none"});
                                        $("#deadPile" + playerIndex).css({display: "none"});
                                        $("#adventureDeck" + playerIndex).css({display: "none"});
                                    } else {
                                        $(this).addClass("opened").css({width: 150 - that.padding + 126});
                                        $("#discard" + playerIndex).css({display: "table-cell"});
                                        $("#deadPile" + playerIndex).css({display: "table-cell"});
                                        $("#adventureDeck" + playerIndex).css({display: "table-cell"});
                                    }
                                }
                            });
                    };
                })(i));

            $("#showStats" + i).append(showBut);
        }

        if (!this.spectatorMode) {
            $("#discard" + this.getPlayerIndex(this.bottomPlayerId)).addClass("clickable").click(
                (function (index) {
                    return function () {
                        openSizeDialog(that.discardPileDialogs[that.bottomPlayerId]);
                    };
                })(i));
            $("#adventureDeck" + this.getPlayerIndex(this.bottomPlayerId)).addClass("clickable").click(
                (function (index) {
                    return function () {
                        openSizeDialog(that.adventureDeckDialogs[that.bottomPlayerId]);
                    };
                })(i));
        }

        for (var i = 0; i < this.allPlayerIds.length; i++) {
            $("#deadPile" + i).addClass("clickable").click(
                (function (index) {
                    return function () {
                        openSizeDialog(that.deadPileDialogs[that.allPlayerIds[index]]);
                    };
                })(i));
        }

        this.alertBox = $("<div class='ui-widget-content'></div>");
        this.alertBox.css({"border-radius": "7px"});

        this.alertText = $("<div></div>");
        this.alertText.css({
            position: "absolute",
            left: "0px",
            top: "0px",
            width: "100%",
            height: "50px",
            scroll: "auto"
        });

        this.alertButtons = $("<div class='alertButtons'></div>");
        this.alertButtons.css({
            position: "absolute",
            left: "0px",
            top: "50px",
            width: "100%",
            height: "30px",
            scroll: "auto"
        });

        this.alertBox.append(this.alertText);
        this.alertBox.append(this.alertButtons);

        $("#main").append(this.alertBox);

        this.statsDiv = $("<div class='ui-widget-content stats'></div>");
        this.statsDiv.css({"border-radius": "7px"});
        this.statsDiv.append("<div class='fpArchery'></div> <div class='shadowArchery'></div> <div class='move'></div>");
        $("#main").append(this.statsDiv);

        var dragFunc = function (event) {
            return that.dragContinuesCardFunction(event);
        };

        $("body").click(
            function (event) {
                return that.clickCardFunction(event);
            });
        $("body").mousedown(
            function (event) {
                $("body").bind("mousemove", dragFunc);
                return that.dragStartCardFunction(event);
            });
        $("body").mouseup(
            function (event) {
                $("body").unbind("mousemove", dragFunc);
                return that.dragStopCardFunction(event);
            });
    },

    addBottomLeftTabPane: function () {
        var that = this;
        var tabsLabels = "<li><a href='#chatBox' class='slimTab'>Chat</a></li>";
        var tabsBodies = "<div id='chatBox' class='slimPanel'></div>";
        if (!this.replayMode) {
            tabsLabels += "<li><a href='#settingsBox' class='slimTab'>Settings</a></li><li><a href='#gameOptionsBox' class='slimTab'>Options</a></li><li><a href='#playersInRoomBox' class='slimTab'>Players</a></li>";
            tabsBodies += "<div id='settingsBox' class='slimPanel'></div><div id='gameOptionsBox' class='slimPanel'></div><div id='playersInRoomBox' class='slimPanel'></div>";
        }
        var tabsStr = "<div id='bottomLeftTabs'><ul>" + tabsLabels + "</ul>" + tabsBodies + "</div>";

        this.tabPane = $(tabsStr).tabs();

        $("#main").append(this.tabPane);

        this.chatBoxDiv = $("#chatBox");

        var foilSelection = $("<select id='foilPresentation' style='font-size: 80%;'>" +
            "<option value='static'>Static layer</option>" +
            "<option value='animated'>Animated layer</option>" +
            "<option value='none'>None</option>" +
            "</select>");

        $("#settingsBox").append("Foil presentation: ");
        $("#settingsBox").append(foilSelection);
        $("#settingsBox").append("<br/>");

        var foilPresentation = $.cookie("foilPresentation");
        if (foilPresentation != null) {
            foilSelection.val(foilPresentation);
            this.settingsFoilPresentation = foilPresentation;
        }

        $("#foilPresentation").bind("change", function () {
            var value = "" + foilSelection.val();
            that.settingsFoilPresentation = value;
            $.cookie("foilPresentation", value, {expires: 365});
        });

        $("#settingsBox").append("<input id='autoAccept' type='checkbox' value='selected' /><label for='autoAccept'>Auto-accept after selecting action or card</label><br />");

        var autoAccept = $.cookie("autoAccept");
        if (autoAccept == "true" || autoAccept == null) {
            $("#autoAccept").prop("checked", true);
            this.settingsAutoAccept = true;
        }

        $("#autoAccept").bind("change", function () {
            var selected = $("#autoAccept").prop("checked");
            that.settingsAutoAccept = selected;
            $.cookie("autoAccept", "" + selected, {expires: 365});
        });

        $("#settingsBox").append("<input id='alwaysDropDown' type='checkbox' value='selected' /><label for='alwaysDropDown'>Always display drop-down in answer selection</label><br />");

        var alwaysDropDown = $.cookie("alwaysDropDown");
        if (alwaysDropDown == "true") {
            $("#alwaysDropDown").prop("checked", true);
            this.settingsAlwaysDropDown = true;
        }

        $("#alwaysDropDown").bind("change", function () {
            var selected = $("#alwaysDropDown").prop("checked");
            that.settingsAlwaysDropDown = selected;
            $.cookie("alwaysDropDown", "" + selected, {expires: 365});
        });

        $("#settingsBox").append("Phases when game auto-passes for you, if you have no phase actions to play<br />");
        $("#settingsBox").append("<input id='autoPassFELLOWSHIP' type='checkbox' value='selected' /><label for='autoPassFELLOWSHIP'>Fellowship</label> ");
        $("#settingsBox").append("<input id='autoPassSHADOW' type='checkbox' value='selected' /><label for='autoPassSHADOW'>Shadow</label> ");
        $("#settingsBox").append("<input id='autoPassMANEUVER' type='checkbox' value='selected' /><label for='autoPassMANEUVER'>Maneuver</label> ");
        $("#settingsBox").append("<input id='autoPassARCHERY' type='checkbox' value='selected' /><label for='autoPassARCHERY'>Archery</label> ");
        $("#settingsBox").append("<input id='autoPassASSIGNMENT' type='checkbox' value='selected' /><label for='autoPassASSIGNMENT'>Assignment</label> ");
        $("#settingsBox").append("<input id='autoPassSKIRMISH' type='checkbox' value='selected' /><label for='autoPassSKIRMISH'>Skirmish</label> ");
        $("#settingsBox").append("<input id='autoPassREGROUP' type='checkbox' value='selected' /><label for='autoPassREGROUP'>Regroup</label>");

        var autoPassPhases = $.cookie("autoPassPhases");
        if (autoPassPhases == null)
            autoPassPhases = "FELLOWSHIP0MANEUVER0ARCHERY0ASSIGNMENT0REGROUP";

        var passPhasesArr = autoPassPhases.split("0");
        for (var i = 0; i < passPhasesArr.length; i++) {
            $("#autoPass" + passPhasesArr[i]).prop("checked", true);
        }

        $("#autoPassFELLOWSHIP,#autoPassSHADOW,#autoPassMANEUVER,#autoPassARCHERY,#autoPassASSIGNMENT,#autoPassSKIRMISH,#autoPassREGROUP").bind("change", function () {
            var newAutoPassPhases = "";
            if ($("#autoPassFELLOWSHIP").prop("checked"))
                newAutoPassPhases += "0FELLOWSHIP";
            if ($("#autoPassSHADOW").prop("checked"))
                newAutoPassPhases += "0SHADOW";
            if ($("#autoPassMANEUVER").prop("checked"))
                newAutoPassPhases += "0MANEUVER";
            if ($("#autoPassARCHERY").prop("checked"))
                newAutoPassPhases += "0ARCHERY";
            if ($("#autoPassASSIGNMENT").prop("checked"))
                newAutoPassPhases += "0ASSIGNMENT";
            if ($("#autoPassSKIRMISH").prop("checked"))
                newAutoPassPhases += "0SKIRMISH";
            if ($("#autoPassREGROUP").prop("checked"))
                newAutoPassPhases += "0REGROUP";

            if (newAutoPassPhases.length > 0)
                newAutoPassPhases = newAutoPassPhases.substr(1);
            $.cookie("autoPassPhases", newAutoPassPhases, {expires: 365});
        });

        var playerListener = function (players) {
            var val = "";
            for (var i = 0; i < players.length; i++)
                val += players[i] + "<br/>";
            $("a[href='#playersInRoomBox']").html("Players(" + players.length + ")");
            $("#playersInRoomBox").html(val);
        };

        var chatRoomName = (this.replayMode ? null : ("Game" + getUrlParam("gameId")));
        this.chatBox = new ChatBoxUI(chatRoomName, $("#chatBox"), this.communication.url, false, playerListener, false, true);
        this.chatBox.chatUpdateInterval = 3000;

        if (!this.spectatorMode && !this.replayMode) {
            $("#gameOptionsBox").append("<button id='concedeGame'>Concede game</button><br/>");
            $("#concedeGame").button().click(
                function () {
                    that.communication.concede();
                });
            $("#gameOptionsBox").append("<button id='cancelGame'>Request game cancel</button>");
            $("#cancelGame").button().click(
                function () {
                    that.communication.cancel();
                });
        }
    },

    clickCardFunction: function (event) {
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
            var selectedCardElem = tar.closest(".card");
            if (!this.successfulDrag) {
                if (event.shiftKey || event.which > 1) {
                    this.displayCardInfo(selectedCardElem.data("card"));
                } else if ((selectedCardElem.hasClass("selectableCard") || selectedCardElem.hasClass("actionableCard")) && !this.replayMode)
                    this.selectionFunction(selectedCardElem.data("card").cardId, event);
                event.stopPropagation();
            }
            return false;
        }

        return true;
    },

    dragCardId: null,
    dragCardIndex: null,
    draggedCardIndex: null,
    dragStartX: null,
    dragStartY: null,
    successfulDrag: null,
    draggingHorizontaly: false,

    dragStartCardFunction: function (event) {
        this.successfulDrag = false;
        var tar = $(event.target);
        if (tar.hasClass("actionArea")) {
            var selectedCardElem = tar.closest(".card");
            if (event.which == 1) {
                var cardData = selectedCardElem.data("card");
                if (cardData) {
                    this.dragCardId = cardData.cardId;
                    this.dragStartX = event.clientX;
                    this.dragStartY = event.clientY;
                    return false;
                }
            }
        }
        return true;
    },

    dragContinuesCardFunction: function (event) {
        if (this.dragCardId != null) {
            if (!this.draggingHorizontaly && Math.abs(this.dragStartX - event.clientX) >= 20) {
                var cardElems = $(".card:cardId(" + this.dragCardId + ")");
                if (cardElems.length > 0) {
                    var cardElem = cardElems[0];
                    var cardData = $(cardElem).data("card");
                    this.draggingHorizontaly = true;
                    var cardGroup = this.getReorganizableCardGroupForCardData(cardData);
                    if (cardGroup != null) {
                        var cardsInGroup = cardGroup.getCardElems();
                        for (var i = 0; i < cardsInGroup.length; i++)
                            if (cardsInGroup[i].data("card").cardId == this.dragCardId) {
                                this.dragCardIndex = i;
                                this.draggedCardIndex = i;
                                break;
                            }
                    }
                }
            }
            if (this.draggingHorizontaly && this.dragCardId != null && this.dragCardIndex != null) {
                var cardElems = $(".card:cardId(" + this.dragCardId + ")");
                if (cardElems.length > 0) {
                    var cardElem = $(cardElems[0]);
                    var cardData = cardElem.data("card");
                    var cardGroup = this.getReorganizableCardGroupForCardData(cardData);
                    if (cardGroup != null) {
                        var cardsInGroup = cardGroup.getCardElems();
                        var width = cardElem.width();
                        var currentIndex;
                        if (event.clientX < this.dragStartX)
                            currentIndex = this.dragCardIndex - Math.floor((this.dragStartX - event.clientX) / width);
                        else
                            currentIndex = this.dragCardIndex + Math.floor((event.clientX - this.dragStartX) / width);

                        if (currentIndex < 0)
                            currentIndex = 0;
                        if (currentIndex >= cardsInGroup.length)
                            currentIndex = cardsInGroup.length - 1;

                        var cardIdAtIndex = $(cardsInGroup[currentIndex]).data("card").cardId;
                        if (cardIdAtIndex != this.dragCardId) {
                            //                            var sizeListeners = $(cardElem).data("sizeListeners");
                            if (currentIndex < this.draggedCardIndex)
                                $(".card:cardId(" + cardIdAtIndex + ")").before($(".card:cardId(" + this.dragCardId + ")"));
                            else
                                $(".card:cardId(" + cardIdAtIndex + ")").after($(".card:cardId(" + this.dragCardId + ")"));
                            //                            $(cardElem).data("card", cardData);
                            //                            $(cardElem).data("sizeListeners", sizeListeners);
                            cardGroup.layoutCards();
                            this.draggedCardIndex = currentIndex;
                        }
                    }
                }
            }
        }
    },

    dragStopCardFunction: function (event) {
        if (this.dragCardId != null) {
            if (this.dragStartY - event.clientY >= 20 && !this.draggingHorizontaly) {
                var cardElems = $(".card:cardId(" + this.dragCardId + ")");
                if (cardElems.length > 0) {
                    this.displayCardInfo($(cardElems[0]).data("card"));
                    this.successfulDrag = true;
                }
            }
            this.dragCardId = null;
            this.dragCardIndex = null;
            this.draggedCardIndex = null;
            this.dragStartX = null;
            this.dragStartY = null;
            this.draggingHorizontaly = false;
            return false;
        }
        return true;
    },

    displayCard: function (card, extraSpace) {
        this.infoDialog.html("");
        this.infoDialog.html("<div style='scroll: auto'></div>");
        var floatCardDiv = $("<div style='float: left;'></div>");
        floatCardDiv.append(createFullCardDiv(card.imageUrl, card.foil, card.horizontal));
        this.infoDialog.append(floatCardDiv);
        if (extraSpace)
            this.infoDialog.append("<div id='cardEffects'></div>");

        var windowWidth = $(window).width();
        var windowHeight = $(window).height();

        var horSpace = (extraSpace ? 200 : 0) + 30;
        var vertSpace = 45;

        if (card.horizontal) {
            // 500x360
            this.infoDialog.dialog({
                width: Math.min(500 + horSpace, windowWidth),
                height: Math.min(360 + vertSpace, windowHeight)
            });
        } else {
            // 360x500
            this.infoDialog.dialog({
                width: Math.min(360 + horSpace, windowWidth),
                height: Math.min(500 + vertSpace, windowHeight)
            });
        }
        this.infoDialog.dialog("open");
    },

    displayCardInfo: function (card) {
        var showModifiers = false;
        var cardId = card.cardId;
        if (!this.replayMode && (cardId.length < 4 || cardId.substring(0, 4) != "temp"))
            showModifiers = true;

        this.displayCard(card, showModifiers);

        if (showModifiers)
            this.getCardModifiersFunction(cardId, this.setCardModifiers);
    },

    setCardModifiers: function (html) {
        $("#cardEffects").replaceWith(html);
    },

    initializeDialogs: function () {
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

        this.cardActionDialog.bind("dialogresize", function () {
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
                that.infoDialog.prop({scrollTop: that.infoDialog.prop("scrollHeight")});
                return false;
            },
            swipeDown: function (event) {
                that.infoDialog.prop({scrollTop: 0});
                return false;
            }
        };
        this.infoDialog.swipe(swipeOptions);
    },

    windowResized: function () {
        this.animations.windowResized();
    },

    layoutUI: function (sizeChanged) {
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
            this.statsDiv.css({
                position: "absolute",
                left: padding + "px",
                top: height - (padding * 2) - chatHeight - 34 + "px",
                width: advPathWidth - 4,
                height: 30
            });

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
                this.skirmishGroupDiv.css({
                    left: x + "px",
                    top: y + "px",
                    width: groupWidth,
                    height: groupHeight,
                    position: "absolute"
                });
                var strengthBoxSize = 40;
                var dmgBoxSize = 30;
                if (currentPlayerTurn) {
                    this.skirmishShadowGroup.setBounds(x + 3, y + 3, groupWidth - 6, heightScales[2] * heightPerScale - 6);
                    this.skirmishFellowshipGroup.setBounds(x + 3, y + heightScales[2] * heightPerScale + padding + 3, groupWidth - 6, heightScales[3] * heightPerScale - 6);
                    this.fpStrengthDiv.css({
                        position: "absolute",
                        left: groupWidth - strengthBoxSize - 2 + "px",
                        top: groupHeight - strengthBoxSize - 2 + "px",
                        width: strengthBoxSize,
                        height: strengthBoxSize,
                        "z-index": 50
                    });
                    this.fpDamageBonusDiv.css({
                        position: "absolute",
                        left: groupWidth - strengthBoxSize - dmgBoxSize - 2 + "px",
                        top: groupHeight - dmgBoxSize - 2 + "px",
                        width: dmgBoxSize,
                        height: dmgBoxSize,
                        "z-index": 50
                    });
                    this.shadowStrengthDiv.css({
                        position: "absolute",
                        left: groupWidth - strengthBoxSize - 2 + "px",
                        top: 2 + "px",
                        width: strengthBoxSize,
                        height: strengthBoxSize,
                        "z-index": 50
                    });
                    this.shadowDamageBonusDiv.css({
                        position: "absolute",
                        left: groupWidth - strengthBoxSize - dmgBoxSize - 2 + "px",
                        top: 2 + "px",
                        width: dmgBoxSize,
                        height: dmgBoxSize,
                        "z-index": 50
                    });
                } else {
                    this.skirmishFellowshipGroup.setBounds(x + 3, y + 3, groupWidth - 6, heightScales[1] * heightPerScale - 6);
                    this.skirmishShadowGroup.setBounds(x + 3, y + heightScales[1] * heightPerScale + padding + 3, groupWidth - 6, heightScales[2] * heightPerScale - 6);
                    this.shadowStrengthDiv.css({
                        position: "absolute",
                        left: groupWidth - strengthBoxSize - 2 + "px",
                        top: groupHeight - strengthBoxSize - 2 + "px",
                        width: strengthBoxSize,
                        height: strengthBoxSize,
                        "z-index": 50
                    });
                    this.shadowDamageBonusDiv.css({
                        position: "absolute",
                        left: groupWidth - strengthBoxSize - dmgBoxSize - 2 + "px",
                        top: groupHeight - dmgBoxSize - 2 + "px",
                        width: dmgBoxSize,
                        height: dmgBoxSize,
                        "z-index": 50
                    });
                    this.fpStrengthDiv.css({
                        position: "absolute",
                        left: groupWidth - strengthBoxSize - 2 + "px",
                        top: 2 + "px",
                        width: strengthBoxSize,
                        height: strengthBoxSize,
                        "z-index": 50
                    });
                    this.fpDamageBonusDiv.css({
                        position: "absolute",
                        left: groupWidth - strengthBoxSize - dmgBoxSize - 2 + "px",
                        top: 2 + "px",
                        width: dmgBoxSize,
                        height: dmgBoxSize,
                        "z-index": 50
                    });
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
                    this.assignGroupDivs[assignIndex].css({
                        left: x + "px",
                        top: y + "px",
                        width: groupWidth,
                        height: groupHeight,
                        position: "absolute"
                    });
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

            this.gameStateElem.css({
                position: "absolute",
                left: padding * 2 + advPathWidth,
                top: padding,
                width: specialUiWidth - padding,
                height: height - padding * 4 - alertHeight - chatHeight
            });
            this.alertBox.css({
                position: "absolute",
                left: padding * 2 + advPathWidth,
                top: height - (padding * 2) - alertHeight - chatHeight,
                width: specialUiWidth - padding,
                height: alertHeight
            });


            for (var playerId in this.discardPileGroups)
                if (this.discardPileGroups.hasOwnProperty(playerId))
                    this.discardPileGroups[playerId].layoutCards();

            for (var playerId in this.adventureDeckGroups)
                if (this.adventureDeckGroups.hasOwnProperty(playerId))
                    this.adventureDeckGroups[playerId].layoutCards();

            for (var playerId in this.deadPileGroups)
                if (this.deadPileGroups.hasOwnProperty(playerId))
                    this.deadPileGroups[playerId].layoutCards();
        }
        this.tabPane.css({
            position: "absolute",
            left: padding,
            top: height - padding - chatHeight,
            width: specialUiWidth + advPathWidth - padding,
            height: chatHeight - padding
        });
        this.chatBox.setBounds(4, 4 + 25, specialUiWidth + advPathWidth - 8, chatHeight - 8 - 25);

        if (this.replayMode) {
            $(".replay").css({
                position: "absolute",
                left: width - 66 - 4 - padding,
                top: height - 97 - 2 - padding,
                width: 66,
                height: 97,
                "z-index": 1000
            });
        }
    },

    startReplaySession: function (replayId) {
        var that = this;
        this.communication.getReplay(replayId,
            function (xml) {
                that.processXmlReplay(xml, true);
            });
    },

    startGameSession: function () {
        var that = this;
        this.communication.startGameSession(
            function (xml) {
                that.processXml(xml, false);
            }, this.gameErrorMap());
    },

    updateGameState: function () {
        var that = this;
        this.communication.updateGameState(
            this.channelNumber,
            function (xml) {
                that.processXml(xml, true);
            }, this.gameErrorMap());
    },

    decisionFunction: function (decisionId, result) {
        var that = this;
        this.stopAnimatingTitle();
        this.communication.gameDecisionMade(decisionId, result,
            this.channelNumber,
            function (xml) {
                that.processXml(xml, true);
            }, this.gameErrorMap());
    },

    gameErrorMap: function () {
        var that = this;
        return {
            "0": function () {
                that.showErrorDialog("Server connection error", "Unable to connect to server. Either server is down or there is a problem with your internet connection.", true, false, false);
            },
            "401": function () {
                that.showErrorDialog("Authentication error", "You are not logged in", false, true, false);
            },
            "403": function () {
                that.showErrorDialog("Game access forbidden", "This game is private and does not allow spectators.", false, false, true);
            },
            "409": function () {
                that.showErrorDialog("Concurrent access error", "You are observing this Game Hall from another browser or window. Close this window or if you wish to observe it here, click \"Refresh page\".", true, false, false);
            },
            "410": function () {
                that.showErrorDialog("Inactivity error", "You were inactive for too long and have been removed from observing this game. If you wish to start again, click \"Refresh page\".", true, false, false);
            }
        };
    },

    showErrorDialog: function (title, text, reloadButton, mainPageButton, gameHallButton) {
        var buttons = {};
        if (reloadButton) {
            buttons["Refresh page"] =
                function () {
                    location.reload(true);
                };
        }
        if (mainPageButton) {
            buttons["Go to main page"] =
                function () {
                    location.href = "/gemp-lotr/";
                };
        }
        if (gameHallButton) {
            buttons["Go to Game Hall"] =
                function () {
                    location.href = "/gemp-lotr/hall.html";
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

    getCardModifiersFunction: function (cardId, func) {
        var that = this;
        this.communication.getGameCardModifiers(cardId,
            function (html) {
                that.setCardModifiers(html);
            });
    },

    processXml: function (xml, animate) {
        log(xml);
        var root = xml.documentElement;
        if (root.tagName == 'gameState' || root.tagName == 'update')
            this.processGameEventsXml(root, animate);
    },

    replayGameEventNextIndex: 0,
    replayGameEvents: null,

    processXmlReplay: function (xml, animate) {
        var that = this;
        log(xml);
        var root = xml.documentElement;
        if (root.tagName == 'gameReplay') {
            this.replayGameEvents = root.getElementsByTagName("ge");
            this.replayGameEventNextIndex = 0;

            $("#replayButton").click(
                function () {
                    if (that.replayPlay) {
                        that.replayPlay = false;
                        $("#replayButton").attr("src", "images/play.png");
                    } else {
                        that.replayPlay = true;
                        $("#replayButton").attr("src", "images/pause.png");
                        that.playNextReplayEvent();
                    }
                });

            this.playNextReplayEvent();
        }
    },

    shouldPlay: function () {
        return this.replayPlay;
    },

    playNextReplayEvent: function () {
        if (this.shouldPlay()) {
            var that = this;
            if (this.replayGameEventNextIndex < this.replayGameEvents.length) {
                $("#main").queue(
                    function (next) {
                        that.cleanupDecision();
                        next();
                    });
                var gameEvent = this.replayGameEvents[this.replayGameEventNextIndex];
                this.processGameEvent(gameEvent, true);

                this.replayGameEventNextIndex++;

                $("#main").queue(
                    function (next) {
                        that.playNextReplayEvent();
                        next();
                    });
            }
        }
    },

    processGameEvent: function (gameEvent, animate) {
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
        } else if (eventType == "ATS") {
            this.animations.addToSkirmish(gameEvent, animate);
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
            this.animations.processDecision(gameEvent, animate);
        }
    },

    processGameEventsXml: function (element, animate) {
        try {
            this.channelNumber = element.getAttribute("cn");

            var gameEvents = element.getElementsByTagName("ge");

            var hasDecision = false;

            // Go through all the events
            for (var i = 0; i < gameEvents.length; i++) {
                var gameEvent = gameEvents[i];
                this.processGameEvent(gameEvent, animate);
                var eventType = gameEvent.getAttribute("type");
                if (eventType == "D")
                    hasDecision = true;
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
                        var hours = Math.floor(value / 3600);
                        var minutes = Math.floor(value / 60) % 60;
                        var seconds = value % 60;

                        if (hours > 0)
                            $("#clock" + index).text(sign + hours + ":" + ((minutes < 10) ? ("0" + minutes) : minutes) + ":" + ((seconds < 10) ? ("0" + seconds) : seconds));
                        else
                            $("#clock" + index).text(sign + minutes + ":" + ((seconds < 10) ? ("0" + seconds) : seconds));
                    }
                }
            }

            if (!hasDecision) {
                this.animations.updateGameState(animate);
            } else {
                this.startAnimatingTitle();
            }
        } catch (e) {
            this.showErrorDialog("Game error", "There was an error while processing game events in your browser. Reload the game to continue", true, false, false);
        }
    },

    keepAnimating: false,

    startAnimatingTitle: function () {
        var that = this;
        this.keepAnimating = true;
        setTimeout(function () {
            that.setDecisionTitle();
        }, 500);
    },

    stopAnimatingTitle: function () {
        this.keepAnimating = false;
        window.document.title = "Game of Gemp-LotR";
    },

    setDecisionTitle: function () {
        if (this.keepAnimating) {
            window.document.title = "Waiting for your decision";
            var that = this;
            setTimeout(function () {
                that.setNormalTitle();
            }, 500);
        }
    },

    setNormalTitle: function () {
        if (this.keepAnimating) {
            window.document.title = "Game of Gemp-LotR";
            var that = this;
            setTimeout(function () {
                that.setDecisionTitle();
            }, 500);
        }
    },

    getPlayerIndex: function (playerId) {
        for (var plId = 0; plId < this.allPlayerIds.length; plId++)
            if (this.allPlayerIds[plId] == playerId)
                return plId;
        return -1;
    },

    layoutZones: function () {
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

    participant: function (element) {
        var participantId = element.getAttribute("participantId");
        this.allPlayerIds = element.getAttribute("allParticipantIds").split(",");

        this.bottomPlayerId = participantId;

        var that = this;

        var index = this.getPlayerIndex(this.bottomPlayerId);
        if (index == -1) {
            this.bottomPlayerId = this.allPlayerIds[1];
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
            this.discardPileGroups[participantId] = new NormalCardGroup(discardPileDialog, function (card) {
                return true;
            }, false);

            this.discardPileGroups[participantId].setBounds(this.padding, this.padding, 580 - 2 * (this.padding), 250 - 2 * (this.padding));

            discardPileDialog.bind("dialogresize", function () {
                that.dialogResize(discardPileDialog, that.discardPileGroups[participantId]);
            });

            var adventureDeckDialog = $("<div></div>").dialog({
                autoOpen: false,
                closeOnEscape: true,
                resizable: true,
                title: "Adventure deck - " + participantId,
                minHeight: 80,
                minWidth: 200,
                width: 600,
                height: 300
            });

            this.adventureDeckDialogs[participantId] = adventureDeckDialog;
            this.adventureDeckGroups[participantId] = new NormalCardGroup(adventureDeckDialog, function (card) {
                return true;
            }, false);

            this.adventureDeckGroups[participantId].setBounds(this.padding, this.padding, 580 - 2 * (this.padding), 250 - 2 * (this.padding));

            adventureDeckDialog.bind("dialogresize", function () {
                that.dialogResize(adventureDeckDialog, that.adventureDeckGroups[participantId]);
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
            this.deadPileGroups[this.allPlayerIds[i]] = new NormalCardGroup(deadPileDialog, function (card) {
                return true;
            }, false);

            this.deadPileGroups[this.allPlayerIds[i]].setBounds(this.padding, this.padding, 580 - 2 * (this.padding), 250 - 2 * (this.padding));

            deadPileDialog.bind("dialogresize", (function (dialog, index) {
                return function () {
                    that.dialogResize(dialog, that.deadPileGroups[that.allPlayerIds[index]]);
                }
            })(deadPileDialog, i));
        }

        this.initializeGameUI();
        this.layoutUI(true);
    },

    getDecisionParameter: function (decision, name) {
        var parameters = decision.getElementsByTagName("parameter");
        for (var i = 0; i < parameters.length; i++)
            if (parameters[i].getAttribute("name") == name)
                return parameters[i].getAttribute("value");

        return null;
    },

    getDecisionParameters: function (decision, name) {
        var result = new Array();
        var parameters = decision.getElementsByTagName("parameter");
        for (var i = 0; i < parameters.length; i++)
            if (parameters[i].getAttribute("name") == name)
                result.push(parameters[i].getAttribute("value"));

        return result;
    },

    cleanupDecision: function () {
        this.smallDialog.dialog("close");
        this.cardActionDialog.dialog("close");
        this.clearSelection();
        if (this.alertText != null)
            this.alertText.html("");
        if (this.alertButtons != null)
            this.alertButtons.html("");
        // ****CCG League****: Border around alert box
        if (this.alertBox != null)
            this.alertBox.css({"border-radius": "7px", "border-color": ""});

        $(".card").each(
            function () {
                var card = $(this).data("card");
                if (card.zone == "EXTRA")
                    $(this).remove();
            });
        if (this.hand != null)
            this.hand.layoutCards();
    },

    integerDecision: function (decision) {
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
                    "OK": function () {
                        $(this).dialog("close");
                        that.decisionFunction(id, $("#integerDecision").val());
                    }
                });
        }

        $("#integerDecision").SpinnerControl({
            type: 'range',
            typedata: {
                min: parseInt(min),
                max: parseInt(max),
                interval: 1,
                decimalplaces: 0
            },
            defaultVal: val,
            width: '50px',
            backColor: "#000000"
        });

        this.smallDialog.dialog("open");
        $('.ui-dialog :button').blur();
    },

    multipleChoiceDecision: function (decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var results = this.getDecisionParameters(decision, "results");

        var that = this;
        this.smallDialog
            .html(text);

        if (results.length > 2 || this.settingsAlwaysDropDown) {
            var html = "<br /><select id='multipleChoiceDecision' selectedIndex='0'>";
            for (var i = 0; i < results.length; i++)
                html += "<option value='" + i + "'>" + results[i] + "</option>";
            html += "</select>";
            this.smallDialog.append(html);

            if (!this.replayMode) {
                this.smallDialog.dialog("option", "buttons",
                    {
                        "OK": function () {
                            that.smallDialog.dialog("close");
                            that.decisionFunction(id, $("#multipleChoiceDecision").val());
                        }
                    });
            }
        } else {
            this.smallDialog.append("<br />");
            for (var i = 0; i < results.length; i++) {
                if (i > 0)
                    this.smallDialog.append(" ");

                var but = $("<button></button>").html(results[i]).button();
                if (!this.replayMode) {
                    but.click(
                        (function (ind) {
                            return function () {
                                that.smallDialog.dialog("close");
                                that.decisionFunction(id, "" + ind);
                            }
                        })(i));
                }
                this.smallDialog.append(but);
            }
            if (!this.replayMode)
                this.smallDialog.dialog("option", "buttons", {});
        }

        this.smallDialog.dialog("open");
        $('.ui-dialog :button').blur();
    },

    ensureCardHasBoxes: function (cardDiv) {
        if ($(".cardStrength", cardDiv).length == 0) {
            var tokenOverlay = $(".tokenOverlay", cardDiv);

            var cardStrengthBgDiv = $("<div class='cardStrengthBg'><img src='images/o_icon_strength.png' width='100%' height='100%'></div>");
            tokenOverlay.append(cardStrengthBgDiv);

            var cardStrengthDiv = $("<div class='cardStrength'></div>");
            tokenOverlay.append(cardStrengthDiv);

            var cardVitalityBgDiv = $("<div class='cardVitalityBg'><img src='images/o_icon_vitality.png' width='100%' height='100%'></div>");
            tokenOverlay.append(cardVitalityBgDiv);

            var cardVitalityDiv = $("<div class='cardVitality'></div>");
            tokenOverlay.append(cardVitalityDiv);

            var cardSiteNumberBgDiv = $("<div class='cardSiteNumberBg'><img src='images/o_icon_compass.png' width='100%' height='100%'></div>");
            cardSiteNumberBgDiv.css({display: "none"});
            tokenOverlay.append(cardSiteNumberBgDiv);

            var cardSiteNumberDiv = $("<div class='cardSiteNumber'></div>");
            cardSiteNumberDiv.css({display: "none"});
            tokenOverlay.append(cardSiteNumberDiv);

            var cardResistanceBgDiv = $("<div class='cardResistanceBg'><img src='images/o_icon_resistance.png' width='100%' height='100%'></div>");
            cardResistanceBgDiv.css({display: "none"});
            tokenOverlay.append(cardResistanceBgDiv);

            var cardResistanceDiv = $("<div class='cardResistance'></div>");
            cardResistanceDiv.css({display: "none"});
            tokenOverlay.append(cardResistanceDiv);

            var sizeListeners = new Array();
            sizeListeners[0] = {
                sizeChanged: function (cardElem, width, height) {
                    var maxDimension = Math.max(width, height);

                    var size = 0.0865 * maxDimension;

                    var x = 0.09 * maxDimension - size / 2;
                    var strengthY = 0.688 * maxDimension - size / 2;
                    var vitalityY = 0.800 * maxDimension - size / 2;
                    var minionSiteNumberY = 0.905 * maxDimension - size / 2;

                    var fontPerc = (size * 5.5) + "%";
                    var borderRadius = Math.ceil(size / 5) + "px";

                    var strBgX = 0.03800 * maxDimension;
                    var strBgY = 0.60765 * maxDimension;
                    var strBgWidth = 0.1624 * width;
                    var strBgHeight = 0.1650 * height;

                    var vitBgX = 0.0532 * width;
                    var vitBgY = 0.7465 * height;
                    var vitalityBgSize = 0.105 * height;

                    var thirdBoxX = 0.0532 * width;
                    var thirdBoxY = 0.845 * height;
                    var thirdBoxSize = 0.115 * height;

                    $(".cardStrengthBg", cardElem).css({
                        position: "absolute",
                        left: strBgX + "px",
                        top: strBgY + "px",
                        width: strBgWidth,
                        height: strBgHeight
                    });
                    $(".cardStrength", cardElem).css({
                        position: "absolute",
                        "font-size": fontPerc,
                        left: x + "px",
                        top: strengthY + "px",
                        width: size,
                        height: size
                    });
                    $(".cardVitalityBg", cardElem).css({
                        position: "absolute",
                        left: vitBgX + "px",
                        top: vitBgY + "px",
                        width: vitalityBgSize,
                        height: vitalityBgSize
                    });
                    $(".cardVitality", cardElem).css({
                        position: "absolute",
                        "font-size": fontPerc,
                        left: x + "px",
                        top: vitalityY + "px",
                        width: size,
                        height: size
                    });
                    $(".cardSiteNumberBg", cardElem).css({
                        position: "absolute",
                        left: thirdBoxX + "px",
                        top: thirdBoxY + "px",
                        width: thirdBoxSize,
                        height: thirdBoxSize
                    });
                    $(".cardSiteNumber", cardElem).css({
                        position: "absolute",
                        "border-radius": borderRadius,
                        "font-size": fontPerc,
                        left: x + "px",
                        top: minionSiteNumberY + "px",
                        width: size,
                        height: size
                    });
                    $(".cardResistanceBg", cardElem).css({
                        position: "absolute",
                        left: thirdBoxX + "px",
                        top: thirdBoxY + "px",
                        width: thirdBoxSize,
                        height: thirdBoxSize
                    });
                    $(".cardResistance", cardElem).css({
                        position: "absolute",
                        "border-radius": borderRadius,
                        "font-size": fontPerc,
                        left: x + "px",
                        top: minionSiteNumberY + "px",
                        width: size,
                        height: size
                    });
                }
            };

            cardDiv.data("sizeListeners", sizeListeners);
            sizeListeners[0].sizeChanged(cardDiv, $(cardDiv).width(), $(cardDiv).height());
        }
    },

    createCardDiv: function (card, text) {
        var cardDiv = createCardDiv(card.imageUrl, text, card.isFoil(), true, false, card.hasErrata());

        cardDiv.data("card", card);

        var that = this;
        var swipeOptions = {
            threshold: 20,
            fallbackToMouseEvents: false,
            swipeUp: function (event) {
                var tar = $(event.target);
                if (tar.hasClass("actionArea")) {
                    var selectedCardElem = tar.closest(".card");
                    that.displayCardInfo(selectedCardElem.data("card"));
                }
                return false;
            },
            click: function (event) {
                return that.clickCardFunction(event);
            }
        };
        cardDiv.swipe(swipeOptions);

        return cardDiv;
    },

    attachSelectionFunctions: function (cardIds, selection) {
        if (selection) {
            if (cardIds.length > 0)
                $(".card:cardId(" + cardIds + ")").addClass("selectableCard");
        } else {
            if (cardIds.length > 0)
                $(".card:cardId(" + cardIds + ")").addClass("actionableCard");
        }
    },

    // Choosing cards from a predefined selection (for example stating fellowship)
    arbitraryCardsDecision: function (decision) {
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

            var card = new Card(blueprintId, "SPECIAL", cardId, null);

            var cardDiv = this.createCardDiv(card);

            $("#arbitraryChoice").append(cardDiv);
        }

        var finishChoice = function () {
            that.cardActionDialog.dialog("close");
            $("#arbitraryChoice").html("");
            that.clearSelection();
            that.decisionFunction(id, "" + selectedCardIds);
        };

        var resetChoice = function () {
            selectedCardIds = new Array();
            that.clearSelection();
            allowSelection();
            processButtons();
        };

        var processButtons = function () {
            var buttons = {};
            if (selectedCardIds.length > 0)
                buttons["Clear selection"] = function () {
                    resetChoice();
                    processButtons();
                };
            if (selectedCardIds.length >= min)
                buttons["Done"] = function () {
                    finishChoice();
                };
            that.cardActionDialog.dialog("option", "buttons", buttons);
        };

        var allowSelection = function () {
            that.selectionFunction = function (cardId) {
                selectedCardIds.push(cardId);

                if (selectedCardIds.length == max) {
                    if (that.settingsAutoAccept) {
                        finishChoice();
                        return;
                    } else {
                        that.clearSelection();
                        if (selectedCardIds.length > 0)
                            $(".card:cardId(" + selectedCardIds + ")").addClass("selectedCard");
                    }
                } else {
                    $(".card:cardId(" + cardId + ")").removeClass("selectableCard").addClass("selectedCard");
                }

                processButtons();
            };

            that.attachSelectionFunctions(selectableCardIds, true);
        };

        allowSelection();
        if (!this.replayMode)
            processButtons();

        openSizeDialog(this.cardActionDialog);
        this.arbitraryDialogResize(false);
        $('.ui-dialog :button').blur();
    },

    // Choosing one action to resolve, for example phase actions
    cardActionChoiceDecision: function (decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var cardIds = this.getDecisionParameters(decision, "cardId");
        var blueprintIds = this.getDecisionParameters(decision, "blueprintId");
        var actionIds = this.getDecisionParameters(decision, "actionId");
        var actionTexts = this.getDecisionParameters(decision, "actionText");

        var that = this;

        if (cardIds.length == 0 && this.settingsAutoPass && !this.replayMode) {
            that.decisionFunction(id, "");
            return;
        }

        var selectedCardIds = new Array();

        this.alertText.html(text);
        // ****CCG League****: Border around alert box
        this.alertBox.css({"border-radius": "7px", "border-color": "#7f7fff", "border-width": "2px"});

        var processButtons = function () {
            that.alertButtons.html("");
            if (selectedCardIds.length == 0) {
                that.alertButtons.append("<button id='Pass'>Pass</button>");
                $("#Pass").button().click(function () {
                    finishChoice();
                });
            }
            if (selectedCardIds.length > 0) {
                that.alertButtons.append("<button id='ClearSelection'>Reset choice</button>");
                that.alertButtons.append("<button id='Done' style='float: right'>Done</button>");
                $("#Done").button().click(function () {
                    finishChoice();
                });
                $("#ClearSelection").button().click(function () {
                    resetChoice();
                });
            }
        };

        var finishChoice = function () {
            that.alertText.html("");
            // ****CCG League****: Border around alert box
            that.alertBox.css({"border-radius": "7px", "border-color": "", "border-width": "1px"});
            that.alertButtons.html("");
            that.clearSelection();
            $(".card").each(
                function () {
                    var card = $(this).data("card");
                    if (card.zone == "EXTRA")
                        $(this).remove();
                });
            that.hand.layoutCards();
            that.decisionFunction(id, "" + selectedCardIds);
        };

        var resetChoice = function () {
            selectedCardIds = new Array();
            that.clearSelection();
            allowSelection();
            processButtons();
        };

        var allowSelection = function () {
            var hasVirtual = false;

            for (var i = 0; i < cardIds.length; i++) {
                var cardId = cardIds[i];
                var actionId = actionIds[i];
                var actionText = actionTexts[i];
                var blueprintId = blueprintIds[i];

                if (blueprintId == "inPlay") {
                    var cardIdElem = $(".card:cardId(" + cardId + ")");
                    if (cardIdElem.data("action") == null) {
                        cardIdElem.data("action", new Array());
                    }

                    var actions = cardIdElem.data("action");
                    actions.push({actionId: actionId, actionText: actionText});
                } else {
                    hasVirtual = true;
                    cardIds[i] = "extra" + cardId;
                    var card = new Card(blueprintId, "EXTRA", "extra" + cardId, null);

                    var cardDiv = that.createCardDiv(card);
                    $(cardDiv).css({opacity: "0.8"});

                    $("#main").append(cardDiv);

                    var cardIdElem = $(".card:cardId(extra" + cardId + ")");
                    if (cardIdElem.data("action") == null) {
                        cardIdElem.data("action", new Array());
                    }

                    var actions = cardIdElem.data("action");
                    actions.push({actionId: actionId, actionText: actionText});
                }
            }

            if (hasVirtual) {
                that.hand.layoutCards();
            }

            that.selectionFunction = function (cardId, event) {
                var cardIdElem = $(".card:cardId(" + cardId + ")");
                var actions = cardIdElem.data("action");

                var selectActionFunction = function (actionId) {
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

            that.attachSelectionFunctions(cardIds, false);
        };

        allowSelection();
        if (!this.replayMode)
            processButtons();

        $(':button').blur();
    },

    createActionChoiceContextMenu: function (actions, event, selectActionFunction) {
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
            function () {
                $(div).find('LI.hover').removeClass('hover');
                $(this).parent().addClass('hover');
            }).mouseout(function () {
            $(div).find('LI.hover').removeClass('hover');
        });

        var getRidOfContextMenu = function () {
            $(div).remove();
            $(document).unbind("click", getRidOfContextMenu);
            return false;
        };

        // When items are selected
        $(div).find('A').unbind('click');
        $(div).find('LI:not(.disabled) A').click(function () {
            $(document).unbind('click', getRidOfContextMenu);
            $(".contextMenu").remove();

            var actionId = $(this).attr('href').substr(1);
            selectActionFunction(actionId);
            return false;
        });

        // Hide bindings
        setTimeout(function () { // Delay for Mozilla
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
            var card = new Card(blueprintId, "SPECIAL", "temp" + i, null);

            var cardDiv = this.createCardDiv(card, actionTexts[i]);

            $("#arbitraryChoice").append(cardDiv);
        }

        var finishChoice = function () {
            that.cardActionDialog.dialog("close");
            $("#arbitraryChoice").html("");
            that.clearSelection();
            that.decisionFunction(id, "" + selectedActionIds);
        };

        var resetChoice = function () {
            selectedActionIds = new Array();
            that.clearSelection();
            allowSelection();
            processButtons();
        };

        var processButtons = function () {
            var buttons = {};
            if (selectedActionIds.length > 0) {
                buttons["Clear selection"] = function () {
                    resetChoice();
                    processButtons();
                };
                buttons["Done"] = function () {
                    finishChoice();
                };
            }
            that.cardActionDialog.dialog("option", "buttons", buttons);
        };

        var allowSelection = function () {
            that.selectionFunction = function (cardId) {
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

            that.attachSelectionFunctions(cardIds, true);
        };

        allowSelection();
        if (!this.replayMode)
            processButtons();

        openSizeDialog(this.cardActionDialog);
        this.arbitraryDialogResize(false);
        $('.ui-dialog :button').blur();
    },

    // Choosing some number of cards, for example to wound
    cardSelectionDecision: function (decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var min = this.getDecisionParameter(decision, "min");
        var max = this.getDecisionParameter(decision, "max");
        var cardIds = this.getDecisionParameters(decision, "cardId");

        var that = this;

        this.alertText.html(text);
        // ****CCG League****: Border around alert box
        this.alertBox.css({"border-radius": "7px", "border-color": "#7faf7f", "border-width": "2px"});

        var selectedCardIds = new Array();

        var finishChoice = function () {
            that.alertText.html("");
            // ****CCG League****: Border around alert box
            that.alertBox.css({"border-radius": "7px", "border-color": "", "border-width": "1px"});
            that.alertButtons.html("");
            that.clearSelection();
            that.decisionFunction(id, "" + selectedCardIds);
        };

        var resetChoice = function () {
            selectedCardIds = new Array();
            that.clearSelection();
            allowSelection();
            processButtons();
        };

        var processButtons = function () {
            that.alertButtons.html("");
            if (selectedCardIds.length > 0) {
                that.alertButtons.append("<button id='ClearSelection'>Reset choice</button>");
                $("#ClearSelection").button().click(function () {
                    resetChoice();
                });
            }
            if (selectedCardIds.length >= min) {
                that.alertButtons.append("<button id='Done' style='float: right'>Done</button>");
                $("#Done").button().click(function () {
                    finishChoice();
                });
            }
        };

        var allowSelection = function () {
            that.selectionFunction = function (cardId) {
                selectedCardIds.push(cardId);
                if (selectedCardIds.length == max) {
                    if (this.settingsAutoAccept) {
                        finishChoice();
                        return;
                    } else {
                        that.clearSelection();
                        if (selectedCardIds.length > 0)
                            $(".card:cardId(" + selectedCardIds + ")").addClass("selectedCard");
                    }
                } else {
                    $(".card:cardId(" + cardId + ")").removeClass("selectableCard").addClass("selectedCard");
                }

                processButtons();
            };

            that.attachSelectionFunctions(cardIds, true);
        };

        allowSelection();
        if (!this.replayMode)
            processButtons();
    },

    assignMinionsDecision: function (decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var freeCharacters = this.getDecisionParameters(decision, "freeCharacters");
        var minions = this.getDecisionParameters(decision, "minions");

        var that = this;

        this.alertText.html(text);
        // ****CCG League****: Border around alert box
        this.alertBox.css({"border-radius": "7px", "border-color": "#7faf7f", "border-width": "2px"});
        if (!this.replayMode) {
            this.alertButtons.html("<button id='Done'>Done</button>");
            $("#Done").button().click(function () {
                var atLeastOnMinionUnassigned = false;

                var assignmentMap = {};
                for (var i = 0; i < freeCharacters.length; i++) {
                    assignmentMap[freeCharacters[i]] = freeCharacters[i];
                }
                if (minions.length > 0)
                    $(".card:cardId(" + minions + ")").each(function () {
                        var card = $(this).data("card");
                        if (card.assign != null)
                            assignmentMap[card.assign] += " " + card.cardId;
                        else
                            atLeastOnMinionUnassigned = true;
                    });

                if (atLeastOnMinionUnassigned && !confirm("At least one minion has not been assigned, do you want to proceed?"))
                    return;

                var assignmentArray = new Array();
                for (var i = 0; i < freeCharacters.length; i++) {
                    assignmentArray.push(assignmentMap[freeCharacters[i]]);
                }

                that.alertText.html("");
                // ****CCG League****: Border around alert box
                that.alertBox.css({"border-radius": "7px", "border-color": "", "border-width": "1px"});
                that.alertButtons.html("");
                that.clearSelection();

                that.decisionFunction(id, "" + assignmentArray);
            });
        }

        this.doAssignments(freeCharacters, minions);
    },

    unassignMinion: function (minionId) {
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

    assignMinion: function (minionId, characterId) {
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

    doAssignments: function (freeCharacters, minions) {
        var that = this;
        this.selectionFunction = function (cardId) {
            that.clearSelection();

            that.selectionFunction = function (secondCardId) {
                that.clearSelection();
                if (cardId != secondCardId) {
                    that.assignMinion(cardId, secondCardId);
                } else {
                    that.unassignMinion(cardId);
                }
                that.layoutUI(false);
                that.doAssignments(freeCharacters, minions);
            };

            that.attachSelectionFunctions(freeCharacters, true);
            that.attachSelectionFunctions([cardId], true);
        };

        this.attachSelectionFunctions(minions, true);
    },

    clearSelection: function () {
        $(".selectableCard").removeClass("selectableCard").data("action", null);
        $(".actionableCard").removeClass("actionableCard").data("action", null);
        $(".selectedCard").removeClass("selectedCard");
        this.selectionFunction = null;
    },

    dialogResize: function (dialog, group) {
        var width = dialog.width() + 10;
        var height = dialog.height() + 10;
        group.setBounds(this.padding, this.padding, width - 2 * this.padding, height - 2 * this.padding);
    },

    arbitraryDialogResize: function (texts) {
        if (texts) {
            var width = this.cardActionDialog.width() + 10;
            var height = this.cardActionDialog.height() - 10;
            this.specialGroup.setBounds(this.padding, this.padding, width - 2 * this.padding, height - 2 * this.padding);
        } else
            this.dialogResize(this.cardActionDialog, this.specialGroup);
    }
});
