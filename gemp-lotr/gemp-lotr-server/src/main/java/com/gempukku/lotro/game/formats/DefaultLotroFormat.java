package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.DeckInvalidException;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.*;

public class DefaultLotroFormat implements LotroFormat {
    private LotroCardBlueprintLibrary _library;
    private String _name;
    private Block _siteBlock;
    private boolean _validateShadowFPCount = true;
    private int _maximumSameName = 4;
    private boolean _mulliganRule;
    private boolean _canCancelRingBearerSkirmish;
    private int _minimumDeckSize = 60;
    private List<String> _bannedCards = new ArrayList<String>();
    private List<String> _restrictedCards = new ArrayList<String>();
    private List<String> _validCards = new ArrayList<String>();
    private List<Integer> _validSets = new ArrayList<Integer>();

    public DefaultLotroFormat(LotroCardBlueprintLibrary library, String name, Block siteBlock, boolean validateShadowFPCount, int minimumDeckSize, int maximumSameName, boolean mulliganRule, boolean canCancelRingBearerSkirmish) {
        _library = library;
        _name = name;
        _siteBlock = siteBlock;
        _validateShadowFPCount = validateShadowFPCount;
        _minimumDeckSize = minimumDeckSize;
        _maximumSameName = maximumSameName;
        _mulliganRule = mulliganRule;
        _canCancelRingBearerSkirmish = canCancelRingBearerSkirmish;
    }

    @Override
    public final boolean isOrderedSites() {
        return _siteBlock != Block.SHADOWS;
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
    public boolean canCancelRingBearerSkirmish() {
        return _canCancelRingBearerSkirmish;
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
    public List<String> getValidCards() {
        return Collections.unmodifiableList(_validCards);
    }

    @Override
    public Block getSiteBlock() {
        return _siteBlock;
    }

    protected void addBannedCard(String baseBlueprintId) {
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

    protected void addRestrictedCard(String baseBlueprintId) {
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

    protected void addValidCard(String baseBlueprintId) {
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

    protected void addValidSet(int setNo) {
        _validSets.add(setNo);
    }

    private void validateSet(String blueprintId) throws DeckInvalidException {
        blueprintId = _library.getBaseBlueprintId(blueprintId);
        if (_validCards.contains(blueprintId))
            return;
        for (int validSet : _validSets)
            if (blueprintId.startsWith(validSet + "_")
                    || _library.hasAlternateInSet(blueprintId, validSet))
                return;

        throw new DeckInvalidException("Deck contains card not from valid set: " + GameUtils.getFullName(_library.getLotroCardBlueprint(blueprintId)));
    }

    @Override
    public void validateDeck(LotroDeck deck) throws DeckInvalidException {
        try {
            // Ring-bearer
            if (deck.getRingBearer() == null)
                throw new DeckInvalidException("Deck doesn't have a Ring-bearer");
            LotroCardBlueprint ringBearer = _library.getLotroCardBlueprint(deck.getRingBearer());
            if (!ringBearer.hasKeyword(Keyword.CAN_START_WITH_RING))
                throw new DeckInvalidException("Card assigned as Ring-bearer can not bear the ring");

            // Ring
            if (deck.getRing() == null)
                throw new DeckInvalidException("Deck doesn't have a Ring");
            LotroCardBlueprint ring = _library.getLotroCardBlueprint(deck.getRing());
            if (ring.getCardType() != CardType.THE_ONE_RING)
                throw new DeckInvalidException("Card assigned as Ring is not The One Ring");

            // Sites
            if (deck.getSites() == null)
                throw new DeckInvalidException("Deck doesn't have sites");
            if (deck.getSites().size() != 9)
                throw new DeckInvalidException("Deck doesn't have nine sites");
            for (String site : deck.getSites()) {
                LotroCardBlueprint siteBlueprint = _library.getLotroCardBlueprint(site);
                if (siteBlueprint.getCardType() != CardType.SITE)
                    throw new DeckInvalidException("Card assigned as Site is not really a site");
                if (siteBlueprint.getSiteBlock() != _siteBlock && _siteBlock != Block.SPECIAL)
                    throw new DeckInvalidException("Invalid site: " + GameUtils.getFullName(siteBlueprint));
                if (_siteBlock == Block.SPECIAL && siteBlueprint.getSiteBlock() == Block.SHADOWS)
                    throw new DeckInvalidException("Invalid site: " + GameUtils.getFullName(siteBlueprint));
            }
            if (_siteBlock == Block.SPECIAL) {
                Block usedBlock = null;
                for (String site : deck.getSites()) {
                    LotroCardBlueprint siteBlueprint = _library.getLotroCardBlueprint(site);
                    Block block = siteBlueprint.getSiteBlock();
                    if (usedBlock == null)
                        usedBlock = block;
                    else if (usedBlock != block)
                        throw new DeckInvalidException("All your sites have to be from the same block");
                }
            }

            if (_validSets.size() > 0) {
                validateSet(deck.getRingBearer());
                validateSet(deck.getRing());
                for (String site : deck.getSites())
                    validateSet(site);
                for (String card : deck.getAdventureCards())
                    validateSet(card);
            }

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

            // Deck
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

            // Card count in deck and Ring-bearer
            Map<String, Integer> cardCountByName = new HashMap<String, Integer>();
            Map<String, Integer> cardCountByBaseBlueprintId = new HashMap<String, Integer>();

            processCardCounts(deck.getRing(), cardCountByName, cardCountByBaseBlueprintId);
            processCardCounts(deck.getRingBearer(), cardCountByName, cardCountByBaseBlueprintId);
            for (String blueprintId : deck.getAdventureCards())
                processCardCounts(blueprintId, cardCountByName, cardCountByBaseBlueprintId);
            for (String blueprintId : deck.getSites())
                processCardCounts(blueprintId, cardCountByName, cardCountByBaseBlueprintId);

            for (Map.Entry<String, Integer> count : cardCountByName.entrySet()) {
                if (count.getValue() > _maximumSameName)
                    throw new DeckInvalidException("Deck contains more of the same card than allowed: " + count.getKey());
            }

            // Restricted cards
            for (String blueprintId : _restrictedCards) {
                Integer count = cardCountByBaseBlueprintId.get(blueprintId);
                if (count != null && count > 1)
                    throw new DeckInvalidException("Deck contains more than one copy of an R-listed card: " + GameUtils.getFullName(_library.getLotroCardBlueprint(blueprintId)));
            }

            // Banned cards
            for (String blueprintId : _bannedCards) {
                Integer count = cardCountByBaseBlueprintId.get(blueprintId);
                if (count != null && count > 0)
                    throw new DeckInvalidException("Deck contains a copy of an X-listed card: " + GameUtils.getFullName(_library.getLotroCardBlueprint(blueprintId)));
            }

        } catch (IllegalArgumentException exp) {
            throw new DeckInvalidException("Deck contains unrecognizable card");
        }
    }

    private void processCardCounts(String blueprintId, Map<String, Integer> cardCountByName, Map<String, Integer> cardCountByBaseBlueprintId) {
        LotroCardBlueprint cardBlueprint = _library.getLotroCardBlueprint(blueprintId);
        increaseCount(cardCountByName, cardBlueprint.getName());
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
