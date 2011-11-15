package com.gempukku.lotro.logic.timing.processes.pregame;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

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
            PhysicalCard ringBearer = game.getGameState().getRingBearer(playerId);
            gameState.addCardToZone(game, ringBearer, Zone.FREE_CHARACTERS);

            PhysicalCard ring = game.getGameState().getRing(playerId);
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
