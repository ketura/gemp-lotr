package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class AllyMayNotParticipateInArcheryFireAndSkirmishesModifier extends AbstractModifier {
    public AllyMayNotParticipateInArcheryFireAndSkirmishesModifier(PhysicalCard source, Condition condition, Filter affectFilter) {
        super(source, "May not participate in archery fire and skirmishes", affectFilter, condition, ModifierEffect.PRESENCE_MODIFIER);
    }

    @Override
    public boolean isAllyPreventedFromParticipatingInArcheryFire(DefaultGame game, PhysicalCard card) {
        return true;
    }

    @Override
    public boolean isAllyPreventedFromParticipatingInSkirmishes(DefaultGame game, Side sidePlayer, PhysicalCard card) {
        return true;
    }
}
