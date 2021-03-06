package com.gempukku.lotro.cards.set3.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.SpecialFlagModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Twilight Cost: 6
 * Type: Site
 * Site: 7
 * Game Text: River. While the fellowship is at The Great River, cards may not be played from draw decks or discard piles.
 */
public class Card3_118 extends AbstractSite {
    public Card3_118() {
        super("The Great River", SitesBlock.FELLOWSHIP, 7, 6, Direction.RIGHT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new SpecialFlagModifier(self, new LocationCondition(self), ModifierFlag.CANT_PLAY_FROM_DISCARD_OR_DECK));
    }
}
