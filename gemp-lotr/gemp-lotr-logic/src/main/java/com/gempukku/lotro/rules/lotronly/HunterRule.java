package com.gempukku.lotro.rules.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.ModifiersLogic;
import com.gempukku.lotro.modifiers.StrengthModifier;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;

public class HunterRule {
    private final ModifiersLogic _modifiersLogic;

    public HunterRule(ModifiersLogic modifiersLogic) {
        _modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        _modifiersLogic.addAlwaysOnModifier(
                new StrengthModifier(null, Filters.and(Keyword.HUNTER, Filters.inSkirmishAgainst(Filters.character, Filters.not(Keyword.HUNTER))), null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(DefaultGame game, LotroPhysicalCard self) {
                                return game.getModifiersQuerying().getKeywordCount(game, self, Keyword.HUNTER);
                            }
                        }));
    }
}
