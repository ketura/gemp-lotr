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

    cardBelongs: function(cardData) {
        return this.belongTestFunc(cardData);
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

        layoutTokens(cardElem);
    }
});

var VerticalBarGroup = CardGroup.extend({
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

var AdvPathCardGroup = CardGroup.extend({
    positions: null,
    currentPlayerIndex: null,

    init: function(container) {
        this._super(container,
                function(card) {
                    return (card.zone == "ADVENTURE_PATH");
                });
    },

    setPositions: function(positions) {
        this.positions = positions;
    },

    setCurrentPlayerIndex: function(index) {
        this.currentPlayedIndex = index;
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
                    if (this.positions[i] == cardData.siteNumber) {
                        if (i == this.currentPlayedIndex)
                            cardData.tokens["" + (i + 1) + "-a"] = 1;
                        else
                            cardData.tokens["" + (i + 1) + "-i"] = 1;
                    }
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
            var cardWidth = cardData.getWidthForMaxDimension(this.height);
            var attachmentWidths = 0;
            for (var i = 0; i < cardData.attachedCards.length; i++)
                attachmentWidths += cardData.attachedCards[i].data("card").getWidthForMaxDimension(this.height) * 0.2;
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
            var cardWidth = cardData.getWidthForMaxDimension(rowHeight);
            var attachmentWidths = 0;
            for (var i = 0; i < cardData.attachedCards.length; i++)
                attachmentWidths += cardData.attachedCards[i].data("card").getWidthForMaxDimension(rowHeight) * 0.2;
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
            var cardWidth = cardData.getWidthForMaxDimension(height);

            for (var i = 0; i < cardData.attachedCards.length; i++) {
                var attachedCardData = cardData.attachedCards[i].data("card");
                var attachedCardWidth = attachedCardData.getWidthForMaxDimension(height);
                this.layoutCard(cardData.attachedCards[i], this.x + x, this.y + y, attachedCardWidth, attachedCardData.getHeightForWidth(attachedCardWidth), index);
                x += Math.floor(attachedCardWidth * 0.2);
                index++;
            }
            this.layoutCard(cardElem, this.x + x, this.y + y, cardWidth, cardData.getHeightForWidth(cardWidth), index);
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
            var cardWidth = cardData.getWidthForMaxDimension(rowHeight);

            var attachmentWidths = 0;
            for (var i = 0; i < cardData.attachedCards.length; i++)
                attachmentWidths += cardData.attachedCards[i].data("card").getWidthForMaxDimension(rowHeight) * 0.2;
            var cardWidthWithAttachments = cardWidth + attachmentWidths;
            if (x + cardWidthWithAttachments > this.width) {
                row++;
                x = 0;
                y = row * (rowHeight + this.padding);
            }

            for (var i = 0; i < cardData.attachedCards.length; i++) {
                var attachedCardData = cardData.attachedCards[i].data("card");
                var attachedCardWidth = attachedCardData.getWidthForMaxDimension(rowHeight);
                this.layoutCard(cardData.attachedCards[i], this.x + x, this.y + y, attachedCardWidth, attachedCardData.getHeightForWidth(attachedCardWidth), index);
                x += Math.floor(attachedCardWidth * 0.2);
                index++;
            }
            this.layoutCard(cardElem, this.x + x, this.y + y, cardWidth, cardData.getHeightForWidth(cardWidth), index);
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
    tokenOverlay.css({position: "absolute", left: 0 + "px", top: 0 + "px", width: width, height: height});

    $(".foilOverlay", cardElem).css({position: "absolute", left: 0 + "px", top: 0 + "px", width: width, height: height});

    var maxDimension = Math.max(width, height);
    var borderWidth = Math.floor(maxDimension / 30);

    var borderOverlay = $(".borderOverlay", cardElem);
    borderOverlay.css({position: "absolute", left: 0 + "px", top: 0 + "px", width: width - 2 * borderWidth, height: height - 2 * borderWidth, "border-width": borderWidth + "px"});
    var sizeListeners = cardElem.data("sizeListeners");
    if (sizeListeners != null)
        for (var i = 0; i < sizeListeners.length; i++)
            sizeListeners[i].sizeChanged(cardElem, width, height);
}

function layoutTokens(cardElem) {
    var width = cardElem.width();
    var height = cardElem.height();
    var maxDimension = Math.max(width, height);

    var tokenOverlay = $(".tokenOverlay", cardElem);

    var tokenSize = Math.floor(maxDimension / 13) * 2;

    // Remove all existing tokens
    $(".token", tokenOverlay).remove();

    var tokens = cardElem.data("card").tokens;
    if (tokens != null) {
        var tokenInColumnMax = 10;
        var tokenColumns = 0;

        for (var token in tokens)
            if (tokens.hasOwnProperty(token) && tokens[token] > 0) {
                tokenColumns += (1 + Math.floor((tokens[token] - 1) / tokenInColumnMax));
            }

        var tokenIndex = 1;
        for (var token in tokens)
            if (tokens.hasOwnProperty(token) && tokens[token] > 0) {
                var tokenCount = tokens[token];

                var tokenX = (tokenIndex * (width / (tokenColumns + 1))) - (tokenSize / 2);
                var tokenInColumnIndex = 0;
                for (var i = 0; i < tokenCount; i++) {
                    if (tokenInColumnIndex == tokenInColumnMax) {
                        tokenInColumnIndex = 0;
                        tokenIndex++;
                        tokenX = (tokenIndex * (width / (tokenColumns + 1))) - (tokenSize / 2)
                    }
                    var tokenY = Math.floor((maxDimension / 13) * (1 + tokenInColumnIndex));

                    var tokenElem = $("<img class='token' src='images/tokens/" + token.toLowerCase() + ".png' width='" + tokenSize + "' height='" + tokenSize + "'></img>").css({position: "absolute", left: tokenX + "px", top: tokenY + "px"});
                    tokenOverlay.append(tokenElem);
                    tokenInColumnIndex++;
                }
                tokenIndex ++;
            }
    }
}
