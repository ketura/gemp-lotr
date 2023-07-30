package com.gempukku.lotro.rules.lotronly;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.ModifiersLogic;
import com.gempukku.lotro.modifiers.StrengthModifier;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;

public class EnduringRule {
    private final ModifiersLogic _modifiersLogic;

    public EnduringRule(ModifiersLogic modifiersLogic) {
        _modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        _modifiersLogic.addAlwaysOnModifier(
                new StrengthModifier(null, Filters.and(Filters.wounded, Keyword.ENDURING), null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(DefaultGame game, LotroPhysicalCard self) {
                                return 2 * game.getGameState().getWounds(self);
                            }
                        }));
    }
}
