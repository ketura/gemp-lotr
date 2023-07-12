package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.SubAction;
import com.gempukku.lotro.game.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.game.actions.Action;

public class OptionalEffect extends AbstractSubActionEffect {
    private final Action _action;
    private final String _playerId;
    private final Effect _optionalEffect;

    public OptionalEffect(Action action, String playerId, Effect optionalEffect) {
        _action = action;
        _playerId = playerId;
        _optionalEffect = optionalEffect;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return true;
    }

    @Override
    public void playEffect(final DefaultGame game) {
        if (_optionalEffect.isPlayableInFull(game)) {
            String text = _optionalEffect.getText(game);
            if(text != null)
                text = text.toLowerCase();
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new MultipleChoiceAwaitingDecision(1, "Do you wish to " + text + "?", new String[]{"Yes", "No"}) {
                        @Override
                        protected void validDecisionMade(int index, String result) {
                            if (index == 0) {
                                SubAction subAction = new SubAction(_action);
                                subAction.appendEffect(_optionalEffect);
                                processSubAction(game, subAction);
                            }
                        }
                    });
        }
    }
}
