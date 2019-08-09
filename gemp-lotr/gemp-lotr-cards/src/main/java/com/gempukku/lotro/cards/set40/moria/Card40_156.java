package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.CantBeAssignedAgainstModifier;
import com.gempukku.lotro.logic.modifiers.ShouldSkipPhaseModifier;
import com.gempukku.lotro.logic.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Arrays;
import java.util.List;

/**
 * Title: *The Balrog, Demon of Might
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 14
 * Type: Minion - Balrog
 * Strength: 17
 * Vitality: 5
 * Home: 4
 * Card Number: 1R156
 * Game Text: Damage +2. Fierce. While at an underground site, skip the maneuver and archery phases
 * and The Balrog cannot be assigned to skirmish a companion of strength 6 or less.
 */
public class Card40_156 extends AbstractMinion {
    public Card40_156() {
        super(14, 17, 5, 4, Race.BALROG, Culture.MORIA, "The Balrog", "Demon of Might", true);
        addKeyword(Keyword.DAMAGE, 2);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        ShouldSkipPhaseModifier skipManeuver = new ShouldSkipPhaseModifier(self, new LocationCondition(Keyword.UNDERGROUND), Phase.MANEUVER);
        ShouldSkipPhaseModifier skipArchery = new ShouldSkipPhaseModifier(self, new LocationCondition(Keyword.UNDERGROUND), Phase.ARCHERY);
        CantBeAssignedAgainstModifier assignment = new CantBeAssignedAgainstModifier(self, null,
                Filters.and(CardType.COMPANION, Filters.lessStrengthThan(7)), new LocationCondition(Keyword.UNDERGROUND), self);
        return Arrays.asList(skipManeuver, skipArchery, assignment);
    }
}
