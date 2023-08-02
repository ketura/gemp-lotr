package com.gempukku.lotro.processes.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.effects.AddTwilightEffect;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.effects.TriggeringResultEffect;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.UnrespondableEffect;
import com.gempukku.lotro.processes.GameProcess;
import com.gempukku.lotro.effects.results.WhenMoveFromResult;
import com.gempukku.lotro.effects.results.WhenMoveToResult;
import com.gempukku.lotro.effects.results.WhenMovesResult;

public class MovementGameProcess implements GameProcess {
    private final GameProcess _afterMovementGameProcess;

    public MovementGameProcess(GameProcess afterMovementGameProcess) {
        _afterMovementGameProcess = afterMovementGameProcess;
    }

    @Override
    public void process(DefaultGame game) {
        LotroPhysicalCard currentSite = game.getGameState().getCurrentSite();
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
                                    public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
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
