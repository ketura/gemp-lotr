package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.timing.ChooseableEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.LinkedList;
import java.util.List;

public class ChoiceEffect extends UnrespondableEffect {
    private CostToEffectAction _action;
    private String _choicePlayerId;
    private List<ChooseableEffect> _possibleEffects;

    public ChoiceEffect(CostToEffectAction action, String choicePlayerId, List<ChooseableEffect> possibleEffects) {
        _action = action;
        _choicePlayerId = choicePlayerId;
        _possibleEffects = possibleEffects;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        final List<Effect> possibleEffects = new LinkedList<Effect>();
        for (ChooseableEffect effect : _possibleEffects) {
            if (effect.canPlayEffect(game))
                possibleEffects.add(effect);
        }

        if (possibleEffects.size() == 1) {
            _action.appendEffect(possibleEffects.get(0));
        } else {
            game.getUserFeedback().sendAwaitingDecision(_choicePlayerId,
                    new MultipleChoiceAwaitingDecision(1, "Choose effect to use", getEffectsText(possibleEffects, game)) {
                        @Override
                        protected void validDecisionMade(int index, String result) {
                            _action.appendEffect(possibleEffects.get(index));
                        }
                    });
        }
    }

    private String[] getEffectsText(List<Effect> possibleEffects, LotroGame game) {
        String[] result = new String[possibleEffects.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = possibleEffects.get(i).getText(game);
        return result;
    }
}