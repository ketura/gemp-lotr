package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 0
 * Type: Possession • Armor
 * Strength: +2
 * Vitality: -1
 * Game Text: Bearer must be Merry. While you can spot 3 [ROHAN] Men, Merry cannot be overwhelmed unless his strength
 * is tripled.
 */
public class Card7_241 extends AbstractAttachableFPPossession {
    public Card7_241() {
        super(0, 2, -1, Culture.ROHAN, PossessionClass.ARMOR, "Merry's Armor", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Merry");
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new OverwhelmedByMultiplierModifier(self, Filters.hasAttached(self), new SpotCondition(3, Culture.ROHAN, Race.MAN), 3));
    }
}
