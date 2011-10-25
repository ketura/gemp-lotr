package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.modifiers.ShouldSkipPhaseModifier;
import com.gempukku.lotro.cards.modifiers.conditions.CantSpotCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 1
 * Type: Site
 * Site: 2
 * Game Text: River. While only Hobbits are in the fellowship, there are no assignment and skirmish phases at
 * Buckleberry Ferry.
 */
public class Card1_330 extends AbstractSite {
    public Card1_330() {
        super("Buckleberry Ferry", Block.FELLOWSHIP, 2, 1, Direction.LEFT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new ShouldSkipPhaseModifier(self, new CantSpotCondition(CardType.COMPANION, Filters.not(Race.HOBBIT)), Phase.ASSIGNMENT));
        modifiers.add(
                new ShouldSkipPhaseModifier(self, new CantSpotCondition(CardType.COMPANION, Filters.not(Race.HOBBIT)), Phase.SKIRMISH));
        return modifiers;
    }
}
