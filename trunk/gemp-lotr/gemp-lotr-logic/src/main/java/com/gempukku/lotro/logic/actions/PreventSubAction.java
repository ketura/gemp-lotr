package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Iterator;
import java.util.LinkedList;

public class PreventSubAction implements Action {
    private Action _action;

    private Effect _effectToExecute;
    private Iterator<String> _choicePlayers;
    private Effect _preventionCost;

    private LinkedList<Effect> _effectQueue = new LinkedList<Effect>();

    public PreventSubAction(Action action, Effect effectToExecute, Iterator<String> choicePlayers, Effect preventionCost) {
        _action = action;
        _effectToExecute = effectToExecute;
        _choicePlayers = choicePlayers;
        _preventionCost = preventionCost;

        _effectQueue.add(new DecideIfPossible());
    }

    @Override
    public PhysicalCard getActionSource() {
        return _action.getActionSource();
    }

    @Override
    public PhysicalCard getActionAttachedToCard() {
        return _action.getActionAttachedToCard();
    }

    @Override
    public Phase getActionTimeword() {
        return _action.getActionTimeword();
    }

    @Override
    public void setActionTimeword(Phase phase) {
        _action.setActionTimeword(phase);
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Effect nextEffect(LotroGame game) {
        return _effectQueue.poll();
    }

    private class DecideIfPossible extends UnrespondableEffect {
        @Override
        protected void doPlayEffect(LotroGame game) {
            if (_preventionCost.isPlayableInFull(game) && _choicePlayers.hasNext()) {
                _effectQueue.add(
                        new PlayoutDecisionEffect(game.getUserFeedback(), _choicePlayers.next(),
                                new MultipleChoiceAwaitingDecision(1, "Would you like to - " + _preventionCost.getText(game) + " to prevent - " + _effectToExecute.getText(game), new String[]{"Yes", "No"}) {
                                    @Override
                                    protected void validDecisionMade(int index, String result) {
                                        if (result.equals("Yes")) {
                                            startPrevention();
                                        } else {
                                            _effectQueue.add(new DecideIfPossible());
                                        }
                                    }
                                }));
            } else {
                _effectQueue.add(_effectToExecute);
            }
        }
    }

    private void startPrevention() {
        _preventionCost.reset();
        _effectQueue.add(_preventionCost);
        _effectQueue.add(new CheckIfPreventingCostWasSuccessful());
    }

    private class CheckIfPreventingCostWasSuccessful extends UnrespondableEffect {
        @Override
        protected void doPlayEffect(LotroGame game) {
            if (!_preventionCost.wasCarriedOut())
                _effectQueue.add(new DecideIfPossible());
        }
    }
}
