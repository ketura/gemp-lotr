package com.gempukku.lotro.effects;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;

import java.util.Collection;

public abstract class AbstractSubActionEffect implements Effect {
    private CostToEffectAction _subAction;

    protected void processSubAction(DefaultGame game, CostToEffectAction subAction) {
        _subAction = subAction;
        game.getActionsEnvironment().addActionToStack(_subAction);
    }

    protected final String getAppendedNames(Collection<? extends LotroPhysicalCard> cards) {
        return GameUtils.getAppendedNames(cards);
    }

    @Override
    public boolean wasCarriedOut() {
        return _subAction != null && _subAction.wasCarriedOut();
    }
}
