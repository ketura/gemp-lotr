package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class UnhastyCompanionParticipatesInSkirmishedModifier extends AbstractModifier {

    public UnhastyCompanionParticipatesInSkirmishedModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Can participate in archery and skirmishes", affectFilter, ModifierEffect.PRESENCE_MODIFIER);
    }

    @Override
    public boolean isUnhastyCompanionAllowedToParticipateInSkirmishes(LotroGame game, PhysicalCard card) {
        return true;
    }
}
