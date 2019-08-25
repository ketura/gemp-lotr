package com.gempukku.lotro.cards.build.field.effect.trigger;

import com.gempukku.lotro.cards.build.field.effect.DefaultActionSource;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class TriggerActionSource extends DefaultActionSource {
    private TriggerChecker triggerChecker;

    public void setTriggerChecker(TriggerChecker triggerChecker) {
        this.triggerChecker = triggerChecker;
    }

    @Override
    public boolean isValid(String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        final boolean triggerValid = triggerChecker.isTriggerValid(playerId, game, self, effectResult, effect);
        if (!triggerValid)
            return false;

        return super.isValid(playerId, game, self, effectResult, effect);
    }
}
