package com.gempukku.lotro.actions.lotronly;

import com.gempukku.lotro.decisions.YesNoDecision;
import com.gempukku.lotro.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.effects.PreventableEffect;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.Action;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.UnrespondableEffect;

import java.util.Iterator;

public class PreventSubAction extends SubAction {
    private final Effect _effectToExecute;
    private final Iterator<String> _choicePlayers;
    private final PreventableEffect.PreventionCost _preventionCost;
    private final Effect _insteadEffect;

    private Effect _playerPreventionCost;

    public PreventSubAction(Action action, Effect effectToExecute, Iterator<String> choicePlayers, PreventableEffect.PreventionCost preventionCost, Effect insteadEffect) {
        super(action);
        _effectToExecute = effectToExecute;
        _choicePlayers = choicePlayers;
        _preventionCost = preventionCost;
        _insteadEffect = insteadEffect;

        appendEffect(new DecideIfPossible());
    }

    private class DecideIfPossible extends UnrespondableEffect {
        @Override
        protected void doPlayEffect(DefaultGame game) {
            if (_choicePlayers.hasNext()) {
                String nextPlayer = _choicePlayers.next();
                _playerPreventionCost = _preventionCost.createPreventionCostForPlayer(PreventSubAction.this, nextPlayer);
                if (_playerPreventionCost.isPlayableInFull(game)) {
                    appendEffect(
                            new PlayoutDecisionEffect(nextPlayer,
                                    new YesNoDecision("Would you like to - " + _playerPreventionCost.getText(game) + " to prevent - " + _effectToExecute.getText(game)) {
                                        @Override
                                        protected void yes() {
                                            startPrevention();
                                        }

                                        @Override
                                        protected void no() {
                                            appendEffect(new DecideIfPossible());
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
        protected void doPlayEffect(DefaultGame game) {
            if (!_playerPreventionCost.wasCarriedOut())
                appendEffect(new DecideIfPossible());
            else if (_insteadEffect != null)
                appendEffect(_insteadEffect);
        }
    }
}
