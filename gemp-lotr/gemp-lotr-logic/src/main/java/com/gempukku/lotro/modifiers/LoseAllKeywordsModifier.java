package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;

public class LoseAllKeywordsModifier extends AbstractModifier {
    public LoseAllKeywordsModifier(LotroPhysicalCard source, Filterable affectFilter) {
        this(source, affectFilter, null);
    }

    public LoseAllKeywordsModifier(LotroPhysicalCard source, Filterable affectFilter, Condition condition) {
        super(source, "Loses all keywords", affectFilter, condition, ModifierEffect.LOSE_ALL_KEYWORDS_MODIFIER);
    }

    @Override
    public boolean lostAllKeywords(DefaultGame game, LotroPhysicalCard card) {
        return true;
    }
}
