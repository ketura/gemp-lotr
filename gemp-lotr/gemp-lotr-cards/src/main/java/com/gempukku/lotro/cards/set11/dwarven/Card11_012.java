package com.gempukku.lotro.cards.set11.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Condition • Support Area
 * Game Text: To play, spot 2 Dwarf companions. While the fellowship is at a mountain site, each Dwarf is strength +2.
 */
public class Card11_012 extends AbstractPermanent {
    public Card11_012() {
        super(Side.FREE_PEOPLE, 3, CardType.CONDITION, Culture.DWARVEN, "Mountain Homestead");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Race.DWARF, CardType.COMPANION);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Race.DWARF, new LocationCondition(Keyword.MOUNTAIN), 2));
    }
}
