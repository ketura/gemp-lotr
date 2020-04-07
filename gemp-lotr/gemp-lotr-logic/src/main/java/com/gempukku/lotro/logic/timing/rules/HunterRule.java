package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class HunterRule {
    private ModifiersLogic _modifiersLogic;

    public HunterRule(ModifiersLogic modifiersLogic) {
        _modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        _modifiersLogic.addAlwaysOnModifier(
                new StrengthModifier(null, Filters.and(Keyword.HUNTER, Filters.inSkirmishAgainst(Filters.character, Filters.not(Keyword.HUNTER))), null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard self) {
                                return game.getModifiersQuerying().getKeywordCount(game, self, Keyword.HUNTER);
                            }
                        }));
    }
}
