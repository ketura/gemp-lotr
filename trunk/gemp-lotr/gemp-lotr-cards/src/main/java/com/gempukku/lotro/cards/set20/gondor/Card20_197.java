package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * 1
 * Into the Wild
 * Gondor	Condition • Support Area
 * While you can spot a [Gondor] ranger, each minion's site number is +1 for each site from your adventure deck on the adventure path
 * in the current region.
 */
public class Card20_197 extends AbstractPermanent {
    public Card20_197() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Into the Wild", null, true);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new MinionSiteNumberModifier(
                self, CardType.MINION, new SpotCondition(Culture.GONDOR, Keyword.RANGER),
                new CountActiveEvaluator(CardType.SITE, Zone.ADVENTURE_PATH, Filters.owner(self.getOwner()), Filters.currentRegion));
    }
}
