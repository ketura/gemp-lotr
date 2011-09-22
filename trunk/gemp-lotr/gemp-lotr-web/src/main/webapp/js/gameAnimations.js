var GameAnimations = Class.extend({
    game: null,
    playEventDuration: 500,
    cardAffectsCardDuration: 500,

    init: function(gameUI) {
        this.game = gameUI;
    },

    eventPlayed: function(element) {
        var that = this;

        var participantId = element.getAttribute("participantId");
        var blueprintId = element.getAttribute("blueprintId");

        var card = new Card(blueprintId, "ANIMATION", "anim", participantId);
        var cardDiv = createSimpleCardDiv(card.imageUrl);

        $("#main").queue(
                function(next) {
                    cardDiv.data("card", card);
                    $("#main").append(cardDiv);

                    var gameWidth = $("#main").width();
                    var gameHeight = $("#main").height();

                    var cardWidth = card.getWidthForHeight(gameHeight / 2);

                    $(cardDiv).css(
                    {
                        position: "absolute",
                        left: 0,
                        top: gameHeight / 4,
                        width: cardWidth,
                        height: gameHeight / 2,
                        "z-index": 100,
                        opacity: 0});

                    $(cardDiv).animate(
                    {
                        left: "+=" + (gameWidth - cardWidth)},
                    {
                        duration: that.playEventDuration,
                        easing: "linear",
                        queue: false,
                        complete: next});
                    $(cardDiv).animate(
                    {
                        opacity: 1},
                    {
                        duration: that.playEventDuration / 2,
                        easing: "easeOutQuart",
                        queue: false,
                        complete: function() {
                            $(cardDiv).animate(
                            {
                                opacity: 0},
                            {
                                duration: that.playEventDuration / 2,
                                easing: "easeInQuart",
                                queue: false
                            });
                        }});
                }).queue(
                function(next) {
                    $(cardDiv).remove();
                    next();
                });
    },

    cardAffectsCard: function(element) {
        var that = this;

        var participantId = element.getAttribute("participantId");
        var blueprintId = element.getAttribute("blueprintId");
        var targetCardId = element.getAttribute("targetCardId");

        var card = new Card(blueprintId, "ANIMATION", "anim", participantId);
        var cardDiv = createSimpleCardDiv(card.imageUrl);

        $("#main").queue(
                function(next) {
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
                            complete: next});
                    } else {
                        next();
                    }
                }).queue(
                function(next) {
                    $(cardDiv).remove();
                    next();
                });
    }
});