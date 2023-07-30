package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;

public class AllyParticipatesInArcheryFireModifier extends AbstractModifier {
    public AllyParticipatesInArcheryFireModifier(LotroPhysicalCard source, Filterable affectFilter) {
        super(source, "Can participate in archery fire", affectFilter, ModifierEffect.PRESENCE_MODIFIER);
    }

    @Override
    public boolean isAllyParticipateInArcheryFire(DefaultGame game, LotroPhysicalCard card) {
        return true;
    }
}