package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.DeckInvalidException;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class DefaultLotroFormat implements LotroFormat {
    private LotroCardBlueprintLibrary _library;
    private Block _siteBlock;
    private boolean _validateShadowFPCount = true;
    private int _maximumSameName = 4;
    private boolean _mulliganRule;
    private boolean _canCancelRingBearerSkirmish;
    private int _minimumDeckSize = 60;
    private Set<String> _bannedCards = new HashSet<String>();
    private Set<String> _restrictedCards = new HashSet<String>();
    private Set<Integer> _validSets = new HashSet<Integer>();

    public DefaultLotroFormat(LotroCardBlueprintLibrary library, Block siteBlock, boolean validateShadowFPCount, int minimumDeckSize, int maximumSameName, boolean mulliganRule, boolean canCancelRingBearerSkirmish) {
        _library = library;
        _siteBlock = siteBlock;
        _validateShadowFPCount = validateShadowFPCount;
        _minimumDeckSize = minimumDeckSize;
        _maximumSameName = maximumSameName;
        _mulliganRule = mulliganRule;
        _canCancelRingBearerSkirmish = canCancelRingBearerSkirmish;
    }

    @Override
    public boolean hasMulliganRule() {
        return _mulliganRule;
    }

    @Override
    public boolean canCancelRingBearerSkirmish() {
        return _canCancelRingBearerSkirmish;
    }

    protected void addBannedCard(String baseBlueprintId) {
        _bannedCards.add(baseBlueprintId);
    }

    protected void addRestrictedCard(String baseBlueprintId) {
        _restrictedCards.add(baseBlueprintId);
    }

    protected void addValidSet(int setNo) {
        _validSets.add(setNo);
    }

    private void validateSet(String blueprintId) throws DeckInvalidException {
        for (int validSet : _validSets)
            if (blueprintId.startsWith(validSet + "_")
                    || _library.hasAlternateInSet(blueprintId, validSet))
                return;

        throw new DeckInvalidException("Deck contains card not from valid set: " + _library.getLotroCardBlueprint(blueprintId).getName());
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
                if (siteBlueprint.getSiteBlock() != _siteBlock)
                    throw new DeckInvalidException("Invalid site: " + siteBlueprint.getName());
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
            cardCountByName.put(ringBearer.getName(), 1);
            for (String blueprintId : deck.getAdventureCards()) {
                LotroCardBlueprint cardBlueprint = _library.getLotroCardBlueprint(blueprintId);
                increateCount(cardCountByName, cardBlueprint.getName());
                increateCount(cardCountByBaseBlueprintId, _library.getBaseBlueprintId(blueprintId));
            }
            for (Map.Entry<String, Integer> count : cardCountByName.entrySet()) {
                if (count.getValue() > _maximumSameName)
                    throw new DeckInvalidException("Deck contains more of the same card than allowed: " + count.getKey());
            }

            // Restricted cards
            for (String blueprintId : _restrictedCards) {
                Integer count = cardCountByBaseBlueprintId.get(blueprintId);
                if (count != null && count > 1)
                    throw new DeckInvalidException("Deck contains more than one copy of an R-listed card: " + blueprintId);
            }

            // Banned cards
            for (String blueprintId : _bannedCards) {
                Integer count = cardCountByBaseBlueprintId.get(blueprintId);
                if (count != null && count > 0)
                    throw new DeckInvalidException("Deck contains a copy of an X-listed card: " + blueprintId);
            }

        } catch (IllegalArgumentException exp) {
            throw new DeckInvalidException("Deck contains unrecognizable card");
        }
    }

    private void increateCount(Map<String, Integer> counts, String name) {
        Integer count = counts.get(name);
        if (count == null) {
            counts.put(name, 1);
        } else {
            counts.put(name, count + 1);
        }
    }
}
