package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.LinkedList;
import java.util.List;

public class ChoiceEffect extends UnrespondableEffect {
    private CostToEffectAction _action;
    private String _choicePlayerId;
    private List<Effect> _possibleEffects;
    private boolean _addedAsCost;

    public ChoiceEffect(CostToEffectAction action, String choicePlayerId, List<Effect> possibleEffects, boolean addedAsCost) {
        _action = action;
        _choicePlayerId = choicePlayerId;
        _possibleEffects = possibleEffects;
        _addedAsCost = addedAsCost;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        for (Effect effect : _possibleEffects) {
            boolean result = effect.canPlayEffect(game);
            if (result)
                return true;
        }

        return false;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        final List<Effect> possibleEffects = new LinkedList<Effect>();
        for (Effect effect : _possibleEffects) {
            if (effect.canPlayEffect(game))
                possibleEffects.add(effect);
        }

        if (possibleEffects.size() == 1) {
            if (_addedAsCost)
                _action.addCost(possibleEffects.get(0));
            else
                _action.addEffect(possibleEffects.get(0));
        } else {
            game.getUserFeedback().sendAwaitingDecision(_choicePlayerId,
                    new MultipleChoiceAwaitingDecision(1, "Choose effect to use", getEffectsText(possibleEffects)) {
                        @Override
                        protected void validDecisionMade(int index, String result) {
                            if (_addedAsCost)
                                _action.addCost(possibleEffects.get(index));
                            else
                                _action.addEffect(possibleEffects.get(index));
                        }
                    });
        }
    }

    private String[] getEffectsText(List<Effect> possibleEffects) {
        String[] result = new String[possibleEffects.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = possibleEffects.get(i).getText();
        return result;
    }
}