package com.gempukku.lotro.game.modifiers.lotronly;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.AbstractModifier;
import com.gempukku.lotro.game.modifiers.ModifierEffect;

public class AllyParticipatesInArcheryFireAndSkirmishesModifier extends AbstractModifier {
    private final LotroPhysicalCard _source;

    public AllyParticipatesInArcheryFireAndSkirmishesModifier(LotroPhysicalCard source, Filterable affectFilter) {
        super(source, "Can participate in archery and skirmishes", affectFilter, ModifierEffect.PRESENCE_MODIFIER);
        _source = source;
    }

    @Override
    public boolean isAllyParticipateInArcheryFire(DefaultGame game, LotroPhysicalCard card) {
        return true;
    }

    @Override
    public boolean isAllyParticipateInSkirmishes(DefaultGame game, Side sidePlayer, LotroPhysicalCard card) {
        boolean unhasty = game.getModifiersQuerying().hasKeyword(game, card, Keyword.UNHASTY);
        return sidePlayer == Side.SHADOW
                || !unhasty || _source.getBlueprint().getCulture() == Culture.GANDALF;
    }
}
