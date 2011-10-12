var GameAnimations = Class.extend({
    game: null,
    playEventDuration: 1500,
    cardAffectsCardDuration: 1200,

    init: function(gameUI) {
        this.game = gameUI;
    },

    eventPlayed: function(element) {
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
    },

    cardAffectsCard: function(element) {
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