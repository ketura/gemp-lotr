var cardCache = {};
var cardScale = 357 / 497;
var fixedImages = {
    // "Forth the Three Hunters!" cards, separate special images
    "15_204": "http://lotrtcgdb.com/images/LOTR15060D.jpg",
    "15_205":  "http://lotrtcgdb.com/images/LOTR15060E.jpg",
    "15_206":  "http://lotrtcgdb.com/images/LOTR15060G.jpg"
};

var packBlueprints = {
    "(S)FotR - Starter": "/gemp-lotr/images/boosters/fotr_starter_selection.png",
    "(S)MoM - Starter": "/gemp-lotr/images/boosters/mom_starter_selection.png",
    "(S)RotEL - Starter": "/gemp-lotr/images/boosters/rotel_starter_selection.png",

    "(S)TTT - Starter": "/gemp-lotr/images/boosters/ttt_starter_selection.png",
    "(S)BoHD - Starter": "/gemp-lotr/images/boosters/bohd_starter_selection.png",
    "(S)EoF - Starter": "/gemp-lotr/images/boosters/eof_starter_selection.png",

    "(S)FotR - Tengwar": "/gemp-lotr/images/boosters/fotr_tengwar_selection.png",
    "(S)TTT - Tengwar": "/gemp-lotr/images/boosters/ttt_tengwar_selection.png",
    "(S)RotK - Tengwar": "/gemp-lotr/images/boosters/rotk_tengwar_selection.png",
    "(S)SH - Tengwar": "/gemp-lotr/images/boosters/sh_tengwar_selection.png",

    "(S)Booster Choice": "/gemp-lotr/images/boosters/booster_selection.png",

    "FotR - League Starter": "/gemp-lotr/images/boosters/fotr_league_starter.png",
    "Random FotR Foil Common": "/gemp-lotr/images/boosters/random_foil.png",
    "Random FotR Foil Uncommon": "/gemp-lotr/images/boosters/random_foil.png",

    "FotR - Gandalf Starter": "/gemp-lotr/images/boosters/fotr_gandalf_starter.png",
    "FotR - Aragorn Starter": "/gemp-lotr/images/boosters/fotr_aragorn_starter.png",
    "FotR - Booster": "/gemp-lotr/images/boosters/fotr_booster.png",

    "MoM - Gandalf Starter": "/gemp-lotr/images/boosters/mom_gandalf_starter.png",
    "MoM - Gimli Starter": "/gemp-lotr/images/boosters/mom_gimli_starter.png",
    "MoM - Booster": "/gemp-lotr/images/boosters/mom_booster.png",

    "RotEL - Boromir Starter": "/gemp-lotr/images/boosters/rotel_boromir_starter.png",
    "RotEL - Legolas Starter": "/gemp-lotr/images/boosters/rotel_legolas_starter.png",
    "RotEL - Booster": "/gemp-lotr/images/boosters/rotel_booster.png",

    "TTT - Aragorn Starter": "/gemp-lotr/images/boosters/ttt_aragorn_starter.png",
    "TTT - Theoden Starter": "/gemp-lotr/images/boosters/ttt_theoden_starter.png",
    "TTT - Booster": "/gemp-lotr/images/boosters/ttt_booster.png",

    "BoHD - Eowyn Starter": "/gemp-lotr/images/boosters/bohd_eowyn_starter.png",
    "BoHD - Legolas Starter": "/gemp-lotr/images/boosters/bohd_legolas_starter.png",
    "BoHD - Booster": "/gemp-lotr/images/boosters/bohd_booster.png",

    "EoF - Faramir Starter": "/gemp-lotr/images/boosters/eof_faramir_starter.png",
    "EoF - Witch-king Starter": "/gemp-lotr/images/boosters/eof_witch_king_starter.png",
    "EoF - Booster": "/gemp-lotr/images/boosters/eof_booster.png",

    "RotK - Aragorn Starter": "/gemp-lotr/images/boosters/ttt_aragorn_starter.png",
    "RotK - Theoden Starter": "/gemp-lotr/images/boosters/ttt_theoden_starter.png",
    "RotK - Booster": "/gemp-lotr/images/boosters/rotk_booster.png",

    "SoG - Eowyn Starter": "/gemp-lotr/images/boosters/bohd_eowyn_starter.png",
    "SoG - Legolas Starter": "/gemp-lotr/images/boosters/bohd_legolas_starter.png",
    "SoG - Booster": "/gemp-lotr/images/boosters/sog_booster.png",

    "MD - Faramir Starter": "/gemp-lotr/images/boosters/eof_faramir_starter.png",
    "MD - Witch-king Starter": "/gemp-lotr/images/boosters/eof_witch_king_starter.png",
    "MD - Booster": "/gemp-lotr/images/boosters/md_booster.png",

    "SH - Faramir Starter": "/gemp-lotr/images/boosters/eof_faramir_starter.png",
    "SH - Witch-king Starter": "/gemp-lotr/images/boosters/eof_witch_king_starter.png",
    "SH - Booster": "/gemp-lotr/images/boosters/sh_booster.png",

    "BR - Faramir Starter": "/gemp-lotr/images/boosters/eof_faramir_starter.png",
    "BR - Witch-king Starter": "/gemp-lotr/images/boosters/eof_witch_king_starter.png",
    "BR - Booster": "/gemp-lotr/images/boosters/br_booster.png",

    "BL - Faramir Starter": "/gemp-lotr/images/boosters/eof_faramir_starter.png",
    "BL - Witch-king Starter": "/gemp-lotr/images/boosters/eof_witch_king_starter.png",
    "BL - Booster": "/gemp-lotr/images/boosters/bl_booster.png",

    "HU - Faramir Starter": "/gemp-lotr/images/boosters/eof_faramir_starter.png",
    "HU - Witch-king Starter": "/gemp-lotr/images/boosters/eof_witch_king_starter.png",
    "HU - Booster": "/gemp-lotr/images/boosters/hu_booster.png",

    "RoS - Faramir Starter": "/gemp-lotr/images/boosters/eof_faramir_starter.png",
    "RoS - Witch-king Starter": "/gemp-lotr/images/boosters/eof_witch_king_starter.png",
    "RoS - Booster": "/gemp-lotr/images/boosters/ros_booster.jpg",

    "TaD - Faramir Starter": "/gemp-lotr/images/boosters/eof_faramir_starter.png",
    "TaD - Witch-king Starter": "/gemp-lotr/images/boosters/eof_witch_king_starter.png",
    "TaD - Booster": "/gemp-lotr/images/boosters/tad_booster.jpg"
};

var Card = Class.extend({
    blueprintId: null,
    foil: null,
    tengwar: null,
    horizontal: null,
    imageUrl: null,
    zone: null,
    cardId: null,
    owner: null,
    siteNumber: null,
    attachedCards: null,

    init: function(blueprintId, zone, cardId, owner, siteNumber) {
        this.blueprintId = blueprintId;

        var len = blueprintId.length;
        this.foil = blueprintId.substring(len - 1, len) == "*";
        if (this.foil)
            blueprintId = blueprintId.substring(0, len - 1);
        len = blueprintId.length;
        this.tengwar = blueprintId.substring(len - 1, len) == "T";
        if (this.tengwar)
            blueprintId = blueprintId.substring(0, len - 1);

        this.zone = zone;
        this.cardId = cardId;
        this.owner = owner;
        if (siteNumber)
            this.siteNumber = parseInt(siteNumber);
        this.attachedCards = new Array();
        if (blueprintId == "rules") {
            this.imageUrl = "/gemp-lotr/images/rules.png";
        } else {
            if (cardCache[this.blueprintId] != null) {
                var cardFromCache = cardCache[this.blueprintId];
                this.horizontal = cardFromCache.horizontal;
                this.imageUrl = cardFromCache.imageUrl;
            } else {
                this.imageUrl = this.getUrlByBlueprintId(this.blueprintId);
                this.horizontal = this.isHorizontal(this.blueprintId);
                cardCache[this.blueprintId] = {
                    imageUrl: this.imageUrl,
                    horizontal: this.horizontal
                };
            }
        }
    },

    isTengwar: function() {
        return this.tengwar;
    },

    isFoil: function() {
        return this.foil;
    },

    isPack: function() {
        return packBlueprints[this.blueprintId] != null;
    },

    isHorizontal: function(blueprintId) {
        var separator = blueprintId.indexOf("_");
        var setNo = parseInt(blueprintId.substr(0, separator));
        var cardNo = parseInt(blueprintId.substr(separator + 1));

        if (setNo == 0)
            return (cardNo == 1 || cardNo == 4 || cardNo == 6 || cardNo == 8);
        if (setNo == 1)
            return (cardNo >= 319 && cardNo <= 363);
        if (setNo == 2)
            return (cardNo >= 115 && cardNo <= 120);
        if (setNo == 3)
            return (cardNo >= 115 && cardNo <= 120);
        if (setNo == 4)
            return (cardNo >= 323 && cardNo <= 363);
        if (setNo == 5)
            return (cardNo >= 118 && cardNo <= 120);
        if (setNo == 6)
            return (cardNo >= 115 && cardNo <= 120);
        if (setNo == 7)
            return (cardNo >= 329 && cardNo <= 363);
        if (setNo == 8)
            return (cardNo >= 117 && cardNo <= 120);
        if (setNo == 10)
            return (cardNo >= 117 && cardNo <= 120);
        if (setNo == 11)
            return (cardNo >= 227 && cardNo <= 266);
        if (setNo == 12)
            return (cardNo >= 185 && cardNo <= 194);
        if (setNo == 13)
            return (cardNo >= 185 && cardNo <= 194);
        if (setNo == 15)
            return (cardNo >= 187 && cardNo <= 194);
        if (setNo == 17)
            return (cardNo >= 145 && cardNo <= 148);
        if (setNo == 18)
            return (cardNo >= 134 && cardNo <= 140);
        return false;
    },

    getUrlByBlueprintId: function(blueprintId) {
        if (fixedImages[blueprintId] != null)
            return fixedImages[blueprintId];

        if (packBlueprints[blueprintId] != null)
            return packBlueprints[blueprintId];

        var separator = blueprintId.indexOf("_");
        var setNo = parseInt(blueprintId.substr(0, separator));
        var cardNo = parseInt(blueprintId.substr(separator + 1));

        var mainLocation = this.getMainLocation(setNo, cardNo);

        var setNoStr;
        if (setNo < 10)
            setNoStr = "0" + setNo;
        else
            setNoStr = setNo;

        var cardStr;
        if (cardNo < 10)
            cardStr = setNoStr + "00" + cardNo;
        else if (cardNo < 100)
            cardStr = setNoStr + "0" + cardNo;
        else
            cardStr = setNoStr + "" + cardNo;

        if (this.isMasterworks(setNo, cardNo))
            cardStr = setNoStr + "O0" + (cardNo - this.getMasterworksOffset(setNo));

        return mainLocation + "LOTR" + cardStr + (this.isTengwar() ? "T" : "") + ".jpg";
    },

    getMainLocation: function(setNo, cardNo) {
        if (this.isErrata(setNo, cardNo))
            return "/gemp-lotr/images/erratas/";
        else
            return "http://lotrtcgdb.com/images/";
    },

    getMasterworksOffset: function(setNo) {
        if (setNo == 17)
            return 148;
        if (setNo == 18)
            return 140;
        return 194;
    },

    isMasterworks: function(setNo, cardNo) {
        if (setNo == 12)
            return cardNo > 194;
        if (setNo == 13)
            return cardNo > 194;
        if (setNo == 15)
            return cardNo > 194 && cardNo < 204;
        if (setNo == 17)
            return cardNo > 148;
        if (setNo == 18)
            return cardNo > 140;
        return false;
    },

    isErrata: function(setNo, cardNo) {
        if (setNo == 0)
            return cardNo == 7;
        else if (setNo == 1)
            return cardNo == 12 || cardNo == 43 || cardNo == 46;
        else
            return false;
    },

    getHeightForWidth: function(width) {
        if (this.horizontal)
            return Math.floor(width * cardScale);
        else
            return Math.floor(width / cardScale);
    },

    getWidthForHeight: function(height) {
        if (this.horizontal)
            return Math.floor(height / cardScale);
        else
            return Math.floor(height * cardScale);
    },

    getWidthForMaxDimension: function(maxDimension) {
        if (this.horizontal)
            return maxDimension;
        else
            return Math.floor(maxDimension * cardScale);
    }
});

function createCardDiv(image, text, foil, tokens, noBorder) {
    var cardDiv = $("<div class='card'><img src='" + image + "' width='100%' height='100%'>" + ((text != null) ? text : "") + "</div>");
    if (foil) {
        var foilDiv = $("<div class='foilOverlay'><img src='/gemp-lotr/images/foil.gif' width='100%' height='100%'></div>");
        cardDiv.append(foilDiv);
    }

    if (tokens === undefined || tokens) {
        var overlayDiv = $("<div class='tokenOverlay'></div>");
        cardDiv.append(overlayDiv);
    }
    var borderDiv = $("<div class='borderOverlay'><img class='actionArea' src='/gemp-lotr/images/pixel.png' width='100%' height='100%'></div>");
    if (noBorder)
        borderDiv.addClass("noBorder");
    cardDiv.append(borderDiv);

    return cardDiv;
}

function createFullCardDiv(image, foil, horizontal, noBorder) {
    if (horizontal) {
        var cardDiv = $("<div style='position: relative;width:497px;height:357px;'></div>");
        cardDiv.append("<div style='position:absolute'><img src='" + image + "' width='497' height='357'></div>");

        if (noBorder) {
            var borderDiv = $("<div class='borderOverlay,noBorder' style='position:absolute;width:497px;height:357px;border-width:0px'><img class='actionArea' src='/gemp-lotr/images/pixel.png' width='100%' height='100%'></div>");
            cardDiv.append(borderDiv);
        } else {
            var borderDiv = $("<div class='borderOverlay' style='position:absolute;width:465px;height:325px;border-width:16px'><img class='actionArea' src='/gemp-lotr/images/pixel.png' width='100%' height='100%'></div>");
            cardDiv.append(borderDiv);
        }

        if (foil) {
            var foilDiv = $("<div class='foilOverlay' style='position:absolute;width:497px;height:357px'><img src='/gemp-lotr/images/foil.gif' width='100%' height='100%'></div>");
            cardDiv.append(foilDiv);
        }
    } else {
        var cardDiv = $("<div style='position: relative;width:357px;height:497px;'></div>");
        cardDiv.append("<div style='position:absolute'><img src='" + image + "' width='357' height='497'></div>");

        if (noBorder) {
            var borderDiv = $("<div class='borderOverlay,noBorder' style='position:absolute;width:357px;height:497px;border-width:0px'><img class='actionArea' src='/gemp-lotr/images/pixel.png' width='100%' height='100%'></div>");
            cardDiv.append(borderDiv);
        } else {
            var borderDiv = $("<div class='borderOverlay' style='position:absolute;width:325px;height:465px;border-width:16px'><img class='actionArea' src='/gemp-lotr/images/pixel.png' width='100%' height='100%'></div>");
            cardDiv.append(borderDiv);
        }

        if (foil) {
            var foilDiv = $("<div class='foilOverlay' style='position:absolute;width:357px;height:497px'><img src='/gemp-lotr/images/foil.gif' width='100%' height='100%'></div>");
            cardDiv.append(foilDiv);
        }
    }

    return cardDiv;
}

function createSimpleCardDiv(image) {
    var cardDiv = $("<div class='card'><img src='" + image + "' width='100%' height='100%'></div>");

    return cardDiv;
}
