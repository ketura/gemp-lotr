package com.gempukku.lotro.cards.set6.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Ent
 * Strength: 6
 * Vitality: 4
 * Resistance: 6
 * Game Text: While you can spot 3 Ents, this companion is strength +2.
 */
public class Card6_027 extends AbstractCompanion {
    public Card6_027() {
        super(4, 6, 4, Culture.GANDALF, Race.ENT, null, "Ent Avenger");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, self, new SpotCondition(3, Race.ENT), 2));
    }
}
