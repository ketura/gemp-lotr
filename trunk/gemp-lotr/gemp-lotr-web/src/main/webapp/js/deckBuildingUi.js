var GempLotrDeckBuildingUI = Class.extend({
    deckDiv: null,
    collectionDiv: null,
    loadCollectionFunc: null,

    init: function() {
        this.deckDiv = $("<div></div>");
        this.collectionDiv = $("<div></div>");

        $("#main").append(this.deckDiv);
        $("#main").append(this.collectionDiv);
    },

    setLoadCollectionFunc: function(func) {
        this.loadCollectionFunc = func;
    },

    setupDeck: function(xml) {
        this.loadCollectionFunc("default", "cardType:-SITE,THE_ONE_RING", 0, 18);
    },

    displayCollection: function(xml) {
        alert(xml.text);
    },

    layoutUI: function() {
        var width = $(window).width();
        var height = $(window).height();

        var deckHeight = Math.floor(height * 0.3);

        this.deckDiv.css({left:0 + "px", top:0 + "px", width: width, height: deckHeight, position: "absolute"});
        this.collectionDiv.css({left:0 + "px", top:deckHeight + "px", width: width, height: height - deckHeight, position: "absolute"});
    },

    processError: function (xhr, ajaxOptions, thrownError) {
        alert("There was a problem during communication with server");
    }
});