package com.gempukku.lotro.cards.set5.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.decisions.ForEachYouSpotDecision;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Spot X Uruk-hai. The Free Peoples player may assign X wounds to your minions. Play X [ISENGARD]
 * weapons from your discard pile.
 */
public class Card5_048 extends AbstractEvent {
    public Card5_048() {
        super(Side.SHADOW, 1, Culture.ISENGARD, "Black Shapes Crawling", Phase.MANEUVER);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                        new ForEachYouSpotDecision(1, "Choose number of Uruk-hai you wish to spot?", game, Filters.race(Race.URUK_HAI), Integer.MAX_VALUE) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int count = getValidatedResult(result);
                                for (int i = 0; i < count; i++)
                                    action.appendEffect(
                                            new ChooseAndWoundCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 0, 1, Filters.owner(playerId), CardType.MINION));
                                for (int i = 0; i < count; i++)
                                    action.appendEffect(
                                            new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId), Culture.ISENGARD, Filters.weapon));
                            }
                        }));
        return action;
    }
}
