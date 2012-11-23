var DeckPanel = Class.extend({
    deckDiv:null,

    init:function (deckDiv) {
        this.deckDiv = deckDiv;
        $(this.deckDiv).tabs();
        
    },

    clearDeck: function() {

    },

    layoutUi: function(x, y, width, height) {

    }
});