package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.SubAction;
import com.gempukku.lotro.game.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.game.actions.Action;

import java.util.LinkedList;
import java.util.List;

public class ChoiceEffect extends AbstractSubActionEffect {
    private final Action _action;
    private final String _choicePlayerId;
    private final List<Effect> _possibleEffects;

    public ChoiceEffect(Action action, String choicePlayerId, List<Effect> possibleEffects) {
        _action = action;
        _choicePlayerId = choicePlayerId;
        _possibleEffects = possibleEffects;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        for (Effect effect : _possibleEffects) {
            if (effect.isPlayableInFull(game))
                return true;
        }
        return false;
    }

    @Override
    public void playEffect(final DefaultGame game) {
        final List<Effect> possibleEffects = new LinkedList<>();
        for (Effect effect : _possibleEffects) {
            if (effect.isPlayableInFull(game))
                possibleEffects.add(effect);
        }

        if (possibleEffects.size() == 1) {
            SubAction subAction = new SubAction(_action);
            subAction.appendEffect(possibleEffects.get(0));
            processSubAction(game, subAction);
        } else if (possibleEffects.size() > 0) {
            game.getUserFeedback().sendAwaitingDecision(_choicePlayerId,
                    new MultipleChoiceAwaitingDecision(1, "Choose effect to use", getEffectsText(possibleEffects, game)) {
                        @Override
                        protected void validDecisionMade(int index, String result) {
                            SubAction subAction = new SubAction(_action);
                            subAction.appendEffect(possibleEffects.get(index));
                            processSubAction(game, subAction);
                        }
                    });
        }
    }

    private String[] getEffectsText(List<Effect> possibleEffects, DefaultGame game) {
        String[] result = new String[possibleEffects.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = possibleEffects.get(i).getText(game);
        return result;
    }
}