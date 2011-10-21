package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ExertResult;

import java.util.Collections;

public class ExhaustCharacterEffect extends AbstractEffect {
    private String _playerId;
    private CostToEffectAction _action;
    private PhysicalCard _physicalCard;
    private boolean _sendMessage;

    public ExhaustCharacterEffect(String playerId, CostToEffectAction action, PhysicalCard physicalCard) {
        this(playerId, action, physicalCard, true);
    }

    private ExhaustCharacterEffect(String playerId, CostToEffectAction action, PhysicalCard physicalCard, boolean sendMessage) {
        _playerId = playerId;
        _action = action;
        _physicalCard = physicalCard;
        _sendMessage = sendMessage;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Exhaust " + _physicalCard.getBlueprint().getName();
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return PlayConditions.canExert(_action.getActionSource(), game.getGameState(), game.getModifiersQuerying(), _physicalCard);
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        boolean canExert = PlayConditions.canExert(_action.getActionSource(), game.getGameState(), game.getModifiersQuerying(), _physicalCard);
        if (canExert) {
            if (_sendMessage)
                game.getGameState().sendMessage(_playerId + " exhausts " + GameUtils.getCardLink(_physicalCard));
            game.getGameState().addWound(_physicalCard);
            _action.appendEffect(new ExhaustCharacterEffect(_playerId, _action, _physicalCard, false));
            return new FullEffectResult(new EffectResult[]{new ExertResult(Collections.singleton(_physicalCard))}, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}