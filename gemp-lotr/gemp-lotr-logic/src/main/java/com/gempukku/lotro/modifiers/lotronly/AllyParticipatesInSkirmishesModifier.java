package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.Condition;
import com.gempukku.lotro.modifiers.ModifierEffect;

public class AllyParticipatesInSkirmishesModifier extends AbstractModifier {
    private final LotroPhysicalCard _source;

    public AllyParticipatesInSkirmishesModifier(LotroPhysicalCard source, Filterable... affectFilters) {
        this(source, null, affectFilters);
    }

    public AllyParticipatesInSkirmishesModifier(LotroPhysicalCard source, Condition condition, Filterable... affectFilters) {
        super(source, "Can participate in skirmishes", Filters.and(affectFilters), condition, ModifierEffect.PRESENCE_MODIFIER);
        _source = source;
    }

    @Override
    public boolean isAllyParticipateInSkirmishes(DefaultGame game, Side sidePlayer, LotroPhysicalCard card) {
        boolean unhasty = game.getModifiersQuerying().hasKeyword(game, card, Keyword.UNHASTY);
        return sidePlayer == Side.SHADOW
                || !unhasty || _source.getBlueprint().getCulture() == Culture.GANDALF;
    }
}
