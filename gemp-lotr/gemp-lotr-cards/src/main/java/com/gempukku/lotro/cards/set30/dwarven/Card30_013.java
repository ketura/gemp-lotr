package com.gempukku.lotro.cards.set30.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;


/**
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Heal a companion (or 2 Dwarf companions if you spot Elrond).
 */
public class Card30_013 extends AbstractEvent {
    public Card30_013() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Lore of Imladris", Phase.MANEUVER);
    }

    @Override
	public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
		final PlayEventAction action = new PlayEventAction(self);
		if (Filters.canSpot(game, Filters.name("Elrond")))
            action.appendEffect(
                    new PlayoutDecisionEffect(playerId,
							new MultipleChoiceAwaitingDecision(1, "Do you want to spot Elrond to heal 2 Dwarf companions?", new String[]{"Yes", "No"}) {
								@Override
                                protected void validDecisionMade(int index, String result) {
                                    if (result.equals("Yes")) {
                                        action.appendEffect(
                                                new ChooseAndHealCharactersEffect(action, playerId, 0, 2, CardType.COMPANION, Race.DWARF));
                                    } else {
                                        action.appendEffect(
                                                new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION));
                                    }
                                }
                            }));
        else
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION));

        return action;
    }
}