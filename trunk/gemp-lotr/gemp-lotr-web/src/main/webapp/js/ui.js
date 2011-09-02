var GempLotrUI = Class.extend({
    padding: 5,

    updateStateFunction: null,
    decisionFunction: null,
    getCardModifiersFunction: null,

    selfPlayerId: null,
    allPlayerIds: null,
    dialogInstance: null,
    gameStateElem: null,
    alert: null,
    infoDialog: null,
    assignmentDialog: null,
    skirmishDialog: null,

    advPathGroup: null,
    supportOpponent: null,
    charactersOpponent: null,
    shadow: null,
    charactersPlayer: null,
    supportPlayer: null,
    hand: null,
    shadowOpponent: null,
    shadowPlayer: null,
    specialGroup: null,

    skirmishShadowGroup: null,
    skirmishFellowshipGroup: null,

    shadowAssignGroups: null,
    freePeopleAssignGroups: null,

    selectionFunction: null,

    init: function() {
        log("ui initialized");

        $.expr[':'].cardId = function(obj, index, meta, stack) {
            var cardIds = meta[3].split(",");
            var cardData = $(obj).data("card");
            return (cardData != null && ($.inArray(cardData.cardId, cardIds) > -1));
        };

        this.shadowAssignGroups = {};
        this.freePeopleAssignGroups = {};

        this.skirmishShadowGroup = new NormalCardGroup(null, function (card) {
            return card.zone == "SHADOW_CHARACTERS" && card.skirmish == true;
        });
        this.skirmishFellowshipGroup = new NormalCardGroup(null, function (card) {
            return (card.zone == "FREE_SUPPORT" || card.zone == "FREE_CHARACTERS") && card.skirmish == true;
        });

        //        document.oncontextmenu = function() {return false;};

        this.initializeDialogs();
    },

    initializeGameUI: function() {
        this.advPathGroup = new AdvPathCardGroup();

        var that = this;

        this.supportOpponent = new NormalCardGroup("Free People Support of Opponent", function(card) {
            return (card.zone == "FREE_SUPPORT" && card.owner != that.selfPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        this.charactersOpponent = new NormalCardGroup("Free People Characters of Opponent", function(card) {
            return (card.zone == "FREE_CHARACTERS" && card.owner != that.selfPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        this.shadow = new NormalCardGroup("Shadow minions", function(card) {
            return (card.zone == "SHADOW_CHARACTERS" && card.assign == null && card.skirmish == null);
        });
        this.charactersPlayer = new NormalCardGroup("Free People Characters of Player", function(card) {
            return (card.zone == "FREE_CHARACTERS" && card.owner == that.selfPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        this.supportPlayer = new NormalCardGroup("Free People Support of Player", function(card) {
            return (card.zone == "FREE_SUPPORT" && card.owner == that.selfPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        this.hand = new NormalCardGroup("Hand", function(card) {
            return (card.zone == "HAND");
        });
        this.shadowOpponent = new NormalCardGroup("Shadow Support of Opponent", function(card) {
            return (card.zone == "SHADOW_SUPPORT" && card.owner != that.selfPlayerId);
        });
        this.shadowPlayer = new NormalCardGroup("Shadow Support of Player", function(card) {
            return (card.zone == "SHADOW_SUPPORT" && card.owner == that.selfPlayerId);
        });

        this.specialGroup = new NormalCardGroup(null, function(card) {
            return (card.zone == "SPECIAL");
        });
        this.specialGroup.setBounds(this.padding, this.padding, 400, 200);

        this.gameStateElem = $("<div></div>");

        this.gameStateElem.append("<b>Players:</b><br>");
        for (var i = 0; i < this.allPlayerIds.length; i++)
            this.gameStateElem.append("<div class='player'>" + this.allPlayerIds[i] + "</div>");

        this.gameStateElem.append("<br>");

        this.gameStateElem.append("<b>Phases:</b><br>");
        this.gameStateElem.append("<div class='phase' id='FELLOWSHIP'>Fellowship</div>");
        this.gameStateElem.append("<div class='phase' id='SHADOW'>Shadow</div>");
        this.gameStateElem.append("<div class='phase' id='MANEUVER'>Maneuver</div>");
        this.gameStateElem.append("<div class='phase' id='ARCHERY'>Archery</div>");
        this.gameStateElem.append("<div class='phase' id='ASSIGNMENT'>Assignment</div>");
        this.gameStateElem.append("<div class='phase' id='SKIRMISH'>Skirmish</div>");
        this.gameStateElem.append("<div class='phase' id='REGROUP'>Regroup</div>");

        this.gameStateElem.append("<br>");

        this.gameStateElem.append("<b>Twilight pool:</b><br>");
        this.gameStateElem.append("<div class='twilightPool'>0</div>");

        $("#main").append(this.gameStateElem);

        this.alert = $("<div></div>");
        $("#main").append(this.alert);

        $("body").click(function (event) {
            var selectedCardElem = $(event.target).closest(".card");
            if (selectedCardElem.length != 0) {
                if (event.which == 1) {
                    if (event.shiftKey) {
                        that.displayCardInfo($(selectedCardElem[0]).data("card"));
                    }
                }
            }
            return false;
        });
    },

    displayCardInfo: function(card) {
        this.infoDialog.html("<div><div style='float: left;'><img src='" + card.imageUrl + "'></div><div id='cardEffects'></div></div>");

        var cardId = card.cardId;
        if (cardId.length < 4 || cardId.substring(0, 4) != "temp")
            this.getCardModifiersFunction(cardId, this.setCardModifiers);

        this.infoDialog.dialog("open");
    },

    setCardModifiers: function(html) {
        $("#cardEffects").replaceWith(html);
    },

    initializeDialogs: function() {
        this.dialogInstance = $("<div></div>")
                .dialog({
            autoOpen: false,
            closeOnEscape: false,
            resizable: false,
            minHeight: 80
        });

        this.assignmentDialog = $("<div></div>")
                .dialog({
            autoOpen: false,
            closeOnEscape: false,
            title: "Assignments",
            resizable: true,
            minHeight: 240,
            minWidth: 400,
            width: 500,
            height: 200,
            position: ["right", "bottom"]
        });

        this.skirmishDialog = $("<div></div>")
                .dialog({
            autoOpen: false,
            closeOnEscape: false,
            title: "Skirmish",
            resizable: true,
            minHeight: 240,
            minWidth: 400,
            width: 500,
            height: 200,
            position: ["right", "top"]
        });

        $(".ui-dialog-titlebar-close").hide();

        this.infoDialog = $("<div></div>")
                .dialog({
            autoOpen: false,
            closeOnEscape: true,
            resizable: true,
            title: "Card information",
            minHeight: 80,
            minWidth: 200,
            width: 600,
            height: 400
        });

    },

    layoutUI: function() {
        if (this.advPathGroup != null) {
            var padding = this.padding;
            var width = $(window).width();
            var height = $(window).height();

            var heightScales = [5, 9, 9, 10, 6, 10];
            var yScales = new Array();
            var scaleTotal = 0;
            for (var i = 0; i < heightScales.length; i++) {
                yScales[i] = scaleTotal;
                scaleTotal += heightScales[i];
            }

            var heightPerScale = (height - (padding * 7)) / scaleTotal;

            var advPathWidth = Math.min(150, width * 0.1);
            var shadowSupportWidth = width * 0.2;
            var specialUiWidth = 150;

            var alertHeight = 80;

            this.advPathGroup.setBounds(padding, padding, advPathWidth, height - (padding * 2));
            this.supportOpponent.setBounds(advPathWidth + specialUiWidth + (padding * 2), padding + yScales[0] * heightPerScale, width - (advPathWidth + specialUiWidth + shadowSupportWidth + padding * 4), heightScales[0] * heightPerScale);
            this.charactersOpponent.setBounds(advPathWidth + specialUiWidth + (padding * 2), padding * 2 + yScales[1] * heightPerScale, width - (advPathWidth + specialUiWidth + shadowSupportWidth + padding * 4), heightScales[1] * heightPerScale);
            this.shadow.setBounds(advPathWidth + specialUiWidth + (padding * 2), padding * 3 + yScales[2] * heightPerScale, width - (advPathWidth + specialUiWidth + padding * 3), heightScales[2] * heightPerScale);
            this.charactersPlayer.setBounds(advPathWidth + specialUiWidth + (padding * 2), padding * 4 + yScales[3] * heightPerScale, width - (advPathWidth + specialUiWidth + shadowSupportWidth + padding * 4), heightScales[3] * heightPerScale);
            this.supportPlayer.setBounds(advPathWidth + specialUiWidth + (padding * 2), padding * 5 + yScales[4] * heightPerScale, width - (advPathWidth + specialUiWidth + shadowSupportWidth + padding * 4), heightScales[4] * heightPerScale);
            this.hand.setBounds(advPathWidth + (padding * 2), padding * 6 + yScales[5] * heightPerScale, width - (advPathWidth + padding * 3), heightScales[5] * heightPerScale);
            this.shadowOpponent.setBounds(width - (shadowSupportWidth + padding), padding, shadowSupportWidth, padding + (heightScales[0] + heightScales[1]) * heightPerScale);
            this.shadowPlayer.setBounds(width - (shadowSupportWidth + padding), padding * 4 + yScales[3] * heightPerScale, shadowSupportWidth, padding + (heightScales[3] + heightScales[4]) * heightPerScale);

            this.gameStateElem.css({ position: "absolute", left: padding * 2 + advPathWidth, top: padding, width: specialUiWidth - padding, height: padding * 5 + yScales[5] * heightPerScale - alertHeight });
            this.alert.css({ position: "absolute", left: padding * 2 + advPathWidth, top: padding * 6 + yScales[5] * heightPerScale - alertHeight, width: specialUiWidth - padding, height: alertHeight });
        }
    },

    setUpdateState: function(func) {
        this.updateStateFunction = func;
    },

    setDecisionFunction: function(func) {
        this.decisionFunction = func;
    },

    setGetCardModifiers: function(func) {
        this.getCardModifiersFunction = func;
    },

    processXml: function(xml) {
        log(xml);
        var root = xml.documentElement;
        if (root.tagName == 'gameState' || root.tagName == 'update')
            this.processGameEventsXml(root);
    },

    processGameEventsXml: function(element) {
        var gameEvents = element.getElementsByTagName("gameEvent");
        for (var i = 0; i < gameEvents.length; i++) {
            var gameEvent = gameEvents[i];
            var eventType = gameEvent.getAttribute("type");
            if (eventType == "PUT_CARD_IN_PLAY") {
                this.putCardInPlay(gameEvent);
            } else if (eventType == "PARTICIPANT") {
                this.participant(gameEvent);
            } else if (eventType == "REMOVE_CARD_FROM_PLAY") {
                this.removeCardFromPlay(gameEvent);
            } else if (eventType == "GAME_PHASE_CHANGE") {
                this.gamePhaseChange(gameEvent);
            } else if (eventType == "TWILIGHT_POOL") {
                this.twilightPool(gameEvent);
            } else if (eventType == "TURN_CHANGE") {
                this.turnChange(gameEvent);
            } else if (eventType == "ADD_ASSIGNMENT") {
                this.addAssignment(gameEvent);
            } else if (eventType == "REMOVE_ASSIGNMENT") {
                this.removeAssignment(gameEvent);
            } else if (eventType == "START_SKIRMISH") {
                this.startSkirmish(gameEvent);
            } else if (eventType == "END_SKIRMISH") {
                this.endSkirmish();
            } else if (eventType == "ADD_TOKENS") {
                this.addTokens(gameEvent);
            } else if (eventType == "REMOVE_TOKENS") {
                this.removeTokens(gameEvent);
            }
        }

        var decisions = element.getElementsByTagName("decision");
        if (decisions.length == 1) {
            var decision = decisions[0];
            var decisionType = decision.getAttribute("type");
            if (decisionType == "INTEGER") {
                this.integerDecision(decision);
            } else if (decisionType == "MULTIPLE_CHOICE") {
                this.multipleChoiceDecision(decision);
            } else if (decisionType == "ARBITRARY_CARDS") {
                this.arbitraryCardsDecision(decision);
            } else if (decisionType == "ACTION_CHOICE") {
                this.actionChoiceDecision(decision);
            } else if (decisionType == "CARD_ACTION_CHOICE") {
                this.cardActionChoiceDecision(decision);
            } else if (decisionType == "CARD_SELECTION") {
                this.cardSelectionDecision(decision);
            } else if (decisionType == "ASSIGN_MINIONS") {
                this.assignMinionsDecision(decision);
            }
        } else {
            setTimeout(this.updateStateFunction, 1000);
        }
    },

    processError: function (xhr, ajaxOptions, thrownError) {
        alert("There was a problem during communication with server");
    },

    addTokens: function(element) {
        var cardId = element.getAttribute("cardId");
        var zone = element.getAttribute("zone");
        var token = element.getAttribute("token");
        var count = parseInt(element.getAttribute("count"));

        var cardData = $(".card:cardId(" + cardId + ")").data("card");
        if (cardData.tokens == null)
            cardData.tokens = {};
        if (cardData.tokens[token] == null)
            cardData.tokens[token] = 0;
        cardData.tokens[token] += count;

        this.layoutZone(zone);
        this.assignmentsChanged();
    },

    removeTokens: function(element) {
        var cardId = element.getAttribute("cardId");
        var zone = element.getAttribute("zone");
        var token = element.getAttribute("token");
        var count = parseInt(element.getAttribute("count"));

        var cardData = $(".card:cardId(" + cardId + ")").data("card");
        if (cardData.tokens == null)
            cardData.tokens = {};
        if (cardData.tokens[token] == null)
            cardData.tokens[token] = 0;
        cardData.tokens[token] -= count;

        this.layoutZone(zone);
        this.assignmentsChanged();
    },

    startSkirmish: function(element) {
        var cardId = element.getAttribute("cardId");
        var opposingCardIds = element.getAttribute("opposingCardIds").split(",");

        $(".card:cardId(" + opposingCardIds + "," + cardId + ")").each(function() {
            $(this).data("card").skirmish = true;
        });

        this.moveCardToElement(cardId, this.skirmishDialog);
        for (var i = 0; i < opposingCardIds.length; i++)
            this.moveCardToElement(opposingCardIds[i], this.skirmishDialog);

        var that = this;
        this.skirmishDialog.dialog("open");
        this.skirmishDialog.bind("dialogresize", function() {
            that.skirmishDialogResized();
        });

        this.skirmishDialogResized();
        this.assignmentsChanged();
    },

    endSkirmish: function() {
        this.skirmishDialog.dialog("close");
        this.skirmishDialog.unbind("dialogresize");

        var that = this;
        $(".card").each(function() {
            var cardData = $(this).data("card");
            if (cardData.skirmish == true) {
                that.moveCardToElement(cardData.cardId, $("#main"));
                delete cardData.skirmish;
            }
        });

        this.skirmishDialog.html("");

        this.assignmentsChanged();
    },

    removeAssignment: function(element) {
        var cardId = element.getAttribute("cardId");

        var that = this;
        $(".card").each(function() {
            var cardData = $(this).data("card");
            if (cardData.assign == cardId)
                that.unassignMinion(cardData.cardId);
        });
    },

    addAssignment: function(element) {
        var cardId = element.getAttribute("cardId");
        var opposingCardIds = element.getAttribute("opposingCardIds").split(",");

        for (var i = 0; i < opposingCardIds.length; i++) {
            if ($(".card:cardId(" + opposingCardIds[i] + ")").data("card").assign != cardId)
                this.assignMinion(opposingCardIds[i], cardId);
        }

        this.assignmentsChanged();
    },

    putCardInPlay: function(element) {
        var participantId = element.getAttribute("participantId");
        var blueprintId = element.getAttribute("blueprintId");
        var cardId = element.getAttribute("cardId");
        var zone = element.getAttribute("zone");
        var targetCardId = element.getAttribute("targetCardId");

        // TODO finish off the other zones (DISCARD, DEAD)
        if (zone != "DISCARD" && zone != "DEAD") {
            var card = new Card(blueprintId, zone, cardId, participantId);

            var cardDiv = this.createCardDiv(card);
            $("#main").append(cardDiv);

            var affectedZone = zone;

            if (targetCardId != null) {
                var targetCardData = $(".card:cardId(" + targetCardId + ")").data("card");
                targetCardData.attachedCards.push(cardDiv);
                affectedZone = targetCardData.zone;
            }

            this.layoutZone(affectedZone);
        }
    },

    layoutZone: function(zone) {
        if (zone == "ADVENTURE_PATH") {
            this.advPathGroup.layoutCards();
        } else if (zone == "FREE_CHARACTERS") {
            this.charactersPlayer.layoutCards();
            this.charactersOpponent.layoutCards();
        } else if (zone == "FREE_SUPPORT") {
            this.supportPlayer.layoutCards();
            this.supportOpponent.layoutCards();
        } else if (zone == "HAND") {
            this.hand.layoutCards();
        } else if (zone == "SHADOW_CHARACTERS") {
            this.shadow.layoutCards();
        } else if (zone == "SHADOW_SUPPORT") {
            this.shadowPlayer.layoutCards();
            this.shadowOpponent.layoutCards();
        }
    },

    participant: function(element) {
        var participantId = element.getAttribute("participantId");
        this.allPlayerIds = element.getAttribute("allParticipantIds").split(",");

        this.selfPlayerId = participantId;

        this.initializeGameUI();
        this.layoutUI();
    },

    removeCardFromPlay: function(element) {
        var cardId = element.getAttribute("cardId");
        var zone = element.getAttribute("zone");

        $(".card:cardId(" + cardId + ")").remove();

        this.layoutZone(zone);
    },

    gamePhaseChange: function(element) {
        var phase = element.getAttribute("phase");

        $(".phase").removeClass("current");
        $(".phase#" + phase).addClass("current");
    },

    twilightPool: function(element) {
        var count = element.getAttribute("count");

        $(".twilightPool").html("" + count);
    },

    turnChange: function(element) {
        var playerId = element.getAttribute("participantId");
        var playerIndex = $.inArray(playerId, this.allPlayerIds);

        $(".player").each(function(index) {
            if (index == playerIndex)
                $(this).addClass("current");
            else
                $(this).removeClass("current");
        });
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

    integerDecision: function(decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var min = this.getDecisionParameter(decision, "min");
        if (min == null)
            min = 0;
        var max = this.getDecisionParameter(decision, "max");
        if (max == null)
            max = 1000;

        var that = this;
        this.dialogInstance
                .html(text + "<br /><input id='integerDecision' type='text' value='0'>")
                .dialog("option", "buttons",
        {
            "OK": function() {
                $(this).dialog("close");
                that.decisionFunction(id, $("#integerDecision").val());
            }
        }
                )
                .dialog("option", "width", "400")
                .dialog("option", "height", "auto");
        ;

        $("#integerDecision").SpinnerControl({ type: 'range',
            typedata: {
                min: min,
                max: max,
                interval: 1,
                decimalplaces: 0
            },
            width: '50px',
            backColor: "#000000"
        });

        this.dialogInstance.dialog("open");
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
        this.dialogInstance
                .html(html)
                .dialog("option", "buttons",
        {
            "OK": function() {
                $(this).dialog("close");
                that.decisionFunction(id, $("#multipleChoiceDecision").val());
            }
        }
                )
                .dialog("option", "width", "400")
                .dialog("option", "height", "auto");

        this.dialogInstance.dialog("open");
    },

    createCardDiv: function(card, text) {
        var cardDiv = $("<div class='card'><img src='" + card.imageUrl + "' width='100%' height='100%'>" + ((text != null) ? text : "") + "</div>");
        cardDiv.data("card", card);
        var overlayDiv = $("<div class='tokenOverlay'></div>");
        cardDiv.append(overlayDiv);
        var borderDiv = $("<div class='borderOverlay'></div>");
        cardDiv.append(borderDiv);
        return cardDiv;
    },

    attachSelectionFunctions: function(cardIds) {
        var that = this;
        $(".card:cardId(" + cardIds + ")").each(
                function() {
                    var theOtherOne = that;
                    $(this).addClass("selectableCard");
                    var cardData = $(this).data("card");
                    $(".borderOverlay", $(this)).bind("click",
                            function(event) {
                                if (event.which == 1)
                                    theOtherOne.selectionFunction(cardData.cardId);
                            });
                }
                );
    },

    arbitraryCardsDecision: function(decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var min = this.getDecisionParameter(decision, "min");
        var max = this.getDecisionParameter(decision, "max");
        var cardIds = this.getDecisionParameters(decision, "cardId");
        var blueprintIds = this.getDecisionParameters(decision, "blueprintId");

        var that = this;

        var selectedCardIds = new Array();

        this.dialogInstance
                .html("<div id='arbitraryChoice'></div>")
                .dialog("option", "title", text)
                .dialog("option", "buttons", {})
                .dialog("option", "width", "600")
                .dialog("option", "height", "300");

        if (min == 0) {
            this.dialogInstance.dialog("option", "buttons", {
                "DONE": function() {
                    $(this).dialog("close");
                    $("#arbitraryChoice").html("");
                    that.clearSelection();
                    that.decisionFunction(id, "" + selectedCardIds);
                }
            });
        }

        for (var i = 0; i < blueprintIds.length; i++) {
            var cardId = cardIds[i];
            var blueprintId = blueprintIds[i];

            var card = new Card(blueprintId, "SPECIAL", cardId, this.selfParticipantId);

            var cardDiv = this.createCardDiv(card);

            $("#arbitraryChoice").append(cardDiv);
        }

        this.selectionFunction = function(cardId) {
            selectedCardIds.push(cardId);

            if (selectedCardIds.length == min) {
                this.dialogInstance.dialog("option", "buttons", {
                    "DONE": function() {
                        $(this).dialog("close");
                        $("#arbitraryChoice").html("");
                        that.clearSelection();
                        that.decisionFunction(id, "" + selectedCardIds);
                    }
                });
            }

            if (selectedCardIds.length == max) {
                that.clearSelection();
                $(".card:cardId(" + cardId + ")").addClass("selectedCard");
            } else {
                $(".card:cardId(" + cardId + ")").removeClass("selectableCard").addClass("selectedCard");
            }
        };

        this.attachSelectionFunctions(cardIds);

        this.specialGroup.setBounds(10, 10, 547, 188);

        $(".ui-dialog-titlebar").show();
        this.dialogInstance.dialog("open");
    },

    cardActionChoiceDecision: function (decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var cardIds = this.getDecisionParameters(decision, "cardId");
        var actionIds = this.getDecisionParameters(decision, "actionId");
        var actionTexts = this.getDecisionParameters(decision, "actionText");

        var that = this;

        this.alert.html(text + "<br><button id='DONE'>DONE</button>");
        $("#DONE").button().click(function() {
            that.alert.html("");
            that.clearSelection();
            that.decisionFunction(id, "");
        });

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

        this.selectionFunction = function(cardId) {
            var cardIdElem = $(".card:cardId(" + cardId + ")");
            var actions = cardIdElem.data("action");
            if (actions.length == 1) {
                var action = actions[0];
                that.alert.html("");
                that.clearSelection();
                that.decisionFunction(id, "" + action.actionId);
            }
        };

        this.attachSelectionFunctions(cardIds);
    },

    actionChoiceDecision: function (decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var blueprintIds = this.getDecisionParameters(decision, "blueprintId");
        var actionIds = this.getDecisionParameters(decision, "actionId");
        var actionTexts = this.getDecisionParameters(decision, "actionText");

        var that = this;

        this.dialogInstance
                .html("<div id='arbitraryChoice'></div>")
                .dialog("option", "title", text)
                .dialog("option", "buttons", {})
                .dialog("option", "width", "600")
                .dialog("option", "height", "300");

        var cardIds = new Array();

        for (var i = 0; i < blueprintIds.length; i++) {
            var blueprintId = blueprintIds[i];

            cardIds.push("temp" + i);
            var card = new Card(blueprintId, "SPECIAL", "temp" + i, this.selfParticipantId);

            var cardDiv = this.createCardDiv(card, actionTexts[i]);

            $("#arbitraryChoice").append(cardDiv);
        }

        this.selectionFunction = function(cardId) {
            var actionId = actionIds[parseInt(cardId.substring(4))];

            this.dialogInstance.dialog("option", "buttons", {
                "DONE": function() {
                    $(this).dialog("close");
                    $("#arbitraryChoice").html("");
                    that.clearSelection();
                    that.decisionFunction(id, "" + actionId);
                }
            });

            that.clearSelection();
            $(".card:cardId(" + cardId + ")").addClass("selectedCard");
        };

        this.attachSelectionFunctions(cardIds);

        this.specialGroup.setBounds(10, 10, 547, 188);

        $(".ui-dialog-titlebar").show();
        this.dialogInstance.dialog("open");
    },

    cardSelectionDecision: function(decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var min = this.getDecisionParameter(decision, "min");
        var max = this.getDecisionParameter(decision, "max");
        var cardIds = this.getDecisionParameters(decision, "cardId");

        var that = this;

        this.alert.html(text);

        var selectedCardIds = new Array();

        if (min == 0) {
            this.alert.append("<br><button id='DONE'>DONE</button>");
            $("#DONE").button().click(function() {
                that.alert.html("");
                that.clearSelection();
                that.decisionFunction(id, "" + selectedCardIds);
            });
        }

        this.selectionFunction = function(cardId) {
            selectedCardIds.push(cardId);
            if (selectedCardIds.length == min) {
                this.alert.append("<br><button id='DONE'>DONE</button>");
                $("#DONE").button().click(function() {
                    that.alert.html("");
                    that.clearSelection();
                    that.decisionFunction(id, "" + selectedCardIds);
                });
            }

            if (selectedCardIds.length == max) {
                that.clearSelection();
                $(".card:cardId(" + cardId + ")").addClass("selectedCard");
            } else {

                $(".card:cardId(" + cardId + ")").removeClass("selectableCard").addClass("selectedCard");
            }
        };

        this.attachSelectionFunctions(cardIds);
    },

    assignMinionsDecision: function(decision) {
        var id = decision.getAttribute("id");
        var text = decision.getAttribute("text");

        var freeCharacters = this.getDecisionParameters(decision, "freeCharacters");
        var minions = this.getDecisionParameters(decision, "minions");

        var that = this;

        this.alert.html(text + "<br><button id='DONE'>DONE</button>");
        $("#DONE").button().click(function() {
            that.alert.html("");
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

        this.doAssignments(freeCharacters, minions);
    },

    unassignMinion: function(minionId) {
        var previousCharacterId = $(".card:cardId(" + minionId + ")").data("card").assign;
        delete $(".card:cardId(" + minionId + ")").data("card").assign;
        this.moveCardToElement(minionId, $("#main"));

        var characterHasMinion = false;
        $(".card").each(function () {
            if ($(this).data("card").assign == previousCharacterId) characterHasMinion = true;
        });

        if (!characterHasMinion) {
            this.moveCardToElement(previousCharacterId, $("#main"));
            delete this.shadowAssignGroups[previousCharacterId];
            delete this.freePeopleAssignGroups[previousCharacterId];

            if (getMapSize(this.shadowAssignGroups) == 0) {
                this.assignmentDialog.unbind("dialogresize");
                this.assignmentDialog.dialog("close");
            }
        }
    },

    assignMinion: function(minionId, characterId) {
        if ($(".card:cardId(" + minionId + ")").data("card").assign != null)
            this.unassignMinion(minionId);

        var that = this;
        if (!this.assignmentDialog.dialog("isOpen")) {
            this.assignmentDialog.bind("dialogresize", function() {
                that.assignDialogResized();
            });

            that.shadowAssignGroups = {};
            that.freePeopleAssignGroups = {};

            this.assignmentDialog.dialog("open");
        }

        if (this.shadowAssignGroups[characterId] == null) {
            this.shadowAssignGroups[characterId] = new NormalCardGroup(null, function (card) {
                return (card.zone == "SHADOW_CHARACTERS" && card.assign == characterId);
            }
                    );
            this.freePeopleAssignGroups[characterId] = new NormalCardGroup(null, function (card) {
                return (card.cardId == characterId);
            }
                    );

            this.moveCardToElement(characterId, this.assignmentDialog);
            this.assignDialogResized();
        }

        this.moveCardToElement(minionId, this.assignmentDialog);
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
                that.assignmentsChanged();
                that.doAssignments(freeCharacters, minions);
            };

            that.attachSelectionFunctions(freeCharacters);
            that.attachSelectionFunctions([cardId]);
        };

        this.attachSelectionFunctions(minions);
    },

    moveCardToElement: function(cardId, element) {
        var charDiv = $(".card:cardId(" + cardId + ")");
        var charData = charDiv.data("card");
        charDiv.remove();
        element.append(charDiv);
        charDiv.data("card", charData);
        for (var j = 0; j < charData.attachedCards.length; j++) {
            var attachedDiv = charData.attachedCards[j];
            var attachedData = attachedDiv.data("card");
            attachedDiv.remove();
            element.append(attachedDiv);
            attachedDiv.data("card", attachedData);
        }
    },

    assignDialogResized: function() {
        var width = this.assignmentDialog.width();
        var height = this.assignmentDialog.height() + 10;

        var groupCount = getMapSize(this.shadowAssignGroups);
        var cellWidth = Math.floor(width / groupCount);
        var cellHeight = Math.floor(height / 2);
        var index = 0;
        for (var characterId in this.shadowAssignGroups) {
            if (this.shadowAssignGroups.hasOwnProperty(characterId)) {
                this.shadowAssignGroups[characterId].setBounds(index * cellWidth, 0, cellWidth, cellHeight);
                this.freePeopleAssignGroups[characterId].setBounds(index * cellWidth, cellHeight, cellWidth, cellHeight);
                index++;
            }
        }
    },

    skirmishDialogResized: function() {
        var width = this.skirmishDialog.width();
        var height = this.skirmishDialog.height() + 10;

        var cellHeight = Math.floor(height / 2);
        this.skirmishShadowGroup.setBounds(0, 0, width, cellHeight);
        this.skirmishFellowshipGroup.setBounds(0, cellHeight, width, cellHeight);
    },

    assignmentsChanged: function() {
        this.charactersPlayer.layoutCards();
        this.supportPlayer.layoutCards();
        this.charactersOpponent.layoutCards();
        this.supportOpponent.layoutCards();
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

    clearSelection: function() {
        $(".selectableCard").each(
                function() {
                    $(".borderOverlay", $(this)).unbind("click");
                });
        $(".selectableCard").removeClass("selectableCard").data("action", null);
        $(".selectedCard").removeClass("selectedCard");
        this.selectionFunction = null;
    }

});
