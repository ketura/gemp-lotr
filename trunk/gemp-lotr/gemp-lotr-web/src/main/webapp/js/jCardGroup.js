var CardGroup = Class.extend({
    container: null,
    x: null,
    y: null,
    width: null,
    height: null,
    belongTestFunc: null,
    padding: 5,
    cardId: null,
    maxCardHeight: null,
    descDiv: null,

    init: function(container, belongTest, createDiv) {
        this.container = container;
        this.belongTestFunc = belongTest;

        if (createDiv === undefined || createDiv) {
            this.descDiv = $("<div class='ui-widget-content'></div>");
            this.descDiv.css({"border-radius": "7px"});

            container.append(this.descDiv);
        }
    },

    setBounds: function(x, y, width, height) {
        this.x = x + 3;
        this.y = y + 3;
        this.width = width - 6;
        this.height = height - 6;
        if (this.descDiv != null)
            this.descDiv.css({left:x + "px", top:y + "px", width: width, height: height, position: "absolute"});
        this.layoutCards();
    },

    layoutCards: function() {
        alert("This should be overriden by the extending classes");
    },

    layoutCard: function(cardElem, x, y, width, height, index) {
        layoutCardElem(cardElem, x, y, width, height, index);

        var maxDimension = Math.max(width, height);

        var tokenOverlay = $(".tokenOverlay", cardElem);

        var tokenSize = Math.floor(maxDimension / 12) * 2;

        var tokens = cardElem.data("card").tokens;
        if (tokens != null) {
            var tokenTypesCount = 0;

            for (var token in tokens)
                if (tokens.hasOwnProperty(token) && tokens[token] > 0)
                    tokenTypesCount++;

            var tokenIndex = 1;
            for (var token in tokens)
                if (tokens.hasOwnProperty(token) && tokens[token] > 0) {
                    var tokenX = (tokenIndex * (width / (tokenTypesCount + 1))) - (tokenSize / 2);
                    var tokenCount = tokens[token];

                    for (var i = 0; i < tokenCount; i++) {
                        var tokenElem = $("<img src='images/tokens/" + token.toLowerCase() + ".png' width='" + tokenSize + "' height='" + tokenSize + "'></img>").css({position: "absolute", left: tokenX + "px", top: (1 * tokenSize / 2) * (1 + i) + "px"});
                        tokenOverlay.append(tokenElem);
                    }
                    tokenIndex ++;
                }
        }
    }
});

var AdvPathCardGroup = CardGroup.extend({
    positions: null,

    init: function(container) {
        this._super(container,
                function(card) {
                    return (card.zone == "ADVENTURE_PATH");
                });
    },

    setPositions: function(positions) {
        this.positions = positions;
    },

    layoutCards: function() {
        var cardsToLayout = new Array();
        var that = this;
        $(".card", this.container).each(function(index) {
            var card = $(this).data("card");
            if (that.belongTestFunc(card)) {
                cardsToLayout.push($(this));
            }
        });

        cardsToLayout.sort(
                function(first, second) {
                    return (first.data("card").siteNumber - second.data("card").siteNumber);
                }
                );

        var cardCount = cardsToLayout.length;
        var totalHeight = 0;

        for (var cardId in cardsToLayout)
            totalHeight += cardsToLayout[cardId].data("card").getHeightForWidth(this.width);

        var resultPadding = Math.min(this.padding, (this.height - totalHeight) / (cardCount - 1));

        var x = this.x;
        var y = this.y;
        var index = 10;
        for (var cardId in cardsToLayout) {
            var cardElem = cardsToLayout[cardId];
            var cardData = cardsToLayout[cardId].data("card");
            var cardHeight = (cardElem.data("card").getHeightForWidth(this.width));

            cardData.tokens = {};
            if (this.positions != null) {
                for (var i = 0; i < this.positions.length; i++)
                    if (this.positions[i] == cardData.siteNumber)
                        cardData.tokens["" + (i + 1)] = 1;
            }

            if (cardData.attachedCards.length == 1) {
                this.layoutCard(cardData.attachedCards[0], x + (this.width - cardHeight) / 2, y - (this.width - cardHeight) / 2, cardHeight, this.width, index);
                index++;
            } else {
                for (var i = 0; i < cardData.attachedCards.length; i++) {
                    this.layoutCard(cardData.attachedCards[i], x + i * (this.width - cardHeight) / (cardData.attachedCards.length - 1), y - (this.width - cardHeight) / 2, cardHeight, this.width, index);
                    index++;
                }
            }

            this.layoutCard(cardElem, x, y, this.width, cardHeight, index);

            y += cardHeight + resultPadding;
            index++;
        }
    }
});

var NormalCardGroup = CardGroup.extend({

    init: function(container, belongTest, createDiv) {
        this._super(container, belongTest, createDiv);
    },

    layoutCards: function() {
        var cardsToLayout = new Array();
        var that = this;
        $(".card", this.container).each(function(index) {
            var card = $(this).data("card");
            if (that.belongTestFunc(card)) {
                cardsToLayout.push($(this));
            }
        });

        var cardCount = cardsToLayout.length;

        var oneRowHeight = this.getHeightForLayoutInOneRow(cardsToLayout);
        if (oneRowHeight * 2 + this.padding <= this.height) {
            var result;
            var rows = 1;
            do {
                rows++;
                result = this.tryIfCanLayoutInRows(rows, cardsToLayout);
            } while (!result);
            this.layoutInRows(rows, cardsToLayout);
        } else {
            this.layoutInRow(cardsToLayout, oneRowHeight);
        }
    },

    getHeightForLayoutInOneRow: function(cardsToLayout) {
        var totalWidth = 0;
        for (var cardId in cardsToLayout) {
            var cardData = cardsToLayout[cardId].data("card");
            var cardWidth = cardData.getWidthForHeight(this.height);
            var attachmentWidths = 0;
            for (var i = 0; i < cardData.attachedCards.length; i++)
                attachmentWidths += cardData.attachedCards[i].data("card").getWidthForHeight(this.height) * 0.2;
            var cardWidthWithAttachments = cardWidth + attachmentWidths;
            totalWidth += cardWidthWithAttachments;
        }
        var widthWithoutPadding = this.width - (this.padding * (cardsToLayout.length - 1));
        if (totalWidth > widthWithoutPadding) {
            return Math.floor(this.height / (totalWidth / widthWithoutPadding));
        } else {
            return this.height;
        }
    },

    tryIfCanLayoutInRows: function(rowCount, cardsToLayout) {
        var rowHeight = (this.height - (this.padding * (rowCount - 1))) / rowCount;
        if (this.maxCardHeight != null)
            rowHeight = Math.min(this.maxCardHeight, rowHeight);
        var totalWidth = 0;
        var row = 0;
        for (var cardId in cardsToLayout) {
            var cardData = cardsToLayout[cardId].data("card");
            var cardWidth = cardData.getWidthForHeight(rowHeight);
            var attachmentWidths = 0;
            for (var i = 0; i < cardData.attachedCards.length; i++)
                attachmentWidths += cardData.attachedCards[i].data("card").getWidthForHeight(rowHeight) * 0.2;
            var cardWidthWithAttachments = cardWidth + attachmentWidths;
            totalWidth += cardWidthWithAttachments;
            if (totalWidth > this.width) {
                row++;
                if (row >= rowCount)
                    return false;
                totalWidth = cardWidthWithAttachments;
            }
            totalWidth += this.padding;
        }
        return true;
    },

    layoutInRow: function(cardsToLayout, height) {
        if (this.maxCardHeight != null)
            height = Math.min(this.maxCardHeight, height);
        var x = 0;
        var row = 0;
        var y = Math.floor((this.height - height) / 2);

        for (var cardId in cardsToLayout) {
            var index = 10;
            var cardElem = cardsToLayout[cardId];
            var cardData = cardsToLayout[cardId].data("card");
            var cardWidth = cardData.getWidthForHeight(height);

            for (var i = 0; i < cardData.attachedCards.length; i++) {
                var attachedCardWidth = cardData.attachedCards[i].data("card").getWidthForHeight(height);
                this.layoutCard(cardData.attachedCards[i], this.x + x, this.y + y, attachedCardWidth, height, index);
                x += Math.floor(attachedCardWidth * 0.2);
                index++;
            }
            this.layoutCard(cardElem, this.x + x, this.y + y, cardWidth, height, index);
            x += cardWidth;
            x += this.padding;
        }
    },

    layoutInRows: function(rowCount, cardsToLayout) {
        var rowHeight = (this.height - ((rowCount - 1) * this.padding)) / rowCount;
        if (this.maxCardHeight != null)
            rowHeight = Math.min(this.maxCardHeight, rowHeight);
        var x = 0;
        var row = 0;
        var y = 0;


        for (var cardId in cardsToLayout) {
            var index = 10;
            var cardElem = cardsToLayout[cardId];
            var cardData = cardsToLayout[cardId].data("card");
            var cardWidth = cardData.getWidthForHeight(rowHeight);

            var attachmentWidths = 0;
            for (var i = 0; i < cardData.attachedCards.length; i++)
                attachmentWidths += cardData.attachedCards[i].data("card").getWidthForHeight(rowHeight) * 0.2;
            var cardWidthWithAttachments = cardWidth + attachmentWidths;
            if (x + cardWidthWithAttachments > this.width) {
                row++;
                x = 0;
                y = row * (rowHeight + this.padding);
            }

            for (var i = 0; i < cardData.attachedCards.length; i++) {
                var attachedCardWidth = cardData.attachedCards[i].data("card").getWidthForHeight(rowHeight);
                this.layoutCard(cardData.attachedCards[i], this.x + x, this.y + y, attachedCardWidth, rowHeight, index);
                x += Math.floor(attachedCardWidth * 0.2);
                index++;
            }
            this.layoutCard(cardElem, this.x + x, this.y + y, cardWidth, rowHeight, index);
            x += cardWidth;
            if (x > this.width)
                return false;
            x += this.padding;
        }

        return true;
    }
});

function layoutCardElem(cardElem, x, y, width, height, index) {
    cardElem.css({position: "absolute", left: x + "px", top: y + "px", width: width, height: height, zIndex: index });

    var tokenOverlay = $(".tokenOverlay", cardElem);
    tokenOverlay.css({position: "absolute", left: 0 + "px", top: 0 + "px", width: width, height: height})
            .html("");

    $(".foilOverlay", cardElem).css({position: "absolute", left: 0 + "px", top: 0 + "px", width: width, height: height});

    var maxDimension = Math.max(width, height);
    var borderWidth = Math.floor(maxDimension / 30);

    var borderOverlay = $(".borderOverlay", cardElem);
    borderOverlay.css({position: "absolute", left: 0 + "px", top: 0 + "px", width: width - 2 * borderWidth, height: height - 2 * borderWidth, "border-width": borderWidth + "px"});
}