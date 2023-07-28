package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.cards.CardBlueprintLibrary;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.cards.LotroCardBlueprint;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.game.adventure.Adventure;
import com.gempukku.lotro.game.adventure.AdventureLibrary;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.cards.lotronly.LotroDeck;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultLotroFormat implements LotroFormat {

    public static final String CardRemovedError = "Deck contains card removed from the set";
    private final Adventure _adventure;
    private final CardBlueprintLibrary _library;
    private final String _name;
    private final String _code;
    private final int _order;
    private final boolean _hallVisible;
    private final SitesBlock _siteBlock;
    private boolean _validateShadowFPCount = true;
    private int _maximumSameName = 4;
    private final boolean _mulliganRule;
    private final boolean _canCancelRingBearerSkirmish;
    private final boolean _hasRuleOfFour;
    private final boolean _winAtEndOfRegroup;
    private final boolean _discardPileIsPublic;
    private final boolean _winOnControlling5Sites;
    private int _minimumDeckSize = 60;
    private final List<String> _bannedCards = new ArrayList<>();
    private final List<String> _restrictedCards = new ArrayList<>();
    private final List<String> _validCards = new ArrayList<>();
    private final List<Integer> _validSets = new ArrayList<>();
    private final List<String> _restrictedCardNames = new ArrayList<>();
    private final String _surveyUrl;
    private final boolean _isPlaytest;

    //Additional Hobbit Draft parameters
    private final List<String> _limit2Cards = new ArrayList<>();
    private final List<String> _limit3Cards = new ArrayList<>();
    private final Map<String,String> _errataCardMap = new TreeMap<>();

    public DefaultLotroFormat(AdventureLibrary adventureLibrary, CardBlueprintLibrary library, JSONDefs.Format def) throws InvalidPropertiesFormatException{
        this(library, adventureLibrary.getAdventure(def.adventure), def.name, def.code, def.order, def.surveyUrl, SitesBlock.valueOf(def.sites),
                def.validateShadowFPCount, def.minimumDeckSize, def.maximumSameName, def.mulliganRule, def.cancelRingBearerSkirmish,
                def.ruleOfFour, def.winAtEndOfRegroup, def.discardPileIsPublic, def.winOnControlling5Sites, def.playtest, def.hall);

        if(def.set != null)
            def.set.forEach(this::addValidSet);
        if(def.banned != null)
            def.banned.forEach(this::addBannedCard);
        if(def.restricted != null)
            def.restricted.forEach(this::addRestrictedCard);
        if(def.valid != null)
            def.valid.forEach(this::addValidCard);
        if(def.limit2 != null)
            def.limit2.forEach(this::addLimit2Card);
        if(def.limit3 != null)
            def.limit3.forEach(this::addLimit3Card);
        if(def.restrictedName != null)
            def.restrictedName.forEach(this::addRestrictedCardName);
        if(def.errataSets != null) {
            for (Integer errataSet : def.errataSets) {
                addErrataSet(errataSet);
            }
        }
        if(def.errata != null)
            def.errata.forEach(this::addCardErrata);
    }

    public DefaultLotroFormat(CardBlueprintLibrary library,
                              Adventure adventure, String name, String code, int order, String surveyUrl,
                              SitesBlock siteBlock,
                              boolean validateShadowFPCount, int minimumDeckSize, int maximumSameName, boolean mulliganRule,
                              boolean canCancelRingBearerSkirmish, boolean hasRuleOfFour, boolean winAtEndOfRegroup, boolean discardPileIsPublic,
                              boolean winOnControlling5Sites, boolean playtest, boolean hallVisible) {
        _adventure = adventure;
        _library = library;
        _name = name;
        _code = code;
        _order = order;
        _surveyUrl = surveyUrl;
        _siteBlock = siteBlock;
        _validateShadowFPCount = validateShadowFPCount;
        _minimumDeckSize = minimumDeckSize;
        _maximumSameName = maximumSameName;
        _mulliganRule = mulliganRule;
        _canCancelRingBearerSkirmish = canCancelRingBearerSkirmish;
        _hasRuleOfFour = hasRuleOfFour;
        _winAtEndOfRegroup = winAtEndOfRegroup;
        _discardPileIsPublic = discardPileIsPublic;
        _winOnControlling5Sites = winOnControlling5Sites;
        _isPlaytest = playtest;
        _hallVisible = hallVisible;
    }

    @Override
    public Adventure getAdventure() {
        return _adventure;
    }

    @Override
    public final boolean isOrderedSites() {
        return _siteBlock != SitesBlock.SHADOWS;
    }

    @Override
    public String getName() {
        return _name;
    }
    @Override
    public String getCode() {
        return _code;
    }
    @Override
    public int getOrder() {
        return _order;
    }
    @Override
    public boolean hallVisible() {
        return _hallVisible;
    }

    @Override
    public boolean hasMulliganRule() {
        return _mulliganRule;
    }

    @Override
    public boolean winWhenShadowReconciles() {
        return _winAtEndOfRegroup;
    }

    @Override
    public boolean discardPileIsPublic() { return _discardPileIsPublic; }

    @Override
    public boolean canCancelRingBearerSkirmish() {
        return _canCancelRingBearerSkirmish;
    }

    @Override
    public boolean hasRuleOfFour() {
        return _hasRuleOfFour;
    }

    @Override
    public boolean winOnControlling5Sites() {
        return _winOnControlling5Sites;
    }

    @Override
    public boolean isPlaytest() {
        return _isPlaytest;
    }

    @Override
    public List<Integer> getValidSets() {
        return Collections.unmodifiableList(_validSets);
    }

    @Override
    public List<String> getBannedCards() {
        return Collections.unmodifiableList(_bannedCards);
    }

    @Override
    public List<String> getRestrictedCards() {
        return Collections.unmodifiableList(_restrictedCards);
    }

    @Override
    public List<String> getRestrictedCardNames() {
        return Collections.unmodifiableList(_restrictedCardNames);
    }

    @Override
    public List<String> getValidCards() {
        return Collections.unmodifiableList(_validCards);
    }

    //Additional Hobbit Draft parameters
    @Override
    public List<String> getLimit2Cards() {
        return Collections.unmodifiableList(_limit2Cards);
    }

    @Override
    public List<String> getLimit3Cards() {
        return Collections.unmodifiableList(_limit3Cards);
    }

    @Override
    public Map<String,String> getErrataCardMap() {
        return Collections.unmodifiableMap(_errataCardMap);
    }

    @Override
    public SitesBlock getSiteBlock() {
        return _siteBlock;
    }

    @Override
    public String getSurveyUrl() {
        return _surveyUrl;
    }

    @Override
    public int getHandSize() {
        return 3;
    }

    public void addBannedCard(String baseBlueprintId) {
        if (baseBlueprintId.contains("-")) {
            String[] parts = baseBlueprintId.split("_");
            String set = parts[0];
            int from = Integer.parseInt(parts[1].split("-")[0]);
            int to = Integer.parseInt(parts[1].split("-")[1]);
            for (int i = from; i <= to; i++)
                _bannedCards.add(set + "_" + i);
        } else
            _bannedCards.add(baseBlueprintId);
    }

    public void addRestrictedCard(String baseBlueprintId) {
        if (baseBlueprintId.contains("-")) {
            String[] parts = baseBlueprintId.split("_");
            String set = parts[0];
            int from = Integer.parseInt(parts[1].split("-")[0]);
            int to = Integer.parseInt(parts[1].split("-")[1]);
            for (int i = from; i <= to; i++)
                _restrictedCards.add(set + "_" + i);
        } else
            _restrictedCards.add(baseBlueprintId);
    }

    public void addValidCard(String baseBlueprintId) {
        if (baseBlueprintId.contains("-")) {
            String[] parts = baseBlueprintId.split("_");
            String set = parts[0];
            int from = Integer.parseInt(parts[1].split("-")[0]);
            int to = Integer.parseInt(parts[1].split("-")[1]);
            for (int i = from; i <= to; i++)
                _validCards.add(set + "_" + i);
        } else
            _validCards.add(baseBlueprintId);
    }

    public void addValidSet(int setNo) {
        _validSets.add(setNo);
    }

    //Additional Hobbit Draft card lists
    public void addLimit2Card(String baseBlueprintId) {
        _limit2Cards.add(baseBlueprintId);
    }

    public void addLimit3Card(String baseBlueprintId) {
        _limit3Cards.add(baseBlueprintId);
    }

    public void addRestrictedCardName(String cardName) {
        _restrictedCardNames.add(cardName);
    }

    public void addErrataSet(int setID) throws InvalidPropertiesFormatException {
        //Valid errata sets:
        // 50-69 are live errata versions of sets 0-19
        // 70-89 are playtest errata versions of sets 0-19
        // 150-199 are playtest versions of sets V0-V49
        if(setID < 50 || (setID >= 90 && setID <= 149) || setID > 199)
            throw new InvalidPropertiesFormatException("Errata sets must be 50-69, 70-89, or 150-159.  Received: " + setID);

        //maps 69 to 19, and also 151 to 101
        int ogSet = setID - 50;
        //playtest sets are offset by 20 more
        if(setID >= 70 && setID <=89)
            ogSet = setID - 70;

        var cards = _library.getBaseCards().keySet().stream().filter(x -> x.startsWith("" + setID)).toList();
        for(String errataBP : cards) {
            String cardID = errataBP.split("_")[1];

            addCardErrata("" + ogSet + "_" + cardID, errataBP);
        }
    }
    public void addCardErrata(String baseBlueprintId, String errataBaseBlueprint) {
        _errataCardMap.put(baseBlueprintId, errataBaseBlueprint);
    }

    @Override
    public String validateCard(String blueprintId) {
        blueprintId = _library.getBaseBlueprintId(blueprintId);
        try {
            _library.getLotroCardBlueprint(blueprintId);
            if (_validCards.contains(blueprintId) || _errataCardMap.containsValue(blueprintId))
                return null;

            if (_validSets.size() > 0 && !isValidInSets(blueprintId))
                return "Deck contains card not from valid set: " + GameUtils.getFullName(_library.getLotroCardBlueprint(blueprintId));

            // Banned cards
            Set<String> allAlternates = _library.getAllAlternates(blueprintId);
            for (String bannedBlueprintId : _bannedCards) {
                if (bannedBlueprintId.equals(blueprintId) || (allAlternates != null && allAlternates.contains(bannedBlueprintId)))
                    return "Deck contains a copy of an X-listed card: " + GameUtils.getFullName(_library.getLotroCardBlueprint(bannedBlueprintId));
            }

            // Errata
            for (String originalBlueprintId : _errataCardMap.keySet()) {
                if (originalBlueprintId.equals(blueprintId) || (allAlternates != null && allAlternates.contains(originalBlueprintId)))
                    return "Deck contains non-errata of an errata'd card: " + GameUtils.getFullName(_library.getLotroCardBlueprint(originalBlueprintId));
            }

        } catch (CardNotFoundException e) {
            return null;
        }

        return null;
    }

    private boolean isValidInSets(String blueprintId)  {
        for (int validSet : _validSets)
            if (blueprintId.startsWith(validSet + "_")
                    || _library.hasAlternateInSet(blueprintId, validSet))
                return true;
        return false;
    }

    @Override
    public String validateDeckForHall(LotroDeck deck) {
        List<String> validations = validateDeck(deck);
        if(validations.size() == 0)
            return "";

        String firstValidation = validations.stream().findFirst().get();
        long count = firstValidation.chars().filter(x -> x == '\n').count();
        if(firstValidation.contains("\n"))
        {
            firstValidation = firstValidation.substring(0, firstValidation.indexOf("\n"));
        }

        return "Deck targets '" + deck.getTargetFormat() + "' format and is incompatible with '" + _name + "'.  Issues include: `"
                + firstValidation + "` and " + (validations.size() - 1 + count - 1) + " other issues.";
    }

    @Override
    public List<String> validateDeck(LotroDeck deck) {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> errataResult = new ArrayList<>();
        String valid = null;

        // Deck
        valid = validateDeckStructure(deck);
        if(valid != null && !valid.isEmpty()) {
            result.add(valid);
        }

        String prevLine = "";
        String newLine = "";
        for (String card : deck.getDrawDeckCards()){
            newLine = validateCard(card);
            if(newLine == null || newLine.isEmpty())
                continue;

            if(!newLine.equalsIgnoreCase(prevLine))
            {
                if(newLine.toLowerCase().contains("errata"))
                {
                    errataResult.add(newLine);
                }
                else
                {
                    result.add(newLine);
                }
            }
            else
            {
                prevLine = newLine;
            }
        }

        // Sites
        valid = validateSitesStructure(deck);
        if(valid != null && !valid.isEmpty()) {
            result.add(valid);
        }


        // Card count in deck and Ring-bearer
        Map<String, Integer> cardCountByName = new HashMap<>();
        Map<String, Integer> cardCountByBaseBlueprintId = new HashMap<>();

        if (deck.getRing() != null) {
            processCardCounts(deck.getRing(), cardCountByName, cardCountByBaseBlueprintId);
        }
        if (deck.getRingBearer() != null) {
            processCardCounts(deck.getRingBearer(), cardCountByName, cardCountByBaseBlueprintId);
        }
        for (String blueprintId : deck.getDrawDeckCards())
            processCardCounts(blueprintId, cardCountByName, cardCountByBaseBlueprintId);
        for (String blueprintId : deck.getSites())
            processCardCounts(blueprintId, cardCountByName, cardCountByBaseBlueprintId);

        for (Map.Entry<String, Integer> count : cardCountByName.entrySet()) {
            if (count.getValue() > _maximumSameName) {
                result.add("Deck contains more of the same card than allowed - " + count.getKey() + " (" + count.getValue() + ">" + _maximumSameName + "): " + count.getKey());
            }
        }

        try {
            // Restricted cards
            for (String blueprintId : _restrictedCards) {
                Integer count = cardCountByBaseBlueprintId.get(blueprintId);
                if (count != null && count > 1) {
                    result.add("Deck contains more than one copy of an R-listed card: " + GameUtils.getFullName(_library.getLotroCardBlueprint(blueprintId)));
                }
            }

            // New Hobbit Draft restrictions
            for (String blueprintId : _limit2Cards) {
                Integer count = cardCountByBaseBlueprintId.get(blueprintId);
                if (count != null && count > 2)
                    result.add("Deck contains more than two copies of a 2x limited card: " + GameUtils.getFullName(_library.getLotroCardBlueprint(blueprintId)));
            }
            for (String blueprintId : _limit3Cards) {
                Integer count = cardCountByBaseBlueprintId.get(blueprintId);
                if (count != null && count > 3)
                    result.add("Deck contains more than three copies of a 3x limited card: " + GameUtils.getFullName(_library.getLotroCardBlueprint(blueprintId)));
            }
        }
        catch(CardNotFoundException ex)
        {
            //By this point all the cards in the deck have been pulled from the blueprint library multiple times, and
            // adding the error to the list is just going to add more spam for no reason.
        }

        for (String restrictedCardName : _restrictedCardNames) {
            Integer count = cardCountByName.get(restrictedCardName);
            if (count != null && count > 1)
                result.add("Deck contains more than one copy of a card restricted by name: " + restrictedCardName);
        }

        result.addAll(errataResult);

        return result.stream()
                .filter(x -> x != null && !x.isEmpty())
                .collect(Collectors.toList());

    }

    @Override
    public LotroDeck applyErrata(LotroDeck deck) {
        LotroDeck deckWithErrata = new LotroDeck(deck.getDeckName());
        deckWithErrata.setTargetFormat(deck.getTargetFormat());
        if (deck.getRingBearer() != null) {
            deckWithErrata.setRingBearer(applyErrata(deck.getRingBearer()));
        }
        if (deck.getRing() != null) {
            deckWithErrata.setRing(applyErrata(deck.getRing()));
        }
        for (String card : deck.getDrawDeckCards()) {
            deckWithErrata.addCard(applyErrata(card));
        }
        for (String site : deck.getSites()) {
            deckWithErrata.addSite(applyErrata(site));
        }
        return deckWithErrata;
    }

    @Override
    public String applyErrata(String original) {
        var base = _library.getBaseBlueprintId(original);
        var errata = _errataCardMap.getOrDefault(base, base);
        if (original.endsWith("*") && !errata.endsWith("*")) {
            errata += "*";
        }

        if(errata.equals(base))
            return original;

        return errata;
    }

    @Override
    public List<String> findBaseCards(String bpID) {
        return _errataCardMap
                .entrySet().stream()
                .filter(x-> x.getValue().equals(bpID))
                .map(x -> x.getKey())
                .toList();
    }

    private String validateDeckStructure(LotroDeck deck) {
        String result = "";
        if (deck.getDrawDeckCards().size() < _minimumDeckSize) {
            result += "Deck contains below minimum number of cards: " + deck.getDrawDeckCards().size() + "<" + _minimumDeckSize + ".\n";
        }
        if (_validateShadowFPCount) {
            int shadow = 0;
            int fp = 0;
            for (String blueprintId : deck.getDrawDeckCards()) {
                try {
                    LotroCardBlueprint card = _library.getLotroCardBlueprint(blueprintId);
                    if (card.getSide() == Side.SHADOW)
                        shadow++;
                    else if (card.getSide() == Side.FREE_PEOPLE)
                        fp++;
                    else
                        result += "Deck contains non-Shadow, non-Free-Peoples card: " + GameUtils.getFullName(card) + ".\n";
                }
                catch(CardNotFoundException exception)
                {
                    result += CardRemovedError + ": " + blueprintId + ".\n";
                }
            }
            if (fp != shadow) {
                result += "Deck contains different number of Shadow and Free peoples cards.\n";
            }
        }

        return result;
    }

    private String validateSitesStructure(LotroDeck deck)  {
        String result = "";
        if (isOrderedSites()) {
            boolean[] sites = new boolean[9];
            for (String site : deck.getSites()) {
                try {
                    LotroCardBlueprint blueprint = _library.getLotroCardBlueprint(site);
                    if(blueprint.getSiteNumber() == 0)
                        continue; // a shadows site which will already have tripped a validation
                    if (sites[blueprint.getSiteNumber() - 1]) {
                        result += "Deck has multiple of the same site number: " + blueprint.getSiteNumber() + "\n";
                    }
                    sites[blueprint.getSiteNumber() - 1] = true;
                }
                catch(CardNotFoundException exception)
                {
                    result += CardRemovedError + ": " + site + "\n";
                }
            }
        } else {
            Set<LotroCardBlueprint> siteBlueprints = new HashSet<>();

            List<String> sites = deck.getSites();
            int size = sites.size();
            for (String site : sites) {
                try {
                    siteBlueprints.add(_library.getLotroCardBlueprint(site));
                }
                catch(CardNotFoundException ex)
                {
                    result += CardRemovedError + ": " + site + "\n";
                }
            }

            if (siteBlueprints.size() < size) {
                result += "Deck contains multiple of the same site.\n";
            }

            Map<Integer, Integer> twilightCount = new HashMap<>();
            for (LotroCardBlueprint siteBlueprint : siteBlueprints) {
                int twilight = siteBlueprint.getTwilightCost();
                Integer count = twilightCount.get(twilight);
                if (count == null)
                    count = 0;
                twilightCount.put(twilight, count + 1);
            }

            for (Map.Entry<Integer, Integer> twilightCountEntry : twilightCount.entrySet()) {
                if (twilightCountEntry.getValue() > 3) {
                    result += "Deck contains " + twilightCountEntry.getValue() + " sites with twilight number of " + twilightCountEntry.getKey() + ".\n";
                }
            }
        }

        return result;
    }

    private void processCardCounts(String blueprintId, Map<String, Integer> cardCountByName, Map<String, Integer> cardCountByBaseBlueprintId)  {
        try {
            LotroCardBlueprint cardBlueprint = _library.getLotroCardBlueprint(blueprintId);
            increaseCount(cardCountByName, cardBlueprint.getTitle());
            increaseCount(cardCountByBaseBlueprintId, _library.getBaseBlueprintId(blueprintId));
        }
        catch(CardNotFoundException exception)
        {

        }
    }

    private void increaseCount(Map<String, Integer> counts, String name) {
        Integer count = counts.get(name);
        if (count == null) {
            counts.put(name, 1);
        } else {
            counts.put(name, count + 1);
        }
    }

    @Override
    public JSONDefs.Format Serialize() {
        return new JSONDefs.Format() {{
            adventure = null;
            code = _code;
            name = _name;
            order = _order;
            surveyUrl = _surveyUrl;
            sites = _siteBlock.getHumanReadable();
            cancelRingBearerSkirmish = _canCancelRingBearerSkirmish;
            ruleOfFour = _hasRuleOfFour;
            winAtEndOfRegroup = _winAtEndOfRegroup;
            discardPileIsPublic = _discardPileIsPublic;
            winOnControlling5Sites = _winOnControlling5Sites;
            playtest = _isPlaytest;
            validateShadowFPCount = _validateShadowFPCount;
            minimumDeckSize = _minimumDeckSize;
            maximumSameName = _maximumSameName;
            mulliganRule = _mulliganRule;
            set = null;
            banned = null;
            restricted = null;
            valid = null;
            limit2 = null;
            limit3 = null;
            restrictedName = null;
            errataSets = null;
            errata = null;
            hall = _hallVisible;
        }};
    }
}
