package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.modifiers.condition.Condition;

public interface ExtraPlayCost {
    void appendExtraCosts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard card);

    boolean canPayExtraCostsToPlay(DefaultGame game, LotroPhysicalCard card);

    Condition getCondition();
}
