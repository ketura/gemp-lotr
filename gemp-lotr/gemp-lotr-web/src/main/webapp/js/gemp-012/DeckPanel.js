// Each card should have properties:
// group: ring, ringBearer, site, fp, shadow
// blueprintId: blueprintId of the card
var DeckPanel = Class.extend({
    tabs:null,

    ringBearerDiv:null,
    ringDiv: null,
    sitesDiv:null,
    fpDiv:null,
    shadowDiv:null,

    ringBearerContainer:null,
    ringContainer:null,
    sitesContainer:null,
    fpContainer:null,
    shadowContainer:null,

    init:function (deckDiv) {
        var tabGroup = $("<ul></ul>");
        tabGroup.append("<li><a href='#ringBearer'>Ring-bearer (0)</a></li>");
        tabGroup.append("<li><a href='#ring'>Ring (0)</a></li>");
        tabGroup.append("<li><a href='#sites'>Sites (0)</a></li>");
        tabGroup.append("<li><a href='#fp'>Free People (0)</a></li>");
        tabGroup.append("<li><a href='#shadow'>Shadow (0)</a></li>");
        deckDiv.append(tabGroup);

        this.ringBearerDiv = $("<div id='ringBearer' class='deckPart'></div>");
        deckDiv.append(this.ringBearerDiv);
        this.ringDiv = $("<div id='ring' class='deckPart'></div>");
        deckDiv.append(this.ringDiv);
        this.sitesDiv = $("<div id='sites' class='deckPart'></div>");
        deckDiv.append(this.sitesDiv);
        this.fpDiv = $("<div id='fp' class='deckPart'></div>");
        deckDiv.append(this.fpDiv);
        this.shadowDiv = $("<div id='shadow' class='deckPart'></div>");
        deckDiv.append(this.shadowDiv);

        this.tabs = $(deckDiv).tabs();

        var isFirstWithSameBlueprintIdInGroup =
                function(testedCardId, cardGroup, blueprintId) {
                    var first;
                    var finished = false;

                    cardGroup.iterCards(
                            function(cardDiv, cardId, props, layoutFunc) {
                                if (!finished && props["blueprintId"] == blueprintId) {
                                    first = (cardId == testedCardId);
                                    finished = true;
                                }
                            });
                    
                    return first;
                };

        this.ringBearerContainer = new CardContainer(this.ringBearerDiv, this.layoutCardContainer);
        var ringBearerGroup = new RowCardLayoutCardGroup(this.ringBearerDiv,
                function() {
                    return true;
                });
        ringBearerGroup.setZIndexBase(100);
        this.ringBearerContainer.addCardGroup("main", ringBearerGroup);

        this.ringContainer = new CardContainer(this.ringDiv, this.layoutCardContainer);
        var ringGroup = new RowCardLayoutCardGroup(this.ringDiv,
                function() {
                    return true;
                });
        ringGroup.setZIndexBase(100);
        this.ringContainer.addCardGroup("main", ringGroup);

        this.sitesContainer = new CardContainer(this.sitesDiv, this.layoutCardContainer);
        var sitesGroup = new RowCardLayoutCardGroup(this.sitesDiv,
                function() {
                    return true;
                });
        sitesGroup.setZIndexBase(100);
        this.sitesContainer.addCardGroup("main", sitesGroup);

        this.fpContainer = new CardContainer(this.fpDiv, this.layoutCardContainer);
        var fpGroup = new AttachedCardsLayoutCardGroup(this.fpDiv,
                function(cardDiv, cardId, props) {
                    return true;
                },
                function(cardDiv, cardId, props) {
                    return isFirstWithSameBlueprintIdInGroup(cardId, fpGroup, props["blueprintId"]);
                });
        fpGroup.addAttachedGroup(-1/7, 0,
                function(cardDiv, cardId, props, cardDivAtt, cardIdAtt, propsAtt) {
                    return propsAtt["blueprintId"] == props["blueprintId"];
                });
        fpGroup.setZIndexBase(100);
        this.fpContainer.addCardGroup("main", fpGroup);

        this.shadowContainer = new CardContainer(this.shadowDiv, this.layoutCardContainer);
        var shadowGroup = new AttachedCardsLayoutCardGroup(this.shadowDiv,
                function(cardDiv, cardId, props) {
                    return true;
                },
                function(cardDiv, cardId, props) {
                    return isFirstWithSameBlueprintIdInGroup(cardId, shadowGroup, props["blueprintId"]);
                });
        shadowGroup.addAttachedGroup(-1/7, 0,
                function(cardDiv, cardId, props, cardDivAtt, cardIdAtt, propsAtt) {
                    return propsAtt["blueprintId"] == props["blueprintId"];
                });
        shadowGroup.setZIndexBase(100);
        this.shadowContainer.addCardGroup("main", shadowGroup);
    },

    layoutCardContainer : function(cardGroups, left, top, width, height) {
        var padding = 3;
        cardGroups["main"].setLayout(left + padding, top + padding, width - 2 * padding, height - 2 * padding);
    },

    clearDeck: function() {
        this.ringBearerContainer.removeCards();
        this.ringContainer.removeCards();
        this.sitesContainer.removeCards();
        this.fpContainer.removeCards();
        this.shadowContainer.removeCards();

        this.updateDeckCounts();
    },

    layoutUi: function(x, y, width, height) {
        this.tabs.css({"left": x, "top": y, "width": width, "height": height});

        var border = this.tabs.border();

        var innerWidth = width - border.left - border.right;
        var innerHeight = height - border.top - border.bottom;

        var resultHeight = innerHeight - $("ul", this.tabs).height();
        resultHeight = resultHeight - 3;

        this.ringBearerContainer.setLayout(0, 0, innerWidth, resultHeight);
        this.ringContainer.setLayout(0, 0, innerWidth, resultHeight);
        this.sitesContainer.setLayout(0, 0, innerWidth, resultHeight);
        this.fpContainer.setLayout(0, 0, innerWidth, resultHeight);
        this.shadowContainer.setLayout(0, 0, innerWidth, resultHeight);
    },

    getCardCount: function(blueprintId) {
        var count = 0;

        var incrementCount =
                function(cardDiv, cardId, props, layout) {
                    if (props["blueprintId"] == blueprintId)
                        count++;
                };

        this.ringBearerContainer.iterCards(incrementCount);
        this.ringContainer.iterCards(incrementCount);
        this.sitesContainer.iterCards(incrementCount);
        this.fpContainer.iterCards(incrementCount);
        this.shadowContainer.iterCards(incrementCount);

        return count;
    },

    updateDeckCounts: function() {
        var that = this;

        var updateGroup = function (groupContainer, titlePrefix, tabHref) {
            groupContainer.layoutCards();
            var count = groupContainer.getCardsCount();
            var title = titlePrefix + " (" + count + ")";
            $(".ui-tabs-nav a[href='#" + tabHref + "']", that.tabs).html(title);
        };

        updateGroup(this.ringContainer, "Ring", "ring");
        updateGroup(this.ringBearerContainer, "Ring-bearer", "ringBearer");
        updateGroup(this.sitesContainer, "Sites", "sites");
        updateGroup(this.fpContainer, "Free People", "fp");
        updateGroup(this.shadowContainer, "Shadow", "shadow");
    },

    addCard: function(cardElem, cardId, cardProps, layoutCardFunc, widthToHeightScaleFunc) {
        var group = cardProps["group"];

        var addCardToGroup = function (groupContainer) {
            groupContainer.addCard(cardElem, cardId, cardProps, layoutCardFunc, widthToHeightScaleFunc);
        };

        if (group == "ring") {
            addCardToGroup(this.ringContainer);
        } else if (group == "ringBearer" || group == "fp") {
            addCardToGroup(this.fpContainer);
        } else if (group == "site") {
            addCardToGroup(this.sitesContainer);
        } else if (group == "shadow") {
            addCardToGroup(this.shadowContainer);
        }
    },

    addRingBearerCard: function(cardElem, cardId, cardProps, layoutCardFunc, widthToHeightScaleFunc) {
        this.ringBearerContainer.addCard(cardElem, cardId, cardProps, layoutCardFunc, widthToHeightScaleFunc);
    },

    finishAddingCards: function() {
        this.updateDeckCounts();
    },

    addSingleCard: function(cardElem, cardId, cardProps, layoutCardFunc, widthToHeightScaleFunc) {
        var group = cardProps["group"];

        var that = this;

        var addCardToGroup = function (groupContainer, titlePrefix, tabHref) {
            groupContainer.addCard(cardElem, cardId, cardProps, layoutCardFunc, widthToHeightScaleFunc);
            groupContainer.layoutCards();
            var count = groupContainer.getCardsCount();
            var title = titlePrefix + " (" + count + ")";
            $(".ui-tabs-nav a[href='#" + tabHref + "']", that.tabs).html(title);
        };

        if (group == "ring") {
            addCardToGroup(this.ringContainer, "Ring", "ring");
        } else if (group == "ringBearer") {
            var selectedTabIndex = this.tabs.tabs('option', 'selected');

            if (selectedTabIndex == 0) {
                addCardToGroup(this.ringBearerContainer, "Ring-bearer", "ringBearer");
            } else {
                addCardToGroup(this.fpContainer, "Free People", "fp");
            }
        } else if (group == "site") {
            addCardToGroup(this.sitesContainer, "Sites", "sites");
        } else if (group == "fp") {
            addCardToGroup(this.fpContainer, "Free People", "fp");
        } else if (group == "shadow") {
            addCardToGroup(this.shadowContainer, "Shadow", "shadow");
        }
    }
});