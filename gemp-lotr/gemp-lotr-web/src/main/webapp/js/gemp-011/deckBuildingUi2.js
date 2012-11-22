var GempLotrDeckBuildingUI2 = Class.extend({
    comm:null,
    cardFilter:null,

    init:function (filterDiv, pageDiv) {
        var that = this;

        this.comm = new GempLotrCommunication("/gemp-lotr-server", that.processError);

        this.cardFilter = new CardFilter(filterDiv, pageDiv, 
            function (filter, start, count, callback) {
                that.comm.getCollection(that.collectionType, filter, start, count, function (xml) {
                    callback(xml);
                }, {
                    "404":function () {
                        alert("You don't have collection of that type.");
                    }
                });
            },
            function () {
                that.clearCollection();
            },
            function (elem, type, blueprintId, count) {
                that.addCardToCollection(type, blueprintId, count, elem.getAttribute("side"), elem.getAttribute("contents"));
            },
            function () {
                that.finishCollection();
            });
        this.collectionType = "default";
    },

    layoutUI: function() {
        
    }
});
