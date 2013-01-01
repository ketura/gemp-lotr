var GempLotrDeckBuildingUI2 = Class.extend({
    comm:null,
    collectionType:null,

    collectionContentsDiv:null,
    collectionContainer:null,
    collectionGroup:null,

    collectionSelect: null,

    deckDiv: null,

    cardFilter:null,
    deckPanel:null,
    padding: 3,

    cardInCollectionId:0,
    cardInDeckId:0,

    cardInformationDialog: null,

    // Card properties:
    // type: pack, card
    // blueprintId: id of the blueprint as in server
    // count: number of cards of that type
    // contents: contents of selection pack
    // hor: if card is horizontal
    // group: ring, ringBearer, site, fp, shadow
    // image: image url

    init:function (filterDiv, pageDiv, collectionContentsDiv, deckDiv) {
        this.collectionContentsDiv = collectionContentsDiv;
        this.deckDiv = deckDiv;

        var that = this;

        this.collectionContainer = new CardContainer(collectionContentsDiv, this.layoutCollectionContainer, this.layoutCardInCollection, this.widthToHeightScaleInCollection);
        this.collectionGroup = new RowCardLayoutCardGroup(collectionContentsDiv,
                function() {
                    return true;
                });
        this.collectionGroup.setZIndexBase(10);
        this.collectionContainer.addCardGroup("main", this.collectionGroup);

        this.comm = new GempLotrCommunication("/gemp-lotr-server", that.processError);

        var collectionTypeDiv = $("<div></div>");
        var filterDivInternal = $("<div></div>");

        collectionTypeDiv.append("Collection: ");

        this.collectionSelect = $("<select></select>");
        this.collectionSelect.css({"float":"right", "width":"180px", "font-size": "75%"});
        this.collectionSelect.append("<option value='default'>All cards</option>");
        this.collectionSelect.append("<option value='permanent'>My cards</option>");

        collectionTypeDiv.append(this.collectionSelect);

        this.collectionSelect.change(
            function () {
                that.collectionType = $("option:selected", this.collectionSelect).prop("value");
                that.cardFilter.getCollection();
            });

        filterDiv.append(collectionTypeDiv);
        filterDiv.append(filterDivInternal);

        this.cardFilter = new CardFilter(filterDivInternal, pageDiv,
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
                    that.addCardToCollection(type, blueprintId, count, elem.getAttribute("group"), elem.getAttribute("contents"));
                },
                function () {
                    that.finishCollection();
                });

        this.deckPanel = new DeckPanel(deckDiv, this.layoutCardInDeck, this.widthToHeightScaleInCollection);

        this.collectionType = "default";
        this.cardFilter.getCollection();

        this.cardInformationDialog = new CardInformationDialog();

        $("body").click(
                function (event) {
                    return that.clickCardFunction(event);
                });
        $("body").mousedown(
                function (event) {
                    return that.dragStartCardFunction(event);
                });
        $("body").mouseup(
                function (event) {
                    return that.dragStopCardFunction(event);
                });

        this.loadExtraCollectionTypes();
    },

    dragCardProps:null,
    dragStartX:null,
    dragStartY:null,
    successfulDrag:null,

    dragStartCardFunction:function (event) {
        this.successfulDrag = false;
        var tar = $(event.target);
        if (tar.hasClass("clickArea")) {
            var cardElem = tar.closest(".card");
            var cardProps = cardElem.data("props");

            if (event.which == 1) {
                this.dragCardProps = cardProps;
                this.dragStartX = event.clientX;
                this.dragStartY = event.clientY;
                return false;
            }
        }
        return true;
    },

    dragStopCardFunction:function (event) {
        if (this.dragCardProps != null) {
            if (this.dragStartY - event.clientY >= 20) {
                this.cardInformationDialog.showCardInfo(this.dragCardProps["blueprintId"]);
                this.successfulDrag = true;
            }
            this.dragCardProps = null;
            this.dragStartX = null;
            this.dragStartY = null;
            return false;
        }
        return true;
    },

    clickCardFunction: function(event) {
        var tar = $(event.target);
        if (tar.length == 1 && tar[0].tagName == "A")
            return true;

        if (!this.successfulDrag && this.cardInformationDialog.isOpened()) {
            this.cardInformationDialog.close();
            event.stopPropagation();
            return false;
        }

        if (tar.hasClass("clickArea")) {
            var cardElem = tar.closest(".card");
            var cardProps = cardElem.data("props");

            if (event.which == 1) {
                if (!this.successfulDrag) {
                    // If card in collection
                    if (event.shiftKey) {
                        this.cardInformationDialog.showCardInfo(cardProps["blueprintId"]);
                    } else if (tar.hasClass("cardInCollection")) {
                        // add card to deck
                        this.addCardToDeck(cardProps["type"], cardProps["group"],
                                cardProps["blueprintId"], cardProps["image"]);
                        this.updateCardCount(cardElem, cardProps);
                    } else if (tar.hasClass("cardInDeck")) {
                        // remove card from deck
                        this.deckPanel.removeCardAndUpdate(cardElem, cardElem.data("id"), cardProps);
                        this.updateCardCounts();
                    }
                }
            }
        }
        return true;
    },

    loadExtraCollectionTypes:function () {
        var that = this;
        this.comm.getCollectionTypes(
            function (xml) {
                var root = xml.documentElement;
                if (root.tagName == "collections") {
                    var collections = root.getElementsByTagName("collection");
                    for (var i = 0; i < collections.length; i++) {
                        var collection = collections[i];
                        that.collectionSelect.append("<option value='" + collection.getAttribute("type") + "'>" + collection.getAttribute("name") + "</option>");
                    }
                }
            });
    },

    layoutCollectionContainer : function(cardGroups, left, top, width, height) {
        var padding = 3;
        cardGroups["main"].setLayout(left + padding, top + padding, width - 2 * padding, height - 2 * padding);
    },

    clearCollection: function() {
        $(".card", this.collectionContentsDiv).remove();
        this.cardInCollectionId = 0;
    },

    addCardToDeck: function(type, group, blueprintId, image, horizontal) {
        var cardProps = {};
        cardProps["type"] = type;
        cardProps["group"] = group;
        cardProps["blueprintId"] = blueprintId;
        cardProps["image"] = image;
        cardProps["hor"] = horizontal;
        this.deckPanel.addCardAndUpdate(this.createDeckCardElem(cardProps), "" + (this.cardInDeckId++), cardProps);
    },

    addCardToCollection: function(type, blueprintId, count, group, contents) {
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
            props["hor"] = card.horizontal;
        }
        props["image"] = card.imageUrl;
        props["group"] = group;

        this.collectionContainer.addCard(this.createCollectionCardElem(props), "" + (this.cardInCollectionId++), props);
    },

    updateCardCount: function(cardDiv, props) {
        var count = props["count"] - this.deckPanel.getCardCount(props["blueprintId"]);
        $(".count", cardDiv).html(count);
    },

    updateCardCounts: function() {
        var that = this;
        this.collectionContainer.iterCards(
                function(cardDiv, cardId, props, layout) {
                    that.updateCardCount(cardDiv, props);
                });
    },

    createCollectionCardElem: function(props) {
        var cardDiv = $("<div class='cardInCollection'></div>");
        this.appendCardElemDetails(cardDiv, props);
        cardDiv.append("<div class='count'></div>");
        cardDiv.append("<div class='click'><img class='clickArea' src='images/pixel.png' width='100%' height='100%'></div>");
        return cardDiv;
    },

    createDeckCardElem: function(props) {
        var cardDiv = $("<div class='cardInDeck'></div>");
        this.appendCardElemDetails(cardDiv, props);
        cardDiv.append("<div class='count'></div>");
        cardDiv.append("<div class='click'><img class='clickArea' src='images/pixel.png' width='100%' height='100%'></div>");
        return cardDiv;
    },

    appendCardElemDetails: function(cardDiv, props) {
        cardDiv.append("<div class='img'><img src='" + props["image"] + "' width='100%' height='100%'></div>");
        if (props["type"] == "card")
            cardDiv.append("<div class='border'><img src='images/pixel.png' width='100%' height='100%'></div>");
    },

    widthToHeightScaleInCollection: function(cardId, props) {
        if (props["hor"])
            return 500 / 360;
        else
            return 360 / 500;
    },

    layoutCardInCollection: function(cardDiv, cardId, props, zIndex, cardLeft, cardTop, cardWidth, cardHeight) {
        $(".img", cardDiv).css({"left":0, "top":0, "width":cardWidth, "height":cardHeight});
        var maxDimension = Math.max(cardWidth, cardHeight)
        if (props["type"] == "card") {
            var borderWidth = Math.floor(maxDimension / 30);
            $(".border", cardDiv).css({"left":0, "top":0, "width":(cardWidth - 2 * borderWidth), "height":(cardHeight - 2 * borderWidth), "border-width": borderWidth + "px"});
        }
        $(".count", cardDiv).css({"left":0, "top":0, "width":cardWidth, "height":cardHeight, "font-size": maxDimension * 1.5 + "%"});
        $(".click", cardDiv).css({"left":0, "top":0, "width":cardWidth, "height":cardHeight});
    },

    layoutCardInDeck: function(cardDiv, cardId, props, zIndex, cardLeft, cardTop, cardWidth, cardHeight) {
        $(".img", cardDiv).css({"left":0, "top":0, "width":cardWidth, "height":cardHeight});
        var maxDimension = Math.max(cardWidth, cardHeight)
        if (props["type"] == "card") {
            var borderWidth = Math.floor(maxDimension / 30);
            $(".border", cardDiv).css({"left":0, "top":0, "width":(cardWidth - 2 * borderWidth), "height":(cardHeight - 2 * borderWidth), "border-width": borderWidth + "px"});
        }
        $(".count", cardDiv).css({"left":0, "top":0, "width":cardWidth, "height":cardHeight, "font-size": maxDimension * 1.5 + "%"});
        $(".click", cardDiv).css({"left":0, "top":0, "width":cardWidth, "height":cardHeight});

        $(".count", cardDiv).html(props["countInDeck"]);
    },

    finishCollection: function() {
        this.collectionContainer.layoutCards();
        this.updateCardCounts();
    },

    layoutUI: function(collectionWidth, collectionHeight, deckWidth, deckHeight) {
        this.cardFilter.layoutPageUi(0, 0, collectionWidth);
        this.collectionContentsDiv.css({left: "0px", top: "0px", width: collectionWidth - 3 + "px", height: collectionHeight - 38 + "px"});
        this.collectionContainer.setLayout(0, 0, collectionWidth - 3, collectionHeight - 38);
        this.deckPanel.layoutUi(0, 0, deckWidth, deckHeight);
    }
});
