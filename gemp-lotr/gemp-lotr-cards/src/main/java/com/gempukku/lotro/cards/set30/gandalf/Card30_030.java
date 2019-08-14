package com.gempukku.lotro.cards.set30.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.decisions.YesNoDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event • Maneuver
 * Game Text: Spot Gandalf to remove a burden (or 2 burdens, if you exert Gandalf and a [ELVEN] Wise Ally).
 */
public class Card30_030 extends AbstractEvent {
    public Card30_030() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "He Gives Me Courage", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.gandalf);
    }

	@Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
		final PlayEventAction action = new PlayEventAction(self);
		if ((PlayConditions.canExert(self, game, Filters.gandalf))
				&& (PlayConditions.canExert(self, game, Keyword.WISE, Culture.ELVEN, CardType.ALLY))) {
			action.appendCost(
					new PlayoutDecisionEffect(playerId,
							new YesNoDecision("Do you want to exert Gandalf and a Wise ally?") {
				@Override
				protected void yes() {
					action.appendCost(
							new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gandalf));
					action.appendCost(
							new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Keyword.WISE, Culture.ELVEN, CardType.ALLY));
					action.appendEffect(
							new RemoveBurdenEffect(playerId, self, 2));
				};

				@Override
				protected void no() {
					action.appendEffect(
							new RemoveBurdenEffect(playerId, self, 1));
				}
			}));
		} else {
			action.appendEffect(
							new RemoveBurdenEffect(playerId, self, 1));
		}
		return action;
	}
}

