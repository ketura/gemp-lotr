var CardGroup = Class.extend({
    cardContainerDiv: null,
    cardContainFunc: null,
    left: null,
    top: null,
    width: null,
    height: null,

    init: function(cardContainerDiv, cardContainFunc) {
        this.cardContainerDiv = cardContainerDiv;
        this.cardContainFunc = cardContainFunc;
    },

    iterCards: function(func) {
        var that = this;
        $(".card", this.cardContainerDiv).each(
                function() {
                    var cardDiv = $(this);
                    var cardId = cardDiv.data("id");
                    var props = cardDiv.data("props");
                    var layout = cardDiv.data("layout");
                    if (that.cardContainFunc(cardDiv, cardId, props))
                        func(cardDiv, cardId, props, layout);
                });
    },

    findCardPropsById: function(cardId) {
        var result = null;
        $(".card", this.cardContainerDiv).each(
          function() {
              var cardDiv = $(this);
              var foundCardId = cardDiv.data("id");
              if (foundCardId == cardId)
                result = cardDiv.data("props");
          });
        
        return result;
    },

    setLayout: function(left, top, width, height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;

        this.layoutCards();
    },

    layoutCards: function() {
        log("CardGroup::layoutCards - This method should be overriden");
    }
});