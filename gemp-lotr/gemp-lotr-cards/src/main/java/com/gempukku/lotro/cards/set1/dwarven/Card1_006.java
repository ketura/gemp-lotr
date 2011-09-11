package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Exert a Dwarf companion to draw 3 cards.
 */
public class Card1_006 extends AbstractEvent {
    public Card1_006() {
        super(Side.FREE_PEOPLE, Culture.DWARVEN, "Delving", Phase.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF), Filters.type(CardType.COMPANION), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseActiveCardEffect(playerId, "Choose Dwarf companion", Filters.race(Race.DWARF), Filters.type(CardType.COMPANION), Filters.canExert()) {
                    @Override
                    protected void cardSelected(PhysicalCard dwarf) {
                        action.addCost(new ExertCharacterEffect(dwarf));
                        action.addEffect(new DrawCardEffect(playerId));
                        action.addEffect(new DrawCardEffect(playerId));
                        action.addEffect(new DrawCardEffect(playerId));
                    }
                }
        );
        return action;
    }
}
