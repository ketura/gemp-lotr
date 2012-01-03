package com.gempukku.lotro.cards.set13.gandalf;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Each companion bearing a follower gains muster (and is strength +1 if that companion is a [GANDALF]
 * companion).
 */
public class Card13_041 extends AbstractPermanent {
    public Card13_041() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GANDALF, Zone.SUPPORT, "Strange Meeting");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.and(CardType.COMPANION, Filters.hasAttached(CardType.FOLLOWER)), Keyword.MUSTER));
        modifiers.add(
                new StrengthModifier(self, Filters.and(CardType.COMPANION, Culture.GANDALF, Filters.hasAttached(CardType.FOLLOWER)), 1));
        return modifiers;
    }
}
