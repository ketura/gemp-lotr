package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

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
                        // Need to insert them, but in the reverse order
                        final List<? extends Effect> effects = createEffects(action, playerId, game, self, effectResult, effect);
                        final Effect[] effectsArray = effects.toArray(new Effect[0]);
                        for (int i = effectsArray.length - 1; i >= 0; i--)
                            action.insertCost(effectsArray[i]);
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
                        // Need to insert them, but in the reverse order
                        final List<? extends Effect> effects = createEffects(action, playerId, game, self, effectResult, effect);
                        final Effect[] effectsArray = effects.toArray(new Effect[0]);
                        for (int i = effectsArray.length - 1; i >= 0; i--)
                            action.insertEffect(effectsArray[i]);
                    }

                    @Override
                    public String getText(LotroGame game) {
                        return text;
                    }
                });
    }

    protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        throw new UnsupportedOperationException("One of createEffect or createEffects has to be overwritten");
    }

    protected List<? extends Effect> createEffects(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        return Collections.singletonList(createEffect(action, playerId, game, self, effectResult, effect));
    }

    @Override
    public boolean isPlayableInFull(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        return true;
    }
}
