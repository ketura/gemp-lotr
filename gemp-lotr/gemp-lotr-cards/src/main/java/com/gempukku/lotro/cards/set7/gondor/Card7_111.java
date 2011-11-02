package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: The site number of each minion skirmishing a [GONDOR] Man is +1.
 */
public class Card7_111 extends AbstractPermanent {
    public Card7_111() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Man the Walls");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new MinionSiteNumberModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(Culture.GONDOR, Race.MAN)), null, 1));
    }
}
