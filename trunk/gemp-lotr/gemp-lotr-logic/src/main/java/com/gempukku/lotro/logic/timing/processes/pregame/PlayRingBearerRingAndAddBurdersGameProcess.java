package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.Collections;
import java.util.Map;

public class PlayRingBearerRingAndAddBurdersGameProcess implements GameProcess {
    private LotroGame _game;
    private Map<String, Integer> _bids;
    private String _firstPlayer;

    public PlayRingBearerRingAndAddBurdersGameProcess(LotroGame game, Map<String, Integer> bids, String firstPlayer) {
        _game = game;
        _bids = bids;
        _firstPlayer = firstPlayer;
    }

    @Override
    public void process() {
        GameState gameState = _game.getGameState();
        for (String playerId : gameState.getPlayerOrder().getAllPlayers()) {
            PhysicalCard ringBearer = Filters.filter(gameState.getDeck(playerId), gameState, _game.getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER)).iterator().next();
            gameState.removeCardsFromZone(Collections.singleton(ringBearer));
            gameState.addCardToZone(_game, ringBearer, Zone.FREE_CHARACTERS);
            gameState.setRingBearer(ringBearer);

            PhysicalCard ring = Filters.filter(gameState.getDeck(playerId), gameState, _game.getModifiersQuerying(), Filters.type(CardType.THE_ONE_RING)).iterator().next();
            gameState.removeCardsFromZone(Collections.singleton(ring));
            gameState.attachCard(_game, ring, ringBearer);

            gameState.startPlayerTurn(playerId);
            gameState.addBurdens(_bids.get(playerId));
        }
    }

    @Override
    public GameProcess getNextProcess() {
        GameState gameState = _game.getGameState();
        return new PlayStartingFellowshipGameProcess(_game, gameState.getPlayerOrder().getClockwisePlayOrder(_firstPlayer, false), _firstPlayer);
    }
}
