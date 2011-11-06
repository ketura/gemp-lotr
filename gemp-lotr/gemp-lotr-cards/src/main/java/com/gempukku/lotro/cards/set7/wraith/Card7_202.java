package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Minion • Orc
 * Strength: 3
 * Vitality: 1
 * Site: 4
 * Game Text: While you can spot a wound on the Ring-bearer, this minion is strength +6.
 */
public class Card7_202 extends AbstractMinion {
    public Card7_202() {
        super(1, 3, 1, 4, Race.ORC, Culture.WRAITH, "Morgul Whelp");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, self, new SpotCondition(Keyword.RING_BEARER, Filters.wounded), 6));
    }
}
