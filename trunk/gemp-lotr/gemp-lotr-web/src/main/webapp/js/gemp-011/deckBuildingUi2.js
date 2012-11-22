var GempLotrDeckBuildingUI2 = Class.extend({
    comm:null,
    collectionType:null,
    cardFilter:null,
    collectionContentsDiv:null,
    deckDiv:null,
    padding: 3,

    init:function (filterDiv, pageDiv, collectionContentsDiv, deckDiv) {
        this.collectionContentsDiv = collectionContentsDiv;
        this.deckDiv = deckDiv;

        var that = this;

        this.comm = new GempLotrCommunication("/gemp-lotr-server", that.processError);

        this.cardFilter = new CardFilter(filterDiv, pageDiv,
            function (filter, start, count, callback) {
                that.comm.getCollection(that.collectionType, filter, start, count, function (xml) {
                    callback(xml);
                }, {
                    "404":function () {
                        alert("You don't have collection of that type.");
                    }
                });
            },
            function () {
                that.clearCollection();
            },
            function (elem, type, blueprintId, count) {
                that.addCardToCollection(type, blueprintId, count, elem.getAttribute("side"), elem.getAttribute("contents"));
            },
            function () {
                that.finishCollection();
            });
        this.collectionType = "default";
        this.cardFilter.getCollection();
    },

    layoutUI: function(collectionWidth, collectionHeight, deckWidth, deckHeight) {
        this.cardFilter.layoutPageUi(0, 0, collectionWidth);
        this.collectionContentsDiv.css({left: "0px", top: "0px", width: collectionWidth-3+"px", height: collectionHeight-38+"px"});
        this.deckDiv.css({left: "0px", top: "0px", width: deckWidth+"px", height: deckHeight+"px"})
    }
});
