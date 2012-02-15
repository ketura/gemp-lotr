var CardFilter = Class.extend({
    communication: null,
    clearCollectionFunc: null,
    addCardFunc: null,
    finishCollectionFunc: null,

    collectionType: null,

    filter: null,
    start: 0,
    count: 18,

    pageDiv: null,
    fullFilterDiv: null,
    filterDiv: null,

    previousPageBut: null,
    nextPageBut: null,
    countSlider: null,

    setSelect: null,
    nameInput: null,
    sortSelect: null,
    raritySelect: null,

    init: function(url, elem, clearCollectionFunc, addCardFunc, finishCollectionFunc) {
        this.communication = new GempLotrCommunication(url,
                function(xhr, ajaxOptions, thrownError) {
                });

        this.clearCollectionFunc = clearCollectionFunc;
        this.addCardFunc = addCardFunc;
        this.finishCollectionFunc = finishCollectionFunc;

        this.filter = "cardType:-THE_ONE_RING";

        this.buildUi(elem);
    },

    setCollectionType: function(collectionType) {
        this.collectionType = collectionType;
        this.start = 0;
        this.getCollection();
    },

    enableDetailFilters: function(enable) {
        $("#culture1").buttonset("option", "disabled", !enable);
        $("#culture2").buttonset("option", "disabled", !enable);
        $("#cardType").prop("disabled", !enable);
        $("#keyword").prop("disabled", !enable);
    },

    setFilter: function(filter) {
        this.filter = filter;

        this.start = 0;
        this.getCollection();
    },

    buildUi: function(elem) {
        var that = this;

        this.pageDiv = $("<div></div>");

        this.previousPageBut = $("<button id='previousPage' style='float: left;'>Previous page</button>").button({
            text: false,
            icons: {
                primary: "ui-icon-circle-triangle-w"
            },
            disabled: true
        }).click(
                function() {
                    that.disableNavigation();
                    that.start -= that.count;
                    that.getCollection();
                });

        this.nextPageBut = $("<button id='nextPage' style='float: right;'>Next page</button>").button({
            text: false,
            icons: {
                primary: "ui-icon-circle-triangle-e"
            },
            disabled: true
        }).click(
                function() {
                    that.disableNavigation();
                    that.start += that.count;
                    that.getCollection();
                });

        this.countSlider = $("<div id='countSlider' style='left: 50px; top: 10px; width: 200px;'></div>").slider({
            value:18,
            min: 4,
            max: 40,
            step: 1,
            disabled: true,
            slide: function(event, ui) {
                that.start = 0;
                that.count = ui.value;
                that.getCollection();
            }
        });

        this.pageDiv.append(this.previousPageBut);
        this.pageDiv.append(this.nextPageBut);
        this.pageDiv.append(this.countSlider);

        elem.append(this.pageDiv);

        this.fullFilterDiv = $("<div></div>");
        this.setSelect = $("<select style='width: 130px; font-size: 80%;'>"
                + "<option value=''>All Sets</option>"
                + "<option value='0'>00 - Promo</option>"
                + "<option value='1,2,3'>Fellowship Block</option>"
                + "<option value='1'>01 - The Fellowship of the Ring</option>"
                + "<option value='2'>02 - Mines of Moria</option>"
                + "<option value='3'>03 - Realms of the Elf-lords</option>"
                + "<option value='4,5,6'>Towers Block</option>"
                + "<option value='4'>04 - The Two Towers</option>"
                + "<option value='5'>05 - Battle of Helm's Deep</option>"
                + "<option value='6'>06 - Ents of Fangorn</option>"
                + "<option value='7,8,10'>King Block</option>"
                + "<option value='7'>07 - The Return of the King</option>"
                + "<option value='8'>08 - Siege of Gondor</option>"
                + "<option value='9'>09 - Reflections</option>"
                + "<option value='10'>10 - Mount Doom</option>"
                + "<option value='11,12,13'>War of the Ring Block</option>"
                + "<option value='11'>11 - Shadows</option>"
                + "<option value='12'>12 - Black Rider</option>"
                + "<option value='13'>13 - Bloodlines</option>"
                + "<option value='14'>14 - Expanded Middle-earth</option>"
                + "<option value='15,17,18'>Hunters Block</option>"
                + "<option value='15'>15 - The Hunters</option>"
                + "<option value='16'>16 - The Wraith Collection</option>"
                + "<option value='17'>17 - Rise of Saruman</option>"
                + "<option value='18'>18 - Treachery & Deceit</option>"
                + "<option value='19'>19 - Ages End</option>"
                + "</select>");
        this.nameInput = $("<input type='text' value='Card name' style='width: 130px; font-size: 70%;'>");
        this.sortSelect = $("<select style='width: 80px; font-size: 80%;'>"
                + "<option value=''>Sort by:</option>"
                + "<option value='name'>Name</option>"
                + "<option value='twilight'>Twilight</option>"
                + "<option value='siteNumber'>Site number</option>"
                + "<option value='strength'>Strength</option>"
                + "<option value='vitality'>Vitality</option>"
                + "</select>");
        this.raritySelect = $("<select style='width: 40px; font-size: 80%;'>"
                + "<option value=''>Rarity:</option>"
                + "<option value='R'>Rare</option>"
                + "<option value='U'>Uncommon</option>"
                + "<option value='C'>Common</option>"
                + "<option value='A'>Alternate Image</option>"
                + "<option value='P'>Promo</option>"
                + "<option value='unknown'>Unknown</option>"
                + "</select>");

        this.fullFilterDiv.append(this.setSelect);
        this.fullFilterDiv.append(this.nameInput);
        this.fullFilterDiv.append(this.sortSelect);
        this.fullFilterDiv.append(this.raritySelect);

        elem.append(this.fullFilterDiv);

        this.filterDiv = $("<div></div>");

        this.filterDiv.append("<div id='culture1'>"
                + "<input type='checkbox' id='DWARVEN'/><label for='DWARVEN' id='labelDWARVEN'><img src='images/cultures/dwarven.png'/></label>"
                + "<input type='checkbox' id='ELVEN'/><label for='ELVEN' id='labelELVEN'><img src='images/cultures/elven.png'/></label>"
                + "<input type='checkbox' id='GANDALF'/><label for='GANDALF' id='labelGANDALF'><img src='images/cultures/gandalf.png'/></label>"
                + "<input type='checkbox' id='GONDOR'/><label for='GONDOR' id='labelGONDOR'><img src='images/cultures/gondor.png'/></label>"
                + "<input type='checkbox' id='ROHAN'/><label for='ROHAN' id='labelROHAN'><img src='images/cultures/rohan.png'/></label>"
                + "<input type='checkbox' id='SHIRE'/><label for='SHIRE' id='labelSHIRE'><img src='images/cultures/shire.png'/></label>"
                + "<input type='checkbox' id='GOLLUM'/><label for='GOLLUM' id='labelGOLLUM'><img src='images/cultures/gollum.png'/></label>"
                + "</div>");
        this.filterDiv.append("<div id='culture2'>"
                + "<input type='checkbox' id='DUNLAND'/><label for='DUNLAND' id='labelDUNLAND'><img src='images/cultures/dunland.png'/></label>"
                + "<input type='checkbox' id='ISENGARD'/><label for='ISENGARD' id='labelISENGARD'><img src='images/cultures/isengard.png'/></label>"
                + "<input type='checkbox' id='MEN'/><label for='MEN' id='labelMEN'><img src='images/cultures/men.png'/></label>"
                + "<input type='checkbox' id='MORIA'/><label for='MORIA' id='labelMORIA'><img src='images/cultures/moria.png'/></label>"
                + "<input type='checkbox' id='ORC'/><label for='ORC' id='labelORC'><img src='images/cultures/orc.png'/></label>"
                + "<input type='checkbox' id='RAIDER'/><label for='RAIDER' id='labelRAIDER'><img src='images/cultures/raider.png'/></label>"
                + "<input type='checkbox' id='SAURON'/><label for='SAURON' id='labelSAURON'><img src='images/cultures/sauron.png'/></label>"
                + "<input type='checkbox' id='URUK_HAI'/><label for='URUK_HAI' id='labelURUK_HAI'><img src='images/cultures/uruk_hai.png'/></label>"
                + "<input type='checkbox' id='WRAITH'/><label for='WRAITH' id='labelWRAITH'><img src='images/cultures/wraith.png'/></label>"
                + "</div>");

        var combos = $("<div></div>");

        combos.append(" <select id='cardType' style='font-size: 80%;'>"
                + "<option value=''>All Card Types</option>"
                + "<option value='COMPANION,ALLY,MINION'>Characters</option>"
                + "<option value='POSSESSION,ARTIFACT'>Items</option>"
                + "<option value='SITE'>Sites</option>"
                + "<option value='ALLY'>Allies</option>"
                + "<option value='ARTIFACT'>Artifacts</option>"
                + "<option value='COMPANION'>Companions</option>"
                + "<option value='CONDITION'>Conditions</option>"
                + "<option value='EVENT'>Events</option>"
                + "<option value='FOLLOWER'>Followers</option>"
                + "<option value='MINION'>Minions</option>"
                + "<option value='POSSESSION'>Possessions</option>"
                + "</select>");
        combos.append(" <select id='keyword' style='font-size: 80%;'>"
                + "<option value=''>No keyword filtering</option>"
                + "<option value='ARCHER'>Archer</option>"
                + "<option value='BESIEGER'>Besieger</option>"
                + "<option value='CORSAIR'>Corsair</option>"
                + "<option value='EASTERLING'>Easterling</option>"
                + "<option value='ENDURING'>Enduring</option>"
                + "<option value='ENGINE'>Engine</option>"
                + "<option value='FIERCE'>Fierce</option>"
                + "<option value='FORTIFICATION'>Fortification</option>"
                + "<option value='KNIGHT'>Knight</option>"
                + "<option value='LURKER'>Lurker</option>"
                + "<option value='MACHINE'>Machine</option>"
                + "<option value='MUSTER'>Muster</option>"
                + "<option value='PIPEWEED'>Pipeweed</option>"
                + "<option value='RANGER'>Ranger</option>"
                + "<option value='RING_BOUND'>Ring-bound</option>"
                + "<option value='SEARCH'>Search</option>"
                + "<option value='SOUTHRON'>Southron</option>"
                + "<option value='SPELL'>Spell</option>"
                + "<option value='STEALTH'>Stealth</option>"
                + "<option value='TALE'>Tale</option>"
                + "<option value='TENTACLE'>Tentacle</option>"
                + "<option value='TRACKER'>Tracker</option>"
                + "<option value='TWILIGHT'>Twilight</option>"
                + "<option value='UNHASTY'>Unhasty</option>"
                + "<option value='VALIANT'>Valiant</option>"
                + "<option value='VILLAGER'>Villager</option>"
                + "<option value='WARG_RIDER'>Warg-rider</option>"
                + "<option value='WEATHER'>Weather</option>"
                + "</select>");
        combos.append(" <select id='type' style='font-size: 80%'>"
                + "<option value=''>All types</option>"
                + "<option value='pack'>Packs</option>"
                + "<option value='card'>Cards</option>"
                + "<option value='foil'>Foils</option>"
                + "<option value='nonFoil'>Non-foils</option>"
                + "</select>");
        this.filterDiv.append(combos);

        elem.append(this.filterDiv);

        $("#culture1").buttonset();
        $("#culture2").buttonset();

        var fullFilterChanged = function() {
            that.start = 0;
            that.getCollection();
            return true;
        };

        this.setSelect.change(fullFilterChanged);
        this.nameInput.change(fullFilterChanged);
        this.sortSelect.change(fullFilterChanged);
        this.raritySelect.change(fullFilterChanged);

        var filterOut = function() {
            that.filter = that.calculateNormalFilter();
            that.start = 0;
            that.getCollection();
            return true;
        };

        $("#cardType").change(filterOut);
        $("#keyword").change(filterOut);
        $("#type").change(filterOut);

        $("#labelDWARVEN,#labelELVEN,#labelGANDALF,#labelGONDOR,#labelROHAN,#labelSHIRE,#labelGOLLUM,#labelDUNLAND,#labelISENGARD,#labelMEN,#labelMORIA,#labelORC,#labelRAIDER,#labelSAURON,#labelURUK_HAI,#labelWRAITH").click(filterOut);
    },

    layoutUi: function(x, y, width, height) {
        this.pageDiv.css({ position: "absolute", left: x, top: y, width: width, height: 50 });
        this.countSlider.css({width: width - 100});
        this.fullFilterDiv.css({position:"absolute", left: x, top: y + 50, width: width, height: 30});
        this.filterDiv.css({ position: "absolute", left: x, top: y + 80, width: width, height: 80 });
    },

    disableNavigation: function() {
        this.previousPageBut.button("option", "disabled", true);
        this.nextPageBut.button("option", "disabled", true);
        this.countSlider.button("option", "disabled", true);
    },

    calculateNormalFilter: function() {
        var cultures = new Array();
        $("label", $("#culture1")).each(
                function() {
                    if ($(this).hasClass("ui-state-active"))
                        cultures.push($(this).prop("id").substring(5));
                });
        $("label", $("#culture2")).each(
                function() {
                    if ($(this).hasClass("ui-state-active"))
                        cultures.push($(this).prop("id").substring(5));
                });

        var cardType = $("#cardType option:selected").prop("value");
        if (cardType == "")
            cardType = "cardType:-THE_ONE_RING";
        else
            cardType = "cardType:" + cardType;

        var keyword = $("#keyword option:selected").prop("value");
        if (keyword != "")
            keyword = " keyword:" + keyword;

        var type = $("#type option:selected").prop("value");
        if (type != "")
            type = " type:" + type;

        if (cultures.length > 0)
            return cardType + " culture:" + cultures + keyword + type;
        else
            return cardType + keyword + type;
    },

    calculateFullFilterPostfix: function() {
        var setNo = $("option:selected", this.setSelect).prop("value");
        if (setNo != "")
            setNo = " set:" + setNo;

        var sort = $("option:selected", this.sortSelect).prop("value");
        if (sort != "")
            sort = " sort:" + sort;

        var cardName = this.nameInput.val();
        if (cardName == "Card name")
            cardName = "";
        else {
            var cardNameElems = cardName.split(" ");
            cardName = "";
            for (var i = 0; i < cardNameElems.length; i++)
                cardName += " name:" + cardNameElems[i];
        }

        var rarity = $("option:selected", this.raritySelect).prop("value");
        if (rarity != "")
            rarity = " rarity:" + rarity;

        return setNo + sort + cardName + rarity;
    },

    getCollection: function() {
        var that = this;
        this.communication.getCollection(this.collectionType, this.filter + this.calculateFullFilterPostfix(), this.start, this.count, function(xml) {
            that.displayCollection(xml);
        });
    },

    displayCollection: function(xml) {
        log(xml);
        var root = xml.documentElement;
        if (root.tagName == "collection") {
            this.clearCollectionFunc();

            var packs = root.getElementsByTagName("pack");
            for (var i = 0; i < packs.length; i++) {
                var packElem = packs[i];
                var blueprintId = packElem.getAttribute("blueprintId");
                var count = packElem.getAttribute("count");
                this.addCardFunc("pack", blueprintId, count, null, packElem.getAttribute("contents"));
            }

            var cards = root.getElementsByTagName("card");
            for (var i = 0; i < cards.length; i++) {
                var cardElem = cards[i];
                var blueprintId = cardElem.getAttribute("blueprintId");
                var count = cardElem.getAttribute("count");
                this.addCardFunc("card", blueprintId, count, cardElem.getAttribute("side"), null);
            }

            this.finishCollectionFunc();

            $("#previousPage").button("option", "disabled", this.start == 0);
            var cnt = parseInt(root.getAttribute("count"));
            $("#nextPage").button("option", "disabled", (this.start + this.count) >= cnt);
            $("#countSlider").slider("option", "disabled", false);
        }
    }
});