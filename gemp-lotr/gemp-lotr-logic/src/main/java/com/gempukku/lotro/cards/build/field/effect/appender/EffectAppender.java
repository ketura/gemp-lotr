package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.game.DefaultGame;

public interface EffectAppender<AbstractGame extends DefaultGame> {
    void appendEffect(boolean cost, CostToEffectAction action, DefaultActionContext<AbstractGame> actionContext);

    boolean isPlayableInFull(DefaultActionContext<AbstractGame> actionContext);

    default boolean isPlayabilityCheckedForEffect() {
        return false;
    }
}
