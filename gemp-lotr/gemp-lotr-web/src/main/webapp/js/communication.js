var GempLotrCommunication = Class.extend({
    url: null,
    failure: null,

    init: function(url, failure) {
        this.url = url;
        this.failure = failure;
    },

    startGameSession: function(callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/game/" + getUrlParam("gameId"),
            cache: false,
            data: { participantId: getUrlParam("participantId") },
            success: callback,
            error: this.failure,
            dataType: "xml"
        });
    },
    updateGameState: function(callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/game/" + getUrlParam("gameId"),
            cache: false,
            data: { participantId: getUrlParam("participantId") },
            success: callback,
            error: this.failure,
            dataType: "xml"
        });
    },
    getGameCardModifiers: function(cardId, callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/game/" + getUrlParam("gameId") + "/cardInfo",
            cache: false,
            data: { cardId: cardId,
                participantId: getUrlParam("participantId") },
            success: callback,
            error: this.failure,
            dataType: "html"
        });
    },
    gameDecisionMade: function(decisionId, response, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/game/" + getUrlParam("gameId"),
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                decisionId: decisionId,
                decisionValue: response},
            success: callback,
            error: this.failure,
            dataType: "xml"
        });
    },
    getDeck: function(deckType, callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/deck/" + deckType,
            cache: false,
            data: {
                participantId: getUrlParam("participantId")},
            success: callback,
            error: this.failure,
            dataType: "xml"
        });
    },
    getCollection: function(collectionType, filter, start, count, callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/collection/" + collectionType,
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                filter: filter,
                start: start,
                count: count},
            success: callback,
            error: this.failure,
            dataType: "xml"
        });
    }
});
