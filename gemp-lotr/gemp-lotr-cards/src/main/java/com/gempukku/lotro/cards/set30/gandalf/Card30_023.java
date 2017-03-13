package com.gempukku.lotro.cards.set30.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Play Gandalf or an ally (except a Dwarf) from your draw deck or discard pile.
 */
public class Card30_023 extends AbstractEvent {
    public Card30_023() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "A Wizard Is Never Late", Phase.FELLOWSHIP);
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
}
