var GempLotrGameUI = Class.extend({
    padding: 5,

    updateGameState: null,
    decisionFunction: null,
    getCardModifiersFunction: null,

    selfPlayerId: null,
    currentPlayerId: null,
    allPlayerIds: null,
    playerPositions: null,
    cardActionDialog: null,
    smallDialog: null,
    gameStateElem: null,
    alert: null,
    infoDialog: null,

    advPathGroup: null,
    supportOpponent: null,
    charactersOpponent: null,
    shadow: null,
    charactersPlayer: null,
    supportPlayer: null,
    hand: null,
    specialGroup: null,

    skirmishGroupDiv: null,
    fpStrengthDiv: null,
    shadowStrengthDiv: null,
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

    init: function(communication) {
        log("ui initialized");
        this.communication = communication;

        $.expr[':'].cardId = function(obj, index, meta, stack) {
            var cardIds = meta[3].split(",");
            var cardData = $(obj).data("card");
            return (cardData != null && ($.inArray(cardData.cardId, cardIds) > -1));
        };

        this.shadowAssignGroups = {};
        this.freePeopleAssignGroups = {};
        this.assignGroupDivs = new Array();

        this.skirmishShadowGroup = new NormalCardGroup($("#main"), function (card) {
            return card.zone == "SHADOW_CHARACTERS" && card.skirmish == true;
        });
        this.skirmishFellowshipGroup = new NormalCardGroup($("#main"), function (card) {
            return (card.zone == "FREE_SUPPORT" || card.zone == "FREE_CHARACTERS") && card.skirmish == true;
        });

        this.initializeDialogs();
    },

    initializeGameUI: function() {
        this.advPathGroup = new AdvPathCardGroup($("#main"));

        var that = this;

        this.supportOpponent = new NormalCardGroup($("#main"), function(card) {
            return ((card.zone == "FREE_SUPPORT" || card.zone == "SHADOW_SUPPORT") && card.owner != that.selfPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        this.charactersOpponent = new NormalCardGroup($("#main"), function(card) {
            return (card.zone == "FREE_CHARACTERS" && card.owner != that.selfPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        this.shadow = new NormalCardGroup($("#main"), function(card) {
            return (card.zone == "SHADOW_CHARACTERS" && card.assign == null && card.skirmish == null);
        });
        this.charactersPlayer = new NormalCardGroup($("#main"), function(card) {
            return (card.zone == "FREE_CHARACTERS" && card.owner == that.selfPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        this.supportPlayer = new NormalCardGroup($("#main"), function(card) {
            return ((card.zone == "FREE_SUPPORT" || card.zone == "SHADOW_SUPPORT") && card.owner == that.selfPlayerId && that.shadowAssignGroups[card.cardId] == null && card.skirmish == null);
        });
        this.hand = new NormalCardGroup($("#main"), function(card) {
            return (card.zone == "HAND");
        });

        this.specialGroup = new NormalCardGroup(this.cardActionDialog, function(card) {
            return (card.zone == "SPECIAL");
        }, false);
        this.specialGroup.setBounds(this.padding, this.padding, this.cardActionDialog.width() + 20, this.cardActionDialog.height() + 20);

        this.gameStateElem = $("<div class='ui-widget-content'></div>");
        this.gameStateElem.css({"border-radius": "7px"});

        this.gameStateElem.append("<b>Players:</b><br>");
        for (var i = 0; i < this.allPlayerIds.length; i++)
            this.gameStateElem.append("<div class='player'>" + this.allPlayerIds[i] + "</div>");

        this.gameStateElem.append("<br>");

        this.gameStateElem.append("<div class='twilightPool'>0</div>");
        this.gameStateElem.append("<b>Phases:</b><br>");
        this.gameStateElem.append("<div class='phase' id='FELLOWSHIP'>Fellowship</div>");
        this.gameStateElem.append("<div class='phase' id='SHADOW'>Shadow</div>");
        this.gameStateElem.append("<div class='phase' id='MANEUVER'>Maneuver</div>");
        this.gameStateElem.append("<div class='phase' id='ARCHERY'>Archery</div>");
        this.gameStateElem.append("<div class='phase' id='ASSIGNMENT'>Assignment</div>");
        this.gameStateElem.append("<div class='phase' id='SKIRMISH'>Skirmish</div>");
        this.gameStateElem.append("<div class='phase' id='REGROUP'>Regroup</div>");

        $("#main").append(this.gameStateElem);

        this.alert = $("<div class='ui-widget-content'></div>");
        this.alert.css({"border-radius": "7px"});
        $("#main").append(this.alert);

        this.tabPane = $("<div><ul><li><a href='#chatBox'>Chat</a></li><li><a href='#settingsBox'>Settings</a></li></ul><div id='chatBox'></div><div id='settingsBox'></div></div>").tabs();

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

        this.chatBox = new ChatBoxUI("Game" + getUrlParam("gameId"), $("#chatBox"), this.communication.url);

        $("body").click(
                function (event) {
                    that.clickCardFunction(event);
                });
    },

    clickCardFunction: function(event) {
        var tar = $(event.target);
        if (tar.hasClass("actionArea")) {
            tar = tar.parent();
            if (tar.hasClass("borderOverlay")) {
                var selectedCardElem = tar.parent();
                if (event.which == 1) {
                    if (event.shiftKey) {
                        this.displayCardInfo(selectedCardElem.data("card"));
                    } else if (selectedCardElem.hasClass("selectableCard"))
                        this.selectionFunction(selectedCardElem.data("card").cardId);
                }
            }
        }
        return false;
    },

    displayCardInfo: function(card) {
        this.infoDialog.html("<div style='scroll: auto'><div style='float: left;'><img src='" + card.imageUrl + "'></div><div id='cardEffects'></div></div>");

        var cardId = card.cardId;
        if (cardId.length < 4 || cardId.substring(0, 4) != "temp")
            this.getCardModifiersFunction(cardId, this.setCardModifiers);

        this.infoDialog.dialog("open");
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

        this.infoDialog = $("<div></div>")
                .dialog({
                    autoOpen: false,
                    closeOnEscape: true,
                    resizable: true,
                    title: "Card information",
                    minHeight: 80,
                    minWidth: 200,
                    width: 600,
                    height: 300
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

    layoutUI: function(sizeNotChanged) {
        if (this.advPathGroup != null) {
            var padding = this.padding;
            var width = $(window).width();
            var height = $(window).height();
            if (sizeNotChanged) {
                width = this.windowWidth;
                height = this.windowHeight;
            } else {
                this.windowWidth = width;
                this.windowHeight = height;
            }

            var heightScales = [5, 9, 9, 10, 6, 10];
            var yScales = new Array();
            var scaleTotal = 0;
            for (var i = 0; i < heightScales.length; i++) {
                yScales[i] = scaleTotal;
                scaleTotal += heightScales[i];
            }

            var heightPerScale = (height - (padding * 7)) / scaleTotal;

            var advPathWidth = Math.min(150, width * 0.1);
            var specialUiWidth = 150;

            var alertHeight = 80;

            var chatHeight = 200;

            var assignmentsCount = this.assignGroupDivs.length + ((this.skirmishGroupDiv != null) ? 1 : 0);

            var charsWidth = width - (advPathWidth + specialUiWidth + padding * 3);
            var charsWidthWithAssignments = 2 * charsWidth / (2 + assignmentsCount);

            var currentPlayerTurn = (this.currentPlayerId == this.selfPlayerId);

            this.advPathGroup.setBounds(padding, padding, advPathWidth, height - (padding * 3) - chatHeight);
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
                if (currentPlayerTurn) {
                    this.skirmishShadowGroup.setBounds(x + 3, y + 3, groupWidth - 6, heightScales[2] * heightPerScale - 6);
                    this.skirmishFellowshipGroup.setBounds(x + 3, y + heightScales[2] * heightPerScale + padding + 3, groupWidth - 6, heightScales[3] * heightPerScale - 6);
                    this.fpStrengthDiv.css({left: 2 + "px", top: groupWidth - 20 - 2 + "px", width: 20, height: 20});
                    this.shadowStrengthDiv.css({left: 2 + "px", top: 2 + "px", width: 20, height: 20});
                } else {
                    this.skirmishFellowshipGroup.setBounds(x + 3, y + 3, groupWidth - 6, heightScales[1] * heightPerScale - 6);
                    this.skirmishShadowGroup.setBounds(x + 3, y + heightScales[1] * heightPerScale + padding + 3, groupWidth - 6, heightScales[2] * heightPerScale - 6);
                    this.shadowStrengthDiv.css({left: 2 + "px", top: groupWidth - 20 - 2 + "px", width: 20, height: 20});
                    this.fpStrengthDiv.css({left: 2 + "px", top: 2 + "px", width: 20, height: 20});
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
            this.hand.setBounds(advPathWidth + specialUiWidth + (padding * 2), padding * 6 + yScales[5] * heightPerScale, width - (advPathWidth + specialUiWidth + padding * 3), heightScales[5] * heightPerScale);

            this.gameStateElem.css({ position: "absolute", left: padding * 2 + advPathWidth, top: padding, width: specialUiWidth - padding, height: height - padding * 4 - alertHeight - chatHeight});
            this.alert.css({ position: "absolute", left: padding * 2 + advPathWidth, top: height - (padding * 2) - alertHeight - chatHeight, width: specialUiWidth - padding, height: alertHeight });
            this.tabPane.css({ position: "absolute", left: padding, top: height - padding - chatHeight, width: specialUiWidth + advPathWidth - padding, height: chatHeight - padding});
            this.chatBox.setBounds(4, 4 + 25, specialUiWidth + advPathWidth - 8, chatHeight - 8 - 25);
        }
    },

    setUpdateState: function(func) {
        this.updateGameState = func;
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
            } else if (eventType == "MOVE_CARD_IN_PLAY") {
                this.moveCardInPlay(gameEvent);
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
            } else if (eventType == "PLAYER_POSITION") {
                this.playerPosition(gameEvent);
            } else if (eventType == "MESSAGE") {
                this.message(gameEvent);
            } else if (eventType == "WARNING") {
                this.warning(gameEvent);
            }
        }
        var skirmish = element.getElementsByTagName("skirmish")
        if (skirmish.length > 0) {
            this.fpStrengthDiv.text(skirmish[0].getAttribute("fpStrength"));
            this.shadowStrengthDiv.text(skirmish[1].getAttribute("shadowStrength"));
        }

        if (gameEvents.length > 0)
            this.layoutUI(true);

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
            setTimeout(this.updateGameState, 1000);
        }
    },

    processError: function (xhr, ajaxOptions, thrownError) {
        alert("There was a problem during communication with server");
    },

    playerPosition: function(element) {
        var participantId = element.getAttribute("participantId");
        var position = element.getAttribute("index");

        if (this.playerPositions == null)
            this.playerPositions = new Array();

        var index = -1;
        for (var i = 0; i < this.allPlayerIds.length; i++)
            if (this.allPlayerIds[i] == participantId)
                this.playerPositions[i] = position;

        this.advPathGroup.setPositions(this.playerPositions);
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
    },

    message: function(element) {
        var message = element.getAttribute("message");
        if (this.chatBox != null)
            this.chatBox.appendMessage(message, "gameMessage");
    },

    warning: function(element) {
        var message = element.getAttribute("message");
        if (this.chatBox != null)
            this.chatBox.appendMessage(message, "warningMessage");
    },

    startSkirmish: function(element) {
        var cardId = element.getAttribute("cardId");
        var opposingCardIds = element.getAttribute("opposingCardIds").split(",");

        $(".card:cardId(" + opposingCardIds + "," + cardId + ")").each(function() {
            $(this).data("card").skirmish = true;
        });

        this.fpStrengthDiv = $("<div class='fpStrength'></div>");
        this.shadowStrengthDiv = $("<div class='shadowStrength'></div>");

        this.skirmishGroupDiv = $("<div class='ui-widget-content skirmish'></div>");
        this.skirmishGroupDiv.css({"border-radius": "7px", "border-color": "#ff0000"});
        this.skirmishGroupDiv.append(this.fpStrengthDiv);
        this.skirmishGroupDiv.append(this.shadowStrengthDiv);
        $("#main").append(this.skirmishGroupDiv);
    },

    endSkirmish: function() {
        this.skirmishGroupDiv.remove();
        this.skirmishGroupDiv = null;
        this.fpStrengthDiv = null;
        this.shadowStrengthDiv = null;

        $(".card").each(function() {
            var cardData = $(this).data("card");
            if (cardData.skirmish == true) {
                delete cardData.skirmish;
            }
        });
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
    },

    putCardInPlay: function(element) {
        var participantId = element.getAttribute("participantId");
        var blueprintId = element.getAttribute("blueprintId");
        var cardId = element.getAttribute("cardId");
        var zone = element.getAttribute("zone");
        var targetCardId = element.getAttribute("targetCardId");

        // TODO finish off the other zones (DISCARD, DEAD)
        if (zone != "DISCARD" && zone != "DEAD") {
            var card;
            if (zone == "ADVENTURE_PATH")
                card = new Card(blueprintId, zone, cardId, participantId, element.getAttribute("index"));
            else
                card = new Card(blueprintId, zone, cardId, participantId);

            var cardDiv = this.createCardDiv(card);
            $("#main").append(cardDiv);

            if (targetCardId != null) {
                var targetCardData = $(".card:cardId(" + targetCardId + ")").data("card");
                targetCardData.attachedCards.push(cardDiv);
            }
        }
    },

    moveCardInPlay: function(element) {
        var cardId = element.getAttribute("cardId");
        var zone = element.getAttribute("zone");
        var targetCardId = element.getAttribute("targetCardId");

        // Remove from where it was already attached
        $(".card").each(
                function() {
                    var cardData = $(this).data("card");
                    var index = -1;
                    for (var i = 0; i < cardData.attachedCards.length; i++)
                        if (cardData.attachedCards[i].data("card").cardId == cardId) {
                            index = i;
                            break;
                        }
                    if (index != -1)
                        cardData.attachedCards.splice(index, 1);
                }
        );

        var card = $(".card:cardId(" + cardId + ")");
        var cardData = card.data("card");
        // move to new zone
        cardData.zone = zone;

        if (targetCardId != null) {
            // attach to new card if it's attached
            var targetCardData = $(".card:cardId(" + targetCardId + ")").data("card");
            targetCardData.attachedCards.push(card);
        }
    },

    layoutZones: function() {
        this.advPathGroup.layoutCards();
        this.charactersPlayer.layoutCards();
        this.charactersOpponent.layoutCards();
        this.supportPlayer.layoutCards();
        this.supportOpponent.layoutCards();
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

        this.selfPlayerId = participantId;

        this.initializeGameUI();
        this.layoutUI();
    },

    removeCardFromPlay: function(element) {
        var cardId = element.getAttribute("cardId");
        var zone = element.getAttribute("zone");

        var card = $(".card:cardId(" + cardId + ")");

        if (zone == "ATTACHED" || zone == "STACKED") {
            $(".card").each(
                    function() {
                        var cardData = $(this).data("card");
                        var index = -1;
                        for (var i = 0; i < cardData.attachedCards.length; i++)
                            if (cardData.attachedCards[i].data("card").cardId == cardId) {
                                index = i;
                                break;
                            }
                        if (index != -1)
                            cardData.attachedCards.splice(index, 1);
                    }
            );
        }

        card.remove();
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

        this.currentPlayerId = playerId;

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
        this.smallDialog
                .html(text + "<br /><input id='integerDecision' type='text' value='0'>")
                .dialog("option", "buttons",
                {
                    "OK": function() {
                        $(this).dialog("close");
                        that.decisionFunction(id, $("#integerDecision").val());
                    }
                }
        );

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
                .html(html)
                .dialog("option", "buttons",
                {
                    "OK": function() {
                        $(this).dialog("close");
                        that.decisionFunction(id, $("#multipleChoiceDecision").val());
                    }
                }
        );

        this.smallDialog.dialog("open");
    },

    createCardDiv: function(card, text) {
        var cardDiv = createCardDiv(card.imageUrl, text);
        cardDiv.data("card", card);

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
                .dialog("option", "title", text)
                .dialog("option", "buttons", {});

        var finishChoice = function() {
            that.cardActionDialog.dialog("close");
            $("#arbitraryChoice").html("");
            that.clearSelection();
            that.decisionFunction(id, "" + selectedCardIds);
        };

        if (min == 0) {
            this.cardActionDialog.dialog("option", "buttons", {
                "DONE": function() {
                    finishChoice();
                }
            });
        }

        for (var i = 0; i < blueprintIds.length; i++) {
            var cardId = cardIds[i];
            var blueprintId = blueprintIds[i];

            if (selectable[i] == "true")
                selectableCardIds.push(cardId);

            var card = new Card(blueprintId, "SPECIAL", cardId, this.selfParticipantId);

            var cardDiv = this.createCardDiv(card);

            $("#arbitraryChoice").append(cardDiv);
        }

        this.selectionFunction = function(cardId) {
            selectedCardIds.push(cardId);

            if (selectedCardIds.length == min) {
                this.cardActionDialog.dialog("option", "buttons", {
                    "DONE": function() {
                        finishChoice();
                    }
                });
            }

            if (selectedCardIds.length == max) {
                if (that.settingsAutoAccept) {
                    finishChoice();
                } else {
                    that.clearSelection();
                    $(".card:cardId(" + cardId + ")").addClass("selectedCard");
                }
            } else {
                $(".card:cardId(" + cardId + ")").removeClass("selectableCard").addClass("selectedCard");
            }
        };

        this.attachSelectionFunctions(selectableCardIds);

        $(".ui-dialog-titlebar").show();
        this.cardActionDialog.dialog("open");
        this.arbitraryDialogResize();
    },

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

        var finishChoice = function() {
            that.alert.html("");
            that.clearSelection();
            that.decisionFunction(id, "" + selectedCardIds);
        };

        this.alert.html(text + "<br><button id='DONE'>DONE</button>");
        $("#DONE").button().click(function() {
            finishChoice();
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
                selectedCardIds.push(action.actionId);
                if (this.settingsAutoAccept) {
                    finishChoice();
                } else {
                    that.clearSelection();
                    $(".card:cardId(" + cardId + ")").addClass("selectedCard");
                }
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

        var selectedActionIds = new Array();

        this.cardActionDialog
                .html("<div id='arbitraryChoice'></div>")
                .dialog("option", "title", text)
                .dialog("option", "buttons", {});

        var cardIds = new Array();

        var finishChoice = function() {
            that.cardActionDialog.dialog("close");
            $("#arbitraryChoice").html("");
            that.clearSelection();
            that.decisionFunction(id, "" + selectedActionIds);
        };

        for (var i = 0; i < blueprintIds.length; i++) {
            var blueprintId = blueprintIds[i];

            cardIds.push("temp" + i);
            var card = new Card(blueprintId, "SPECIAL", "temp" + i, this.selfParticipantId);

            var cardDiv = this.createCardDiv(card, actionTexts[i]);

            $("#arbitraryChoice").append(cardDiv);
        }

        this.selectionFunction = function(cardId) {
            var actionId = actionIds[parseInt(cardId.substring(4))];
            selectedActionIds.push(actionId);

            that.clearSelection();

            if (this.settingsAutoAccept) {
                finishChoice();
            } else {
                this.cardActionDialog.dialog("option", "buttons", {
                    "DONE": function() {
                        finishChoice();
                    }
                });

                $(".card:cardId(" + cardId + ")").addClass("selectedCard");
            }
        };

        this.attachSelectionFunctions(cardIds);

        this.specialGroup.setBounds(10, 10, 547, 188);

        $(".ui-dialog-titlebar").show();
        this.cardActionDialog.dialog("open");
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

        var finishChoice = function() {
            that.alert.html("");
            that.clearSelection();
            that.decisionFunction(id, "" + selectedCardIds);
        };

        if (min == 0) {
            this.alert.append("<br><button id='DONE'>DONE</button>");
            $("#DONE").button().click(function() {
                finishChoice();
            });
        }

        this.selectionFunction = function(cardId) {
            selectedCardIds.push(cardId);
            if (selectedCardIds.length == min) {
                this.alert.append("<br><button id='DONE'>DONE</button>");
                $("#DONE").button().click(function() {
                    finishChoice();
                });
            }

            if (selectedCardIds.length == max) {
                if (this.settingsAutoAccept) {
                    finishChoice();
                } else {
                    $(".selectableCard").removeClass("selectableCard");
                    $(".card:cardId(" + cardId + ")").addClass("selectedCard");
                }
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

        var characterHasMinion = false;
        $(".card").each(function () {
            if ($(this).data("card").assign == previousCharacterId) characterHasMinion = true;
        });

        if (!characterHasMinion) {
            delete this.shadowAssignGroups[previousCharacterId];
            delete this.freePeopleAssignGroups[previousCharacterId];

            this.assignGroupDivs[this.assignGroupDivs.length - 1].remove();
            this.assignGroupDivs.splice(this.assignGroupDivs.length - 1, 1);
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
                that.layoutUI(true);
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

    clearSelection: function() {
        $(".selectableCard").removeClass("selectableCard").data("action", null);
        $(".selectedCard").removeClass("selectedCard");
        this.selectionFunction = null;
    },

    arbitraryDialogResize: function() {
        var width = this.cardActionDialog.width() + 20;
        var height = this.cardActionDialog.height() + 20;
        this.specialGroup.setBounds(this.padding, this.padding, width - 2 * this.padding, height - 2 * this.padding);
    }
});
