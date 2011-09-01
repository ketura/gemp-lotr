var GempLotrCommunication = Class.extend({
    url: null,
    success: null,
    failure: null,

    init: function(url, success, failure) {
        this.url = url;
        this.success = success;
        this.failure = failure;
    },

    startSession: function() {
        $.ajax({
            url: this.url,
            cache: false,
            data: { participantId: getUrlParam("participantId") },
            success: this.success,
            error: this.failure,
            dataType: "xml"
        });
    },
    updateStateFunction: function() {
        $.ajax({
            type: "POST",
            url: this.url,
            cache: false,
            data: { participantId: getUrlParam("participantId") },
            success: this.success,
            error: this.failure,
            dataType: "xml"
        });
    },
    getCardModifiers: function(cardId, callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/cardInfo",
            cache: false,
            data: { cardId: cardId,
                participantId: getUrlParam("participantId") },
            success: callback,
            error: this.failure,
            dataType: "html"
        });
    },
    decisionMade: function(decisionId, response) {
        $.ajax({
            type: "POST",
            url: this.url,
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                decisionId: decisionId,
                decisionValue: response},
            success: this.success,
            error: this.failure,
            dataType: "xml"
        });
    }
});
