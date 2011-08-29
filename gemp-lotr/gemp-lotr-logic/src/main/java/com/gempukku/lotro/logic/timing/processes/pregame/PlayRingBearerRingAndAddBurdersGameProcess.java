package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.Map;

public class PlayRingBearerRingAndAddBurdersGameProcess implements GameProcess {
    private LotroGame _game;
    private Map<String, Integer> _bids;

    public PlayRingBearerRingAndAddBurdersGameProcess(LotroGame game, Map<String, Integer> bids) {
        _game = game;
        _bids = bids;
    }

    @Override
    public void process() {
        GameState gameState = _game.getGameState();
        for (String playerId : gameState.getPlayerOrder().getAllPlayers()) {
            PhysicalCard ringBearer = Filters.filter(gameState.getDeck(playerId), gameState, _game.getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER)).get(0);
            gameState.removeCardFromZone(ringBearer);
            gameState.addCardToZone(ringBearer, Zone.FREE_CHARACTERS);
            gameState.setRingBearer(ringBearer);

            PhysicalCard ring = Filters.filter(gameState.getDeck(playerId), gameState, _game.getModifiersQuerying(), Filters.type(CardType.THE_ONE_RING)).get(0);
            gameState.attachCard(ring, ringBearer);

            gameState.addBurdens(playerId, _bids.get(playerId));
        }
    }

    @Override
    public GameProcess getNextProcess() {
        GameState gameState = _game.getGameState();
        return new PlayStartingFellowshipGameProcess(_game, gameState.getPlayerOrder().getClockwisePlayOrder(gameState.getCurrentPlayerId(), false));
    }
}
