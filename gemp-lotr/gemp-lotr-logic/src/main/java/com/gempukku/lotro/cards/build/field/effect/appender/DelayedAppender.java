package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public abstract class DelayedAppender implements EffectAppender {
    private String text;

    public DelayedAppender() {

    }

    public DelayedAppender(String text) {
        this.text = text;
    }

    @Override
    public final void appendCost(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        action.appendCost(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        action.appendCost(createEffect(action, playerId, game, self, effectResult, effect));
                    }

                    @Override
                    public String getText(LotroGame game) {
                        return text;
                    }
                });
    }

    @Override
    public final void appendEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        action.appendEffect(createEffect(action, playerId, game, self, effectResult, effect));
                    }

                    @Override
                    public String getText(LotroGame game) {
                        return text;
                    }
                });
    }

    protected abstract Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect);

    @Override
    public boolean isPlayableInFull(String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        return true;
    }
}
