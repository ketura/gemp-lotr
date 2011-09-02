var CardGroup = Class.extend({
    x: null,
    y: null,
    width: null,
    height: null,
    belongTestFunc: null,
    padding: 10,
    cardId: null,

    init: function(belongTest) {
        this.belongTestFunc = belongTest;
    },

    setBounds: function(x, y, width, height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.layoutCards();
    },

    layoutCards: function() {
        alert("This should be overriden by the extending classes");
    },

    layoutCard: function(cardElem, x, y, width, height, index) {
        cardElem.css({position: "absolute", left: x + "px", top: y + "px", width: width, height: height, zIndex: index });

        var borderOverlay = $(".borderOverlay", cardElem);
        borderOverlay.css({position: "absolute", left: 0 + "px", top: 0 + "px", width: width - 4, height: height - 4});

        var tokenOverlay = $(".tokenOverlay", cardElem);
        tokenOverlay.css({position: "absolute", left: 0 + "px", top: 0 + "px", width: width, height: height})
                .html("");

        var tokens = cardElem.data("card").tokens;
        if (tokens != null) {
            var tokenTypesCount = 0;

            for (var token in tokens)
                if (tokens.hasOwnProperty(token) && tokens[token] > 0)
                    tokenTypesCount++;

            var tokenIndex = 1;
            for (var token in tokens)
                if (tokens.hasOwnProperty(token) && tokens[token] > 0) {
                    var tokenX = (tokenIndex * (width / (tokenTypesCount + 1))) - 11;
                    var tokenCount = tokens[token];

                    for (var i = 0; i < tokenCount; i++) {
                        var tokenElem = $("<img src='images/" + token.toLowerCase() + ".png'></img>").css({position: "absolute", left: tokenX + "px", top: 15 * (1 + i) + "px"});
                        tokenOverlay.append(tokenElem);
                    }
                    tokenIndex ++;
                }
        }
    }
});

var AdvPathCardGroup = CardGroup.extend({
    init: function() {
        this._super(
                function(card) {
                    return (card.zone == "ADVENTURE_PATH");
                });
    },

    layoutCards: function() {
        var cardsToLayout = new Array();
        var that = this;
        $(".card").each(function(index) {
            var card = $(this).data("card");
            if (that.belongTestFunc(card)) {
                cardsToLayout.push($(this));
            }
        });

        var cardCount = cardsToLayout.length;
        var totalHeight = 0;

        for (var cardId in cardsToLayout)
            totalHeight += cardsToLayout[cardId].data("card").getHeightForWidth(this.width);

        var resultPadding = Math.min(this.padding, (this.height - totalHeight) / (cardCount - 1));

        var x = this.x;
        var y = this.y;
        var index = 0;
        for (var cardId in cardsToLayout) {
            var cardElem = cardsToLayout[cardId];
            var cardData = cardsToLayout[cardId].data("card");
            var cardHeight = (cardElem.data("card").getHeightForWidth(this.width));
            this.layoutCard(cardElem, x, y, this.width, cardHeight, index);

            for (var i = 0; i < cardData.attachedCards.length; i++) {
                this.layoutCard(cardData.attachedCards[i], x + (this.width - cardHeight) / 2, y - (this.width - cardHeight) / 2, cardHeight, this.width, index);
                index++;
            }

            y += cardHeight + resultPadding;
            index++;
        }
    }
});

var NormalCardGroup = CardGroup.extend({
    descDiv: null,

    init: function(text, belongTest) {
        this._super(belongTest);
        if (text != null) {
            this.descDiv = $("<div>" + text + "</div>");
            $("#main").append(this.descDiv);
        }
    },

    setBounds: function(x, y, width, height) {
        this._super(x, y, width, height);
        if (this.descDiv != null)
            this.descDiv.css({left:x + "px", top:y + "px", width: width, height: height, "background-color":"#ffffff", position: "absolute", "text-align": "center"});
    },

    layoutCards: function() {
        var cardsToLayout = new Array();
        var that = this;
        $(".card").each(function(index) {
            var card = $(this).data("card");
            if (that.belongTestFunc(card)) {
                cardsToLayout.push($(this));
            }
        });

        var cardCount = cardsToLayout.length;
        var totalWidth = 0;

        for (var cardId in cardsToLayout) {
            var cardData = cardsToLayout[cardId].data("card");
            totalWidth += Math.floor(cardData.getWidthForHeight(this.height) * (1 + (0.2 * cardData.attachedCards.length)));
        }

        var resultPadding = Math.min(this.padding, (this.width - totalWidth) / (cardCount - 1));
        var x = this.x;
        var y = this.y;
        var index = 0;
        for (var cardId in cardsToLayout) {
            var cardElem = cardsToLayout[cardId];
            var cardData = cardsToLayout[cardId].data("card");
            var cardWidth = cardData.getWidthForHeight(this.height);

            for (var i = 0; i < cardData.attachedCards.length; i++) {
                this.layoutCard(cardData.attachedCards[i], x, y, cardWidth, this.height, index);
                x += Math.floor(cardWidth * 0.2);
                index++;
            }
            this.layoutCard(cardElem, x, y, cardWidth, this.height, index);
            x += cardWidth + resultPadding;
            index++;
        }
    }
});