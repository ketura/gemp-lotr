package com.gempukku.lotro.logic.timing.processes.turn.move;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.effects.PlayCardEffect;
import com.gempukku.lotro.logic.timing.actions.SimpleEffectAction;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.SimpleTriggeringGameProcess;
import com.gempukku.lotro.logic.timing.results.WhenMoveFromResult;
import com.gempukku.lotro.logic.timing.results.WhenMoveToResult;
import com.gempukku.lotro.logic.timing.results.WhenMovesResult;

import java.util.Collections;

public class PlayerPlaysNextSiteIfNotThereGameProcess implements GameProcess {
    private LotroGame _game;
    private GameProcess _afterMoveGameProcess;

    private GameProcess _nextProcess;

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
            if (nextSiteDirection == LotroCardBlueprint.Direction.LEFT) {
                PlayOrder order = gameState.getPlayerOrder().getClockwisePlayOrder(gameState.getCurrentPlayerId(), false);
                order.getNextPlayer();
                playerToPlaySite = order.getNextPlayer();
            } else {
                PlayOrder order = gameState.getPlayerOrder().getCounterClockwisePlayOrder(gameState.getCurrentPlayerId(), false);
                order.getNextPlayer();
                playerToPlaySite = order.getNextPlayer();
            }

            nextSite = Filters.filter(gameState.getAdventureDeck(playerToPlaySite), gameState, _game.getModifiersQuerying(),
                    Filters.siteNumber(nextSiteNumber)).iterator().next();

            gameState.removeCardsFromZone(Collections.singleton(nextSite));
            gameState.addCardToZone(nextSite, Zone.ADVENTURE_PATH);

            final PhysicalCard site = nextSite;

            _nextProcess =
                    new GameProcess() {
                        @Override
                        public void process() {
                            _game.getActionsEnvironment().addActionToStack(
                                    new SimpleEffectAction(new PlayCardEffect(site), "Plays next site"));
                        }

                        @Override
                        public GameProcess getNextProcess() {
                            return new SimpleTriggeringGameProcess(_game, new WhenMoveFromResult(), "Fellowship moved from",
                                    new SimpleTriggeringGameProcess(_game, new WhenMovesResult(), "Fellowship moves",
                                            new MovePlayerTokenToNextSiteGameProcess(_game,
                                                    new SimpleTriggeringGameProcess(_game, new WhenMoveToResult(), "Fellowship moved to",
                                                            _afterMoveGameProcess))));
                        }
                    };
        } else {
            _nextProcess =
                    new SimpleTriggeringGameProcess(_game, new WhenMoveFromResult(), "Fellowship moved from",
                            new SimpleTriggeringGameProcess(_game, new WhenMovesResult(), "Fellowship moves",
                                    new MovePlayerTokenToNextSiteGameProcess(_game,
                                            new SimpleTriggeringGameProcess(_game, new WhenMoveToResult(), "Fellowship moved to",
                                                    _afterMoveGameProcess))));

        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
