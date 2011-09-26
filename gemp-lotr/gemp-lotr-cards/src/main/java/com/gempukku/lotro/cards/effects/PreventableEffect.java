package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.timing.ChooseableCost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Iterator;
import java.util.List;

public class PreventableEffect implements Effect {
    private CostToEffectAction _action;
    private Effect _effectToExecute;
    private Iterator<String> _choicePlayers;
    private ChooseableCost _preventionCost;

    public PreventableEffect(CostToEffectAction action, Effect effectToExecute, List<String> choicePlayers, ChooseableCost preventionCost) {
        _action = action;
        _effectToExecute = effectToExecute;
        _choicePlayers = choicePlayers.iterator();
        _preventionCost = preventionCost;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public EffectResult[] playEffect(final LotroGame game) {
        sendDecisionToNextPlayerOrExecute(game);
        return null;
    }

    private void sendDecisionToNextPlayerOrExecute(LotroGame game) {
        if (_choicePlayers.hasNext() && _preventionCost.canPlayCost(game)) {
            sendDecisionToNextPlayer(game);
        } else {
            _action.insertEffect(_effectToExecute);
        }
    }

    private void sendDecisionToNextPlayer(final LotroGame game) {
        game.getUserFeedback().sendAwaitingDecision(_choicePlayers.next(),
                new MultipleChoiceAwaitingDecision(1, "Would you like to " + _preventionCost.getText(game) + " to prevent the effect - " + _effectToExecute.getText(game) + "?", new String[]{"Yes", "No"}) {
                    @Override
                    protected void validDecisionMade(int index, String result) {
                        if (result.equals("Yes")) {
                            _action.insertEffect(new PreventionAttempt());
                        } else {
                            _action.insertEffect(new PreventDecision());
                        }
                    }
                });
    }

    private class PreventDecision implements Effect {
        @Override
        public String getText(LotroGame game) {
            return null;
        }

        @Override
        public EffectResult.Type getType() {
            return null;
        }

        @Override
        public EffectResult[] playEffect(LotroGame game) {
            sendDecisionToNextPlayerOrExecute(game);
            return null;
        }
    }

    private class PreventionAttempt implements Effect {
        @Override
        public String getText(LotroGame game) {
            return _preventionCost.getText(game);
        }

        @Override
        public EffectResult.Type getType() {
            return _preventionCost.getType();
        }

        @Override
        public EffectResult[] playEffect(LotroGame game) {
            CostResolution costResolution = _preventionCost.playCost(game);
            if (!costResolution.isSuccessful())
                _action.insertEffect(new PreventDecision());
            return costResolution.getEffectResults();
        }
    }
}
