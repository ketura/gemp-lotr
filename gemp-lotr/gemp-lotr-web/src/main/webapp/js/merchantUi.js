var GempLotrMerchantUI = Class.extend({
    comm: null,

    cardsDiv: null,
    cardsGroup: null,

    filterDiv: null,
    cardFilter: null,

    infoDialog: null,

    init: function(cardListElem, cardFilterElem) {
        var that = this;

        this.comm = new GempLotrCommunication("/gemp-lotr/server", that.processError);

        this.cardFilter = new CardFilter(cardFilterElem,
                function(filter, start, count, callback) {
                    that.comm.getMerchant(filter, start, count, callback);
                },
                function() {
                    that.clearList();
                },
                function(elem, type, blueprintId, count) {
                    that.addCardToList(elem, type, blueprintId, count);
                },
                function() {
                    that.finishList();
                });

        this.cardsDiv = cardListElem;
        this.cardsGroup = new NormalCardGroup(this.cardsDiv, function(card) {
            return true;
        });

        this.filterDiv = cardFilterElem;

        this.infoDialog = $("<div></div>")
                .dialog({
            autoOpen: false,
            closeOnEscape: true,
            resizable: false,
            title: "Card information"
        });

        var swipeOptions = {
            threshold: 20,
            swipeUp: function (event) {
                that.infoDialog.prop({ scrollTop: that.infoDialog.prop("scrollHeight") });
                return false;
            },
            swipeDown: function (event) {
                that.infoDialog.prop({ scrollTop: 0 });
                return false;
            }
        };
        this.infoDialog.swipe(swipeOptions);

        this.cardFilter.getCollection();
    },

    dragCardData: null,
    dragStartX: null,
    dragStartY: null,
    successfulDrag: null,

    dragStartCardFunction: function(event) {
        this.successfulDrag = false;
        var tar = $(event.target);
        if (tar.hasClass("actionArea")) {
            tar = tar.parent();
            if (tar.hasClass("borderOverlay")) {
                var selectedCardElem = tar.parent();
                if (event.which == 1) {
                    this.dragCardData = selectedCardElem.data("card");
                    this.dragStartX = event.clientX;
                    this.dragStartY = event.clientY;
                    return false;
                }
            }
        }
        return true;
    },

    dragStopCardFunction: function(event) {
        if (this.dragCardData != null) {
            if (this.dragStartY - event.clientY >= 20) {
                this.displayCardInfo(this.dragCardData);
                this.successfulDrag = true;
            }
            this.dragCardData = null;
            this.dragStartX = null;
            this.dragStartY = null;
            return false;
        }
        return true;
    },

    displayCardInfo: function(card) {
        this.infoDialog.html("");
        this.infoDialog.html("<div style='scroll: auto'></div>");
        this.infoDialog.append(createFullCardDiv(card.imageUrl, card.foil, card.horizontal, card.isPack()));
        var windowWidth = $(window).width();
        var windowHeight = $(window).height();

        var horSpace = 30;
        var vertSpace = 45;

        if (card.horizontal) {
            // 500x360
            this.infoDialog.dialog({width: Math.min(500 + horSpace, windowWidth), height: Math.min(360 + vertSpace, windowHeight)});
        } else {
            // 360x500
            this.infoDialog.dialog({width: Math.min(360 + horSpace, windowWidth), height: Math.min(500 + vertSpace, windowHeight)});
        }
        this.infoDialog.dialog("open");
    },

    clearList: function() {
        $(".card", this.cardsDiv).remove();
    },

    addCardToList: function(elem, type, blueprintId, count) {
        if (type == "pack") {
            var card = new Card(blueprintId, "merchant", "collection", "player");
            card.tokens = {"count":count};
            var cardDiv = createCardDiv(card.imageUrl, null, false, true, true);
            cardDiv.data("card", card);
            this.cardsDiv.append(cardDiv);
        } else if (type == "card") {
            var card = new Card(blueprintId, "merchant", "collection", "player");
            card.tokens = {"count":count};
            var cardDiv = createCardDiv(card.imageUrl, null, card.isFoil());
            cardDiv.data("card", card);
            this.cardsDiv.append(cardDiv);
        }
    },

    finishList: function() {
        this.cardsGroup.layoutCards();
    },

    layoutUI: function() {
        var cardsGroupWidth = $(this.cardsDiv).width();
        var cardsGroupHeight = $(this.cardsDiv).height();
        this.cardsGroup.setBounds(0, 0, cardsGroupWidth, cardsGroupHeight);

        var filterWidth = $(this.filterDiv).width();
        var filterHeight = $(this.filterDiv).height();
        this.cardFilter.layoutUi(0, 0, filterWidth, filterHeight);
    },

    processError: function (xhr, ajaxOptions, thrownError) {
        if (thrownError != "abort")
            alert("There was a problem during communication with server");
    }
});
