package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.DefaultGame;

public interface ActionSource {
    boolean requiresRanger();

    boolean isValid(DefaultActionContext<DefaultGame> actionContext);

    void createAction(CostToEffectAction action, DefaultActionContext actionContext);
}
