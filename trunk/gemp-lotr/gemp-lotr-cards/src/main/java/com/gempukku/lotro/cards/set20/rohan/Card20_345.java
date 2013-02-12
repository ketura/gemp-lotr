package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 3
 * •Theoden, Renewed in Spirit
 * Rohan	Companion • Man
 * 7	3	6
 * While you can spot Gandalf, Theoden is strength +2.
 */
public class Card20_345 extends AbstractCompanion {
    public Card20_345() {
        super(3, 7, 3, 6, Culture.ROHAN, Race.MAN, null, Names.theoden, "Renewed in Spirit", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, self, new SpotCondition(Filters.gandalf), 2);
    }
}
