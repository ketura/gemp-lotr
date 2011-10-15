package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Archery: Spot an Elf archer companion to make the fellowship archery total +1.
 */
public class Card1_038 extends AbstractOldEvent {
    public Card1_038() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Double Shot", Phase.ARCHERY);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF), Filters.keyword(Keyword.ARCHER), Filters.type(CardType.COMPANION));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(new AddUntilEndOfPhaseModifierEffect(
                new ArcheryTotalModifier(self, Side.FREE_PEOPLE, 1), Phase.ARCHERY));

        return action;
    }
}
