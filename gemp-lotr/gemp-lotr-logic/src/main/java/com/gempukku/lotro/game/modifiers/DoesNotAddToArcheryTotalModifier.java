package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class DoesNotAddToArcheryTotalModifier extends AbstractModifier {
    public DoesNotAddToArcheryTotalModifier(PhysicalCard source, Filterable affectFilter) {
        this(source, affectFilter, null);
    }

    public DoesNotAddToArcheryTotalModifier(PhysicalCard source, Filterable affectFilter, Condition condition) {
        super(source, "Does not add to archery total", affectFilter, condition, ModifierEffect.ARCHERY_MODIFIER);
    }

    @Override
    public boolean addsToArcheryTotal(DefaultGame game, PhysicalCard card) {
        return false;
    }
}
