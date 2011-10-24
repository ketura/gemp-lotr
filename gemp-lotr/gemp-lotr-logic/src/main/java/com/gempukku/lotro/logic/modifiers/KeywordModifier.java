package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class KeywordModifier extends AbstractModifier {
    private Keyword _keyword;
    private Evaluator _evaluator;

    public KeywordModifier(PhysicalCard physicalCard, Filterable affectFilter, Keyword keyword) {
        this(physicalCard, affectFilter, keyword, 1);
    }

    public KeywordModifier(PhysicalCard physicalCard, Filterable affectFilter, Keyword keyword, int count) {
        this(physicalCard, affectFilter, null, keyword, count);
    }

    public KeywordModifier(PhysicalCard physicalCard, Filterable affectFilter, Condition condition, Keyword keyword, int count) {
        this(physicalCard, affectFilter, condition, keyword, new ConstantEvaluator(count));
    }

    public KeywordModifier(PhysicalCard physicalCard, Filterable affectFilter, Condition condition, Keyword keyword, Evaluator evaluator) {
        super(physicalCard, null, affectFilter, condition, new ModifierEffect[]{ModifierEffect.KEYWORD_MODIFIER});
        _keyword = keyword;
        _evaluator = evaluator;
    }

    @Override
    public String getText(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (_keyword.isMultiples()) {
            int count = _evaluator.evaluateExpression(gameState, modifiersQuerying, self);
            return _keyword.getHumanReadable() + " +" + count;
        }
        return _keyword.getHumanReadable();
    }

    @Override
    public boolean hasKeyword(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword) {
        return (keyword == _keyword);
    }

    @Override
    public int getKeywordCountModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword) {
        if (keyword == _keyword)
            return _evaluator.evaluateExpression(gameState, modifiersQuerying, physicalCard);
        else
            return 0;
    }
}
