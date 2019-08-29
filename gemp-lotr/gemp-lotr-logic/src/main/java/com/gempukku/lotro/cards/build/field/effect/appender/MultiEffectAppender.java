package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;

public class MultiEffectAppender implements EffectAppender {
    private List<EffectAppender> effectAppenders = new LinkedList<>();

    public void addEffectAppender(EffectAppender effectAppender) {
        effectAppenders.add(effectAppender);
    }

    @Override
    public void appendCost(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        for (EffectAppender effectAppender : effectAppenders)
            effectAppender.appendCost(action, playerId, game, self, effectResult, effect);
    }

    @Override
    public void appendEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        for (EffectAppender effectAppender : effectAppenders)
            effectAppender.appendEffect(action, playerId, game, self, effectResult, effect);
    }

    @Override
    public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        for (EffectAppender effectAppender : effectAppenders) {
            if (!effectAppender.isPlayableInFull(action, playerId, game, self, effectResult, effect))
                return false;
        }

        return true;
    }
}
