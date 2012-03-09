package com.gempukku.lotro.cards.set19.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.PreventMinionBeingAssignedToCharacterModifier;
import com.gempukku.lotro.cards.modifiers.ShouldSkipPhaseModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ages End
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 14
 * Type: Minion • Balrog
 * Strength: 17
 * Vitality: 5
 * Site: 4
 * Game Text: Damage +2. Fierce. While at an underground site, skip the archery phase, the maneuver phase,
 * and The Balrog cannot be assigned to skirmish companions of strength 6 or less.
 */
public class Card19_018 extends AbstractMinion {
    public Card19_018() {
        super(14, 17, 5, 4, Race.BALROG, Culture.MORIA, "The Balrog", true);
        addKeyword(Keyword.DAMAGE, 2);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new ShouldSkipPhaseModifier(self, new LocationCondition(Keyword.UNDERGROUND), Phase.ARCHERY));
        modifiers.add(
                new ShouldSkipPhaseModifier(self, new LocationCondition(Keyword.UNDERGROUND), Phase.MANEUVER));
        modifiers.add(
                new PreventMinionBeingAssignedToCharacterModifier(self, null, Filters.and(CardType.COMPANION, Filters.lessStrengthThan(7)),
                        new LocationCondition(Keyword.UNDERGROUND), self));
        return modifiers;
    }
}
