package com.gempukku.lotro.cards.set13.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.CantDiscardFromPlayModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Lurker. (Skirmishes involving lurker minions must be resolved after any others.)
 * Possessions borne by this minion cannot be discarded by Free Peoples cards.
 */
public class Card13_163 extends AbstractMinion {
    public Card13_163() {
        super(4, 9, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Entranced Uruk");
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.LURKER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantDiscardFromPlayModifier(self, "Can't be discarded by Free Peoples cards",
                Filters.and(CardType.POSSESSION, Filters.attachedTo(self)),
                Filters.and(Side.FREE_PEOPLE));
    }
}
