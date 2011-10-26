package com.gempukku.lotro.cards.actions;

import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Iterator;

public class PreventSubAction extends SubAction {
    private Effect _effectToExecute;
    private Iterator<String> _choicePlayers;
    private PreventableEffect.PreventionCost _preventionCost;

    private Effect _playerPreventionCost;

    public PreventSubAction(Action action, Effect effectToExecute, Iterator<String> choicePlayers, PreventableEffect.PreventionCost preventionCost) {
        super(action);
        _effectToExecute = effectToExecute;
        _choicePlayers = choicePlayers;
        _preventionCost = preventionCost;

        appendEffect(new DecideIfPossible());
    }

    private class DecideIfPossible extends UnrespondableEffect {
        @Override
        protected void doPlayEffect(LotroGame game) {
            if (_choicePlayers.hasNext()) {
                String nextPlayer = _choicePlayers.next();
                _playerPreventionCost = _preventionCost.createPreventionCostForPlayer(PreventSubAction.this, nextPlayer);
                if (_playerPreventionCost.isPlayableInFull(game)) {
                    appendEffect(
                            new PlayoutDecisionEffect(game.getUserFeedback(), nextPlayer,
                                    new MultipleChoiceAwaitingDecision(1, "Would you like to - " + _playerPreventionCost.getText(game) + " to prevent - " + _effectToExecute.getText(game), new String[]{"Yes", "No"}) {
                                        @Override
                                        protected void validDecisionMade(int index, String result) {
                                            if (result.equals("Yes")) {
                                                startPrevention();
                                            } else {
                                                appendEffect(new DecideIfPossible());
                                            }
                                        }
                                    }));
                } else {
                    appendEffect(new DecideIfPossible());
                }
            } else {
                appendEffect(_effectToExecute);
            }
        }
    }

    private void startPrevention() {
        appendEffect(_playerPreventionCost);
        appendEffect(new CheckIfPreventingCostWasSuccessful());
    }

    private class CheckIfPreventingCostWasSuccessful extends UnrespondableEffect {
        @Override
        protected void doPlayEffect(LotroGame game) {
            if (!_playerPreventionCost.wasCarriedOut())
                appendEffect(new DecideIfPossible());
        }
    }
}
