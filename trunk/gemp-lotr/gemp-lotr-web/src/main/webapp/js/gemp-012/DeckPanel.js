var DeckPanel = Class.extend({
    tabs:null,

    ringBearerDiv:null,
    ringDiv: null,
    sitesDiv:null,
    fpDiv:null,
    shadowDiv:null,

    init:function (deckDiv) {
        var tabGroup = $("<ul></ul>");
        tabGroup.append("<li><a href='#ringBearer'>Ring-bearer (0)</a></li>");
        tabGroup.append("<li><a href='#ring'>Ring (0)</a></li>");
        tabGroup.append("<li><a href='#sites'>Sites (0)</a></li>");
        tabGroup.append("<li><a href='#fp'>Free People (0)</a></li>");
        tabGroup.append("<li><a href='#shadow'>Shadow (0)</a></li>");
        deckDiv.append(tabGroup);

        this.ringBearerDiv = $("<div id='ringBearer'></div>");
        deckDiv.append(this.ringBearerDiv);
        this.ringDiv = $("<div id='ring'></div>");
        deckDiv.append(this.ringDiv);
        this.sitesDiv = $("<div id='sites'></div>");
        deckDiv.append(this.sitesDiv);
        this.fpDiv = $("<div id='fp'></div>");
        deckDiv.append(this.fpDiv);
        this.shadowDiv = $("<div id='shadow'></div>");
        deckDiv.append(this.shadowDiv);

        this.tabs = $(deckDiv).tabs();
    },

    clearDeck: function() {

    },

    layoutUi: function(x, y, width, height) {
        this.tabs.css({"left": x, "top": y, "width": width, "height": height});
    }
});