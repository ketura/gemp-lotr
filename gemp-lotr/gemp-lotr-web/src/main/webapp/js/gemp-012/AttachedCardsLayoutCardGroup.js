var AttachedCardsLayoutCardGroup = RowCardLayoutCardGroup.extend({
    attachedGroupsLeft: null,
    attachedGroupsTop: null,
    attachedGroupsFinderFunc: null,
    attachedGroupsRootRecognizeFunc: null,

    init: function (cardContainerDiv, cardContainFunc) {
        this._super(cardContainerDiv, cardContainFunc);
        this.attachedGroupsLeft = new Array();
        this.attachedGroupsTop = new Array();
        this.attachedGroupsFinderFunc = new Array();
        this.attachedGroupsRootRecognizeFunc = new Array();
    },

    addAttachedGroup: function(left, top, rootRecognizeFunc, finderFunc) {
        this.attachedGroupsLeft.push(left);
        this.attachedGroupsTop.push(top);
        this.attachedGroupsRootRecognizeFunc.push(rootRecognizeFunc);
        this.attachedGroupsFinderFunc.push(finderFunc);
    },

    iterAttached: function(cardDiv, cardId, props, rootRecognizeFunc, finderFunc, func) {
        if (!rootRecognizeFunc(cardDiv, cardId, props))
            return;

        $(".card", this.cardContainerDiv).each(
            function() {
                var cardDivAtt = $(this);
                var cardIdAtt = cardDivAtt.data("id");
                var propsAtt = cardDivAtt.data("props");
                var layout = cardDivAtt.data("layout");
                var widthToHeightScaleFunc = cardDivAtt.data("widthToHeight");
                if (finderFunc(cardDiv, cardId, props, cardDivAtt, cardIdAtt, propsAtt))
                    func(cardDivAtt, cardIdAtt, propsAtt, layout, widthToHeightScaleFunc);
            });
    },

    getCardHeightScale: function(cardDiv, cardId, props) {
        var cardBox = this.getCardGroupBox(cardDiv, cardId, props);
        return Math.min(1, 1 / (cardBox.bottom - cardBox.top));
    },

    getCardBox: function(cardDiv, cardId, props) {
        var cardRatio = cardDiv.data("widthToHeight")(cardId, props);
        var result = {};
        result.left = 0;
        result.top = 0;
        result.right = Math.min(1, cardRatio);
        result.bottom = Math.min(1, 1 / cardRatio);
        return result;
    },

    getCardGroupBox: function(cardDiv, cardId, props) {
        var that = this;

        var minLeft = 0;
        var minTop = 0;
        var maxRight = Math.min(1, cardDiv.data("widthToHeight")(cardId, props));
        var maxBottom = Math.min(1, 1 / cardDiv.data("widthToHeight")(cardId, props));
        var cardWidth = maxRight;
        var cardHeight = maxBottom;

        for (var i = 0; i < this.attachedGroupsFinderFunc.length; i++) {
            var attachFunc = this.attachedGroupsFinderFunc[i];
            var rootRecognizeFunc = this.attachedGroupsRootRecognizeFunc[i];
            var attachLeft = this.attachedGroupsLeft[i];
            var attachTop = this.attachedGroupsTop[i];

            var attIndex = 0;
            this.iterAttached(cardDiv, cardId, props, rootRecognizeFunc, attachFunc,
                function(attCardDiv, attCardId, attProps, layout, attWidthToHeightRatioFunc) {
                    attIndex++;
                    var attBox = that.getCardBox(attCardDiv, attCardId, attProps);
                    var attWidth = attBox.right - attBox.left;
                    var attHeight = attBox.bottom - attBox.top;
                    if (attachLeft < 0) {
                        maxRight = Math.max(maxRight, attachLeft * attIndex + attWidth);
                    } else if (attachLeft > 0) {
                        minLeft = Math.min(minLeft, cardWidth + attachLeft*attIndex - attWidth);
                    }

                    if (attachTop < 0) {
                        maxBottom = Math.max(maxBottom, attachTop * attIndex + attHeight);
                    } else if (attachTop > 0) {
                        minTop = Math.min(minTop, cardHeight + attachTop*attIndex - attHeight);
                    }
                });

            if (attachLeft < 0)
                minLeft = Math.min(minLeft, attachLeft * attIndex);
            else
                maxRight = Math.max(maxRight, cardWidth + attachLeft * attIndex);

            if (attachTop < 0)
                minTop = Math.min(minTop, attachTop * attIndex);
            else
                maxBottom = Math.max(maxBottom, cardHeight + attachTop * attIndex);
        }
        var result = {};
        result.left = minLeft;
        result.top = minTop;
        result.right = maxRight;
        result.bottom = maxBottom;
        return result;
    },

    getCardBoxRatio: function(cardDiv, cardId, props) {
        var cardBox = this.getCardGroupBox(cardDiv, cardId, props);
        var result = {};
        result.x = cardBox.right - cardBox.left;
        result.y = cardBox.bottom - cardBox.top;
        return result;
    },

    layoutCardGroup: function(cardDiv, cardId, props, layout, zIndex, cardBox, boxLeft, boxTop, boxWidth, boxHeight) {
        var that = this;
        var pixelSize = boxWidth / (cardBox.right - cardBox.left);
        var cardGroupHeight = pixelSize * (cardBox.bottom - cardBox.top);
        var cardGroupWidth = pixelSize * (cardBox.right - cardBox.left);

        var cardRatio = cardDiv.data("widthToHeight")(cardId, props);
        var cardWidth = pixelSize * Math.min(1, cardRatio);
        var cardHeight = cardWidth / cardRatio;
        var cardLeft = boxLeft - cardBox.left * pixelSize;
        var cardTop = boxTop - cardBox.top * pixelSize + (boxHeight - cardGroupHeight) / 2;
        this.layoutOneCard(cardDiv, cardId, props, layout, zIndex, cardLeft, cardTop, cardWidth, cardHeight);

        zIndex--;

        for (var i = 0; i < this.attachedGroupsFinderFunc.length; i++) {
            var attachFunc = this.attachedGroupsFinderFunc[i];
            var rootRecognizeFunc = this.attachedGroupsRootRecognizeFunc[i];
            var attachLeft = this.attachedGroupsLeft[i];
            var attachTop = this.attachedGroupsTop[i];

            var index = 0;
            this.iterAttached(cardDiv, cardId, props, rootRecognizeFunc, attachFunc,
                function(attCardDiv, attCardId, attProps, layout, attWidthToHeightRatioFunc) {
                    index++;
                    var attCardBox = that.getCardBox(attCardDiv, attCardId, attProps);
                    var attWidth = pixelSize * (attCardBox.right - attCardBox.left);
                    var attHeight = pixelSize * (attCardBox.bottom - attCardBox.top);

                    var attLeft;
                    if (attachLeft <= 0)
                        attLeft = cardLeft + index * pixelSize * attachLeft;
                    else
                        attLeft = cardLeft + cardWidth - attWidth + index * pixelSize * attachLeft;
                    var attTop;
                    if (attachTop <= 0)
                        attTop = cardTop + index * pixelSize * attachTop;
                    else
                        attTop = cardTop + cardHeight - attHeight + index * pixelSize * attachTop;
                    that.layoutOneCard(attCardDiv, attCardId, attProps, layout, zIndex,
                        attLeft, attTop,
                        attWidth, attHeight);
                    zIndex--;
                });
        }
    },

    layoutCardBox: function(cardDiv, cardId, props, layout, boxLeft, boxTop, boxWidth, boxHeight, ratio) {
        var that = this;
        var zIndex = this.zIndexBase;
        var cardBox = this.getCardGroupBox(cardDiv, cardId, props);
        this.layoutCardGroup(cardDiv, cardId, props, layout, zIndex, cardBox, boxLeft, boxTop, boxWidth, boxHeight);
    }
});