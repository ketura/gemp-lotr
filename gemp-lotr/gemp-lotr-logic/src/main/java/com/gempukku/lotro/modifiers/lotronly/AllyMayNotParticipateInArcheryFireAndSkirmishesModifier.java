package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.condition.Condition;
import com.gempukku.lotro.modifiers.ModifierEffect;

public class AllyMayNotParticipateInArcheryFireAndSkirmishesModifier extends AbstractModifier {
    public AllyMayNotParticipateInArcheryFireAndSkirmishesModifier(LotroPhysicalCard source, Condition condition, Filter affectFilter) {
        super(source, "May not participate in archery fire and skirmishes", affectFilter, condition, ModifierEffect.PRESENCE_MODIFIER);
    }

    @Override
    public boolean isAllyPreventedFromParticipatingInArcheryFire(DefaultGame game, LotroPhysicalCard card) {
        return true;
    }

    @Override
    public boolean isAllyPreventedFromParticipatingInSkirmishes(DefaultGame game, Side sidePlayer, LotroPhysicalCard card) {
        return true;
    }
}
