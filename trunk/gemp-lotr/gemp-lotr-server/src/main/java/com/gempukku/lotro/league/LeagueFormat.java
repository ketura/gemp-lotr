package com.gempukku.lotro.league;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.game.formats.DefaultLotroFormat;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.Map;

public class LeagueFormat extends DefaultLotroFormat {
    private LotroCardBlueprintLibrary _library;
    private LeagueService _leagueService;
    private League _league;
    private boolean _orderedSites;

    public LeagueFormat(LotroCardBlueprintLibrary library, LeagueService leagueService, League league, boolean orderedSites) {
        super(library, Block.FELLOWSHIP, true, 60, Integer.MAX_VALUE, true, true);
        _library = library;
        _leagueService = leagueService;
        _league = league;
        _orderedSites = orderedSites;
    }

    @Override
    public void validateDeck(Player player, LotroDeck deck) throws DeckInvalidException {
        // First validate the deck is valid at all
        super.validateDeck(player, deck);

        CardCollection playerCardCollection = _leagueService.getLeagueCollection(player, _league);

        // Now check if player owns all the cards
        Map<String, Integer> deckCardCounts = CollectionUtils.getTotalCardCountForDeck(deck);
        final Map<String, Integer> collectionCardCounts = playerCardCollection.getAll();

        for (Map.Entry<String, Integer> cardCount : deckCardCounts.entrySet()) {
            final Integer collectionCount = collectionCardCounts.get(cardCount.getKey());
            if (collectionCount == null || collectionCount < cardCount.getValue()) {
                String cardName = _library.getLotroCardBlueprint(cardCount.getKey()).getName();
                int owned = (collectionCount == null) ? 0 : collectionCount;
                throw new DeckInvalidException("You don't have the required cards in collection: " + cardName + " required " + cardCount.getValue() + ", owned " + owned);
            }
        }
    }

    @Override
    public boolean isOrderedSites() {
        return _orderedSites;
    }
}
