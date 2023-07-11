package com.gempukku.lotro.game.effects.discount;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.effects.DiscountEffect;
import com.gempukku.lotro.game.effects.RemoveThreatsEffect;
import com.gempukku.lotro.game.actions.SubAction;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.game.timing.AbstractSubActionEffect;
import com.gempukku.lotro.game.actions.Action;

public class RemoveThreatsToDiscountEffect extends AbstractSubActionEffect implements DiscountEffect {
    private int _minimalThreatsToRemove;
    private int _threatsRemoved;

    private final Action _action;

    public RemoveThreatsToDiscountEffect(Action action) {
        _action = action;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public int getMaximumPossibleDiscount(DefaultGame game) {
        return game.getGameState().getThreats();
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return game.getGameState().getThreats() >= _minimalThreatsToRemove;
    }

    @Override
    public void setMinimalRequiredDiscount(int minimalDiscount) {
        _minimalThreatsToRemove = minimalDiscount;
    }

    @Override
    public void playEffect(final DefaultGame game) {
        if (isPlayableInFull(game)) {
            int maxRemovableThreats = game.getGameState().getThreats();
            if (_minimalThreatsToRemove < maxRemovableThreats) {
                game.getUserFeedback().sendAwaitingDecision(
                        _action.getPerformingPlayer(), new IntegerAwaitingDecision(1, "Choose how many threats to remove", _minimalThreatsToRemove, maxRemovableThreats) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        int threats = getValidatedResult(result);
                        if (threats > 0) {
                            SubAction subAction = new SubAction(_action);
                            subAction.appendEffect(
                                    new RemoveThreatsEffect(_action.getActionSource(), threats));
                            processSubAction(game, subAction);
                            _threatsRemoved = threats;
                            discountPaidCallback(threats);
                        }
                    }
                }
                );
            } else {
                SubAction subAction = new SubAction(_action);
                subAction.appendEffect(
                        new RemoveThreatsEffect(_action.getActionSource(), _minimalThreatsToRemove));
                processSubAction(game, subAction);
                _threatsRemoved = _minimalThreatsToRemove;
            }
        }
    }

    @Override
    public int getDiscountPaidFor() {
        return _threatsRemoved;
    }

    protected void discountPaidCallback(int paid) {  }
}
