package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;

public class UnhastyCompanionParticipatesInSkirmishedModifier extends AbstractModifier {

    public UnhastyCompanionParticipatesInSkirmishedModifier(LotroPhysicalCard source, Filterable affectFilter) {
        super(source, "Can participate in archery and skirmishes", affectFilter, ModifierEffect.PRESENCE_MODIFIER);
    }

    @Override
    public boolean isUnhastyCompanionAllowedToParticipateInSkirmishes(DefaultGame game, LotroPhysicalCard card) {
        return true;
    }
}
