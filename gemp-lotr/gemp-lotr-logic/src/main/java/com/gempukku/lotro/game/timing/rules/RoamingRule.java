package com.gempukku.lotro.game.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.KeywordModifier;
import com.gempukku.lotro.game.modifiers.ModifiersLogic;

public class RoamingRule {
    private final ModifiersLogic _modifiersLogic;

    public RoamingRule(ModifiersLogic modifiersLogic) {
        _modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        Filter roamingFilter = Filters.and(CardType.MINION, new Filter() {
            @Override
            public boolean accepts(DefaultGame game, PhysicalCard physicalCard) {
                return (game.getModifiersQuerying().getMinionSiteNumber(game, physicalCard) > game.getGameState().getCurrentSiteNumber());
            }
        });

        _modifiersLogic.addAlwaysOnModifier(
                new KeywordModifier(null, roamingFilter, Keyword.ROAMING));
    }
}
