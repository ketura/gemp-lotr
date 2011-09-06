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
    saveDeckFunc: null,
    selectionFunc: null,
    filterDiv: null,
    drawDeckDiv: null,
    drawDeckGroup: null,
    start: 0,
    count: 18,

    init: function() {
        var that = this;

        this.deckDiv = $("#deckDiv");

        this.collectionDiv = $("#collectionDiv");

        this.filterDiv = $("<div></div>");
        this.filterDiv.append("<button id='previousPage' style='float: left;'>Previous page</button>");
        this.filterDiv.append("<button id='nextPage' style='float: right;'>Previous page</button>");
        this.collectionDiv.append(this.filterDiv);

        $("#previousPage").button({
            text: false,
            icons: {
                primary: "ui-icon-circle-triangle-w"
            }
        }).click(
                function() {
                    $("#previousPage").button("option", "disabled", true);
                    $("#nextPage").button("option", "disabled", true);
                    $(".card", that.normalCollectionDiv).remove();
                    that.start -= that.count;
                    that.loadCollectionFunc("default", "cardType:-SITE,THE_ONE_RING", that.start, that.count);
                });

        $("#nextPage").button({
            text: false,
            icons: {
                primary: "ui-icon-circle-triangle-e"
            }
        }).click(
                function() {
                    $("#previousPage").button("option", "disabled", true);
                    $("#nextPage").button("option", "disabled", true);
                    $(".card", that.normalCollectionDiv).remove();
                    that.start += that.count;
                    that.loadCollectionFunc("default", "cardType:-SITE,THE_ONE_RING", that.start, that.count);
                });

        this.normalCollectionDiv = $("<div></div>");
        this.normalCollectionGroup = new NormalCardGroup(this.normalCollectionDiv, function(card) {
            return true;
        });
        this.normalCollectionGroup.maxCardHeight = 200;
        this.collectionDiv.append(this.normalCollectionDiv);

        this.specialCollectionDiv = $("<div></div>");
        this.specialCollectionDiv.hide();
        this.specialCollectionGroup = new NormalCardGroup(this.specialCollectionDiv, function(card) {
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
        this.ringBearerGroup = new NormalCardGroup(this.ringBearerDiv, function(card) {
            return true;
        });
        this.deckDiv.append(this.ringBearerDiv);

        this.ringDiv = $("<div>Ring</div>");
        this.ringDiv.click(
                function() {
                    $(".card", that.ringDiv).remove();
                    that.showPredefinedFilter("cardType:THE_ONE_RING", that.ringDiv);
                });
        this.ringGroup = new NormalCardGroup(this.ringDiv, function(card) {
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
            this.siteGroups.push(new NormalCardGroup(siteDiv, function(card) {
                return true;
            }));
            this.siteDivs.push(siteDiv);
        }

        this.drawDeckDiv = $("<div></div>");
        this.drawDeckGroup = new NormalCardGroup(this.drawDeckDiv, function(card) {
            return (card.zone == "deck");
        });
        this.drawDeckGroup.maxCardHeight = 200;

        this.bottomBarDiv = $("<div></div>");
        this.bottomBarDiv.append("<button id='saveDeck' style='float: right;'>Save deck</button>");
        this.deckDiv.append(this.bottomBarDiv);
        $("#saveDeck").button().click(
                function() {
                    that.saveDeck();
                });

        this.deckDiv.append(this.drawDeckDiv);

        this.selectionFunc = this.addCardToDeck;

        $("body").click(function (event) {
            var tar = $(event.target);
            if (tar.hasClass("borderOverlay")) {
                var selectedCardElem = tar.parent();
                if (event.which == 1) {
                    if (event.shiftKey) {
                        // that.displayCardInfo(selectedCardElem.data("card"));
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

    saveDeck: function() {
        var ringBearer = $(".card", this.ringBearerDiv);
        var ring = $(".card", this.ringDiv);
        var site1 = $(".card", this.siteDivs[0]);
        var site2 = $(".card", this.siteDivs[1]);
        var site3 = $(".card", this.siteDivs[2]);
        var site4 = $(".card", this.siteDivs[3]);
        var site5 = $(".card", this.siteDivs[4]);
        var site6 = $(".card", this.siteDivs[5]);
        var site7 = $(".card", this.siteDivs[6]);
        var site8 = $(".card", this.siteDivs[7]);
        var site9 = $(".card", this.siteDivs[8]);

        if (ringBearer.length == 0 || ring.length == 0 || site1.length == 0 || site2.length == 0 || site3.length == 0 || site4.length == 0
                || site5.length == 0 || site6.length == 0 || site7.length == 0 || site8.length == 0 || site9.length == 0) {
            alert("To save a deck, it must have at least a Ring-Bearer, Ring and all 9 sites set");
        } else {
            var cards = new Array();
            cards.push(ringBearer.data("card").blueprintId);
            cards.push(ring.data("card").blueprintId);
            cards.push(site1.data("card").blueprintId);
            cards.push(site2.data("card").blueprintId);
            cards.push(site3.data("card").blueprintId);
            cards.push(site4.data("card").blueprintId);
            cards.push(site5.data("card").blueprintId);
            cards.push(site6.data("card").blueprintId);
            cards.push(site7.data("card").blueprintId);
            cards.push(site8.data("card").blueprintId);
            cards.push(site9.data("card").blueprintId);

            $(".card", this.drawDeckDiv).each(
                    function() {
                        cards.push($(this).data("card").blueprintId);
                    });

            this.saveDeckFunc("default", "" + cards);
        }
    },

    addCardToContainer: function(blueprintId, zone, container) {
        var card = new Card(blueprintId, zone, "deck", "player");
        var cardDiv = createCardDiv(card.imageUrl, null);
        cardDiv.data("card", card);
        container.append(cardDiv);
        this.layoutUI();
        return cardDiv;
    },

    showPredefinedFilter: function(filter, container) {
        this.normalCollectionDiv.hide();
        this.filterDiv.hide();
        this.specialCollectionDiv.show();
        this.loadCollectionFunc("default", filter, 0, 18);
        this.selectionFunc = function(blueprintId) {
            this.addCardToContainer(blueprintId, "special", container);
            this.specialCollectionDiv.hide();
            this.normalCollectionDiv.show();
            this.filterDiv.show();
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

    setSaveDeckFunc: function(func) {
        this.saveDeckFunc = func;
    },

    setupDeck: function(xml) {
        var root = xml.documentElement;
        if (root.tagName == "deck") {
            var ringBearer = root.getElementsByTagName("ringBearer");
            if (ringBearer.length == 1) {
                this.addCardToContainer(ringBearer[0].getAttribute("blueprintId"), "deck", this.ringBearerDiv);
                this.addCardToContainer(root.getElementsByTagName("ring")[0].getAttribute("blueprintId"), "deck", this.ringDiv);
                var sites = root.getElementsByTagName("site");
                for (var i = 0; i < 9; i++)
                    this.addCardToContainer(sites[i].getAttribute("blueprintId"), "deck", this.siteDivs[i]);

                var cards = root.getElementsByTagName("card");
                for (var i = 0; i < cards.length; i++)
                    this.addCardToDeck(cards[i].getAttribute("blueprintId"));

                this.layoutUI();
            }

            this.loadCollectionFunc("default", "cardType:-SITE,THE_ONE_RING", this.start, this.count);
        }
    },

    displayCollection: function(xml) {
        log(xml);
        var root = xml.documentElement;
        if (root.tagName == "collection") {
            if (this.normalCollectionDiv.is(":visible")) {
                $(".card", this.normalCollectionDiv).remove();
            } else {
                $(".card", this.specialCollectionDiv).remove();
            }

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

            if (this.normalCollectionDiv.is(":visible")) {
                $("#previousPage").button("option", "disabled", this.start == 0);
                var cnt = parseInt(root.getAttribute("count"));
                $("#nextPage").button("option", "disabled", (this.start + this.count) >= cnt);
            }
        }
    },

    layoutUI: function() {
        var padding = 5;
        var collectionWidth = this.collectionDiv.width();
        var collectionHeight = this.collectionDiv.height();

        var deckWidth = this.deckDiv.width();
        var deckHeight = this.deckDiv.height();

        var rowHeight = Math.floor((deckHeight - 6 * padding) / 5);
        var sitesWidth = Math.floor(1.5 * deckHeight / 5);
        sitesWidth = Math.min(sitesWidth, 150);

        this.ringBearerDiv.css({ position: "absolute", left: padding, top: padding, width: Math.floor((sitesWidth - padding) / 2), height: rowHeight });
        this.ringBearerGroup.setBounds(0, 0, Math.floor((sitesWidth - padding) / 2), rowHeight);
        this.ringDiv.css({ position: "absolute", left: Math.floor((sitesWidth + 3 * padding) / 2), top: padding, width: Math.floor(sitesWidth - padding / 2), height: rowHeight });
        this.ringGroup.setBounds(0, 0, Math.floor((sitesWidth - padding) / 2), rowHeight);
        for (var i = 0; i < 4; i++) {
            this.siteDivs[i].css({ position: "absolute", left: padding, top: padding + (rowHeight + padding) * (i + 1), width: sitesWidth, height: rowHeight });
            this.siteGroups[i].setBounds(0, 0, sitesWidth, rowHeight);
        }
        for (var i = 4; i < 9; i++) {
            this.siteDivs[i].css({ position: "absolute", left: padding * 2 + sitesWidth, top: padding + (rowHeight + padding) * (i - 4), width: sitesWidth, height: rowHeight });
            this.siteGroups[i].setBounds(0, 0, sitesWidth, rowHeight);
        }
        this.drawDeckDiv.css({ position: "absolute", left: padding * 3 + sitesWidth * 2, top: padding, width: deckWidth - (sitesWidth + padding) * 2 - padding, height: deckHeight - 2 * padding - 50 });
        this.drawDeckGroup.setBounds(0, 0, deckWidth - (sitesWidth + padding) * 2 - padding, deckHeight - 2 * padding - 50);

        this.bottomBarDiv.css({ position: "absolute", left: sitesWidth * 2, top: deckHeight - 50, width: deckWidth - sitesWidth * 2, height: 50 });

        this.normalCollectionDiv.css({ position: "absolute", left: padding, top: 50, width: collectionWidth - padding * 2, height: collectionHeight - 50 });
        this.filterDiv.css({ position: "absolute", left: padding, top: 0, width: collectionWidth - padding, height: 50 });
        this.specialCollectionDiv.css({ position: "absolute", left: padding, top: 0, width: collectionWidth - padding * 2, height: collectionHeight });

        this.normalCollectionGroup.setBounds(0, 0, collectionWidth - padding * 2, collectionHeight - 50);
        this.specialCollectionGroup.setBounds(0, 0, collectionWidth - padding * 2, collectionHeight);
    },

    processError: function (xhr, ajaxOptions, thrownError) {
        alert("There was a problem during communication with server");
    }
});