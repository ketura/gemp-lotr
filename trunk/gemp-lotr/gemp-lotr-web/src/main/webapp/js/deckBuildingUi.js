var GempLotrDeckBuildingUI = Class.extend({
    deckDiv: null,
    ringBearerDiv: null,
    ringBearerGroup: null,
    ringDiv: null,
    ringGroup: null,
    siteDivs: null,
    siteGroups: null,
    collectionDiv: null,
    normalCollectionDiv: null,
    normalCollectionGroup: null,
    specialCollectionDiv: null,
    specialCollectionGroup: null,
    loadCollectionFunc: null,
    selectionFunc: null,

    init: function() {
        var that = this;

        this.deckDiv = $("#deckDiv");

        this.collectionDiv = $("#collectionDiv");

        this.normalCollectionDiv = $("<div></div>");
        this.normalCollectionGroup = new NormalCardGroup(null, this.normalCollectionDiv, function(card) {
            return true;
        });
        this.normalCollectionGroup.maxCardHeight = 200;
        this.collectionDiv.append(this.normalCollectionDiv);

        this.specialCollectionDiv = $("<div></div>");
        this.specialCollectionDiv.hide();
        this.specialCollectionGroup = new NormalCardGroup(null, this.specialCollectionDiv, function(card) {
            return true;
        });
        this.specialCollectionGroup.maxCardHeight = 200;
        this.collectionDiv.append(this.specialCollectionDiv);

        this.ringBearerDiv = $("<div>Ring Bearer</div>");
        this.ringBearerDiv.click(
                function() {
                    $(".card", that.ringBearerDiv).remove();
                    that.showPredefinedFilter("keyword:RING_BEARER", that.ringBearerDiv);
                });
        this.ringBearerGroup = new NormalCardGroup(null, this.ringBearerDiv, function(card) {
            return true;
        });
        this.deckDiv.append(this.ringBearerDiv);

        this.ringDiv = $("<div>Ring</div>");
        this.ringDiv.click(
                function() {
                    $(".card", that.ringDiv).remove();
                    that.showPredefinedFilter("cardType:THE_ONE_RING", that.ringDiv);
                });
        this.ringGroup = new NormalCardGroup(null, this.ringDiv, function(card) {
            return true;
        });
        this.deckDiv.append(this.ringDiv);

        this.siteDivs = new Array();
        this.siteGroups = new Array();
        for (var i = 1; i <= 9; i++) {
            var siteDiv = $("<div>Site " + i + "</div>");
            siteDiv.click(
                    (function (siteNumber, container) {
                        return function() {
                            $(".card", container).remove();
                            that.showPredefinedFilter("cardType:SITE siteNumber:" + siteNumber, container);
                        };
                    })(i, siteDiv));
            this.deckDiv.append(siteDiv);
            this.siteGroups.push(new NormalCardGroup(null, siteDiv, function(card) {
                return true;
            }));
            this.siteDivs.push(siteDiv);
        }

        this.drawDeckDiv = $("<div></div>");
        this.drawDeckGroup = new NormalCardGroup(null, this.drawDeckDiv, function(card) {
            return (card.zone == "deck");
        });
        this.drawDeckGroup.maxCardHeight = 200;
        this.deckDiv.append(this.drawDeckDiv);

        this.selectionFunc = this.addCardToDeck;

        $("body").click(function (event) {
            var tar = $(event.target);
            if (tar.hasClass("borderOverlay")) {
                var selectedCardElem = tar.parent();
                if (event.which == 1) {
                    if (event.shiftKey) {
                        //                        that.displayCardInfo(selectedCardElem.data("card"));
                    } else  if (selectedCardElem.hasClass("cardInCollection")) {
                        that.selectionFunc(selectedCardElem.data("card").blueprintId);
                    } else if (selectedCardElem.hasClass("cardInDeck")) {
                        that.removeCardFromDeck(selectedCardElem);
                    }
                }
            }
            return false;
        });
    },

    addCardToContainer: function(blueprintId, zone, container) {
        var card = new Card(blueprintId, zone, "deckCardId", "player");
        var cardDiv = createCardDiv(card.imageUrl, null);
        cardDiv.data("card", card);
        container.append(cardDiv);
        this.layoutUI();
        return cardDiv;
    },

    showPredefinedFilter: function(filter, container) {
        this.normalCollectionDiv.hide();
        this.specialCollectionDiv.show();
        this.loadCollectionFunc("default", filter, 0, 18);
        this.selectionFunc = function(blueprintId) {
            this.addCardToContainer(blueprintId, "special", container);
            this.specialCollectionDiv.hide();
            this.normalCollectionDiv.show();
            this.selectionFunc = this.addCardToDeck;
        };
    },

    addCardToDeck: function(blueprintId) {
        var that = this;
        var added = false;
        $(".card.cardInDeck", this.drawDeckDiv).each(
                function() {
                    var cardData = $(this).data("card");
                    if (cardData.blueprintId == blueprintId) {
                        var attDiv = that.addCardToContainer(blueprintId, "attached", that.drawDeckDiv);
                        cardData.attachedCards.push(attDiv);
                        added = true;
                        that.layoutUI();
                    }
                });
        if (!added) {
            var div = this.addCardToContainer(blueprintId, "deck", this.drawDeckDiv)
            div.addClass("cardInDeck");
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
        this.layoutUI();
    },

    setLoadCollectionFunc: function(func) {
        this.loadCollectionFunc = func;
    },

    setupDeck: function(xml) {
        this.loadCollectionFunc("default", "cardType:-SITE,THE_ONE_RING", 0, 18);
    },

    displayCollection: function(xml) {
        var root = xml.documentElement;
        if (root.tagName == "collection") {
            if (this.normalCollectionDiv.is(":visible"))
                this.normalCollectionDiv.html("");
            else
                this.specialCollectionDiv.html("");

            var cards = root.getElementsByTagName("card");
            for (var i = 0; i < cards.length; i++) {
                var cardElem = cards[i];
                var blueprintId = cardElem.getAttribute("blueprintId");
                var count = cardElem.getAttribute("count");
                var card = new Card(blueprintId, "collection", "collection" + i, "player");
                var cardDiv = createCardDiv(card.imageUrl, null);
                cardDiv.data("card", card);
                cardDiv.addClass("cardInCollection");
                if (this.normalCollectionDiv.is(":visible"))
                    this.normalCollectionDiv.append(cardDiv);
                else
                    this.specialCollectionDiv.append(cardDiv);
            }

            this.normalCollectionGroup.layoutCards();
            this.specialCollectionGroup.layoutCards();
        }
    },

    layoutUI: function() {
        var deckHeight = this.deckDiv.height();
        var rowHeight = Math.floor(deckHeight / 5);
        var sitesWidth = Math.floor(1.5 * deckHeight / 5);
        sitesWidth = Math.min(sitesWidth, 150);

        this.ringBearerDiv.css({ position: "absolute", left: 0, top: 0, width: Math.floor(sitesWidth / 2), height: rowHeight });
        this.ringBearerGroup.setBounds(0, 0, Math.floor(sitesWidth / 2), rowHeight);
        this.ringDiv.css({ position: "absolute", left: Math.floor(sitesWidth / 2), top: 0, width: Math.floor(sitesWidth / 2), height: rowHeight });
        this.ringGroup.setBounds(0, 0, Math.floor(sitesWidth / 2), rowHeight);
        for (var i = 0; i < 4; i++) {
            this.siteDivs[i].css({ position: "absolute", left: 0, top: rowHeight * (i + 1), width: sitesWidth, height: rowHeight });
            this.siteGroups[i].setBounds(0, 0, sitesWidth, rowHeight);
        }
        for (var i = 4; i < 9; i++) {
            this.siteDivs[i].css({ position: "absolute", left: sitesWidth, top: rowHeight * (i - 4), width: sitesWidth, height: rowHeight });
            this.siteGroups[i].setBounds(0, 0, sitesWidth, rowHeight);
        }
        this.drawDeckDiv.css({ position: "absolute", left: sitesWidth * 2, top: 0, width: this.deckDiv.width() - sitesWidth * 2, height: deckHeight });
        this.drawDeckGroup.setBounds(0, 0, this.deckDiv.width() - sitesWidth * 2, deckHeight);

        this.normalCollectionDiv.css({ position: "absolute", left: 0, top: 50, width: this.collectionDiv.width(), height: this.collectionDiv.height() - 50 })
        this.specialCollectionDiv.css({ position: "absolute", left: 0, top: 0, width: this.collectionDiv.width(), height: this.collectionDiv.height() })

        this.normalCollectionGroup.setBounds(0, 0, this.collectionDiv.width(), this.collectionDiv.height() - 50);
        this.specialCollectionGroup.setBounds(0, 0, this.collectionDiv.width(), this.collectionDiv.height());
    },

    processError: function (xhr, ajaxOptions, thrownError) {
        alert("There was a problem during communication with server");
    }
});