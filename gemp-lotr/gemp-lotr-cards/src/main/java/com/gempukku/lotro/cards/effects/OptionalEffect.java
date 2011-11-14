package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;

public class OptionalEffect extends AbstractSubActionEffect {
    private Action _action;
    private String _playerId;
    private Effect _optionalEffect;

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
    public Collection<? extends EffectResult> playEffect(final LotroGame game) {
        game.getUserFeedback().sendAwaitingDecision(_playerId,
                new MultipleChoiceAwaitingDecision(1, "Do you wish to " + _optionalEffect.getText(game) + "?", new String[]{"Yes", "No"}) {
                    @Override
                    protected void validDecisionMade(int index, String result) {
                        if (index == 0) {
                            SubAction subAction = new SubAction(_action);
                            subAction.appendEffect(_optionalEffect);
                            processSubAction(game, subAction);
                        }
                    }
                });
        return null;
    }
}
