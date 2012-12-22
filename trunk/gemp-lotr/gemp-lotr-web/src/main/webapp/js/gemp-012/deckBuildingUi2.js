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

    // Card properties:
    // type: pack, card
    // blueprintId: id of the blueprint as in server
    // count: number of cards of that type
    // contents: contents of selection pack
    // hor: if card is horizontal
    // image: image url

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

        $("body").click(
            function (event) {
                return that.clickCardFunction(event);
            });
    },

    clickCardFunction: function(event) {
        var tar = $(event.target);
        if (tar.length == 1 && tar[0].tagName == "A")
            return true;

        if (tar.hasClass("clickArea")) {
            tar = tar.parent();
            tar = tar.parent();

            var cardElem = tar.parent();
            // If card in collection
            if (tar.hasClass("cardInCollection")) {
                // add card to deck
                var cardId = cardElem.data("id");
                var cardProps = cardElem.data("props");
                this.deckPanel.addCard(this.createDeckCardElem(cardProps), cardId, cardProps, this.layoutCardInCollection, this.widthToHeightScaleInCollection);
            }
        }
        return true;
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
        props["type"] = type;

        var card = new Card(blueprintId, "collection", "1", "player", null);

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
        props["image"] = card.imageUrl;

        var cardDiv = this.createCollectionCardElem(props);

        this.collectionContainer.addCard(cardDiv, ""+(this.cardInCollectionId++) , props, this.layoutCardInCollection, this.widthToHeightScaleInCollection);
    },

    createCollectionCardElem: function(props) {
        var cardDiv = $("<div class='cardInCollection'></div>");
        this.appendCardElemDetails(cardDiv, props);
        return cardDiv;
    },

    createDeckCardElem: function(props) {
        var cardDiv = $("<div class='cardInDeck'></div>");
        this.appendCardElemDetails(cardDiv, props);
        return cardDiv;
    },

    appendCardElemDetails: function(cardDiv, props) {
        cardDiv.append("<div class='img'><img src='" + props["image"] + "' width='100%' height='100%'></div>");
        if (props["type"] == "card")
            cardDiv.append("<div class='border'><img src='images/pixel.png' width='100%' height='100%'></div>");
        cardDiv.append("<div class='click'><img class='clickArea' src='images/pixel.png' width='100%' height='100%'></div>");

    },

    widthToHeightScaleInCollection: function(cardId, props) {
        if (props["hor"])
            return 500/360;
        else        
            return 360/500;
    },

    layoutCardInCollection: function(cardDiv, cardId, props, cardLeft, cardTop, cardWidth, cardHeight) {
        $(".img", cardDiv).css({"left":0, "top":0, "width":cardWidth, "height":cardHeight});
        if (props["type"] == "card") {
            var borderWidth = Math.floor(Math.max(cardWidth, cardHeight) / 30);
            $(".border", cardDiv).css({"left":0, "top":0, "width":(cardWidth-2*borderWidth), "height":(cardHeight-2*borderWidth), "border-width": borderWidth+"px"});
        }
        $(".click", cardDiv).css({"left":0, "top":0, "width":cardWidth, "height":cardHeight});
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
