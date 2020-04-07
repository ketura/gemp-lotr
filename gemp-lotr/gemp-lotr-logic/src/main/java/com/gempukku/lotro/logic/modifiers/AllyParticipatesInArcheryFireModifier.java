package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class AllyParticipatesInArcheryFireModifier extends AbstractModifier {
    public AllyParticipatesInArcheryFireModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Can participate in archery and skirmishes", affectFilter, ModifierEffect.PRESENCE_MODIFIER);
    }

    @Override
    public boolean isAllyParticipateInArcheryFire(LotroGame game, PhysicalCard card) {
        return true;
    }
}