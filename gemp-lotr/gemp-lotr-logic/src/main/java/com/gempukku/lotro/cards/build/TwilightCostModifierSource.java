package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public interface TwilightCostModifierSource {
    int getTwilightCostModifier(DefaultActionContext<DefaultGame> actionContext, LotroPhysicalCard target);
}
