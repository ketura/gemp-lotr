var GameAnimations = Class.extend({
    game: null,
    animationEffectDiv: null,


    init: function(gameUI) {
        this.game = gameUI;

        this.animationEffectDiv = $("<div id='animation'></div>");
        this.animationEffectDiv.hide();
        $("#main").append(this.animationEffectDiv);
    },

    cardAffectsCard: function(element) {
        var participantId = element.getAttribute("participantId");
        var blueprintId = element.getAttribute("blueprintId");
        var targetCardId = element.getAttribute("targetCardId");

        this.animationEffectDiv.queue("anim",
                function(next) {
                    var targetCard = $(".card:cardId(" + targetCardId + ")");
                    if (targetCard.length > 0) {
                        var card = new Card(blueprintId, "ANIMATION", "anim", participantId);
                        var cardDiv = createCardDiv(card.imageUrl);
                        cardDiv.data("card", card);

                        targetCard = targetCard[0];
                        $("#animation").html(cardDiv);
                        $("#animation").show();
                        var targetCardWidth = $(targetCard).width();
                        var targetCardHeight = $(targetCard).height();

                        var maxDimension = Math.max(targetCardWidth, targetCardHeight);

                        var borderWidth = Math.floor(maxDimension / 30);
                        var endBorderWidth = Math.floor(maxDimension * 2 / 30);
                        $("#animation").css({
                            position: "absolute",
                            left: $(targetCard).position().left,
                            top: $(targetCard).position().top,
                            width: targetCardWidth,
                            height: targetCardHeight,
                            "z-index": 100,
                            opacity: 1});
                        $("#animation").animate(
                        {
                            opacity: 0,
                            left: "-=" + (targetCardWidth / 2),
                            top: "-=" + (targetCardHeight / 2),
                            width: "+=" + targetCardWidth,
                            height: "+=" + targetCardHeight},
                        {
                            duration: 800,
                            easing: "easeInCubic",
                            queue: false,
                            complete: next
                        });
                    }
                }).queue("anim",
                function(next) {
                    $("#animation").hide();
                    next();
                }).dequeue("anim");
    }
});