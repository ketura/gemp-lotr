package com.gempukku.lotro.cards.set31.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDeckIntoHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;


import java.util.Collections;
import java.util.List;


/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Regroup: Discard an attached [DWARVEN] follower with a twilight cost of X to play X [DWARVEN] possessions
 * or X [DWARVEN] artifacts from your draw deck.
 */
public class Card31_002 extends AbstractEvent {
    public Card31_002() {
        super(Side.FREE_PEOPLE, 3, Culture.DWARVEN, "Dwarven Song", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canDiscardFromPlay(self, game, Filters.and(Culture.DWARVEN, CardType.FOLLOWER), Filters.attachedTo(CardType.COMPANION));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose an attached Dwarven follower", Filters.and(Culture.DWARVEN, CardType.FOLLOWER), Filters.attachedTo(CardType.COMPANION)) {
            @Override
            protected void cardSelected(LotroGame game, final PhysicalCard follower) {
				action.insertCost(new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, follower));
				final int twilightCost = follower.getBlueprint().getTwilightCost();
                action.appendEffect(
						new PlayoutDecisionEffect(playerId,
								new MultipleChoiceAwaitingDecision(1, "What would you like to do", new String[]{"Play " + twilightCost + " Dwarven artifacts", "Play " + twilightCost + " Dwarven possessions"}) {
					@Override
					protected void validDecisionMade(int index, String result) {
						new ChooseAndPutCardFromDeckIntoHandEffect(action, playerId, 1, 1, Culture.GONDOR);
						for (int i = 0; i < twilightCost; i++) {
							if (index == 0) {
								action.appendEffect(
										new ChooseAndPutCardFromDeckIntoHandEffect(action, playerId, 0, 1, Culture.DWARVEN, CardType.ARTIFACT));
							} else {
								action.appendEffect(
										new ChooseAndPutCardFromDeckIntoHandEffect(action, playerId, 1, 1, Culture.DWARVEN, CardType.POSSESSION));
							}
						}
						action.appendEffect(new ShuffleDeckEffect(playerId));
					}
				}));
			}
		});
        return action;
    }
}
