var GempLotrCommunication = Class.extend({
    url: null,
    failure: null,

    init: function(url, failure) {
        this.url = url;
        this.failure = failure;
    },

    startGameSession: function(callback) {
        $.ajaxq("gameState", {
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
        $.ajaxq("gameState", {
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
        $.ajaxq("gameCardModifiers");
        $.ajaxq("gameCardModifiers", {
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
        $.ajaxq("gameState", {
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
    concede: function() {
        $.ajaxq("gameState", {
            type: "POST",
            url: this.url + "/game/" + getUrlParam("gameId") + "/concede",
            cache: false,
            data: {
                participantId: getUrlParam("participantId")},
            error: this.failure,
            dataType: "xml"
        });
    },
    getDeck: function(deckType, callback) {
        $.ajaxq("deck", {
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
        $.ajaxq("collection", {
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
    },
    saveDeck: function(deckType, contents, callback) {
        $.ajaxq("deck", {
            type: "POST",
            url: this.url + "/deck/" + deckType,
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                deckContents: contents},
            success: callback,
            error: this.failure,
            dataType: "xml"
        });
    },
    getDeckStats: function(contents, callback) {
        $.ajaxq("deckStats");
        $.ajaxq("deckStats", {
            type: "POST",
            url: this.url + "/deck",
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                deckContents: contents},
            success: callback,
            error: this.failure,
            dataType: "html"
        });
    },
    startChat: function(room, callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/chat/" + room,
            cache: false,
            data: {
                participantId: getUrlParam("participantId")},
            success: callback,
            error: this.failure,
            dataType: "xml"
        });
    },
    updateChat: function(room, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/chat/" + room,
            cache: false,
            data: {
                participantId: getUrlParam("participantId")},
            success: callback,
            error: this.failure,
            dataType: "xml"
        });
    },
    sendChatMessage: function(room, message, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/chat/" + room,
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                message: message},
            success: callback,
            error: this.failure,
            dataType: "xml"
        });
    },
    getHall: function(callback) {
        $.ajaxq("hall", {
            type: "GET",
            url: this.url + "/hall",
            cache: false,
            data: {
                participantId: getUrlParam("participantId")},
            success: callback,
            error: this.failure,
            dataType: "xml"
        });
    },
    joinTable: function(tableId, callback) {
        $.ajaxq("hall", {
            type: "POST",
            url: this.url + "/hall/" + tableId,
            cache: false,
            data: {
                participantId: getUrlParam("participantId")},
            success: callback,
            error: this.failure,
            dataType: "xml"
        });
    },
    createTable: function(format, callback) {
        $.ajaxq("hall", {
            type: "POST",
            url: this.url + "/hall",
            cache: false,
            data: {
                format: format,
                participantId: getUrlParam("participantId")},
            success: callback,
            error: this.failure,
            dataType: "xml"
        });
    },
    leaveTable: function() {
        $.ajaxq("hall", {
            type: "POST",
            url: this.url + "/hall/leave",
            cache: false,
            data: {
                participantId: getUrlParam("participantId")},
            error: this.failure,
            dataType: "xml"
        });
    },
    getStatus: function(callback) {
        $.ajaxq("status", {
            type: "GET",
            url: this.url + "/",
            cache: false,
            data: {
                participantId: getUrlParam("participantId")},
            success: callback,
            error: this.failure,
            dataType: "html"
        });
    },
    login: function(login, password, callback) {
        $.ajaxq("login", {
            type: "POST",
            url: this.url + "/login",
            cache: false,
            data: {
                login: login,
                password: password,
                participantId: getUrlParam("participantId")},
            success: callback,
            error: this.failure,
            dataType: "html"
        });
    },
    register: function(login, password, callback) {
        $.ajaxq("register", {
            type: "POST",
            url: this.url + "/register",
            cache: false,
            data: {
                login: login,
                password: password,
                participantId: getUrlParam("participantId")},
            success: callback,
            error: this.failure,
            dataType: "html"
        });
    },
    getRegistrationForm: function(callback) {
        $.ajax({
            type: "POST",
            url: "/gemp-lotr/includes/registrationForm.html",
            cache: false,
            data: {
                participantId: getUrlParam("participantId")},
            success: callback,
            error: this.failure,
            dataType: "html"
        });
    }
});
