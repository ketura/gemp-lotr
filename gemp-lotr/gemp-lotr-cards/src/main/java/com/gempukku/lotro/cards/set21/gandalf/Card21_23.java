package com.gempukku.lotro.cards.set21.gandalf;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Play Gandalf or an ally (except a Dwarf) from your draw deck or discard pile.
 */
public class Card21_23 extends AbstractOldEvent {
    public Card21_23() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "A Wizard Is Never Late", Phase.FELLOWSHIP);
    }
	
    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
       PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleCosts = new LinkedList<Effect>();
        possibleCosts.add(
                new ChooseAndPlayCardFromDeckEffect(playerId, Filters.or(Filters.gandalf, Filters.and(Filters.not(Race.DWARF), CardType.ALLY))) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play Gandalf or an ally from your deck";
                    }
                });
        possibleCosts.add(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.or(Filters.gandalf, Filters.and(Filters.not(Race.DWARF), CardType.ALLY))) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play Gandalf or an ally from your discard pile";
                    }
				});
		return action;
    }


    @Override
    public int getTwilightCost() {
        return 1;
    }
}
