package com.gempukku.lotro.cards.set12.gandalf;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: While Gandalf is at an underground site, he gains muster. (At the start of the regroup phase, you may
 * discard a card from hand to draw a card.) While the fellowship is at a battleground site, each [GANDALF] character
 * gains muster.
 */
public class Card12_036 extends AbstractPermanent {
    public Card12_036() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GANDALF, Zone.SUPPORT, "With Doom We Come");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.gandalf, new LocationCondition(Keyword.UNDERGROUND), Keyword.MUSTER, 1));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Culture.GANDALF, Filters.character), new LocationCondition(Keyword.BATTLEGROUND), Keyword.MUSTER, 1));
        return modifiers;
    }
}
