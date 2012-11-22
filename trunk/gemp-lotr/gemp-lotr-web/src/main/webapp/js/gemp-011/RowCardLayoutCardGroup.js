var RowCardLayoutCardGroup = CardGroup.extend({
    padding: 3,
    zIndexBase: 0,
    maxCardHeight: null,

    init: function (cardContainerDiv, cardContainFunc) {
        this._super(cardContainerDiv, cardContainFunc);
    },

    setMaxCardHeight: function(maxCardHeight) {
        this.maxCardHeight = maxCardHeight;
    },

    setZIndexBase: function(zIndexBase) {
        this.zIndexBase = zIndexBase;
    },

    getCardHeightScale: function(cardDiv, cardId, props) {
        return 1;
    },

    getCardBoxRatio: function(cardDiv, cardId, props) {
        var result = {};
        result.x = Math.min(1, cardDiv.data("widthToHeight")(cardId, props));
        result.y = Math.min(1, 1 / cardDiv.data("widthToHeight")(cardId, props));
        return result;
    },

    layoutCards: function() {
        var that = this;
        var ratios = {};
        var scale = 1;
        this.iterCards(
            function(cardDiv, cardId, props, layout) {
                ratios[cardId] = that.getCardBoxRatio(cardDiv, cardId, props);
                scale = Math.min(scale, that.getCardHeightScale(cardDiv, cardId, props));
            });

        if (!this.tryLayoutInOneRow(ratios, scale)) {
            var rows = 2;
            if (this.maxCardHeight != null)
                rows = Math.max(rows, Math.ceil((this.height + this.padding) / (this.maxCardHeight + this.padding)));
            while (true) {
                if (this.canLayoutInRows(ratios, rows, scale)) {
                    this.layoutInRows(ratios, rows, scale);
                    return;
                }
                rows++;
            }
        }
    },

    canLayoutInRows: function(ratios, rows, scale) {
        //log("Can layout in rows: "+rows);
        var that = this;
        var rowHeight = this.height / rows - this.padding * (rows - 1);
        var ascentLeft = 0;
        var cardRowCount = 0;
        var row = 0;
        this.iterCards(
            function(cardDiv, cardId, props, layout) {
                var cardWidth = ratios[cardId].x * rowHeight * scale;
                cardRowCount++;
                if (cardRowCount == 1) {
                    ascentLeft += that.padding + cardWidth;
                } else {
                    if (ascentLeft + that.padding + cardWidth > that.width) {
                        ascentLeft = 0;
                        row++;
                        ascentLeft += that.padding + cardWidth;
                        cardRowCount = 1;
                    } else {
                        ascentLeft += that.padding + cardWidth;
                    }
                }
            });
        return row < rows;
    },

    layoutInRows: function(ratios, rows, scale) {
        var that = this;
        var rowHeight = this.height / rows - this.padding * (rows - 1);
        var ascentLeft = 0;
        var ascentTop = 0;
        var cardRowCount = 0;
        this.iterCards(
            function(cardDiv, cardId, props, layout) {
                var ratio = ratios[cardId];
                var boxWidth = ratio.x * rowHeight * scale;
                cardRowCount++;
                if (cardRowCount == 1) {
                    that.layoutCardBox(cardDiv, cardId, props, layout, that.left + ascentLeft, that.top + ascentTop, boxWidth, rowHeight, ratio);
                    ascentLeft += that.padding + boxWidth;
                } else {
                    if (ascentLeft + that.padding + boxWidth > that.width) {
                        ascentLeft = 0;
                        ascentTop += that.padding + rowHeight;
                        that.layoutCardBox(cardDiv, cardId, props, layout, that.left + ascentLeft, that.top + ascentTop, boxWidth, rowHeight, ratio);
                        ascentLeft += that.padding + boxWidth;
                        cardRowCount = 1;
                    } else {
                        that.layoutCardBox(cardDiv, cardId, props, layout, that.left + ascentLeft, that.top + ascentTop, boxWidth, rowHeight, ratio);
                        ascentLeft += that.padding + boxWidth;
                    }
                }
            });
    },

    tryLayoutInOneRow: function(ratios, scale) {
        var totalRatio = 0;
        var cardCount = 0;
        this.iterCards(
            function(cardDiv, cardId, props, layout) {
                cardCount++;
                totalRatio += ratios[cardId].x;
            });

        var availableCardsWidth = this.width - this.padding * (cardCount - 1);
        var maxCardHeight = this.height;
        if (this.maxCardHeight != null)
            maxCardHeight = Math.min(this.maxCardHeight, maxCardHeight);
        var oneRowCardWidths = maxCardHeight * totalRatio * scale;
        if (oneRowCardWidths <= availableCardsWidth) {
            this.layoutInOneFullRow(ratios, scale);
            return true;
        } else {
            var cardHeightInTwoRows = this.height / 2 - this.padding;
            var cardHeightToFitAll = availableCardsWidth / (totalRatio * scale)
            if (cardHeightToFitAll >= cardHeightInTwoRows) {
                this.layoutInOnePartialRow(ratios, cardHeightToFitAll, scale);
                return true;
            }
        }
        return false;
    },

    layoutInOnePartialRow: function(ratios, rowHeight, scale) {
        var that = this;
        var left = 0;
        var index = 0;
        this.iterCards(
            function(cardDiv, cardId, props, layout) {
                var ratio = ratios[cardId];
                var boxWidth = ratio.x * rowHeight * scale;

                that.layoutCardBox(cardDiv, cardId, props, layout, that.left + left, that.top, boxWidth, rowHeight, ratio);

                left += boxWidth + that.padding;
                index++;
            });
    },

    layoutInOneFullRow: function(ratios, scale) {
        var that = this;
        var left = 0;
        var index = 0;
        this.iterCards(
            function(cardDiv, cardId, props, layout) {
                var ratio = ratios[cardId];
                var cardHeight = that.height;
                if (that.maxCardHeight != null)
                    cardHeight = Math.min(cardHeight, that.maxCardHeight);
                var boxWidth = ratio.x * cardHeight * scale;

                that.layoutCardBox(cardDiv, cardId, props, layout, that.left + left, that.top, boxWidth, cardHeight, ratio);

                left += boxWidth + that.padding;
                index++;
            });
    },

    layoutOneCard: function(cardDiv, cardId, props, layout, zIndex, cardLeft, cardTop, cardWidth, cardHeight) {
        //log("layout card "+cardId+": "+cardLeft+","+cardTop+"    "+cardWidth+"x"+cardHeight);
        cardDiv.css({"zIndex": zIndex,"left": cardLeft + "px", "top": cardTop + "px", "width": cardWidth, "height": cardHeight});
        layout(cardDiv, cardId, props, cardLeft, cardTop, cardWidth, cardHeight);
    },

    layoutCardBox: function(cardDiv, cardId, props, layout, boxLeft, boxTop, boxWidth, boxHeight, ratio) {
        var cardLeft = boxLeft;
        var cardWidth = boxWidth;
        var cardHeight = cardWidth / cardDiv.data("widthToHeight")(cardId, props);
        var cardTop = boxTop + (boxHeight - cardHeight) / 2;
        this.layoutOneCard(cardDiv, cardId, props, layout, this.zIndexBase, cardLeft, cardTop, cardWidth, cardHeight);
    }
});