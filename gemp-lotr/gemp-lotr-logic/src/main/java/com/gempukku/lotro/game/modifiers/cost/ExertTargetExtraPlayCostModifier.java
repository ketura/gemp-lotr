package com.gempukku.lotro.game.modifiers.cost;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.AttachPermanentAction;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.game.modifiers.Condition;

public class ExertTargetExtraPlayCostModifier extends AbstractExtraPlayCostModifier {
    public ExertTargetExtraPlayCostModifier(LotroPhysicalCard source, Filterable affects, Condition condition) {
        super(source, "Exert to play", Filters.and(affects), condition);
    }

    @Override
    public boolean canPayExtraCostsToPlay(DefaultGame game, LotroPhysicalCard card) {
        return true;
    }

    @Override
    public void appendExtraCosts(DefaultGame game, CostToEffectAction action, LotroPhysicalCard card) {
        ((AttachPermanentAction) action).setExertTarget(true);
    }
}
