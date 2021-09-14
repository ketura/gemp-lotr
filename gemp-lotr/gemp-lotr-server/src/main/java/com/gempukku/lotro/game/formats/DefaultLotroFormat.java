package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.Adventure;
import com.gempukku.lotro.game.CardNotFoundException;
import com.gempukku.lotro.game.DeckInvalidException;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.*;

public class DefaultLotroFormat implements LotroFormat {
    private Adventure _adventure;
    private LotroCardBlueprintLibrary _library;
    private String _name;
    private SitesBlock _siteBlock;
    private boolean _validateShadowFPCount = true;
    private int _maximumSameName = 4;
    private boolean _mulliganRule;
    private boolean _canCancelRingBearerSkirmish;
    private boolean _hasRuleOfFour;
    private boolean _winAtEndOfRegroup;
    private boolean _winOnControlling5Sites;
    private int _minimumDeckSize = 60;
    private List<String> _bannedCards = new ArrayList<String>();
    private List<String> _restrictedCards = new ArrayList<String>();
    private List<String> _validCards = new ArrayList<String>();
    private List<Integer> _validSets = new ArrayList<Integer>();
    private List<String> _restrictedCardNames = new ArrayList<String>();
    private String _surveyUrl;
    private boolean _isPlaytest;

    //Additional Hobbit Draft parameters
    private List<String> _limit2Cards = new ArrayList<String>();
    private List<String> _limit3Cards = new ArrayList<String>();
    private Map<String,String> _errataCardMap = new TreeMap<String,String>();

    public DefaultLotroFormat(Adventure adventure,
                              LotroCardBlueprintLibrary library, String name, String surveyUrl,
                              SitesBlock siteBlock,
                              boolean validateShadowFPCount, int minimumDeckSize, int maximumSameName, boolean mulliganRule,
                              boolean canCancelRingBearerSkirmish, boolean hasRuleOfFour, boolean winAtEndOfRegroup, boolean winOnControlling5Sites, boolean playtest) {
        _adventure = adventure;
        _library = library;
        _name = name;
        _surveyUrl = surveyUrl;
        _siteBlock = siteBlock;
        _validateShadowFPCount = validateShadowFPCount;
        _minimumDeckSize = minimumDeckSize;
        _maximumSameName = maximumSameName;
        _mulliganRule = mulliganRule;
        _canCancelRingBearerSkirmish = canCancelRingBearerSkirmish;
        _hasRuleOfFour = hasRuleOfFour;
        _winAtEndOfRegroup = winAtEndOfRegroup;
        _winOnControlling5Sites = winOnControlling5Sites;
        _isPlaytest = playtest;
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
    public boolean hasMulliganRule() {
        return _mulliganRule;
    }

    @Override
    public boolean winWhenShadowReconciles() {
        return _winAtEndOfRegroup;
    }

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
        return 8;
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

    public void addCardErrata(String baseBlueprintId, String errataBaseBlueprint) {
        _errataCardMap.put(baseBlueprintId, errataBaseBlueprint);
    }

    @Override
    public void validateCard(String blueprintId) throws DeckInvalidException {
        blueprintId = _library.getBaseBlueprintId(blueprintId);
        try {
            _library.getLotroCardBlueprint(blueprintId);
            if (_validCards.contains(blueprintId) || _errataCardMap.containsValue(blueprintId))
                return;

            if (_validSets.size() > 0 && !isValidInSets(blueprintId))
                throw new DeckInvalidException("Deck contains card not from valid set: " + GameUtils.getFullName(_library.getLotroCardBlueprint(blueprintId)));

            // Banned cards
            Set<String> allAlternates = _library.getAllAlternates(blueprintId);
            for (String bannedBlueprintId : _bannedCards) {
                if (bannedBlueprintId.equals(blueprintId) || (allAlternates != null && allAlternates.contains(bannedBlueprintId)))
                    throw new DeckInvalidException("Deck contains a copy of an X-listed card: " + GameUtils.getFullName(_library.getLotroCardBlueprint(bannedBlueprintId)));
            }

            // Errata
            for (String originalBlueprintId : _errataCardMap.keySet()) {
                if (originalBlueprintId.equals(blueprintId) || (allAlternates != null && allAlternates.contains(originalBlueprintId)))
                    throw new DeckInvalidException("Deck contains non-errata of an errata'd card: " + GameUtils.getFullName(_library.getLotroCardBlueprint(originalBlueprintId)));
            }

        } catch (CardNotFoundException e) {
            // Ignore this card
        }
    }

    private boolean isValidInSets(String blueprintId) throws DeckInvalidException {
        for (int validSet : _validSets)
            if (blueprintId.startsWith(validSet + "_")
                    || _library.hasAlternateInSet(blueprintId, validSet))
                return true;
        return false;
    }

    @Override
    public void validateDeck(LotroDeck deck) throws DeckInvalidException {
        try {
            // Ring-bearer
            validateRingBearer(deck);

            // Ring
            validateRing(deck);

            // Sites
            validateSites(deck);

            validateCard(deck.getRingBearer());
            if (deck.getRing() != null)
                validateCard(deck.getRing());
            for (String site : deck.getSites())
                validateCard(site);
            for (String card : deck.getAdventureCards())
                validateCard(card);

            validateSitesStructure(deck);

            // Deck
            validateDeckStructure(deck);

            // Card count in deck and Ring-bearer
            Map<String, Integer> cardCountByName = new HashMap<String, Integer>();
            Map<String, Integer> cardCountByBaseBlueprintId = new HashMap<String, Integer>();

            if (deck.getRing() != null)
                processCardCounts(deck.getRing(), cardCountByName, cardCountByBaseBlueprintId);
            processCardCounts(deck.getRingBearer(), cardCountByName, cardCountByBaseBlueprintId);
            for (String blueprintId : deck.getAdventureCards())
                processCardCounts(blueprintId, cardCountByName, cardCountByBaseBlueprintId);
            for (String blueprintId : deck.getSites())
                processCardCounts(blueprintId, cardCountByName, cardCountByBaseBlueprintId);

            for (Map.Entry<String, Integer> count : cardCountByName.entrySet()) {
                if (count.getValue() > _maximumSameName)
                    throw new DeckInvalidException("Deck contains more of the same card than allowed - " + count.getKey() + " (" + count.getValue() + ">" + _maximumSameName + "): " + count.getKey());
            }

            // Restricted cards
            for (String blueprintId : _restrictedCards) {
                Integer count = cardCountByBaseBlueprintId.get(blueprintId);
                if (count != null && count > 1)
                    throw new DeckInvalidException("Deck contains more than one copy of an R-listed card: " + GameUtils.getFullName(_library.getLotroCardBlueprint(blueprintId)));
            }

            // New Hobbit Draft restrictions
            for (String blueprintId : _limit2Cards) {
                Integer count = cardCountByBaseBlueprintId.get(blueprintId);
                if (count != null && count > 2)
                    throw new DeckInvalidException("Deck contains more than two copies of a 2x limited card: " + GameUtils.getFullName(_library.getLotroCardBlueprint(blueprintId)));
            }
            for (String blueprintId : _limit3Cards) {
                Integer count = cardCountByBaseBlueprintId.get(blueprintId);
                if (count != null && count > 3)
                    throw new DeckInvalidException("Deck contains more than three copies of a 3x limited card: " + GameUtils.getFullName(_library.getLotroCardBlueprint(blueprintId)));
            }

            for (String restrictedCardName : _restrictedCardNames) {
                Integer count = cardCountByName.get(restrictedCardName);
                if (count != null && count > 1)
                    throw new DeckInvalidException("Deck contains more than one copy of a card restricted by name: " + restrictedCardName);
            }

        } catch (CardNotFoundException exp) {
            throw new DeckInvalidException("Deck contains card removed from the set");
        } catch (IllegalArgumentException exp) {
            throw new DeckInvalidException("Deck contains unrecognizable card");
        }
    }

    @Override
    public LotroDeck applyErrata(LotroDeck deck) {
        LotroDeck deckWithErrata = new LotroDeck(deck.getDeckName());
        if (deck.getRingBearer() != null) {
            deckWithErrata.setRingBearer(_errataCardMap.getOrDefault(deck.getRingBearer(), deck.getRingBearer()));
        }
        if (deck.getRing() != null) {
            deckWithErrata.setRing(_errataCardMap.getOrDefault(deck.getRing(), deck.getRing()));
        }
        for (String card : deck.getAdventureCards()) {
            deckWithErrata.addCard(_errataCardMap.getOrDefault(card, card));
        }
        for (String site : deck.getSites()) {
            deckWithErrata.addSite(_errataCardMap.getOrDefault(site, site));
        }
        return deckWithErrata;
    }

    private void validateDeckStructure(LotroDeck deck) throws DeckInvalidException, CardNotFoundException {
        if (deck.getAdventureCards().size() < _minimumDeckSize)
            throw new DeckInvalidException("Deck contains below minimum number of cards: " + deck.getAdventureCards().size() + "<" + _minimumDeckSize);
        if (_validateShadowFPCount) {
            int shadow = 0;
            int fp = 0;
            for (String blueprintId : deck.getAdventureCards()) {
                LotroCardBlueprint card = _library.getLotroCardBlueprint(blueprintId);
                if (card.getSide() == Side.SHADOW)
                    shadow++;
                else if (card.getSide() == Side.FREE_PEOPLE)
                    fp++;
                else
                    throw new DeckInvalidException("Deck contains non-shadow, non-free-people card");
            }
            if (fp != shadow)
                throw new DeckInvalidException("Deck contains different number of shadow and free people cards");
        }
    }

    private void validateSitesStructure(LotroDeck deck) throws CardNotFoundException, DeckInvalidException {
        if (isOrderedSites()) {
            boolean[] sites = new boolean[9];
            for (String site : deck.getSites()) {
                LotroCardBlueprint blueprint = _library.getLotroCardBlueprint(site);
                if (sites[blueprint.getSiteNumber() - 1])
                    throw new DeckInvalidException("Deck has multiple of the same site number: " + blueprint.getSiteNumber());
                sites[blueprint.getSiteNumber() - 1] = true;
            }
        } else {
            Set<LotroCardBlueprint> siteBlueprints = new HashSet<LotroCardBlueprint>();
            for (String site : deck.getSites())
                siteBlueprints.add(_library.getLotroCardBlueprint(site));

            if (siteBlueprints.size() < 9)
                throw new DeckInvalidException("Deck contains multiple of the same site");

            Map<Integer, Integer> twilightCount = new HashMap<Integer, Integer>();
            for (LotroCardBlueprint siteBlueprint : siteBlueprints) {
                int twilight = siteBlueprint.getTwilightCost();
                Integer count = twilightCount.get(twilight);
                if (count == null)
                    count = 0;
                twilightCount.put(twilight, count + 1);
            }

            for (Map.Entry<Integer, Integer> twilightCountEntry : twilightCount.entrySet()) {
                if (twilightCountEntry.getValue() > 3)
                    throw new DeckInvalidException("Deck contains " + twilightCountEntry.getValue() + " sites with twilight number of " + twilightCountEntry.getKey());
            }
        }
    }

    private void validateSites(LotroDeck deck) throws DeckInvalidException, CardNotFoundException {
        if (deck.getSites() == null)
            throw new DeckInvalidException("Deck doesn't have sites");
        if (deck.getSites().size() != 9)
            throw new DeckInvalidException("Deck doesn't have nine sites");
        for (String site : deck.getSites()) {
            LotroCardBlueprint siteBlueprint = _library.getLotroCardBlueprint(site);
            if (siteBlueprint.getCardType() != CardType.SITE)
                throw new DeckInvalidException("Card assigned as Site is not really a site");
            if (siteBlueprint.getSiteBlock() != _siteBlock && _siteBlock != SitesBlock.SPECIAL)
                throw new DeckInvalidException("Invalid site: " + GameUtils.getFullName(siteBlueprint));
            if (_siteBlock == SitesBlock.SPECIAL && siteBlueprint.getSiteBlock() == SitesBlock.SHADOWS)
                throw new DeckInvalidException("Invalid site: " + GameUtils.getFullName(siteBlueprint));
        }
        if (_siteBlock == SitesBlock.SPECIAL) {
            SitesBlock usedBlock = null;
            for (String site : deck.getSites()) {
                LotroCardBlueprint siteBlueprint = _library.getLotroCardBlueprint(site);
                SitesBlock block = siteBlueprint.getSiteBlock();
                if (usedBlock == null)
                    usedBlock = block;
                else if (usedBlock != block)
                    throw new DeckInvalidException("All your sites have to be from the same block");
            }
        }
    }

    private void validateRing(LotroDeck deck) throws DeckInvalidException, CardNotFoundException {
        // No Ring needed for Hobbit
        if (_siteBlock == SitesBlock.HOBBIT)
            return;
        if (deck.getRing() == null)
            throw new DeckInvalidException("Deck doesn't have a Ring");
        LotroCardBlueprint ring = _library.getLotroCardBlueprint(deck.getRing());
        if (ring.getCardType() != CardType.THE_ONE_RING)
            throw new DeckInvalidException("Card assigned as Ring is not The One Ring");
    }

    private void validateRingBearer(LotroDeck deck) throws DeckInvalidException, CardNotFoundException {
        if (deck.getRingBearer() == null)
            throw new DeckInvalidException("Deck doesn't have a Ring-bearer");
        LotroCardBlueprint ringBearer = _library.getLotroCardBlueprint(deck.getRingBearer());
        if (!ringBearer.hasKeyword(Keyword.CAN_START_WITH_RING))
            throw new DeckInvalidException("Card assigned as Ring-bearer can not bear the ring");
    }

    private void processCardCounts(String blueprintId, Map<String, Integer> cardCountByName, Map<String, Integer> cardCountByBaseBlueprintId) throws CardNotFoundException {
        LotroCardBlueprint cardBlueprint = _library.getLotroCardBlueprint(blueprintId);
        increaseCount(cardCountByName, cardBlueprint.getTitle());
        increaseCount(cardCountByBaseBlueprintId, _library.getBaseBlueprintId(blueprintId));
    }

    private void increaseCount(Map<String, Integer> counts, String name) {
        Integer count = counts.get(name);
        if (count == null) {
            counts.put(name, 1);
        } else {
            counts.put(name, count + 1);
        }
    }
}
