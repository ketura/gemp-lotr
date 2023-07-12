package com.gempukku.lotro.game.timing.processes.lotronly;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.game.effects.AddTwilightEffect;
import com.gempukku.lotro.game.effects.TriggeringResultEffect;
import com.gempukku.lotro.game.effects.Effect;
import com.gempukku.lotro.game.effects.UnrespondableEffect;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.timing.results.WhenMoveFromResult;
import com.gempukku.lotro.game.timing.results.WhenMoveToResult;
import com.gempukku.lotro.game.timing.results.WhenMovesResult;

public class MovementGameProcess implements GameProcess {
    private final GameProcess _afterMovementGameProcess;

    public MovementGameProcess(GameProcess afterMovementGameProcess) {
        _afterMovementGameProcess = afterMovementGameProcess;
    }

    @Override
    public void process(DefaultGame game) {
        PhysicalCard currentSite = game.getGameState().getCurrentSite();
        final SystemQueueAction action = new SystemQueueAction();
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        game.getGameState().setMoving(true);
                    }
                });
        game.getFormat().getAdventure().appendNextSiteAction(action);
        action.appendEffect(
                new TriggeringResultEffect(Effect.Type.BEFORE_MOVE_FROM, new WhenMoveFromResult(currentSite), "Fellowship moved from"));
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        GameState gameState = game.getGameState();
                        gameState.movePlayerToNextSite(game);
                    }
                });
        action.appendEffect(
                new TriggeringResultEffect(Effect.Type.BEFORE_MOVE, new WhenMovesResult(), "Fellowship moves"));
        action.appendEffect(
                new TriggeringResultEffect(Effect.Type.BEFORE_MOVE_TO, new WhenMoveToResult(), "Fellowship moved to"));
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        GameState gameState = game.getGameState();

                        int siteTwilightCost = game.getModifiersQuerying().getTwilightCost(game, gameState.getCurrentSite(), null, 0, false);
                        if (!game.getFormat().isOrderedSites()) {
                            final int siteNumber = gameState.getCurrentSiteNumber();
                            if (siteNumber > 3 && siteNumber <= 6)
                                siteTwilightCost += 3;
                            else if (siteNumber > 6 && siteNumber <= 9)
                                siteTwilightCost += 6;
                        }
                        int companionsAddingTwilightForMoveCount = Filters.countActive(game, CardType.COMPANION,
                                new Filter() {
                                    @Override
                                    public boolean accepts(DefaultGame game, PhysicalCard physicalCard) {
                                        return game.getModifiersQuerying().addsTwilightForCompanionMove(game, physicalCard);
                                    }
                                });

                        AddTwilightEffect effect = new AddTwilightEffect(null, siteTwilightCost + companionsAddingTwilightForMoveCount);
                        effect.setSourceText("Moving");
                        action.insertEffect(effect);
                    }
                });
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        game.getGameState().setMoving(false);
                    }
                });

        game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return _afterMovementGameProcess;
    }
}
