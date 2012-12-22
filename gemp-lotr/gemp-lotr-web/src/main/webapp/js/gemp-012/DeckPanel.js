// Each card should have properties:
// group: ring, ringBearer, site, fp, shadow
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

        this.ringBearerContainer = new CardContainer(this.ringBearerDiv, this.layoutCardContainer);
        var ringBearerGroup = new RowCardLayoutCardGroup(this.ringBearerDiv,
                function() {
                    return true;
                });
        ringBearerGroup.setZIndexBase(10);
        this.ringBearerContainer.addCardGroup("main", ringBearerGroup);

        this.ringContainer = new CardContainer(this.ringDiv, this.layoutCardContainer);
        var ringGroup = new RowCardLayoutCardGroup(this.ringDiv,
                function() {
                    return true;
                });
        ringGroup.setZIndexBase(10);
        this.ringContainer.addCardGroup("main", ringGroup);

        this.sitesContainer = new CardContainer(this.sitesDiv, this.layoutCardContainer);
        var sitesGroup = new RowCardLayoutCardGroup(this.sitesDiv,
                function() {
                    return true;
                });
        sitesGroup.setZIndexBase(10);
        this.sitesContainer.addCardGroup("main", sitesGroup);

        this.fpContainer = new CardContainer(this.fpDiv, this.layoutCardContainer);
        var fpGroup = new RowCardLayoutCardGroup(this.fpDiv,
                function() {
                    return true;
                });
        fpGroup.setZIndexBase(10);
        this.fpContainer.addCardGroup("main", fpGroup);

        this.shadowContainer = new CardContainer(this.shadowDiv, this.layoutCardContainer);
        var shadowGroup = new RowCardLayoutCardGroup(this.shadowDiv,
                function() {
                    return true;
                });
        shadowGroup.setZIndexBase(10);
        this.shadowContainer.addCardGroup("main", shadowGroup);
    },

    layoutCardContainer : function(cardGroups, left, top, width, height) {
        var padding = 3;
        cardGroups["main"].setLayout(left + padding, top + padding, width - 2 * padding, height - 2 * padding);
    },

    clearDeck: function() {

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

    addCard: function(cardElem, cardId, cardProps, layoutCardFunc, widthToHeightScaleFunc) {
        var selectedTabIndex = this.tabs.tabs('option', 'selected');

        var group = cardProps["group"];

        var that = this;

        var addCardToGroup = function (groupContainer, titlePrefix, tabHref) {
            groupContainer.addCard(cardElem, cardId, cardProps, layoutCardFunc, widthToHeightScaleFunc);
            groupContainer.layoutCards();
            var count = groupContainer.getCardsCount();
            var title = titlePrefix+" ("+count+")";
            $(".ui-tabs-nav a[href='#"+tabHref+"']", that.tabs).html(title);
        };

        if (group == "ring") {
            addCardToGroup(this.ringContainer, "Ring", "ring");
        } else if (group == "ringBearer") {
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