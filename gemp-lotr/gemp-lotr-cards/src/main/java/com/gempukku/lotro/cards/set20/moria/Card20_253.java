package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.CantBeAssignedAgainstModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ShouldSkipPhaseModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * 14
 * •The Balrog, Demon of the Ancient World
 * Moria	Minion • Balrog
 * 17	5	4
 * Damage + 1. Fierce.
 * While at an underground site, skip the manuever and archery phases and the Balrog cannot be assigned to skirmish
 * a companion of strength of 6 or less.
 */
public class Card20_253 extends AbstractMinion {
    public Card20_253() {
        super(14, 17, 5, 4, Race.BALROG, Culture.MORIA, "The Balrog", "Demon of the Ancient World", true);
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        LocationCondition condition = new LocationCondition(Keyword.UNDERGROUND);
        modifiers.add(
                new ShouldSkipPhaseModifier(self, condition, Phase.MANEUVER));
        modifiers.add(
                new ShouldSkipPhaseModifier(self, condition, Phase.ARCHERY));
        modifiers.add(
                new CantBeAssignedAgainstModifier(self,null, Filters.and(CardType.COMPANION, Filters.lessStrengthThan(7)), condition, self));
        return modifiers;
    }
}
