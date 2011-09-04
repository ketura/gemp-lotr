var GempLotrDeckBuildingUI = Class.extend({
    deckDiv: null,
    collectionDiv: null,
    collectionGroup: null,
    loadCollectionFunc: null,

    init: function() {
        this.deckDiv = $("#deckDiv");
        this.collectionDiv = $("#collectionDiv");

        this.collectionGroup = new NormalCardGroup(null, function(card) {
            return (card.zone == "collection");
        });


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
            var cards = root.getElementsByTagName("card");
            for (var i = 0; i < cards.length; i++) {
                var cardElem = cards[i];
                var blueprintId = cardElem.getAttribute("blueprintId");
                var count = cardElem.getAttribute("count");
                var card = new Card(blueprintId, "collection", "collection" + i, "player");
                var cardDiv = createCardDiv(card.imageUrl, null);
                cardDiv.data("card", card);
                this.collectionDiv.append(cardDiv);
            }

            this.collectionGroup.layoutCards();
        }
    },

    layoutUI: function() {

        this.collectionGroup.setBounds(0, 0, this.collectionDiv.width(), this.collectionDiv.height());
    },

    processError: function (xhr, ajaxOptions, thrownError) {
        alert("There was a problem during communication with server");
    }
});