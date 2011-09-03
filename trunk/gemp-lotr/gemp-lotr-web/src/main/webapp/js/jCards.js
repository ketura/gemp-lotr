var cardCache = {};
var cardScale = 357 / 497;

var Card = Class.extend({
    blueprintId: null,
    horizontal: null,
    imageUrl: null,
    zone: null,
    cardId: null,
    owner: null,
    attachedCards: null,

    init: function(blueprintId, zone, cardId, owner) {
        this.blueprintId = blueprintId;
        this.zone = zone;
        this.cardId = cardId;
        this.owner = owner;
        this.attachedCards = new Array();
        if (blueprintId == "rules") {
            this.imageUrl = "/gemp-lotr/images/rules.png";
        } else {
            if (cardCache[blueprintId] != null) {
                var cardFromCache = cardCache[blueprintId];
                this.horizontal = cardFromCache.horizontal;
                this.imageUrl = cardFromCache.imageUrl;
            } else {
                this.imageUrl = this.getUrlByBlueprintId(blueprintId);
                this.horizontal = this.isHorizontal(blueprintId);
                cardCache[blueprintId] = {
                    imageUrl: this.imageUrl,
                    horizontal: this.horizontal
                };
            }
        }
    },

    isHorizontal: function(blueprintId) {
        var separator = blueprintId.indexOf("_");
        var setNo = parseInt(blueprintId.substr(0, separator));
        var cardNo = parseInt(blueprintId.substr(separator + 1));

        if (setNo == 1)
            return (cardNo >= 319 && cardNo <= 363);
    },

    getUrlByBlueprintId: function(blueprintId) {
        var separator = blueprintId.indexOf("_");
        var setNo = parseInt(blueprintId.substr(0, separator));
        var cardNo = parseInt(blueprintId.substr(separator + 1));

        var setNoStr;
        if (setNo < 10)
            setNoStr = "0" + setNo;
        else
            setNoStr = setNo;

        if (cardNo < 10)
            return "http://lotrtcgdb.com/images/LOTR" + setNoStr + "00" + cardNo + ".jpg";
        else if (cardNo < 100)
            return "http://lotrtcgdb.com/images/LOTR" + setNoStr + "0" + cardNo + ".jpg";
        else
            return "http://lotrtcgdb.com/images/LOTR" + setNoStr + "" + cardNo + ".jpg";
    },

    getHeightForWidth: function(width) {
        if (this.horizontal)
            return Math.floor(width * cardScale);
        else
            return Math.floor(width / cardScale);
    },

    getWidthForHeight: function(height) {
        if (this.horizontal)
            return Math.floor(height / cardScale);
        else
            return Math.floor(height * cardScale);
    }

});