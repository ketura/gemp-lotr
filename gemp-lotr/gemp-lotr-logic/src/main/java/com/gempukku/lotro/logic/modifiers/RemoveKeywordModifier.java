package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;

public class RemoveKeywordModifier extends AbstractModifier {
    private Keyword _keyword;

    public RemoveKeywordModifier(PhysicalCard physicalCard, Filterable affectFilter, Keyword keyword) {
        super(physicalCard, "Loses " + keyword.getHumanReadable() + "keyword(s)", affectFilter, new ModifierEffect[]{ModifierEffect.KEYWORD_MODIFIER});
        _keyword = keyword;
    }

    public RemoveKeywordModifier(PhysicalCard physicalCard, Filterable affectFilter, Condition condition, Keyword keyword) {
        super(physicalCard, "Loses " + keyword.getHumanReadable() + "keyword(s)", affectFilter, condition, new ModifierEffect[]{ModifierEffect.KEYWORD_MODIFIER});
        _keyword = keyword;
    }

    @Override
    public boolean isKeywordRemoved(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword) {
        return _keyword == keyword;
    }
}
