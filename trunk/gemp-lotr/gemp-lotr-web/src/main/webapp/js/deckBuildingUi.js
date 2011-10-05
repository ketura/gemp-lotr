var GempLotrDeckBuildingUI = Class.extend({
    comm: null,

    collectionType: "default",
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
    selectionFunc: null,
    pageDiv: null,
    filterDiv: null,
    drawDeckDiv: null,
    fpDeckGroup: null,
    shadowDeckGroup: null,
    start: 0,
    count: 18,
    filter: null,

    filterDirty: false,
    deckDirty: false,

    init: function() {
        var that = this;

        this.filter = "cardType:-SITE,THE_ONE_RING";

        this.comm = new GempLotrCommunication("/gemp-lotr/server", that.processError);

        this.deckDiv = $("#deckDiv");

        this.collectionDiv = $("#collectionDiv");

        this.pageDiv = $("<div></div>");
        this.pageDiv.append("<button id='previousPage' style='float: left;'>Previous page</button>");
        this.pageDiv.append("<button id='nextPage' style='float: right;'>Next page</button>");
        this.pageDiv.append("<div id='countSlider' style='left: 33px; top: 10px; width: 200px;'></div>");
        this.collectionDiv.append(this.pageDiv);

        this.filterDiv = $("<div id='filtering'></div>");

        this.filterDiv.append("<div id='culture1'>"
                + "<input type='checkbox' id='DWARVEN'/><label for='DWARVEN' id='labelDWARVEN'><img src='images/cultures/dwarven.gif'/></label>"
                + "<input type='checkbox' id='ELVEN'/><label for='ELVEN' id='labelELVEN'><img src='images/cultures/elven.gif'/></label>"
                + "<input type='checkbox' id='GANDALF'/><label for='GANDALF' id='labelGANDALF'><img src='images/cultures/gandalf.gif'/></label>"
                + "<input type='checkbox' id='GONDOR'/><label for='GONDOR' id='labelGONDOR'><img src='images/cultures/gondor.gif'/></label>"
                + "<input type='checkbox' id='SHIRE'/><label for='SHIRE' id='labelSHIRE'><img src='images/cultures/shire.gif'/></label>"
                + "</div>");
        this.filterDiv.append("<div id='culture2'>"
                + "<input type='checkbox' id='ISENGARD'/><label for='ISENGARD' id='labelISENGARD'><img src='images/cultures/isengard.gif'/></label>"
                + "<input type='checkbox' id='MORIA'/><label for='MORIA' id='labelMORIA'><img src='images/cultures/moria.gif'/></label>"
                + "<input type='checkbox' id='SAURON'/><label for='SAURON' id='labelSAURON'><img src='images/cultures/sauron.gif'/></label>"
                + "<input type='checkbox' id='WRAITH'/><label for='WRAITH' id='labelWRAITH'><img src='images/cultures/wraith.gif'/></label>"
                + "</div>");

        var combos = $("<div></div>");

        combos.append("<select id='set'>"
                + "<option value=''>All Sets</option>"
                + "<option value='1'>01 - The Fellowship of the Ring</option>"
                + "<option value='2'>02 - Mines of Moria</option>"
                + "<option value='3'>03 - Realms of the Elf-lords</option>"
                + "</select>");

        combos.append(" <select id='cardType'>"
                + "<option value=''>All Card Types</option>"
                + "<option value='COMPANION'>Companions</option>"
                + "<option value='ALLY'>Allies</option>"
                + "<option value='MINION'>Minions</option>"
                + "<option value='POSSESSION'>Possessions</option>"
                + "<option value='ARTIFACT'>Artifacts</option>"
                + "<option value='EVENT'>Events</option>"
                + "<option value='CONDITION'>Conditions</option>"
                + "</select>");
        this.filterDiv.append(combos);

        this.collectionDiv.append(this.filterDiv);

        $("#culture1").buttonset();
        $("#culture2").buttonset();

        var filterOut = function() {
            that.filter = that.calculateNormalFilter();
            that.start = 0;
            that.getCollection();
            return true;
        };

        $("#set").change(filterOut);
        $("#cardType").change(filterOut);

        $("#labelDWARVEN,#labelELVEN,#labelGANDALF,#labelGONDOR,#labelSHIRE,#labelISENGARD,#labelMORIA,#labelSAURON,#labelWRAITH").click(filterOut);

        $("#countSlider").slider({
            value:18,
            min: 4,
            max: 40,
            step: 1,
            disabled: true,
            slide: function(event, ui) {
                that.start = 0;
                that.count = ui.value;
                that.getCollection();
            }
        });

        $("#previousPage").button({
            text: false,
            icons: {
                primary: "ui-icon-circle-triangle-w"
            },
            disabled: true
        }).click(
                function() {
                    $("#previousPage").button("option", "disabled", true);
                    $("#nextPage").button("option", "disabled", true);
                    $("#countSlider").button("option", "disabled", true);
                    $(".card", that.normalCollectionDiv).remove();
                    $(".card", that.specialCollectionDiv).remove();
                    that.start -= that.count;
                    that.getCollection();
                });

        $("#nextPage").button({
            text: false,
            icons: {
                primary: "ui-icon-circle-triangle-e"
            },
            disabled: true
        }).click(
                function() {
                    $("#previousPage").button("option", "disabled", true);
                    $("#nextPage").button("option", "disabled", true);
                    $("#countSlider").button("option", "disabled", true);
                    $(".card", that.normalCollectionDiv).remove();
                    $(".card", that.specialCollectionDiv).remove();
                    that.start += that.count;
                    that.getCollection();
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
                    if ($(".card", that.ringBearerDiv).length == 0)
                        that.showPredefinedFilter("keyword:RING_BEARER", that.ringBearerDiv);
                });
        this.ringBearerGroup = new NormalCardGroup(this.ringBearerDiv, function(card) {
            return true;
        });
        this.deckDiv.append(this.ringBearerDiv);

        this.ringDiv = $("<div>Ring</div>");
        this.ringDiv.click(
                function() {
                    if ($(".card", that.ringDiv).length == 0)
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
                            if ($(".card", container).length == 0)
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
        this.fpDeckGroup = new NormalCardGroup(this.drawDeckDiv, function(card) {
            return (card.zone == "FREE_PEOPLE");
        });
        this.fpDeckGroup.maxCardHeight = 200;
        this.shadowDeckGroup = new NormalCardGroup(this.drawDeckDiv, function(card) {
            return (card.zone == "SHADOW");
        });
        this.shadowDeckGroup.maxCardHeight = 200;

        this.bottomBarDiv = $("<div></div>");
        this.bottomBarDiv.append("<button id='saveDeck' style='float: right;'>Save deck</button>");
        this.bottomBarDiv.append("<div id='deckStats'></div>")
        this.deckDiv.append(this.bottomBarDiv);
        $("#saveDeck").button().click(
                function() {
                    that.saveDeck();
                });

        this.deckDiv.append(this.drawDeckDiv);

        this.selectionFunc = this.addCardToDeck;

        $("body").click(function (event) {
            var tar = $(event.target);
            if (tar.hasClass("actionArea")) {
                tar = tar.parent();
                if (tar.hasClass("borderOverlay")) {
                    var selectedCardElem = tar.parent();
                    if (event.which == 1) {
                        if (event.shiftKey) {
                            that.displayCardInfo(selectedCardElem.data("card"));
                        } else  if (selectedCardElem.hasClass("cardInCollection")) {
                            that.selectionFunc(selectedCardElem.data("card").blueprintId, selectedCardElem.data("card").zone);
                        } else if (selectedCardElem.hasClass("cardInDeck")) {
                            that.removeCardFromDeck(selectedCardElem);
                        }
                        return false;
                    }
                }
            }
            return false;
        });

        this.infoDialog = $("<div></div>")
                .dialog({
            autoOpen: false,
            closeOnEscape: true,
            resizable: true,
            title: "Card information",
            minHeight: 80,
            minWidth: 200,
            width: 600,
            height: 300
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

        this.comm.getDeck("default", function(xml) {
            that.setupDeck(xml);
        });

        this.checkDeckStatsDirty();
    },

    displayCardInfo: function(card) {
        this.infoDialog.html("<div style='scroll: auto'><img src='" + card.imageUrl + "'></div>");
        this.infoDialog.dialog("open");
    },

    getDeckContents: function() {
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
            return null;
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

            return "" + cards;
        }
    },

    saveDeck: function() {
        var deckContents = this.getDeckContents();
        if (deckContents == null)
            alert("Deck must contain at least Ring-bearer, The One Ring and 9 sites");
        else
            this.comm.saveDeck("default", deckContents, function(xml) {
                alert("Deck was saved");
            });
    },

    addCardToContainer: function(blueprintId, zone, container) {
        var card = new Card(blueprintId, zone, "deck", "player");
        var cardDiv = createCardDiv(card.imageUrl, null, card.isFoil());
        cardDiv.data("card", card);
        container.append(cardDiv);
        this.layoutUI();
        return cardDiv;
    },

    showPredefinedFilter: function(filter, container) {
        this.normalCollectionDiv.hide();
        this.filterDiv.hide();
        this.specialCollectionDiv.show();

        this.filter = filter;
        this.start = 0;
        this.getCollection();
        this.selectionFunc = function(blueprintId) {
            var cardDiv = this.addCardToContainer(blueprintId, "special", container);
            cardDiv.addClass("cardInDeck");
            this.showNormalFilter();
        };
    },

    showNormalFilter: function() {
        this.specialCollectionDiv.hide();
        this.normalCollectionDiv.show();
        this.filterDiv.show();

        this.filter = this.calculateNormalFilter();
        this.start = 0;
        this.getCollection();
        this.selectionFunc = this.addCardToDeck;
    },

    calculateNormalFilter: function() {
        var cultures = new Array();
        $("label", $("#culture1")).each(
                function() {
                    if ($(this).hasClass("ui-state-active"))
                        cultures.push($(this).prop("id").substring(5));
                });
        $("label", $("#culture2")).each(
                function() {
                    if ($(this).hasClass("ui-state-active"))
                        cultures.push($(this).prop("id").substring(5));
                });

        var setNo = $("#set option:selected").prop("value");
        if (setNo != "")
            setNo = " set:" + setNo;

        var cardType = $("#cardType option:selected").prop("value");
        if (cardType == "")
            cardType = "cardType:-SITE,THE_ONE_RING";
        else
            cardType = "cardType:" + cardType;

        if (cultures.length > 0)
            return cardType + setNo + " culture:" + cultures;
        else
            return cardType + setNo;
    },

    addCardToDeck: function(blueprintId, side) {
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
            var div = this.addCardToContainer(blueprintId, side, this.drawDeckDiv)
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
                        that.checkDeckStatsDirty()
                    }, 100);
        }
    },

    updateDeckStats: function() {
        var that = this;
        var deckContents = this.getDeckContents();
        if (deckContents != null)
            this.comm.getDeckStats(deckContents,
                    function(html) {
                        $("#deckStats").html(html);
                        setTimeout(
                                function() {
                                    that.checkDeckStatsDirty()
                                }, 100);
                    });
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

        this.deckDirty = true;
    },

    getCollection: function() {
        var that = this;
        this.comm.getCollection(this.collectionType, this.filter, this.start, this.count, function(xml) {
            that.displayCollection(xml);
        });
    },

    setupDeck: function(xml) {
        var root = xml.documentElement;
        if (root.tagName == "deck") {
            var ringBearer = root.getElementsByTagName("ringBearer");
            if (ringBearer.length == 1) {
                this.addCardToContainer(ringBearer[0].getAttribute("blueprintId"), "deck", this.ringBearerDiv).addClass("cardInDeck");
                this.addCardToContainer(root.getElementsByTagName("ring")[0].getAttribute("blueprintId"), "deck", this.ringDiv).addClass("cardInDeck");
                var sites = root.getElementsByTagName("site");
                for (var i = 0; i < 9; i++)
                    this.addCardToContainer(sites[i].getAttribute("blueprintId"), "deck", this.siteDivs[i]).addClass("cardInDeck");

                var cards = root.getElementsByTagName("card");
                for (var i = 0; i < cards.length; i++)
                    this.addCardToDeck(cards[i].getAttribute("blueprintId"), cards[i].getAttribute("side"));

                this.layoutUI();
            }

            this.getCollection();
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
                var card = new Card(blueprintId, cardElem.getAttribute("side"), "collection" + i, "player");
                var cardDiv = createCardDiv(card.imageUrl, null, card.isFoil());
                cardDiv.data("card", card);
                cardDiv.addClass("cardInCollection");
                if (this.normalCollectionDiv.is(":visible"))
                    this.normalCollectionDiv.append(cardDiv);
                else
                    this.specialCollectionDiv.append(cardDiv);
            }

            this.normalCollectionGroup.layoutCards();
            this.specialCollectionGroup.layoutCards();

            $("#previousPage").button("option", "disabled", this.start == 0);
            var cnt = parseInt(root.getAttribute("count"));
            $("#nextPage").button("option", "disabled", (this.start + this.count) >= cnt);
            $("#countSlider").slider("option", "disabled", false);
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
        this.fpDeckGroup.setBounds(0, 0, deckWidth - (sitesWidth + padding) * 2 - padding, (deckHeight - 2 * padding - 50) / 2);
        this.shadowDeckGroup.setBounds(0, (deckHeight - 2 * padding - 50) / 2, deckWidth - (sitesWidth + padding) * 2 - padding, (deckHeight - 2 * padding - 50) / 2);

        this.bottomBarDiv.css({ position: "absolute", left: padding * 3 + sitesWidth * 2, top: deckHeight - 50, width: deckWidth - (sitesWidth + padding) * 2 - padding, height: 50 });

        this.filterDiv.css({ position: "absolute", left: padding, top: 50, width: collectionWidth - padding, height: 80 });
        this.normalCollectionDiv.css({ position: "absolute", left: padding, top: 130, width: collectionWidth - padding * 2, height: collectionHeight - 130 });
        this.pageDiv.css({ position: "absolute", left: padding, top: 0, width: collectionWidth - padding, height: 50 });
        this.specialCollectionDiv.css({ position: "absolute", left: padding, top: 50, width: collectionWidth - padding * 2, height: collectionHeight - 50 });

        this.normalCollectionGroup.setBounds(0, 0, collectionWidth - padding * 2, collectionHeight - 130);
        this.specialCollectionGroup.setBounds(0, 0, collectionWidth - padding * 2, collectionHeight - 50);
    },

    processError: function (xhr, ajaxOptions, thrownError) {
        alert("There was a problem during communication with server");
    }
});