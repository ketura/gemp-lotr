var GempLotrDeckBuildingUI = Class.extend({
    comm: null,

    deckDiv: null,

    manageDecksDiv: null,

    ringBearerDiv: null,
    ringBearerGroup: null,

    ringDiv: null,
    ringGroup: null,

    siteDiv: null,
    siteGroup: null,

    collectionDiv: null,

    normalCollectionDiv: null,
    normalCollectionGroup: null,

    selectionFunc: null,
    drawDeckDiv: null,

    fpDeckGroup: null,
    shadowDeckGroup: null,

    start: 0,
    count: 18,
    filter: null,

    deckName: null,

    filterDirty: false,
    deckDirty: false,

    checkDirtyInterval: 1000,

    deckListDialog: null,
    selectionDialog: null,
    selectionGroup: null,
    packSelectionId: null,

    cardFilter: null,

    collectionType: null,

    init: function() {
        var that = this;

        this.comm = new GempLotrCommunication("/gemp-lotr-server", that.processError);

        this.cardFilter = new CardFilter($("#collectionDiv"),
                function(filter, start, count, callback) {
                    that.comm.getCollection(that.collectionType, filter, start, count, function(xml) {
                        callback(xml);
                    }, {
                        "404": function() {
                            alert("You don't have collection of that type.");
                        }
                    });
                },
                function() {
                    that.clearCollection();
                },
                function(elem, type, blueprintId, count) {
                    that.addCardToCollection(type, blueprintId, count, elem.getAttribute("side"), elem.getAttribute("contents"));
                },
                function() {
                    that.finishCollection();
                });
        this.collectionType = "default";

        this.deckDiv = $("#deckDiv");

        this.manageDecksDiv = $("<div id='manageDecks'></div>");

        var collectionSelect = $("<select id='collectionSelect'></select>");
        collectionSelect.css({"float": "right", width: "180px"});
        collectionSelect.append("<option value='default'>All cards</option>");
        collectionSelect.append("<option value='permanent'>My cards</option>");
        this.manageDecksDiv.append(collectionSelect);

        var newDeckBut = $("<button title='New deck'><span class='ui-icon ui-icon-document'></span></button>").button();
        this.manageDecksDiv.append(newDeckBut);

        var saveDeckBut = $("<button title='Save deck'><span class='ui-icon ui-icon-disk'></span></button>").button();
        this.manageDecksDiv.append(saveDeckBut);

        var renameDeckBut = $("<button title='Rename deck'><span class='ui-icon ui-icon-tag'></span></button>").button();
        this.manageDecksDiv.append(renameDeckBut);

        var copyDeckBut = $("<button title='Copy deck to new'><span class='ui-icon ui-icon-copy'></span></button>").button();
        this.manageDecksDiv.append(copyDeckBut);

        var deckListBut = $("<button title='Deck list'><span class='ui-icon ui-icon-suitcase'></span></button>").button();
        this.manageDecksDiv.append(deckListBut);

        this.manageDecksDiv.append("<span id='editingDeck'>New deck</span>");

        this.deckDiv.append(this.manageDecksDiv);

        newDeckBut.click(
                function() {
                    that.deckName = null;
                    $("#editingDeck").text("New deck");
                    that.clearDeck();
                });

        saveDeckBut.click(
                function() {
                    if (that.deckName == null) {
                        var newDeckName = prompt("Enter the name of the deck", "");
                        if (newDeckName == null)
                            return;
                        if (newDeckName.length < 3 || newDeckName.length > 40)
                            alert("Deck name has to have at least 3 characters and at most 40 characters.");
                        else {
                            that.deckName = newDeckName;
                            $("#editingDeck").text(newDeckName);
                            that.saveDeck(true);
                        }
                    } else {
                        that.saveDeck(false);
                    }
                });

        renameDeckBut.click(
                function() {
                    if (that.deckName == null) {
                        alert("You can't rename this deck, since it's not named (saved) yet.");
                        return;
                    }
                    var newDeckName = prompt("Enter new name for the deck", "");
                    if (newDeckName == null)
                        return;
                    if (newDeckName.length < 3 || newDeckName.length > 40)
                        alert("Deck name has to have at least 3 characters and at most 40 characters.");
                    else {
                        var oldDeckName = that.deckName;
                        that.deckName = newDeckName;
                        $("#editingDeck").text(newDeckName);
                        that.comm.renameDeck(oldDeckName, newDeckName,
                                function() {
                                    if (confirm("Do you wish to save this deck?"))
                                        that.saveDeck(false);
                                }, {
                            "404": function() {
                                alert("Couldn't find the deck to rename on the server.");
                            }
                        });
                    }
                });

        copyDeckBut.click(
                function() {
                    that.deckName = null;
                    $("#editingDeck").text("New deck");
                });

        deckListBut.click(
                function() {
                    that.loadDeckList();
                });

        this.collectionDiv = $("#collectionDiv");

        $("#collectionSelect").change(
                function() {
                    that.collectionType = that.getCollectionType();
                    that.cardFilter.getCollection();
                });

        this.normalCollectionDiv = $("<div></div>");
        this.normalCollectionGroup = new NormalCardGroup(this.normalCollectionDiv, function(card) {
            return true;
        });
        this.normalCollectionGroup.maxCardHeight = 200;
        this.collectionDiv.append(this.normalCollectionDiv);

        this.ringBearerDiv = $("<div>Ring Bearer</div>");
        this.ringBearerDiv.click(
                function() {
                    if ($(".card", that.ringBearerDiv).length == 0) {
                        that.showPredefinedFilter("keyword:CAN_START_WITH_RING type:card", that.ringBearerDiv);
                    }
                });
        this.ringBearerGroup = new NormalCardGroup(this.ringBearerDiv, function(card) {
            return true;
        });
        this.deckDiv.append(this.ringBearerDiv);

        this.ringDiv = $("<div>Ring</div>");
        this.ringDiv.click(
                function() {
                    if ($(".card", that.ringDiv).length == 0)
                        that.showPredefinedFilter("cardType:THE_ONE_RING type:card", that.ringDiv);
                });
        this.ringGroup = new NormalCardGroup(this.ringDiv, function(card) {
            return true;
        });
        this.deckDiv.append(this.ringDiv);

        this.siteDiv = $("<div>Sites</div>");
        this.deckDiv.append(this.siteDiv);
        this.siteGroup = new VerticalBarGroup(this.siteDiv, function(card) {
            return true;
        }, true);

        this.drawDeckDiv = $("<div></div>");
        this.fpDeckGroup = new NormalCardGroup(this.drawDeckDiv, function(card) {
            return (card.zone == "FREE_PEOPLE");
        });
        this.fpDeckGroup.maxCardHeight = 200;
        this.shadowDeckGroup = new NormalCardGroup(this.drawDeckDiv, function(card) {
            return (card.zone == "SHADOW");
        });
        this.shadowDeckGroup.maxCardHeight = 200;

        this.bottomBarDiv = $("<div></div>");
        this.bottomBarDiv.css({overflow: "auto"});
        this.bottomBarDiv.append("<div id='deckStats'></div>")
        this.deckDiv.append(this.bottomBarDiv);

        this.deckDiv.append(this.drawDeckDiv);

        this.selectionFunc = this.addCardToDeckAndLayout;

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

        var width = $(window).width();
        var height = $(window).height();

        this.infoDialog = $("<div></div>")
                .dialog({
            autoOpen: false,
            closeOnEscape: true,
            resizable: false,
            title: "Card information"
        });

        var swipeOptions = {
            threshold: 20,
            swipeUp: function (event) {
                that.infoDialog.prop({ scrollTop: that.infoDialog.prop("scrollHeight") });
                return false;
            },
            swipeDown: function (event) {
                that.infoDialog.prop({ scrollTop: 0 });
                return false;
            }
        };
        this.infoDialog.swipe(swipeOptions);

        this.getCollectionTypes();

        this.cardFilter.setFilter("cardType:-THE_ONE_RING");
        this.cardFilter.getCollection();

        this.checkDeckStatsDirty();
    },

    getCollectionType: function() {
        return $("#collectionSelect option:selected").prop("value");
    },

    getCollectionTypes: function() {
        var that = this;
        this.comm.getCollectionTypes(
                function(xml) {
                    var root = xml.documentElement;
                    if (root.tagName == "collections") {
                        var collections = root.getElementsByTagName("collection");
                        for (var i = 0; i < collections.length; i++) {
                            var collection = collections[i];
                            $("#collectionSelect").append("<option value='" + collection.getAttribute("type") + "'>" + collection.getAttribute("name") + "</option>");
                        }
                    }
                });
    },

    loadDeckList: function() {
        var that = this;
        this.comm.getDecks(function(xml) {
            if (that.deckListDialog == null) {
                that.deckListDialog = $("<div></div>")
                        .dialog({
                    title: "Your stored decks",
                    autoOpen: false,
                    closeOnEscape: true,
                    resizable: true,
                    width: 400,
                    height: 400,
                    modal: true
                });
            }
            that.deckListDialog.html("");

            var root = xml.documentElement;
            if (root.tagName == "decks") {
                var decks = root.getElementsByTagName("deck");
                for (var i = 0; i < decks.length; i++) {
                    var deck = decks[i];
                    var deckName = decks[i].childNodes[0].nodeValue;
                    var openDeckBut = $("<button title='Open deck'><span class='ui-icon ui-icon-folder-open'></span></button>").button();
                    var deckListBut = $("<button title='Deck list'><span class='ui-icon ui-icon-clipboard'></span></button>").button();
                    var deleteDeckBut = $("<button title='Delete deck'><span class='ui-icon ui-icon-trash'></span></button>").button();

                    var deckElem = $("<div class='deckItem'></div>");
                    deckElem.append(openDeckBut);
                    deckElem.append(deckListBut);
                    deckElem.append(deleteDeckBut);
                    deckElem.append(deckName);

                    that.deckListDialog.append(deckElem);

                    openDeckBut.click(
                            (function(deckName) {
                                return function() {
                                    that.comm.getDeck(deckName,
                                            function(xml) {
                                                that.setupDeck(xml, deckName);
                                            });
                                };
                            })(deckName));

                    deckListBut.click(
                            (function(deckName) {
                                return function() {
                                    window.open('/gemp-lotr-server/deck/html?deckName=' + encodeURIComponent(deckName), "_blank");
                                };
                            })(deckName));

                    deleteDeckBut.click(
                            (function(deckName) {
                                return function() {
                                    if (confirm("Are you sure you want to delete this deck?")) {
                                        that.comm.deleteDeck(deckName,
                                                function() {
                                                    if (that.deckName == deckName) {
                                                        that.deckName = null;
                                                        $("#editingDeck").text("New deck");
                                                        that.clearDeck();
                                                    }

                                                    that.loadDeckList();
                                                });
                                    }
                                };
                            })(deckName));
                }
            }

            that.deckListDialog.dialog("open");
        });
    },

    clickCardFunction: function(event) {
        var that = this;

        var tar = $(event.target);
        if (tar.length == 1 && tar[0].tagName == "A")
            return true;

        if (!this.successfulDrag && this.infoDialog.dialog("isOpen")) {
            this.infoDialog.dialog("close");
            event.stopPropagation();
            return false;
        }

        if (tar.hasClass("actionArea")) {
            tar = tar.parent();
            if (tar.hasClass("borderOverlay")) {
                var selectedCardElem = tar.parent();
                if (event.which == 1) {
                    if (!this.successfulDrag) {
                        if (event.shiftKey) {
                            this.displayCardInfo(selectedCardElem.data("card"));
                        } else if (selectedCardElem.hasClass("cardInCollection")) {
                            var cardData = selectedCardElem.data("card");
                            this.selectionFunc(cardData.blueprintId, cardData.zone);
                            cardData.tokens = {count:(parseInt(cardData.tokens["count"]) - 1)};
                            layoutTokens(selectedCardElem);
                        } else if (selectedCardElem.hasClass("packInCollection")) {
                            if (confirm("Would you like to open this pack?")) {
                                this.comm.openPack(this.getCollectionType(), selectedCardElem.data("card").blueprintId, function() {
                                    that.cardFilter.getCollection();
                                }, {
                                    "404": function() {
                                        alert("You have no pack of this type in your collection.");
                                    }
                                });
                            }
                        } else if (selectedCardElem.hasClass("cardToSelect")) {
                            this.comm.openSelectionPack(this.getCollectionType(), this.packSelectionId, selectedCardElem.data("card").blueprintId, function() {
                                that.cardFilter.getCollection();
                            }, {
                                "404": function() {
                                    alert("You have no pack of this type in your collection or that selection is not available for this pack.");
                                }
                            });
                            this.selectionDialog.dialog("close");
                        } else if (selectedCardElem.hasClass("selectionInCollection")) {
                            var selectionDialogResize = function() {
                                var width = that.selectionDialog.width() + 10;
                                var height = that.selectionDialog.height() + 10;
                                that.selectionGroup.setBounds(2, 2, width - 2 * 2, height - 2 * 2);
                            };

                            if (this.selectionDialog == null) {
                                this.selectionDialog = $("<div></div>")
                                        .dialog({
                                    title: "Choose one",
                                    autoOpen: false,
                                    closeOnEscape: true,
                                    resizable: true,
                                    width: 400,
                                    height: 200,
                                    modal: true
                                });

                                this.selectionGroup = new NormalCardGroup(this.selectionDialog, function(card) {
                                    return true;
                                }, false);

                                this.selectionDialog.bind("dialogresize", selectionDialogResize);
                            }
                            this.selectionDialog.html("");
                            var cardData = selectedCardElem.data("card");
                            this.packSelectionId = cardData.blueprintId;
                            var selection = selectedCardElem.data("selection");
                            var blueprintIds = selection.split("|");
                            for (var i = 0; i < blueprintIds.length; i++) {
                                var card = new Card(blueprintIds[i], "selection", "selection" + i, "player");
                                var cardDiv = createCardDiv(card.imageUrl, null, card.isFoil(), false, card.isPack(), card.hasErrata());
                                cardDiv.data("card", card);
                                cardDiv.addClass("cardToSelect");
                                this.selectionDialog.append(cardDiv);
                            }
                            openSizeDialog(that.selectionDialog);
                            selectionDialogResize();
                        } else if (selectedCardElem.hasClass("cardInDeck")) {
                            this.removeCardFromDeck(selectedCardElem);
                        }
                        event.stopPropagation();
                    }
                }
                return false;
            }
        }
        return true;
    },

    dragCardData: null,
    dragStartX: null,
    dragStartY: null,
    successfulDrag: null,

    dragStartCardFunction: function(event) {
        this.successfulDrag = false;
        var tar = $(event.target);
        if (tar.hasClass("actionArea")) {
            tar = tar.parent();
            if (tar.hasClass("borderOverlay")) {
                var selectedCardElem = tar.parent();
                if (event.which == 1) {
                    this.dragCardData = selectedCardElem.data("card");
                    this.dragStartX = event.clientX;
                    this.dragStartY = event.clientY;
                    return false;
                }
            }
        }
        return true;
    },

    dragStopCardFunction: function(event) {
        if (this.dragCardData != null) {
            if (this.dragStartY - event.clientY >= 20) {
                this.displayCardInfo(this.dragCardData);
                this.successfulDrag = true;
            }
            this.dragCardData = null;
            this.dragStartX = null;
            this.dragStartY = null;
            return false;
        }
        return true;
    },

    displayCardInfo: function(card) {
        this.infoDialog.html("");
        this.infoDialog.html("<div style='scroll: auto'></div>");
        this.infoDialog.append(createFullCardDiv(card.imageUrl, card.foil, card.horizontal, card.isPack()));
        if (!card.isPack())
            this.infoDialog.append("<div><a href='" + card.getWikiLink() + "' target='_blank'>Wiki</a></div>");
        var windowWidth = $(window).width();
        var windowHeight = $(window).height();

        var horSpace = 30;
        var vertSpace = 45;

        if (card.horizontal) {
            // 500x360
            this.infoDialog.dialog({width: Math.min(500 + horSpace, windowWidth), height: Math.min(380 + vertSpace, windowHeight)});
        } else {
            // 360x500
            this.infoDialog.dialog({width: Math.min(360 + horSpace, windowWidth), height: Math.min(520 + vertSpace, windowHeight)});
        }
        this.infoDialog.dialog("open");
    },

    getDeckContents: function() {
        var ringBearer = $(".card", this.ringBearerDiv);
        var ring = $(".card", this.ringDiv);

        var result = "";
        if (ringBearer.length > 0)
            result += ringBearer.data("card").blueprintId;
        result += "|";
        if (ring.length > 0)
            result += ring.data("card").blueprintId;
        result += "|";

        var sites = new Array();
        $(".card", this.siteDiv).each(
                function() {
                    sites.push($(this).data("card").blueprintId);
                });
        result += sites;
        result += "|";

        var cards = new Array();
        $(".card", this.drawDeckDiv).each(
                function() {
                    cards.push($(this).data("card").blueprintId);
                });
        result += cards;

        return result;
    },

    saveDeck: function(reloadList) {
        var that = this;

        var deckContents = this.getDeckContents();
        if (deckContents == null)
            alert("Deck must contain at least Ring-bearer, The One Ring and 9 sites");
        else
            this.comm.saveDeck(this.deckName, deckContents, function(xml) {
                that.deckModified(false);
                alert("Deck was saved");
            }, {
                "400": function() {
                    alert("Invalid deck format.");
                }
            });
    },

    addCardToContainer: function(blueprintId, zone, container, tokens) {
        var card = new Card(blueprintId, zone, "deck", "player");
        var cardDiv = createCardDiv(card.imageUrl, null, card.isFoil(), tokens, card.isPack(), card.hasErrata());
        cardDiv.data("card", card);
        container.append(cardDiv);
        return cardDiv;
    },

    showPredefinedFilter: function(filter, container) {
        this.cardFilter.enableDetailFilters(false);
        this.cardFilter.setFilter(filter);

        var that = this;
        this.selectionFunc = function(blueprintId) {
            var cardDiv = this.addCardToContainer(blueprintId, "special", container, false);
            cardDiv.addClass("cardInDeck");
            that.showNormalFilter();
            that.layoutSpecialGroups();
            that.deckDirty = true;
            that.deckModified(true);
        };
    },

    showNormalFilter: function() {
        this.cardFilter.enableDetailFilters(true);
        this.cardFilter.setFilter("cardType:-THE_ONE_RING");

        this.selectionFunc = this.addCardToDeckAndLayout;
    },

    addCardToDeckAndLayout: function(blueprintId, side) {
        var that = this;
        if (side == "FREE_PEOPLE") {
            this.addCardToDeck(blueprintId, side);
            that.fpDeckGroup.layoutCards();
        } else if (side == "SHADOW") {
            this.addCardToDeck(blueprintId, side);
            that.shadowDeckGroup.layoutCards();
        } else if (side == null) {
            var div = this.addCardToContainer(blueprintId, side, this.siteDiv, false)
            div.addClass("cardInDeck");
            that.siteGroup.layoutCards();
        }
        that.deckModified(true);
    },

    deckModified: function(value) {
        var name = (this.deckName == null) ? "New deck" : this.deckName;
        if (value)
            $("#editingDeck").text(name + " - modified");
        else
            $("#editingDeck").text(name);
    },

    addCardToDeck: function(blueprintId, side) {
        var that = this;
        var added = false;
        $(".card.cardInDeck", this.drawDeckDiv).each(
                function() {
                    var cardData = $(this).data("card");
                    if (cardData.blueprintId == blueprintId) {
                        var attDiv = that.addCardToContainer(blueprintId, "attached", that.drawDeckDiv, false);
                        cardData.attachedCards.push(attDiv);
                        added = true;
                    }
                });
        if (!added) {
            var div = this.addCardToContainer(blueprintId, side, this.drawDeckDiv, false)
            div.addClass("cardInDeck");
        }

        this.deckDirty = true;
    },

    checkDeckStatsDirty: function() {
        if (this.deckDirty) {
            this.deckDirty = false;
            this.updateDeckStats();
        } else {
            var that = this;
            setTimeout(
                    function() {
                        that.checkDeckStatsDirty();
                    }, that.checkDirtyInterval);
        }
    },

    updateDeckStats: function() {
        var that = this;
        var deckContents = this.getDeckContents();
        if (deckContents != null) {
            this.comm.getDeckStats(deckContents,
                    function(html) {
                        $("#deckStats").html(html);
                        setTimeout(
                                function() {
                                    that.checkDeckStatsDirty();
                                }, that.checkDirtyInterval);
                    }, {
                "400": function() {
                    alert("Invalid deck for getting stats.");
                }
            });
        } else {
            $("#deckStats").html("Deck has no Ring, Ring-bearer or all 9 sites");
            setTimeout(
                    function() {
                        that.checkDeckStatsDirty();
                    }, that.checkDirtyInterval);
        }
    },

    removeCardFromDeck: function(cardDiv) {
        var cardData = cardDiv.data("card");
        if (cardData.attachedCards.length > 0) {
            cardData.attachedCards[0].remove();
            cardData.attachedCards.splice(0, 1);
        } else {
            cardDiv.remove();
        }
        var cardInCollectionElem = null;
        $(".card", this.normalCollectionDiv).each(
                function() {
                    var tempCardData = $(this).data("card");
                    if (tempCardData.blueprintId == cardData.blueprintId)
                        cardInCollectionElem = $(this);
                });
        if (cardInCollectionElem != null) {
            var cardInCollectionData = cardInCollectionElem.data("card");
            cardInCollectionData.tokens = {count:(parseInt(cardInCollectionData.tokens["count"]) + 1)};
            layoutTokens(cardInCollectionElem);
        }

        this.layoutDeck();
        this.deckDirty = true;
        this.deckModified(true);
    },

    clearDeck: function() {
        $(".cardInDeck").each(
                function() {
                    var cardData = $(this).data("card");
                    for (var i = 0; i < cardData.attachedCards.length; i++)
                        cardData.attachedCards[i].remove();
                });
        $(".cardInDeck").remove();

        this.layoutUI(false);

        this.deckDirty = true;
    },

    setupDeck: function(xml, deckName) {
        var root = xml.documentElement;
        if (root.tagName == "deck") {
            this.clearDeck();
            this.deckName = deckName;
            $("#editingDeck").text(deckName);

            var ringBearer = root.getElementsByTagName("ringBearer");
            if (ringBearer.length > 0)
                this.addCardToContainer(ringBearer[0].getAttribute("blueprintId"), "deck", this.ringBearerDiv, false).addClass("cardInDeck");

            var ring = root.getElementsByTagName("ring");
            this.addCardToContainer(ring[0].getAttribute("blueprintId"), "deck", this.ringDiv, false).addClass("cardInDeck");

            var sites = root.getElementsByTagName("site");
            for (var i = 0; i < sites.length; i++)
                this.addCardToContainer(sites[i].getAttribute("blueprintId"), "deck", this.siteDiv, false).addClass("cardInDeck");

            var cards = root.getElementsByTagName("card");
            for (var i = 0; i < cards.length; i++)
                this.addCardToDeck(cards[i].getAttribute("blueprintId"), cards[i].getAttribute("side"));

            this.layoutUI(false);

            this.cardFilter.getCollection();
        }
        this.deckModified(false);
    },

    clearCollection: function() {
        $(".card", this.normalCollectionDiv).remove();
    },

    addCardToCollection: function(type, blueprintId, count, side, contents) {
        if (type == "pack") {
            if (blueprintId.substr(0, 3) == "(S)") {
                var card = new Card(blueprintId, "pack", "collection", "player");
                card.tokens = {"count":count};
                var cardDiv = createCardDiv(card.imageUrl, null, false, true, true, false);
                cardDiv.data("card", card);
                cardDiv.data("selection", contents);
                cardDiv.addClass("selectionInCollection");
            } else {
                var card = new Card(blueprintId, "pack", "collection", "player");
                card.tokens = {"count":count};
                var cardDiv = createCardDiv(card.imageUrl, null, false, true, true, false);
                cardDiv.data("card", card);
                cardDiv.addClass("packInCollection");
            }
            this.normalCollectionDiv.append(cardDiv);
        } else if (type == "card") {
            var card = new Card(blueprintId, side, "collection", "player");
            var countInDeck = 0;
            $(".card", this.deckDiv).each(
                    function () {
                        var tempCardData = $(this).data("card");
                        if (blueprintId == tempCardData.blueprintId)
                            countInDeck++;
                    });
            card.tokens = {"count":count - countInDeck};
            var cardDiv = createCardDiv(card.imageUrl, null, card.isFoil(), true, false, card.hasErrata());
            cardDiv.data("card", card);
            cardDiv.addClass("cardInCollection");
            this.normalCollectionDiv.append(cardDiv);
        }
    },

    finishCollection: function() {
        this.normalCollectionGroup.layoutCards();
    },

    layoutUI: function(layoutDivs) {
        if (layoutDivs) {
            var manageHeight = 23;

            var padding = 5;
            var collectionWidth = this.collectionDiv.width();
            var collectionHeight = this.collectionDiv.height();

            var deckWidth = this.deckDiv.width();
            var deckHeight = this.deckDiv.height() - (manageHeight + padding);

            var rowHeight = Math.floor((deckHeight - 6 * padding) / 5);
            var sitesWidth = Math.floor(1.5 * deckHeight / 5);
            sitesWidth = Math.min(sitesWidth, 250);

            this.manageDecksDiv.css({position: "absolute", left: padding, top: padding, width: deckWidth, height: manageHeight});

            this.ringBearerDiv.css({ position: "absolute", left: padding, top: manageHeight + 2 * padding, width: Math.floor((sitesWidth - padding) / 2), height: rowHeight });
            this.ringBearerGroup.setBounds(0, 0, Math.floor((sitesWidth - padding) / 2), rowHeight);
            this.ringDiv.css({ position: "absolute", left: Math.floor((sitesWidth + 3 * padding) / 2), top: manageHeight + 2 * padding, width: Math.floor((sitesWidth - padding) / 2), height: rowHeight });
            this.ringGroup.setBounds(0, 0, Math.floor((sitesWidth - padding) / 2), rowHeight);

            this.siteDiv.css({ position: "absolute", left: padding, top: manageHeight + 3 * padding + rowHeight, width: sitesWidth, height: deckHeight - rowHeight - 2 * padding});
            this.siteGroup.setBounds(0, 0, sitesWidth, deckHeight - rowHeight - 2 * padding);

            this.drawDeckDiv.css({ position: "absolute", left: padding * 2 + sitesWidth , top: manageHeight + 2 * padding, width: deckWidth - (sitesWidth + padding) - padding, height: deckHeight - 2 * padding - 50 });
            this.fpDeckGroup.setBounds(0, 0, deckWidth - (sitesWidth + padding) - padding, (deckHeight - 2 * padding - 50) / 2);
            this.shadowDeckGroup.setBounds(0, (deckHeight - 2 * padding - 50) / 2, deckWidth - (sitesWidth + padding) - padding, (deckHeight - 2 * padding - 50) / 2);

            this.bottomBarDiv.css({ position: "absolute", left: padding * 2 + sitesWidth , top: manageHeight + padding + deckHeight - 50, width: deckWidth - (sitesWidth + padding) - padding, height: 50 });

            this.cardFilter.layoutUi(padding, 0, collectionWidth - padding, 160);
            this.normalCollectionDiv.css({ position: "absolute", left: padding, top: 160, width: collectionWidth - padding * 2, height: collectionHeight - 160 });

            this.normalCollectionGroup.setBounds(0, 0, collectionWidth - padding * 2, collectionHeight - 160);
        } else {
            this.layoutDeck();
            this.normalCollectionGroup.layoutCards();
        }
    },

    layoutSpecialGroups: function() {
        this.ringBearerGroup.layoutCards();
        this.ringGroup.layoutCards();
        this.siteGroup.layoutCards();

    },

    layoutDeck: function() {
        this.layoutSpecialGroups();
        this.fpDeckGroup.layoutCards();
        this.shadowDeckGroup.layoutCards();
    },

    processError: function (xhr, ajaxOptions, thrownError) {
        if (thrownError != "abort")
            alert("There was a problem during communication with server");
    }
});
