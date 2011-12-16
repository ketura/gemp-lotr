var deliveryDialog = null;
var deliveryGroup = null;

function deliveryService(xml) {
    log("Delivered a package:");
    log(xml);

    var root = xml.documentElement;
    if (root.tagName == "delivery") {

        var deliveryDialogResize = function() {
            var width = deliveryDialog.width() + 10;
            var height = deliveryDialog.height() + 10;
            deliveryGroup.setBounds(2, 2, width - 2 * 2, height - 2 * 2);
        };

        if (deliveryDialog == null) {
            deliveryDialog = $("<div></div>").dialog({
                title: "You've received the following items",
                autoOpen: false,
                closeOnEscape: false,
                resizable: true,
                width: 400,
                height: 200
            });

            deliveryGroup = new NormalCardGroup(deliveryDialog, function(card) {
                return true;
            }, false);

            deliveryDialog.bind("dialogresize", deliveryDialogResize);
            deliveryDialog.bind("dialogclose",
                    function() {
                        deliveryDialog.html("");
                    });
        }

        var collections = root.getElementsByTagName("collectionType");
        for (var i = 0; i < collections.length; i++) {
            var collection = collections[i];

            var packs = collection.getElementsByTagName("pack");
            for (var j = 0; j < packs.length; j++) {
                var packElem = packs[j];
                var blueprintId = packElem.getAttribute("blueprintId");
                var count = packElem.getAttribute("count");
                var card = new Card(blueprintId, "delivery", "deliveryPack" + i, "player");
                card.tokens = {"count":count};
                var cardDiv = createCardDiv(card.imageUrl, null, card.isFoil(), true);
                cardDiv.data("card", card);
                deliveryDialog.append(cardDiv);
            }

            var cards = collection.getElementsByTagName("card");
            for (var j = 0; j < cards.length; j++) {
                var cardElem = cards[j];
                var blueprintId = cardElem.getAttribute("blueprintId");
                var count = cardElem.getAttribute("count");
                var card = new Card(blueprintId, "delivery", "deliveryCard" + i, "player");
                card.tokens = {"count":count};
                var cardDiv = createCardDiv(card.imageUrl, null, card.isFoil());
                cardDiv.data("card", card);
                deliveryDialog.append(cardDiv);
            }
        }

        deliveryDialog.dialog("open");
        deliveryDialogResize();
    }
}