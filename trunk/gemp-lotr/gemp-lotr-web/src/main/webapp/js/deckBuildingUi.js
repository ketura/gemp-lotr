var GempLotrDeckBuildingUI = Class.extend({
    comm: null,

    collectionType: "default",
    deckDiv: null,

    manageDecksDiv: null,
    decksSelect: null,

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
    fullFilterDiv: null,
    filterDiv: null,
    drawDeckDiv: null,
    fpDeckGroup: null,
    shadowDeckGroup: null,
    start: 0,
    count: 18,
    filter: null,

    deckName: null,

    filterDirty: false,
    deckDirty: false,

    checkDirtyInterval: 300,

    init: function() {
        var that = this;

        this.filter = "cardType:-SITE,THE_ONE_RING";

        this.comm = new GempLotrCommunication("/gemp-lotr/server", that.processError);

        this.deckDiv = $("#deckDiv");

        this.manageDecksDiv = $("<div id='manageDecks'></div>");
        this.manageDecksDiv.append("Opened deck: ");
        this.manageDecksDiv.append("<span id='openedDeck'><i>New deck</i></span> ");

        var newDeckBut = $("<button>New deck</button>");
        this.manageDecksDiv.append(newDeckBut);

        var saveDeckBut = $("<button>Save deck</button>");
        this.manageDecksDiv.append(saveDeckBut);

        var renameDeckBut = $("<button>Rename deck</button>");
        this.manageDecksDiv.append(renameDeckBut);

        this.decksSelect = $("<select></select>");
        this.manageDecksDiv.append(this.decksSelect);

        var loadDeckBut = $("<button>Load deck</button>");
        this.manageDecksDiv.append(loadDeckBut);

        this.deckDiv.append(this.manageDecksDiv);

        newDeckBut.click(
                function() {
                    that.deckName = null;
                    $("#openedDeck").html("<i>New deck</i>");
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
                            $("#openedDeck").html(newDeckName);
                            that.saveDeck(true);
                        }
                    } else {
                        that.saveDeck(false);
                    }
                });

        renameDeckBut.click(
                function() {
                    var newDeckName = prompt("Enter new name for the deck", "");
                    if (newDeckName == null)
                        return;
                    if (newDeckName.length < 3 || newDeckName.length > 40)
                        alert("Deck name has to have at least 3 characters and at most 40 characters.");
                    else {
                        var oldDeckName = that.deckName;
                        that.deckName = newDeckName;
                        $("#openedDeck").html(newDeckName);
                        that.comm.renameDeck(oldDeckName, newDeckName,
                                function() {
                                    that.loadDecks();
                                });
                    }
                });

        loadDeckBut.click(
                function() {
                    var deck = that.decksSelect.val();
                    if (deck != null) {
                        that.comm.getDeck(deck,
                                function(xml) {
                                    that.setupDeck(xml, deck);
                                });
                    }
                });

        this.collectionDiv = $("#collectionDiv");

        this.pageDiv = $("<div></div>");
        this.pageDiv.append("<button id='previousPage' style='float: left;'>Previous page</button>");
        this.pageDiv.append("<button id='nextPage' style='float: right;'>Next page</button>");
        this.pageDiv.append("<div id='countSlider' style='left: 50px; top: 10px; width: 200px;'></div>");
        this.collectionDiv.append(this.pageDiv);

        this.fullFilterDiv = $("<div id='fullFiltering'></div>");
        this.fullFilterDiv.append("<select id='set'>"
                + "<option value=''>All Sets</option>"
                + "<option value='1,2,3'>Fellowship Block</option>"
                + "<option value='1'>01 - The Fellowship of the Ring</option>"
                + "<option value='2'>02 - Mines of Moria</option>"
                + "<option value='3'>03 - Realms of the Elf-lords</option>"
                + "<option value='4,5,6'>Towers Block</option>"
                + "<option value='4'>04 - The Two Towers</option>"
                + "<option value='5'>05 - Battle of Helm's Deep</option>"
                + "<option value='6'>06 - Ents of Fangorn</option>"
                + "<option value='7,8,10'>King Block</option>"
                + "<option value='7'>07 - The Return of the King</option>"
                + "<option value='8'>08 - Siege of Gondor</option>"
                + "<option value='9'>09 - Reflections</option>"
                + "<option value='10'>10 - Mount Doom</option>"
                + "</select>");
        this.fullFilterDiv.append("<input type='text' id='cardName' value='Card name'>");
        this.fullFilterDiv.append("<select id='sort'>"
                + "<option value=''>Sort by:</option>"
                + "<option value='name'>Name</option>"
                + "<option value='twilight'>Twilight</option>"
                + "<option value='strength'>Strength</option>"
                + "<option value='vitality'>Vitality</option>"
                + "</select>");
        this.collectionDiv.append(this.fullFilterDiv);

        this.filterDiv = $("<div id='filtering'></div>");

        this.filterDiv.append("<div id='culture1'>"
                + "<input type='checkbox' id='DWARVEN'/><label for='DWARVEN' id='labelDWARVEN'><img src='images/cultures/dwarven.png'/></label>"
                + "<input type='checkbox' id='ELVEN'/><label for='ELVEN' id='labelELVEN'><img src='images/cultures/elven.png'/></label>"
                + "<input type='checkbox' id='GANDALF'/><label for='GANDALF' id='labelGANDALF'><img src='images/cultures/gandalf.png'/></label>"
                + "<input type='checkbox' id='GONDOR'/><label for='GONDOR' id='labelGONDOR'><img src='images/cultures/gondor.png'/></label>"
                + "<input type='checkbox' id='ROHAN'/><label for='ROHAN' id='labelROHAN'><img src='images/cultures/rohan.png'/></label>"
                + "<input type='checkbox' id='SHIRE'/><label for='SHIRE' id='labelSHIRE'><img src='images/cultures/shire.png'/></label>"
                + "<input type='checkbox' id='GOLLUM'/><label for='GOLLUM' id='labelGOLLUM'><img src='images/cultures/gollum.png'/></label>"
                + "</div>");
        this.filterDiv.append("<div id='culture2'>"
                + "<input type='checkbox' id='DUNLAND'/><label for='DUNLAND' id='labelDUNLAND'><img src='images/cultures/dunland.png'/></label>"
                + "<input type='checkbox' id='ISENGARD'/><label for='ISENGARD' id='labelISENGARD'><img src='images/cultures/isengard.png'/></label>"
                + "<input type='checkbox' id='MORIA'/><label for='MORIA' id='labelMORIA'><img src='images/cultures/moria.png'/></label>"
                + "<input type='checkbox' id='RAIDER'/><label for='RAIDER' id='labelRAIDER'><img src='images/cultures/raider.png'/></label>"
                + "<input type='checkbox' id='SAURON'/><label for='SAURON' id='labelSAURON'><img src='images/cultures/sauron.png'/></label>"
                + "<input type='checkbox' id='WRAITH'/><label for='WRAITH' id='labelWRAITH'><img src='images/cultures/wraith.png'/></label>"
                + "</div>");

        var combos = $("<div></div>");

        combos.append(" <select id='cardType'>"
                + "<option value=''>All Card Types</option>"
                + "<option value='COMPANION'>Companions</option>"
                + "<option value='ALLY'>Allies</option>"
                + "<option value='MINION'>Minions</option>"
                + "<option value='POSSESSION'>Possessions</option>"
                + "<option value='ARTIFACT'>Artifacts</option>"
                + "<option value='EVENT'>Events</option>"
                + "<option value='CONDITION'>Conditions</option>"
                + "<option value='POSSESSION,ARTIFACT'>Items</option>"
                + "<option value='COMPANION,ALLY,MINION'>Characters</option>"
                + "</select>");
        combos.append(" <select id='keyword'>"
                + "<option value=''>No keyword filtering</option>"
                + "<option value='ARCHER'>Archer</option>"
                + "<option value='EASTERLING'>Easterling</option>"
                + "<option value='ENDURING'>Enduring</option>"
                + "<option value='ENGINE'>Engine</option>"
                + "<option value='FIERCE'>Fierce</option>"
                + "<option value='FORTIFICATION'>Fortification</option>"
                + "<option value='KNIGHT'>Knight</option>"
                + "<option value='MACHINE'>Machine</option>"
                + "<option value='PIPEWEED'>Pipeweed</option>"
                + "<option value='RANGER'>Ranger</option>"
                + "<option value='RING_BOUND'>Ring-bound</option>"
                + "<option value='SEARCH'>Search</option>"
                + "<option value='SOUTHRON'>Southron</option>"
                + "<option value='SPELL'>Spell</option>"
                + "<option value='STEALTH'>Stealth</option>"
                + "<option value='TALE'>Tale</option>"
                + "<option value='TENTACLE'>Tentacle</option>"
                + "<option value='TRACKER'>Tracker</option>"
                + "<option value='TWILIGHT'>Twilight</option>"
                + "<option value='UNHASTY'>Unhasty</option>"
                + "<option value='VALIANT'>Valiant</option>"
                + "<option value='VILLAGER'>Villager</option>"
                + "<option value='WARG_RIDER'>Warg-rider</option>"
                + "<option value='WEATHER'>Weather</option>"
                + "</select>");
        this.filterDiv.append(combos);

        this.collectionDiv.append(this.filterDiv);

        $("#culture1").buttonset();
        $("#culture2").buttonset();

        var fullFilterChanged = function() {
            that.start = 0;
            that.getCollection();
            return true;
        };

        $("#set").change(fullFilterChanged);
        $("#cardName").change(fullFilterChanged);
        $("#sort").change(fullFilterChanged);

        var filterOut = function() {
            that.filter = that.calculateNormalFilter();
            that.start = 0;
            that.getCollection();
            return true;
        };

        $("#cardType").change(filterOut);
        $("#keyword").change(filterOut);

        $("#labelDWARVEN,#labelELVEN,#labelGANDALF,#labelGONDOR,#labelROHAN,#labelSHIRE,#labelGOLLUM,#labelDUNLAND,#labelISENGARD,#labelMORIA,#labelRAIDER,#labelSAURON,#labelWRAITH").click(filterOut);

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
        this.bottomBarDiv.css({overflow: "auto"});
        this.bottomBarDiv.append("<div id='deckStats'></div>")
        this.deckDiv.append(this.bottomBarDiv);

        this.deckDiv.append(this.drawDeckDiv);

        this.selectionFunc = this.addCardToDeck;

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

        this.loadDecks();

        this.getCollection();

        this.checkDeckStatsDirty();
    },

    clickCardFunction: function(event) {
        var tar = $(event.target);
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
                            this.selectionFunc(selectedCardElem.data("card").blueprintId, selectedCardElem.data("card").zone);
                            this.layoutUI(false);
                        } else if (selectedCardElem.hasClass("cardInDeck")) {
                            this.removeCardFromDeck(selectedCardElem);
                            this.layoutUI(false);
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

    loadDecks: function() {
        var that = this;
        this.comm.getDecks(function(xml) {
            that.setupDeckList(xml);
        });
    },

    setupDeckList: function(xml) {
        var root = xml.documentElement;
        if (root.tagName == "decks") {
            this.decksSelect.html("");
            var decks = root.getElementsByTagName("deck");
            for (var i = 0; i < decks.length; i++) {
                var deck = decks[i];
                var deckName = decks[i].childNodes[0].nodeValue;
                var deckElem = $("<option></option>");
                deckElem.attr("value", deckName);
                deckElem.html(deckName);
                this.decksSelect.append(deckElem);
            }
        }
    },

    displayCardInfo: function(card) {
        this.infoDialog.html("");
        this.infoDialog.html("<div style='scroll: auto'><img src='" + card.imageUrl + "'></div>");
        var windowWidth = $(window).width();
        var windowHeight = $(window).height();

        var horSpace = 30;
        var vertSpace = 45;

        if (card.horizontal) {
            // 500x360
            this.infoDialog.dialog({width: Math.min(500 + horSpace, windowWidth), height: Math.min(360 + vertSpace, windowHeight)});
        } else {
            // 360x500
            this.infoDialog.dialog({width: Math.min(360 + horSpace, windowWidth), height: Math.min(500 + vertSpace, windowHeight)});
        }
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

    saveDeck: function(reloadList) {
        var that = this;

        var deckContents = this.getDeckContents();
        if (deckContents == null)
            alert("Deck must contain at least Ring-bearer, The One Ring and 9 sites");
        else
            this.comm.saveDeck(this.deckName, deckContents, function(xml) {
                alert("Deck was saved");
                if (reloadList)
                    that.loadDecks();
            });
    },

    addCardToContainer: function(blueprintId, zone, container) {
        var card = new Card(blueprintId, zone, "deck", "player");
        var cardDiv = createCardDiv(card.imageUrl, null, card.isFoil());
        cardDiv.data("card", card);
        container.append(cardDiv);
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
            this.deckDirty = true;
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

    calculateFullFilterPostfix: function() {
        var setNo = $("#set option:selected").prop("value");
        if (setNo != "")
            setNo = " set:" + setNo;

        var sort = $("#sort option:selected").prop("value");
        if (sort != "")
            sort = " sort:" + sort;

        var cardName = $("#cardName").val();
        if (cardName == "Card name")
            cardName = "";
        else {
            var cardNameElems = cardName.split(" ");
            cardName = "";
            for (var i = 0; i < cardNameElems.length; i++)
                cardName += " name:" + cardNameElems[i];
        }

        return setNo + sort + cardName;
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

        var cardType = $("#cardType option:selected").prop("value");
        if (cardType == "")
            cardType = "cardType:-SITE,THE_ONE_RING";
        else
            cardType = "cardType:" + cardType;

        var keyword = $("#keyword option:selected").prop("value");
        if (keyword != "")
            keyword = " keyword:" + keyword;

        if (cultures.length > 0)
            return cardType + " culture:" + cultures + keyword;
        else
            return cardType + keyword;
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
                    }
                });
        if (!added) {
            var div = this.addCardToContainer(blueprintId, side, this.drawDeckDiv)
            div.addClass("cardInDeck");
        }

        this.deckDirty = true;
    },

    checkDeckStatsDirty: function() {
        log("Checking stats");
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

        this.deckDirty = true;
    },

    getCollection: function() {
        var that = this;
        this.comm.getCollection(this.collectionType, this.filter + this.calculateFullFilterPostfix(), this.start, this.count, function(xml) {
            that.displayCollection(xml);
        });
    },

    clearDeck: function() {
        $(".cardInDeck").each(
                function() {
                    var cardData = $(this).data("card");
                    for (var i = 0; i < cardData.attachedCards.length; i++)
                        cardData.attachedCards[i].remove();
                });
        $(".cardInDeck").remove();

        this.layoutUI();

        this.deckDirty = true;
    },

    setupDeck: function(xml, deckName) {
        var root = xml.documentElement;
        if (root.tagName == "deck") {
            var ringBearer = root.getElementsByTagName("ringBearer");
            if (ringBearer.length == 1) {
                this.clearDeck();
                this.deckName = deckName;
                $("#openedDeck").html(deckName);

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
        var manageHeight = 30;

        var padding = 5;
        var collectionWidth = this.collectionDiv.width();
        var collectionHeight = this.collectionDiv.height();

        var deckWidth = this.deckDiv.width();
        var deckHeight = this.deckDiv.height() - (manageHeight + padding);

        var rowHeight = Math.floor((deckHeight - 6 * padding) / 5);
        var sitesWidth = Math.floor(1.5 * deckHeight / 5);
        sitesWidth = Math.min(sitesWidth, 150);

        this.manageDecksDiv.css({position: "absolute", left: padding, top: padding, width: deckWidth, height: manageHeight});

        this.ringBearerDiv.css({ position: "absolute", left: padding, top: manageHeight + 2 * padding, width: Math.floor((sitesWidth - padding) / 2), height: rowHeight });
        this.ringBearerGroup.setBounds(0, 0, Math.floor((sitesWidth - padding) / 2), rowHeight);
        this.ringDiv.css({ position: "absolute", left: Math.floor((sitesWidth + 3 * padding) / 2), top: manageHeight + 2 * padding, width: Math.floor((sitesWidth - padding) / 2), height: rowHeight });
        this.ringGroup.setBounds(0, 0, Math.floor((sitesWidth - padding) / 2), rowHeight);
        for (var i = 0; i < 4; i++) {
            this.siteDivs[i].css({ position: "absolute", left: padding, top: manageHeight + 2 * padding + (rowHeight + padding) * (i + 1), width: sitesWidth, height: rowHeight });
            this.siteGroups[i].setBounds(0, 0, sitesWidth, rowHeight);
        }
        for (var i = 4; i < 9; i++) {
            this.siteDivs[i].css({ position: "absolute", left: padding * 2 + sitesWidth, top: manageHeight + 2 * padding + (rowHeight + padding) * (i - 4), width: sitesWidth, height: rowHeight });
            this.siteGroups[i].setBounds(0, 0, sitesWidth, rowHeight);
        }
        this.drawDeckDiv.css({ position: "absolute", left: padding * 3 + sitesWidth * 2, top: manageHeight + 2 * padding, width: deckWidth - (sitesWidth + padding) * 2 - padding, height: deckHeight - 2 * padding - 50 });
        this.fpDeckGroup.setBounds(0, 0, deckWidth - (sitesWidth + padding) * 2 - padding, (deckHeight - 2 * padding - 50) / 2);
        this.shadowDeckGroup.setBounds(0, (deckHeight - 2 * padding - 50) / 2, deckWidth - (sitesWidth + padding) * 2 - padding, (deckHeight - 2 * padding - 50) / 2);

        this.bottomBarDiv.css({ position: "absolute", left: padding * 3 + sitesWidth * 2, top: manageHeight + padding + deckHeight - 50, width: deckWidth - (sitesWidth + padding) * 2 - padding, height: 50 });

        this.pageDiv.css({ position: "absolute", left: padding, top: 0, width: collectionWidth - padding, height: 50 });
        $("#countSlider").css({width: collectionWidth - padding - 100});
        this.fullFilterDiv.css({position:"absolute", left: padding, top: 50, width: collectionWidth - padding, height: 30});
        this.filterDiv.css({ position: "absolute", left: padding, top: 80, width: collectionWidth - padding, height: 80 });
        this.normalCollectionDiv.css({ position: "absolute", left: padding, top: 160, width: collectionWidth - padding * 2, height: collectionHeight - 160 });
        this.specialCollectionDiv.css({ position: "absolute", left: padding, top: 80, width: collectionWidth - padding * 2, height: collectionHeight - 80 });

        this.normalCollectionGroup.setBounds(0, 0, collectionWidth - padding * 2, collectionHeight - 160);
        this.specialCollectionGroup.setBounds(0, 0, collectionWidth - padding * 2, collectionHeight - 80);
    },

    processError: function (xhr, ajaxOptions, thrownError) {
        if (thrownError != "abort")
            alert("There was a problem during communication with server");
    }
});