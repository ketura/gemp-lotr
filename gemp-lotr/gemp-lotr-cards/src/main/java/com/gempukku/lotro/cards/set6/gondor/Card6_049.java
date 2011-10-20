package com.gempukku.lotro.cards.set6.gondor;

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
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. While you can spot 3 rangers, the site number of each minion skirmishing
 * a ranger is +2.
 */
public class Card6_049 extends AbstractPermanent {
    public Card6_049() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Ancient Roads");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new MinionSiteNumberModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(Keyword.RANGER)), null, 2));
    }
}
