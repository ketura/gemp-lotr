package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Archery: Spot an Elf companion to make the minion archery total -1.
 */
public class Card1_052 extends AbstractEvent {
    public Card1_052() {
        super(Side.FREE_PEOPLE, 0, Culture.ELVEN, "Lightfootedness", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Race.ELF, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilEndOfPhaseModifierEffect(
                        new ArcheryTotalModifier(self, Side.SHADOW, -1)));

        return action;
    }
}
