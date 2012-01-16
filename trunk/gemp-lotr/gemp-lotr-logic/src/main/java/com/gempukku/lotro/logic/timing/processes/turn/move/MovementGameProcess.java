package com.gempukku.lotro.logic.timing.processes.turn.move;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.results.WhenMoveFromResult;
import com.gempukku.lotro.logic.timing.results.WhenMoveToResult;
import com.gempukku.lotro.logic.timing.results.WhenMovesResult;

public class MovementGameProcess implements GameProcess {
    private GameProcess _afterMovementGameProcess;

    public MovementGameProcess(GameProcess afterMovementGameProcess) {
        _afterMovementGameProcess = afterMovementGameProcess;
    }

    @Override
    public void process(LotroGame game) {
        PhysicalCard currentSite = game.getGameState().getCurrentSite();
        final SystemQueueAction action = new SystemQueueAction();
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        GameState gameState = game.getGameState();

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

                            action.insertEffect(
                                    new PlaySiteEffect(action, playerToPlaySite, null, nextSiteNumber));
                        }
                    }
                });
        action.appendEffect(
                new TriggeringResultEffect(new WhenMoveFromResult(currentSite), "Fellowship moved from"));
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        GameState gameState = game.getGameState();
                        gameState.movePlayerToNextSite(game);
                    }
                });
        action.appendEffect(
                new TriggeringResultEffect(new WhenMovesResult(), "Fellowship moves"));
        action.appendEffect(
                new TriggeringResultEffect(new WhenMoveToResult(), "Fellowship moved to"));
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        GameState gameState = game.getGameState();

                        int siteTwilightCost = game.getModifiersQuerying().getTwilightCost(gameState, gameState.getCurrentSite(), false);
                        if (!game.getFormat().isOrderedSites()) {
                            final int siteNumber = gameState.getCurrentSiteNumber();
                            if (siteNumber > 3 && siteNumber <= 6)
                                siteTwilightCost += 3;
                            else if (siteNumber > 6 && siteNumber <= 9)
                                siteTwilightCost += 6;
                        }
                        int companionCount = Filters.countActive(gameState, game.getModifiersQuerying(), CardType.COMPANION);

                        AddTwilightEffect effect = new AddTwilightEffect(null, siteTwilightCost + companionCount);
                        effect.setSourceText("Moving");
                        action.insertEffect(effect);
                    }
                });

        game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return _afterMovementGameProcess;
    }
}
