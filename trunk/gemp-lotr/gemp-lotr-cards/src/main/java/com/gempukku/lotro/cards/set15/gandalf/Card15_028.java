package com.gempukku.lotro.cards.set15.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Ent
 * Strength: 6
 * Vitality: 4
 * Resistance: 6
 * Game Text: While you can spot 3 Ents, this companion is strength +2.
 */
public class Card15_028 extends AbstractCompanion {
    public Card15_028() {
        super(4, 6, 4, 6, Culture.GANDALF, Race.ENT, null, "Ent Avenger");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, new SpotCondition(3, Race.ENT), 2);
    }
}
