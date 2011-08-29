package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChoiceCostEffect extends UnrespondableEffect {
    private CostToEffectAction _action;
    private String _choicePlayerId;
    private Map<EffectPreCondition, Effect> _preConditions;
    private boolean _addedAsCost;

    public ChoiceCostEffect(CostToEffectAction action, String choicePlayerId, Map<EffectPreCondition, Effect> preConditions, boolean addedAsCost) {
        _action = action;
        _choicePlayerId = choicePlayerId;
        _preConditions = preConditions;
        _addedAsCost = addedAsCost;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        for (EffectPreCondition effectPreCondition : _preConditions.keySet()) {
            boolean result = effectPreCondition.getResult(game);
            if (result)
                return true;
        }

        return false;
    }

    @Override
    public void playEffect(LotroGame game) {
        final List<Effect> possibleEffects = new LinkedList<Effect>();
        for (Map.Entry<EffectPreCondition, Effect> entry : _preConditions.entrySet()) {
            if (entry.getKey().getResult(game))
                possibleEffects.add(entry.getValue());
        }

        if (possibleEffects.size() > 1) {
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