package com.gempukku.lotro.cards.set15.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 3
 * Type: Condition • Support Area
 * Game Text: Each Hobbit bearing a hand weapon is vitality +1. Each Hobbit bearing a follower is resistance +1.
 * Each unwounded Hobbit is strength +1.
 */
public class Card15_148 extends AbstractPermanent {
    public Card15_148() {
        super(Side.FREE_PEOPLE, 3, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Little Golden Flower", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new VitalityModifier(self, Filters.and(Race.HOBBIT, Filters.hasAttached(PossessionClass.HAND_WEAPON)), 1));
        modifiers.add(
                new ResistanceModifier(self, Filters.and(Race.HOBBIT, Filters.hasAttached(CardType.FOLLOWER)), 1));
        modifiers.add(
                new StrengthModifier(self, Filters.and(Race.HOBBIT, Filters.unwounded), 1));
        return modifiers;
    }
}
