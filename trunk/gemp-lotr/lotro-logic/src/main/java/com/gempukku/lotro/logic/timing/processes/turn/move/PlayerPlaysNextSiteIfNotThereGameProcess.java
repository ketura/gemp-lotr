package com.gempukku.lotro.logic.timing.processes.turn.move;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.SimpleTriggeringGameProcess;
import com.gempukku.lotro.logic.timing.results.WhenMoveFromResult;
import com.gempukku.lotro.logic.timing.results.WhenMoveToResult;
import com.gempukku.lotro.logic.timing.results.WhenMovesResult;

public class PlayerPlaysNextSiteIfNotThereGameProcess implements GameProcess {
    private LotroGame _game;
    private GameProcess _afterMoveGameProcess;

    public PlayerPlaysNextSiteIfNotThereGameProcess(LotroGame game, GameProcess afterMoveGameProcess) {
        _game = game;
        _afterMoveGameProcess = afterMoveGameProcess;
    }

    @Override
    public void process() {
        GameState gameState = _game.getGameState();

        final int nextSiteNumber = gameState.getCurrentSiteNumber() + 1;
        PhysicalCard nextSite = gameState.getSite(nextSiteNumber);

        if (nextSite == null) {
            LotroCardBlueprint.Direction nextSiteDirection = gameState.getCurrentSite().getBlueprint().getSiteDirection();
            String playerToPlaySite;
            if (nextSiteDirection == LotroCardBlueprint.Direction.LEFT)
                playerToPlaySite = gameState.getPlayerOrder().getClockwisePlayOrder(gameState.getCurrentPlayerId(), false).getNextPlayer();
            else
                playerToPlaySite = gameState.getPlayerOrder().getCounterClockwisePlayOrder(gameState.getCurrentPlayerId(), false).getNextPlayer();

            nextSite = Filters.filter(gameState.getAdventureDeck(playerToPlaySite), gameState, _game.getModifiersQuerying(),
                    Filters.siteNumber(nextSiteNumber)).get(0);

            gameState.removeCardFromZone(nextSite);
            gameState.addCardToZone(nextSite, Zone.ADVENTURE_PATH);
            gameState.startAffecting(nextSite, _game.getModifiersEnvironment());
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return new SimpleTriggeringGameProcess(_game, new WhenMoveFromResult(), "Fellowship moved from",
                new SimpleTriggeringGameProcess(_game, new WhenMovesResult(), "Fellowship moves",
                        new MovePlayerTokenToNextSiteGameProcess(_game,
                                new SimpleTriggeringGameProcess(_game, new WhenMoveToResult(), "Fellowship moved to",
                                        _afterMoveGameProcess))));
    }
}
