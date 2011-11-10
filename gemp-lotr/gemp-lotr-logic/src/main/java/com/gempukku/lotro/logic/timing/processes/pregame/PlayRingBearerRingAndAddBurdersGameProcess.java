package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

import java.util.Collections;
import java.util.Map;

public class PlayRingBearerRingAndAddBurdersGameProcess implements GameProcess {
    private Map<String, Integer> _bids;
    private String _firstPlayer;
    private GameProcess _nextProcess;

    public PlayRingBearerRingAndAddBurdersGameProcess(Map<String, Integer> bids, String firstPlayer) {
        _bids = bids;
        _firstPlayer = firstPlayer;
    }

    @Override
    public void process(LotroGame game) {
        GameState gameState = game.getGameState();
        for (String playerId : gameState.getPlayerOrder().getAllPlayers()) {
            PhysicalCard ringBearer = Filters.filter(gameState.getDeck(playerId), gameState, game.getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER)).iterator().next();
            gameState.removeCardsFromZone(null, Collections.singleton(ringBearer));
            gameState.addCardToZone(game, ringBearer, Zone.FREE_CHARACTERS);
            gameState.setRingBearer(ringBearer);

            PhysicalCard ring = Filters.filter(gameState.getDeck(playerId), gameState, game.getModifiersQuerying(), Filters.type(CardType.THE_ONE_RING)).iterator().next();
            gameState.removeCardsFromZone(null, Collections.singleton(ring));
            gameState.attachCard(game, ring, ringBearer);

            gameState.startPlayerTurn(playerId);
            gameState.addBurdens(_bids.get(playerId));
        }
        gameState.setCurrentPhase(Phase.PLAY_STARTING_FELLOWSHIP);

        _nextProcess = new PlayStartingFellowshipGameProcess(game.getGameState().getPlayerOrder().getClockwisePlayOrder(_firstPlayer, false), _firstPlayer);
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
