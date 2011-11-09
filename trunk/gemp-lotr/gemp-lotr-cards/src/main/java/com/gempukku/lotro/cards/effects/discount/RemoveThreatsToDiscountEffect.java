package com.gempukku.lotro.cards.effects.discount;

import com.gempukku.lotro.cards.effects.DiscountEffect;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;

public class RemoveThreatsToDiscountEffect extends AbstractSubActionEffect implements DiscountEffect {
    private int _minimalThreatsToRemove;
    private int _threatsRemoved;

    private Action _action;

    public RemoveThreatsToDiscountEffect(Action action) {
        _action = action;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getThreats() >= _minimalThreatsToRemove;
    }

    @Override
    public void setMinimalRequiredDiscount(int minimalDiscount) {
        _minimalThreatsToRemove = minimalDiscount;
    }

    @Override
    public Collection<? extends EffectResult> playEffect(final LotroGame game) {
        if (isPlayableInFull(game)) {
            int maxRemovableThreats = game.getGameState().getThreats();
            if (_minimalThreatsToRemove < maxRemovableThreats) {
                game.getUserFeedback().sendAwaitingDecision(
                        _action.getPerformingPlayer(), new IntegerAwaitingDecision(1, "Choose how many threats to remove", _minimalThreatsToRemove, maxRemovableThreats) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int threats = getValidatedResult(result);
                                SubAction subAction = new SubAction(_action);
                                subAction.appendEffect(
                                        new RemoveThreatsEffect(_action.getActionSource(), threats));
                                processSubAction(game, subAction);
                                _threatsRemoved = _minimalThreatsToRemove;
                            }
                        }
                );
                return null;
            } else {
                SubAction subAction = new SubAction(_action);
                subAction.appendEffect(
                        new RemoveThreatsEffect(_action.getActionSource(), _minimalThreatsToRemove));
                processSubAction(game, subAction);
                _threatsRemoved = _minimalThreatsToRemove;
                return null;
            }
        }
        return null;
    }

    @Override
    public int getDiscountPaidFor() {
        return _threatsRemoved;
    }
}
