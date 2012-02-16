package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;

public class LoseAllKeywordsModifier extends AbstractModifier {
    public LoseAllKeywordsModifier(PhysicalCard source, Filterable affectFilter) {
        this(source, affectFilter, null);
    }

    public LoseAllKeywordsModifier(PhysicalCard source, Filterable affectFilter, Condition condition) {
        super(source, "Loses all keywords", affectFilter, condition, ModifierEffect.LOSE_ALL_KEYWORDS_MODIFIER);
    }

    @Override
    public boolean lostAllKeywords(GameState gameState, ModifiersLogic modifiersLogic, PhysicalCard card) {
        return true;
    }
}
