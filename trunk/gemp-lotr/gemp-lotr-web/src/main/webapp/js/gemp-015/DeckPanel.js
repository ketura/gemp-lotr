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

    cardLayoutFunc: null,
    cardWidthToHeightFunc: null,

    init:function (deckDiv, cardLayoutFunc, cardWidthToHeightFunc) {
        this.cardLayoutFunc = cardLayoutFunc;
        this.cardWidthToHeightFunc = cardWidthToHeightFunc;

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

        this.ringBearerContainer = new CardContainer(this.ringBearerDiv, this.layoutCardContainer, this.cardLayoutFunc, this.cardWidthToHeightFunc);
        var ringBearerGroup = new RowCardLayoutCardGroup(this.ringBearerDiv,
                function() {
                    return true;
                });
        ringBearerGroup.setZIndexBase(100);
        ringBearerGroup.setMaxCardHeight(300);
        this.ringBearerContainer.addCardGroup("main", ringBearerGroup);

        this.ringContainer = new CardContainer(this.ringDiv, this.layoutCardContainer, this.cardLayoutFunc, this.cardWidthToHeightFunc);
        var ringGroup = new RowCardLayoutCardGroup(this.ringDiv,
                function() {
                    return true;
                });
        ringGroup.setZIndexBase(100);
        ringGroup.setMaxCardHeight(300);
        this.ringContainer.addCardGroup("main", ringGroup);

        this.sitesContainer = new CardContainer(this.sitesDiv, this.layoutCardContainer, this.cardLayoutFunc, this.cardWidthToHeightFunc);
        var sitesGroup = new RowCardLayoutCardGroup(this.sitesDiv,
                function() {
                    return true;
                });
        sitesGroup.setZIndexBase(100);
        sitesGroup.setMaxCardHeight(300);
        this.sitesContainer.addCardGroup("main", sitesGroup);

        this.fpContainer = new CardContainer(this.fpDiv, this.layoutCardContainer, this.cardLayoutFunc, this.cardWidthToHeightFunc);
        var fpGroup = new RowCardLayoutCardGroup(this.fpDiv,
                function(cardDiv, cardId, props) {
                    return true;
                });
        fpGroup.setZIndexBase(100);
        fpGroup.setMaxCardHeight(300);
        this.fpContainer.addCardGroup("main", fpGroup);

        this.shadowContainer = new CardContainer(this.shadowDiv, this.layoutCardContainer, this.cardLayoutFunc, this.cardWidthToHeightFunc);
        var shadowGroup = new RowCardLayoutCardGroup(this.shadowDiv,
                function(cardDiv, cardId, props) {
                    return true;
                });
        shadowGroup.setZIndexBase(100);
        shadowGroup.setMaxCardHeight(300);
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
        var border = this.tabs.border();

        var innerWidth = width - border.left - border.right - 3;
        var innerHeight = height - border.top - border.bottom - 3;

        this.tabs.css({"left": x, "top": y, "width": innerWidth, "height": innerHeight});

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
                        count+=props["countInDeck"];
                };

        this.ringBearerContainer.iterCards(incrementCount);
        this.ringContainer.iterCards(incrementCount);
        this.sitesContainer.iterCards(incrementCount);
        this.fpContainer.iterCards(incrementCount);
        this.shadowContainer.iterCards(incrementCount);

        return count;
    },

    getCardCountInGroup: function(groupContainer) {
        var count = 0;
        groupContainer.iterCards(
                function(cardDiv, cardId, props) {
                    count+=props["countInDeck"];
                });
        return count;
    },

    updateDeckCounts: function() {
        var that = this;

        var updateGroup = function (groupContainer, titlePrefix, tabHref) {
            groupContainer.layoutCards();
            var count = that.getCardCountInGroup(groupContainer);
            var title = titlePrefix + " (" + count + ")";
            $(".ui-tabs-nav a[href='#" + tabHref + "']", that.tabs).html(title);
        };

        updateGroup(this.ringContainer, "Ring", "ring");
        updateGroup(this.ringBearerContainer, "Ring-bearer", "ringBearer");
        updateGroup(this.sitesContainer, "Sites", "sites");
        updateGroup(this.fpContainer, "Free People", "fp");
        updateGroup(this.shadowContainer, "Shadow", "shadow");
    },

    findCardPropsInGroup: function(groupContainer, blueprintId) {
        var cardInGroupProps = null;
        groupContainer.iterCards(
                function(cardDiv, cardId, props) {
                    if (props["blueprintId"] == blueprintId)
                        cardInGroupProps = props;
                });
        return cardInGroupProps;
    },

    addCardToGroup: function(groupContainer, cardElem, cardId, cardProps) {
        var cardInGroupProps = this.findCardPropsInGroup(groupContainer, cardProps["blueprintId"]);;
        if (cardInGroupProps == null) {
            cardProps["countInDeck"] = 1;
            groupContainer.addCard(cardElem, cardId, cardProps);
        } else {
            cardInGroupProps["countInDeck"] = cardInGroupProps["countInDeck"] + 1;
        }
    },

    addCard: function(cardElem, cardId, cardProps) {
        var group = cardProps["group"];

        if (group == "ring") {
            this.addCardToGroup(this.ringContainer);
        } else if (group == "ringBearer" || group == "fp") {
            this.addCardToGroup(this.fpContainer);
        } else if (group == "site") {
            this.addCardToGroup(this.sitesContainer);
        } else if (group == "shadow") {
            this.addCardToGroup(this.shadowContainer);
        }
    },

    addRingBearerCard: function(cardElem, cardId, cardProps) {
        this.addCardToGroup(this.ringBearerContainer, cardElem, cardId, cardProps);
    },

    removeCardAndUpdate: function(cardElem, cardId, cardProps) {
        var that = this;
        var removeFromContainerIfThere = function(groupContainer, titlePrefix, tabHref) {
            if (groupContainer.hasCardId(cardId)) {
                var cardPropsInGroup = that.findCardPropsInGroup(groupContainer, cardProps["blueprintId"]);
                if (cardPropsInGroup["countInDeck"]==1)
                    groupContainer.removeCard(cardId);
                else
                    cardPropsInGroup["countInDeck"]=cardPropsInGroup["countInDeck"]-1;
                groupContainer.layoutCards();

                var count = that.getCardCountInGroup(groupContainer);
                var title = titlePrefix + " (" + count + ")";
                $(".ui-tabs-nav a[href='#" + tabHref + "']", that.tabs).html(title);
            }
        };

        removeFromContainerIfThere(this.ringBearerContainer, "Ring-bearer", "ringBearer");
        removeFromContainerIfThere(this.ringContainer, "Ring", "ring");
        removeFromContainerIfThere(this.sitesContainer, "Sites", "sites");
        removeFromContainerIfThere(this.fpContainer, "Free People", "fp");
        removeFromContainerIfThere(this.shadowContainer, "Shadow", "shadow");
    },

    finishAddingCards: function() {
        this.updateDeckCounts();
    },

    addCardToGroupAndUpdate: function(groupContainer, titlePrefix, tabHref, cardElem, cardId, cardProps) {
        this.addCardToGroup(groupContainer, cardElem, cardId, cardProps);
        groupContainer.layoutCards();
        var count = this.getCardCountInGroup(groupContainer);
        var title = titlePrefix + " (" + count + ")";
        $(".ui-tabs-nav a[href='#" + tabHref + "']", this.tabs).html(title);
    },

    addCardAndUpdate: function(cardElem, cardId, cardProps) {
        var group = cardProps["group"];

        var that = this;

        if (group == "ring") {
            this.addCardToGroupAndUpdate(this.ringContainer, "Ring", "ring", cardElem, cardId, cardProps);
        } else if (group == "ringBearer") {
            var selectedTabIndex = this.tabs.tabs('option', 'selected');

            if (selectedTabIndex == 0) {
                this.addCardToGroupAndUpdate(this.ringBearerContainer, "Ring-bearer", "ringBearer", cardElem, cardId, cardProps);
            } else {
                this.addCardToGroupAndUpdate(this.fpContainer, "Free People", "fp", cardElem, cardId, cardProps);
            }
        } else if (group == "site") {
            this.addCardToGroupAndUpdate(this.sitesContainer, "Sites", "sites", cardElem, cardId, cardProps);
        } else if (group == "fp") {
            this.addCardToGroupAndUpdate(this.fpContainer, "Free People", "fp", cardElem, cardId, cardProps);
        } else if (group == "shadow") {
            this.addCardToGroupAndUpdate(this.shadowContainer, "Shadow", "shadow", cardElem, cardId, cardProps);
        }
    }
});