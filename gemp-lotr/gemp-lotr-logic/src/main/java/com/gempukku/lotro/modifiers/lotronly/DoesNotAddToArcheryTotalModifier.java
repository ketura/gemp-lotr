package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;
import com.gempukku.lotro.modifiers.condition.Condition;

public class DoesNotAddToArcheryTotalModifier extends AbstractModifier {
    public DoesNotAddToArcheryTotalModifier(LotroPhysicalCard source, Filterable affectFilter) {
        this(source, affectFilter, null);
    }

    public DoesNotAddToArcheryTotalModifier(LotroPhysicalCard source, Filterable affectFilter, Condition condition) {
        super(source, "Does not add to archery total", affectFilter, condition, ModifierEffect.ARCHERY_MODIFIER);
    }

    @Override
    public boolean addsToArcheryTotal(DefaultGame game, LotroPhysicalCard card) {
        return false;
    }
}
