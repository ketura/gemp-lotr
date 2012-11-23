var GempLotrDeckBuildingUI2 = Class.extend({
    comm:null,
    collectionType:null,

    collectionContentsDiv:null,
    collectionContainer:null,
    collectionGroup:null,

    deckDiv: null,

    cardFilter:null,
    deckPanel:null,
    padding: 3,

    cardInCollectionId:0,

    init:function (filterDiv, pageDiv, collectionContentsDiv, deckDiv) {
        this.collectionContentsDiv = collectionContentsDiv;
        this.deckDiv = deckDiv;

        var that = this;

        this.collectionContainer = new CardContainer(collectionContentsDiv, this.layoutCollectionContainer);
        this.collectionGroup = new RowCardLayoutCardGroup(collectionContentsDiv,
                function() {
                    return true;
                });
        this.collectionGroup.setZIndexBase(10);
        this.collectionContainer.addCardGroup("main", this.collectionGroup);

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

        this.deckPanel = new DeckPanel(deckDiv);

        this.collectionType = "default";
        this.cardFilter.getCollection();
    },

    layoutCollectionContainer : function(cardGroups, left, top, width, height) {
        var padding = 3;
        cardGroups["main"].setLayout(left + padding, top + padding, width - 2 * padding, height - 2 * padding);
    },

    clearCollection: function() {
        $(".card", this.collectionContentsDiv).remove();
        this.cardInCollectionId = 0;
    },

    addCardToCollection: function(type, blueprintId, count, side, contents) {
        var props = {};
        props["blueprintId"] = blueprintId;
        props["count"] = count;

        var card = new Card(blueprintId, "collection", "1", "player", null);
        cardDiv = this.createCollectionCardElem(card.imageUrl);

        var cardDiv;
        if (type == "pack") {
            if (blueprintId.substr(0, 3) == "(S)") {
                props["contents"] = contents;
            }
        } else if (type == "card") {
            var countInDeck = 0;
            $(".card", this.deckDiv).each(
                    function () {
                        var tempCardData = $(this).data("props");
                        if (blueprintId == tempCardData["blueprintId"])
                            countInDeck++;
                    });
            props["count"] = count-countInDeck;
            props["hor"] = card.horizontal;
        }
        this.collectionContainer.addCard(cardDiv, ""+(this.cardInCollectionId++) , props, this.layoutCardInCollection, this.widthToHeightScaleInCollection);
    },

    createCollectionCardElem: function(image) {
        return "<img src='" + image + "' width='100%' height='100%'>";
    },

    widthToHeightScaleInCollection: function(cardId, props) {
        if (props["hor"])
            return 500/360;
        else        
            return 360/500;
    },

    layoutCardInCollection: function() {
        
    },

    finishCollection: function() {
        this.collectionContainer.layoutCards();
    },

    layoutUI: function(collectionWidth, collectionHeight, deckWidth, deckHeight) {
        this.cardFilter.layoutPageUi(0, 0, collectionWidth);
        this.collectionContentsDiv.css({left: "0px", top: "0px", width: collectionWidth - 3 + "px", height: collectionHeight - 38 + "px"});
        this.collectionContainer.setLayout(0, 0, collectionWidth-3, collectionHeight-38);
        this.deckPanel.layoutUi(0, 0, deckWidth, deckHeight);
    }
});
