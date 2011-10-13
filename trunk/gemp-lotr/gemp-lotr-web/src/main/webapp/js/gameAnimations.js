var GameAnimations = Class.extend({
    game: null,
    playEventDuration: 1500,
    putCardIntoPlayDuration: 1500,
    cardAffectsCardDuration: 1200,

    init: function(gameUI) {
        this.game = gameUI;
    },

    eventPlayed: function(element, animate) {
        if (animate) {
            var that = this;

            var participantId = element.getAttribute("participantId");
            var blueprintId = element.getAttribute("blueprintId");

            // Play-out game event animation only if it's not the player who initiated it
            if (this.game.spectatorMode || (participantId != this.game.bottomPlayerId)) {
                var card = new Card(blueprintId, "ANIMATION", "anim", participantId);
                var cardDiv = createSimpleCardDiv(card.imageUrl);

                $("#main").queue(
                        function(next) {
                            cardDiv.data("card", card);
                            $("#main").append(cardDiv);

                            var gameWidth = $("#main").width();
                            var gameHeight = $("#main").height();

                            var cardHeight = (gameHeight / 2);
                            var cardWidth = card.getWidthForHeight(cardHeight);

                            $(cardDiv).css(
                                    {
                                        position: "absolute",
                                        left: (gameWidth / 2 - cardWidth / 4),
                                        top: gameHeight * (3 / 8),
                                        width: cardWidth / 2,
                                        height: cardHeight / 2,
                                        "z-index": 100,
                                        opacity: 0});

                            $(cardDiv).animate(
                                    {
                                        left: "-=" + cardWidth / 4,
                                        top: "-=" + (gameHeight / 8),
                                        width: "+=" + (cardWidth / 2),
                                        height: "+=" + (cardHeight / 2),
                                        opacity: 1},
                                    {
                                        duration: that.playEventDuration / 8,
                                        easing: "linear",
                                        queue: false,
                                        complete: next});
                        }).queue(
                        function(next) {
                            setTimeout(next, that.playEventDuration * (5 / 8));
                        }).queue(
                        function(next) {
                            $(cardDiv).animate(
                                    {
                                        opacity: 0},
                                    {
                                        duration: that.playEventDuration / 4,
                                        easing: "easeOutQuart",
                                        queue: false,
                                        complete: next});
                        }).queue(
                        function(next) {
                            $(cardDiv).remove();
                            next();
                        });
            }
        }
    },

    cardAffectsCard: function(element, animate) {
        if (animate) {
            var that = this;

            var participantId = element.getAttribute("participantId");
            var blueprintId = element.getAttribute("blueprintId");
            var targetCardIds = element.getAttribute("otherCardIds").split(",");

            // Play-out card affects card animation only if it's not the player who initiated it
            if (this.game.spectatorMode || (participantId != this.game.bottomPlayerId)) {
                $("#main").queue(
                        function(next) {
                            for (var i = 0; i < targetCardIds.length; i++) {
                                var targetCardId = targetCardIds[i];

                                var card = new Card(blueprintId, "ANIMATION", "anim" + i, participantId);
                                var cardDiv = createSimpleCardDiv(card.imageUrl);

                                var targetCard = $(".card:cardId(" + targetCardId + ")");
                                if (targetCard.length > 0) {
                                    cardDiv.data("card", card);
                                    $("#main").append(cardDiv);

                                    targetCard = targetCard[0];
                                    var targetCardWidth = $(targetCard).width();
                                    var targetCardHeight = $(targetCard).height();

                                    $(cardDiv).css(
                                            {
                                                position: "absolute",
                                                left: $(targetCard).position().left,
                                                top: $(targetCard).position().top,
                                                width: targetCardWidth,
                                                height: targetCardHeight,
                                                "z-index": 100,
                                                opacity: 1});
                                    $(cardDiv).animate(
                                            {
                                                opacity: 0,
                                                left: "-=" + (targetCardWidth / 2),
                                                top: "-=" + (targetCardHeight / 2),
                                                width: "+=" + targetCardWidth,
                                                height: "+=" + targetCardHeight},
                                            {
                                                duration: that.cardAffectsCardDuration,
                                                easing: "easeInQuart",
                                                queue: false,
                                                complete: null});
                                }

                                setTimeout(next, that.cardAffectsCardDuration);
                            }
                        }).queue(
                        function(next) {
                            $(".card").each(
                                    function() {
                                        var cardData = $(this).data("card");
                                        if (cardData.zone == "ANIMATION") {
                                            $(this).remove();
                                        }
                                    }
                            );
                            next();
                        });
            }
        }
    },

    putCardInPlay: function(element, animate) {
        var participantId = element.getAttribute("participantId");
        var cardId = element.getAttribute("cardId");

        var that = this;
        $("#main").queue(
                function(next) {
                    var blueprintId = element.getAttribute("blueprintId");
                    var zone = element.getAttribute("zone");
                    var targetCardId = element.getAttribute("targetCardId");
                    var controllerId = element.getAttribute("controllerId");

                    if (controllerId != null)
                        participantId = controllerId;

                    var card;
                    if (zone == "ADVENTURE_PATH")
                        card = new Card(blueprintId, zone, cardId, participantId, element.getAttribute("index"));
                    else
                        card = new Card(blueprintId, zone, cardId, participantId);

                    var cardDiv = that.game.createCardDiv(card, null, card.isFoil());
                    if (zone == "DISCARD")
                        that.game.discardPileDialogs[participantId].append(cardDiv);
                    else if (zone == "DEAD")
                        that.game.deadPileDialogs[participantId].append(cardDiv);
                    else
                        $("#main").append(cardDiv);

                    if (targetCardId != null) {
                        var targetCardData = $(".card:cardId(" + targetCardId + ")").data("card");
                        targetCardData.attachedCards.push(cardDiv);
                    }

                    next();
                });

        if (animate && (this.game.spectatorMode || (participantId != this.game.bottomPlayerId))) {
            $("#main").queue(
                    function(next) {
                        that.game.layoutUI(false);

                        var cardDiv = $(".card:cardId(" + cardId + ")");
                        var card = cardDiv.data("card");

                        var oldZIndex = cardDiv.css("z-index");
                        var oldLeft = cardDiv.css("left");
                        var oldTop = cardDiv.css("top");
                        var oldWidth = cardDiv.css("width");
                        var oldHeight = cardDiv.css("height");

                        // Now we begin the animation
                        var gameWidth = $("#main").width();
                        var gameHeight = $("#main").height();

                        var cardHeight = (gameHeight / 2);
                        var cardWidth = card.getWidthForHeight(cardHeight);

                        $(cardDiv).css(
                                {
                                    position: "absolute",
                                    left: (gameWidth / 2 - cardWidth / 4),
                                    top: gameHeight * (3 / 8),
                                    width: cardWidth / 2,
                                    height: cardHeight / 2,
                                    "z-index": 100,
                                    opacity: 0});

                        $(cardDiv).animate(
                                {
                                    left: "-=" + cardWidth / 4,
                                    top: "-=" + (gameHeight / 8),
                                    width: "+=" + (cardWidth / 2),
                                    height: "+=" + (cardHeight / 2),
                                    opacity: 1},
                                {
                                    duration: that.putCardIntoPlayDuration / 8,
                                    easing: "linear",
                                    queue: false,
                                    complete: null});
                        $(cardDiv).animate(
                                { },
                                {
                                    duration: that.putCardIntoPlayDuration * (5 / 8),
                                    easing: "linear",
                                    complete: null});
                        $(cardDiv).animate(
                                {
                                    left: oldLeft,
                                    top: oldTop,
                                    width: oldWidth,
                                    height: oldHeight},
                                {
                                    duration: that.putCardIntoPlayDuration / 4,
                                    easing: "linear",
                                    complete: null});
                        $(cardDiv).animate(
                                {
                                    "z-index": oldZIndex},
                                {
                                    duration: 0,
                                    easing: "linear",
                                    complete: next});
                    });
        }
    },

    moveCardInPlay: function(element, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    var cardId = element.getAttribute("cardId");
                    var zone = element.getAttribute("zone");
                    var targetCardId = element.getAttribute("targetCardId");
                    var participantId = element.getAttribute("participantId");
                    var controllerId = element.getAttribute("controllerId");

                    if (controllerId != null)
                        participantId = controllerId;

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
                    cardData.owner = participantId;

                    if (targetCardId != null) {
                        // attach to new card if it's attached
                        var targetCardData = $(".card:cardId(" + targetCardId + ")").data("card");
                        targetCardData.attachedCards.push(card);
                    }

                    next();
                });
    },

    removeCardFromPlay: function(element, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    var cardRemovedIds = element.getAttribute("otherCardIds").split(",");

                    for (var i = 0; i < cardRemovedIds.length; i++) {
                        var cardId = cardRemovedIds[i];
                        var card = $(".card:cardId(" + cardId + ")");

                        if (card != null) {
                            var cardData = card.data("card");
                            if (cardData.zone == "ATTACHED" || cardData.zone == "STACKED") {
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
                        }
                    }

                    next();
                });
    },

    gamePhaseChange: function(element, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    var phase = element.getAttribute("phase");

                    $(".phase").removeClass("current");
                    $(".phase#" + phase).addClass("current");

                    next();
                });
    },

    twilightPool: function(element, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    var count = element.getAttribute("count");

                    $(".twilightPool").html("" + count);

                    next();
                });
    },

    turnChange: function(element, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    var playerId = element.getAttribute("participantId");
                    var playerIndex = that.game.getPlayerIndex(playerId);

                    that.game.currentPlayerId = playerId;

                    $(".player").each(function(index) {
                        if (index == playerIndex)
                            $(this).addClass("current");
                        else
                            $(this).removeClass("current");
                    });

                    next();
                });
    },

    addAssignment: function(element, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    var cardId = element.getAttribute("cardId");
                    var opposingCardIds = element.getAttribute("otherCardIds").split(",");

                    for (var i = 0; i < opposingCardIds.length; i++) {
                        if ($(".card:cardId(" + opposingCardIds[i] + ")").data("card").assign != cardId)
                            that.game.assignMinion(opposingCardIds[i], cardId);
                    }

                    next();
                });
    },

    removeAssignment: function(element, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    var cardId = element.getAttribute("cardId");

                    $(".card").each(function() {
                        var cardData = $(this).data("card");
                        if (cardData.assign == cardId)
                            that.game.unassignMinion(cardData.cardId);
                    });

                    next();
                });
    },

    startSkirmish: function(element, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    var cardId = element.getAttribute("cardId");
                    var opposingCardIds = element.getAttribute("otherCardIds").split(",");

                    $(".card:cardId(" + opposingCardIds + ")").each(function() {
                        $(this).data("card").skirmish = true;
                    });

                    if (cardId != null)
                        $(".card:cardId(" + cardId + ")").each(function() {
                            $(this).data("card").skirmish = true;
                        });

                    that.game.fpStrengthDiv = $("<div class='fpStrength'></div>");
                    that.game.shadowStrengthDiv = $("<div class='shadowStrength'></div>");

                    that.game.skirmishGroupDiv = $("<div class='ui-widget-content skirmish'></div>");
                    that.game.skirmishGroupDiv.css({"border-radius": "7px", "border-color": "#ff0000"});
                    that.game.skirmishGroupDiv.append(that.game.fpStrengthDiv);
                    that.game.skirmishGroupDiv.append(that.game.shadowStrengthDiv);
                    $("#main").append(that.game.skirmishGroupDiv);

                    next();
                });
    },

    endSkirmish: function(animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    that.game.skirmishGroupDiv.remove();
                    that.game.skirmishGroupDiv = null;
                    that.game.fpStrengthDiv = null;
                    that.game.shadowStrengthDiv = null;

                    $(".card").each(function() {
                        var cardData = $(this).data("card");
                        if (cardData.skirmish == true) {
                            delete cardData.skirmish;
                        }
                    });

                    next();
                });
    },

    addTokens: function(element, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
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

                    next();
                });
    },

    removeTokens: function(element, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
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

                    next();
                });
    },

    playerPosition: function(element, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    var participantId = element.getAttribute("participantId");
                    var position = element.getAttribute("index");

                    if (that.game.playerPositions == null)
                        that.game.playerPositions = new Array();

                    var index = that.game.getPlayerIndex(participantId);
                    that.game.playerPositions[index] = position;

                    that.game.advPathGroup.setPositions(that.game.playerPositions);

                    next();
                });
    },

    zoneSize: function(element, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    var playerId = element.getAttribute("participantId");
                    var zone = element.getAttribute("zone");
                    var count = element.getAttribute("count");

                    if (zone == "HAND")
                        $("#hand" + that.game.getPlayerIndex(playerId)).text("Hand: " + count);
                    else if (zone == "DISCARD")
                        $("#discard" + that.game.getPlayerIndex(playerId)).text("Discard: " + count);
                    else if (zone == "DEAD")
                        $("#deadPile" + that.game.getPlayerIndex(playerId)).text("Dead pile: " + count);
                    else if (zone == "DECK")
                        $("#deck" + that.game.getPlayerIndex(playerId)).text("Deck: " + count);

                    next();
                });
    },

    message: function(element, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    var message = element.getAttribute("message");
                    if (that.game.chatBox != null)
                        that.game.chatBox.appendMessage(message, "gameMessage");

                    next();
                });
    },

    warning: function(element, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    var message = element.getAttribute("message");
                    if (that.game.chatBox != null)
                        that.game.chatBox.appendMessage(message, "warningMessage");

                    next();
                });
    },

    showSkirmishValues: function(skirmish, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    that.game.fpStrengthDiv.text(skirmish[0].getAttribute("fpStrength"));
                    that.game.shadowStrengthDiv.text(skirmish[0].getAttribute("shadowStrength"));

                    next();
                });
    },

    processDecision: function(decision, animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    var decisionType = decision.getAttribute("type");
                    if (decisionType == "INTEGER") {
                        that.game.integerDecision(decision);
                    } else if (decisionType == "MULTIPLE_CHOICE") {
                        that.game.multipleChoiceDecision(decision);
                    } else if (decisionType == "ARBITRARY_CARDS") {
                        that.game.arbitraryCardsDecision(decision);
                    } else if (decisionType == "ACTION_CHOICE") {
                        that.game.actionChoiceDecision(decision);
                    } else if (decisionType == "CARD_ACTION_CHOICE") {
                        that.game.cardActionChoiceDecision(decision);
                    } else if (decisionType == "CARD_SELECTION") {
                        that.game.cardSelectionDecision(decision);
                    } else if (decisionType == "ASSIGN_MINIONS") {
                        that.game.assignMinionsDecision(decision);
                    }

                    that.game.layoutUI(false);

                    next();
                });
    },

    updateGameState: function(animate) {
        var that = this;
        $("#main").queue(
                function(next) {
                    setTimeout(
                            function() {
                                that.game.updateGameState();
                            }, 1000);

                    that.game.layoutUI(false);

                    next();
                });
    },

    windowResized: function() {
        var that = this;
        $("#main").queue(
                function(next) {
                    that.game.layoutUI(true);
                    next();
                });
    }
});