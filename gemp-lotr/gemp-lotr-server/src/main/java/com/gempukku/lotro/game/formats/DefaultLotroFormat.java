package com.gempukku.lotro.game.formats;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.DeckInvalidException;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
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
    private int _minimumDeckSize = 60;
    private Set<String> _restrictedCard = new HashSet<String>();
    private Set<Integer> _validSets = new HashSet<Integer>();

    public DefaultLotroFormat(LotroCardBlueprintLibrary library, Block siteBlock, boolean validateShadowFPCount, int minimumDeckSize, int maximumSameName) {
        _library = library;
        _siteBlock = siteBlock;
        _validateShadowFPCount = validateShadowFPCount;
        _minimumDeckSize = minimumDeckSize;
        _maximumSameName = maximumSameName;
    }

    protected void addRestrictedCard(String name) {
        _restrictedCard.add(name);
    }

    protected void addValidSet(int setNo) {
        _validSets.add(setNo);
    }

    private void validateSet(String blueprintId) throws DeckInvalidException {
        for (int validSet : _validSets)
            if (blueprintId.startsWith(validSet + "_")
                    || _library.hasAlternateInSet(blueprintId, validSet))
                return;

        throw new DeckInvalidException("Deck contains card not valid for this format: " + _library.getLotroCardBlueprint(blueprintId).getName());
    }

    @Override
    public void validateDeck(LotroDeck deck) throws DeckInvalidException {
        try {
            // Ring-bearer
            if (deck.getRingBearer() == null)
                throw new DeckInvalidException("Deck doesn't have a Ring-bearer");
            LotroCardBlueprint ringBearer = _library.getLotroCardBlueprint(deck.getRingBearer());
            if (!ringBearer.hasKeyword(Keyword.RING_BEARER))
                throw new DeckInvalidException("Assigned Ring-bearer can not bear the ring");

            // Ring
            if (deck.getRing() == null)
                throw new DeckInvalidException("Deck doesn't have a Ring");
            LotroCardBlueprint ring = _library.getLotroCardBlueprint(deck.getRing());
            if (ring.getCardType() != CardType.THE_ONE_RING)
                throw new DeckInvalidException("Assigned Ring is not The One Ring");

            // Sites
            if (deck.getSites() == null)
                throw new DeckInvalidException("Deck doesn't have sites");
            if (deck.getSites().size() != 9)
                throw new DeckInvalidException("Deck doesn't have exactly nine sites");
            for (String site : deck.getSites()) {
                LotroCardBlueprint siteBlueprint = _library.getLotroCardBlueprint(site);
                if (siteBlueprint.getCardType() != CardType.SITE)
                    throw new DeckInvalidException("Assigned Site is not really a site");
                if (siteBlueprint.getSiteBlock() != _siteBlock)
                    throw new DeckInvalidException("One of the sites is from a different block than the format allows");
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
                        throw new DeckInvalidException("Deck has multiple of the same site number");
                    sites[blueprint.getSiteNumber() - 1] = true;
                }
            }

            // Deck
            if (deck.getAdventureCards().size() < _minimumDeckSize)
                throw new DeckInvalidException("Deck contains below minimum number of cards");
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
            cardCountByName.put(ringBearer.getName(), 1);
            for (String blueprintId : deck.getAdventureCards()) {
                LotroCardBlueprint cardBlueprint = _library.getLotroCardBlueprint(blueprintId);
                increateCount(cardCountByName, cardBlueprint.getName());
            }
            for (Map.Entry<String, Integer> count : cardCountByName.entrySet()) {
                if (count.getValue() > _maximumSameName)
                    throw new DeckInvalidException("Deck contains more of the same card than allowed: " + count.getKey());
            }

            // Restricted cards
            for (String name : _restrictedCard) {
                Integer count = cardCountByName.get(name);
                if (count != null && count > 1)
                    throw new DeckInvalidException("Deck contains more than one copy of a restricted card: " + name);
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
