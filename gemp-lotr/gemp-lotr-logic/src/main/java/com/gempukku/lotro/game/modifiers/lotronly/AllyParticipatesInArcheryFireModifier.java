package com.gempukku.lotro.game.modifiers.lotronly;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.AbstractModifier;
import com.gempukku.lotro.game.modifiers.ModifierEffect;

public class AllyParticipatesInArcheryFireModifier extends AbstractModifier {
    public AllyParticipatesInArcheryFireModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Can participate in archery fire", affectFilter, ModifierEffect.PRESENCE_MODIFIER);
    }

    @Override
    public boolean isAllyParticipateInArcheryFire(DefaultGame game, PhysicalCard card) {
        return true;
    }
}