package com.gempukku.lotro.league;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.CollectionUtils;
import com.gempukku.lotro.game.DeckInvalidException;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.formats.DefaultLotroFormat;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.Map;

public class LeagueFormat extends DefaultLotroFormat {
    private CardCollection _playerCardCollection;
    private boolean _orderedSites;

    public LeagueFormat(LotroCardBlueprintLibrary library, CardCollection playerCardCollection, boolean orderedSites) {
        super(library, Block.FELLOWSHIP, true, 60, Integer.MAX_VALUE, true, true);
        _playerCardCollection = playerCardCollection;
        _orderedSites = orderedSites;
    }

    @Override
    public void validateDeck(LotroDeck deck) throws DeckInvalidException {
        // First validate the deck is valid at all
        super.validateDeck(deck);

        // Now check if player owns all the cards
        Map<String, Integer> deckCardCounts = CollectionUtils.getTotalCardCountForDeck(deck);
        final Map<String, Integer> collectionCardCounts = _playerCardCollection.getAll();

        for (Map.Entry<String, Integer> cardCount : deckCardCounts.entrySet()) {
            final Integer collectionCount = collectionCardCounts.get(cardCount.getKey());
            if (collectionCount == null || collectionCount < cardCount.getValue())
                throw new DeckInvalidException("You don't have the required cards in collection for this format");
        }
    }

    @Override
    public boolean isOrderedSites() {
        return _orderedSites;
    }
}
