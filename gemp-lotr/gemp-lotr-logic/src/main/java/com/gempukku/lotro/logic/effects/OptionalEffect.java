package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

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
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    public void playEffect(final LotroGame game) {
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
