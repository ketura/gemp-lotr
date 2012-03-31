var GempLotrCommunication = Class.extend({
    url: null,
    failure: null,

    init: function(url, failure) {
        this.url = url;
        this.failure = failure;
    },

    getDelivery: function(callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/delivery",
            cache: false,
            data: {
                participantId: getUrlParam("participantId") },
            success: callback,
            error: null,
            dataType: "xml"
        });
    },

    deliveryCheck: function(callback) {
        var that = this;
        return function(xml, status, request) {
            var delivery = request.getResponseHeader("Delivery-Service-Package");
            if (delivery == "true" && window.deliveryService != null)
                that.getDelivery(window.deliveryService);
            callback(xml);
        };
    },

    getGameHistory: function(start, count, callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/gameHistory",
            cache: false,
            data: {
                start: start,
                count: count,
                participantId: getUrlParam("participantId") },
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },

    getLeagues: function(callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/league",
            cache: false,
            data: {
                participanId: getUrlParam("participantId") },
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },

    getReplay: function(replayId, callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/replay/" + replayId,
            cache: false,
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    startGameSession: function(callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/game/" + getUrlParam("gameId"),
            cache: false,
            data: { participantId: getUrlParam("participantId") },
            success: this.deliveryCheck(callback),
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
            success: this.deliveryCheck(callback),
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
            success: this.deliveryCheck(callback),
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
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    concede: function() {
        $.ajax({
            type: "POST",
            url: this.url + "/game/" + getUrlParam("gameId") + "/concede",
            cache: false,
            data: {
                participantId: getUrlParam("participantId")},
            error: this.failure,
            dataType: "xml"
        });
    },
    getDeck: function(deckName, callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/deck",
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                deckName: deckName},
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    getDecks: function(callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/deck/list",
            cache: false,
            data: {
                participantId: getUrlParam("participantId")},
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    getCollectionTypes: function(callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/collection",
            cache: false,
            data: {
                participantId: getUrlParam("participantId")},
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    getMerchant: function(filter, ownedMin, start, count, callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/merchant",
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                filter: filter,
                ownedMin: ownedMin,
                start: start,
                count: count},
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    buyItem: function(blueprintId, price, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/merchant/buy",
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                blueprintId: blueprintId,
                price: price},
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    sellItem: function(blueprintId, price, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/merchant/sell",
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                blueprintId: blueprintId,
                price: price},
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    tradeInFoil: function(blueprintId, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/merchant/tradeFoil",
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                blueprintId: blueprintId},
            success: this.deliveryCheck(callback),
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
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    openPack: function(collectionType, pack, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/collection/" + collectionType,
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                pack: pack},
            success: function(xml, status, request) {
                callback(xml);
            },
            error: this.failure,
            dataType: "xml"
        });
    },
    openSelectionPack: function(collectionType, pack, selection, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/collection/" + collectionType,
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                pack: pack,
                selection: selection},
            success: function(xml, status, request) {
                callback(xml);
            },
            error: this.failure,
            dataType: "xml"
        });
    },
    saveDeck: function(deckName, contents, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/deck",
            cache: false,
            async: false,
            data: {
                participantId: getUrlParam("participantId"),
                deckName: deckName,
                deckContents: contents},
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    renameDeck: function(oldDeckName, deckName, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/deck/rename",
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                oldDeckName: oldDeckName,
                deckName: deckName},
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    deleteDeck: function(deckName, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/deck/delete",
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                deckName: deckName},
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    getDeckStats: function(contents, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/deck/stats",
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                deckContents: contents},
            success: this.deliveryCheck(callback),
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
            success: this.deliveryCheck(callback),
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
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    sendChatMessage: function(room, messages, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/chat/" + room,
            cache: false,
            data: {
                participantId: getUrlParam("participantId"),
                message: messages},
            success: this.deliveryCheck(callback),
            traditional: true,
            error: this.failure,
            dataType: "xml"
        });
    },
    getHall: function(callback) {
        $.ajax({
            type: "GET",
            url: this.url + "/hall",
            cache: false,
            data: {
                participantId: getUrlParam("participantId")},
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    joinTable: function(tableId, deckName, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/hall/" + tableId,
            cache: false,
            data: {
                deckName: deckName,
                participantId: getUrlParam("participantId")},
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    createTable: function(format, deckName, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/hall",
            cache: false,
            data: {
                format: format,
                deckName: deckName,
                participantId: getUrlParam("participantId")},
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "xml"
        });
    },
    leaveTable: function() {
        $.ajax({
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
        $.ajax({
            type: "GET",
            url: this.url + "/",
            cache: false,
            data: {
                participantId: getUrlParam("participantId")},
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "html"
        });
    },
    login: function(login, password, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/login",
            cache: false,
            data: {
                login: login,
                password: password,
                participantId: getUrlParam("participantId")},
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "html"
        });
    },
    register: function(login, password, callback) {
        $.ajax({
            type: "POST",
            url: this.url + "/register",
            cache: false,
            data: {
                login: login,
                password: password,
                participantId: getUrlParam("participantId")},
            success: this.deliveryCheck(callback),
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
            success: this.deliveryCheck(callback),
            error: this.failure,
            dataType: "html"
        });
    }
});
