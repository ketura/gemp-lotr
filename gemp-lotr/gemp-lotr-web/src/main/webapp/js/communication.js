var GempLotrCommunication = Class.extend({
    url: null,
    success: null,
    failure: null,

    init: function(url, success, failure) {
        this.url = url;
        this.success = success;
        this.failure = failure;
    },

    startGameSession: function() {
        $.ajax({
            type: "GET",
            url: this.url + "/game/" + getUrlParam("gameId"),
            cache: false,
            data: { participantId: getUrlParam("participantId") },
            success: this.success,
            error: this.failure,
            dataType: "xml"
        });
    },
    updateGameState: function() {
        $.ajax({
            type: "POST",
            url: this.url + "/game/" + getUrlParam("gameId"),
            cache: false,
            data: { participantId: getUrlParam("participantId") },
            success: this.success,
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
    gameDecisionMade: function(decisionId, response) {
        $.ajax({
            type: "POST",
            url: this.url + "/game/" + getUrlParam("gameId"),
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                decisionId: decisionId,
                decisionValue: response},
            success: this.success,
            error: this.failure,
            dataType: "xml"
        });
    },
    getDeck: function(callback) {
        $.ajax({
            type: "GET",
            url: this
        });
    }
});
