package com.gempukku.lotro.cards.set21.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Heal a companion (or 2 Dwarf companions if you spot Elrond).
 */
public class Card21_13 extends AbstractEvent {
    public Card21_13() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Lore of Imladris", Phase.MANEUVER);
    }

    @Override
	public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
		final PlayEventAction action = new PlayEventAction(self);
		if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Elrond"))) {
			action.appendCost(
					new PlayoutDecisionEffect(playerId,
							new YesNoDecision("Do you want to spot Elrond to heal 2 Dwarf companions?") {
				@Override
				protected void yes() {
					action.appendEffect(
							new ChooseAndHealCharactersEffect(action, playerId, 0, 2, CardType.COMPANION, Race.DWARF));
				});

				@Override
				protected void no() {
					action.appendEffect(
							new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION));
				}
			}));
		} else {
			action.appendEffect(
					new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION));
		}
		return action;
	}
}