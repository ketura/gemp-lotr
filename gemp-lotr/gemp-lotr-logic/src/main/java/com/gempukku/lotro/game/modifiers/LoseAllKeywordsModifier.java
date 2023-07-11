package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class LoseAllKeywordsModifier extends AbstractModifier {
    public LoseAllKeywordsModifier(PhysicalCard source, Filterable affectFilter) {
        this(source, affectFilter, null);
    }

    public LoseAllKeywordsModifier(PhysicalCard source, Filterable affectFilter, Condition condition) {
        super(source, "Loses all keywords", affectFilter, condition, ModifierEffect.LOSE_ALL_KEYWORDS_MODIFIER);
    }

    @Override
    public boolean lostAllKeywords(DefaultGame game, PhysicalCard card) {
        return true;
    }
}
