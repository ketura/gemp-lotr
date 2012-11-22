// This is a logical, and not a physical object.
// Its purpose is to maintain order and control over multiple card groups
var CardContainer = Class.extend({
    cardContainerDiv: null,
    cardGroups: null,
    layoutFunc: null,

    init: function(cardContainerDiv, layoutFunc) {
        this.cardContainerDiv = cardContainerDiv;
        this.cardGroups = {};
        this.layoutFunc = layoutFunc;
    },

    addCard: function(elem, cardId, props, layoutFunc, widthToHeightScaleFunc) {
        var cardDiv = $("<div class='card'></div>");
        cardDiv.append(elem);
        this.cardContainerDiv.append(cardDiv);
        cardDiv.data("id", cardId);
        cardDiv.data("props", props);
        cardDiv.data("layout", layoutFunc);
        cardDiv.data("widthToHeight", widthToHeightScaleFunc);
    },

    addCardGroup: function(name, cardGroup) {
        this.cardGroups[name] = cardGroup;
    },

    setLayout: function(left, top, width, height) {
        this.layoutFunc(this.cardGroups, left, top, width, height);
    },

    layoutCards: function() {
        iterObj(this.cardGroups,
                function (groupName, cardGroup) {
                    cardGroup.layoutCards();
                });
    }
});