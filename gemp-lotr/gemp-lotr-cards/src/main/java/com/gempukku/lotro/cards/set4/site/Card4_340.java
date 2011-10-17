package com.gempukku.lotro.cards.set4.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.modifiers.AllyMayNotParticipateInArcheryFireAndSkirmishesModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Twilight Cost: 0
 * Type: Site
 * Site: 3T
 * Game Text: Sanctuary. Each [ROHAN] ally may not participate in archery fire or skirmishes at Streets of Edoras.
 */
public class Card4_340 extends AbstractSite {
    public Card4_340() {
        super("Streets of Edoras", Block.TWO_TOWERS, 3, 0, Direction.RIGHT);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new AllyMayNotParticipateInArcheryFireAndSkirmishesModifier(self, new LocationCondition(Filters.sameCard(self)), Filters.and(Culture.ROHAN, CardType.ALLY)));
    }
}
