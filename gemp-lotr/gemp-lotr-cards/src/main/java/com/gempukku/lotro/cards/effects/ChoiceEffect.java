package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

public class ChoiceEffect extends AbstractEffect {
    private Action _action;
    private String _choicePlayerId;
    private List<Effect> _possibleEffects;

    public ChoiceEffect(Action action, String choicePlayerId, List<Effect> possibleEffects) {
        _action = action;
        _choicePlayerId = choicePlayerId;
        _possibleEffects = possibleEffects;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        for (Effect effect : _possibleEffects) {
            if (effect.isPlayableInFull(game))
                return true;
        }
        return false;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final LotroGame game) {
        final List<Effect> possibleEffects = new LinkedList<Effect>();
        for (Effect effect : _possibleEffects) {
            if (effect.isPlayableInFull(game))
                possibleEffects.add(effect);
        }

        if (possibleEffects.size() == 1) {
            SubAction subAction = new SubAction(_action);
            subAction.appendEffect(possibleEffects.get(0));
            game.getActionsEnvironment().addActionToStack(subAction);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_choicePlayerId,
                    new MultipleChoiceAwaitingDecision(1, "Choose effect to use", getEffectsText(possibleEffects, game)) {
                        @Override
                        protected void validDecisionMade(int index, String result) {
                            SubAction subAction = new SubAction(_action);
                            subAction.appendEffect(possibleEffects.get(index));
                            game.getActionsEnvironment().addActionToStack(subAction);
                        }
                    });
        }

        return new FullEffectResult(null, possibleEffects.size() > 0, possibleEffects.size() > 0);
    }

    private String[] getEffectsText(List<Effect> possibleEffects, LotroGame game) {
        String[] result = new String[possibleEffects.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = possibleEffects.get(i).getText(game);
        return result;
    }
}