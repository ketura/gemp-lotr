package com.gempukku.lotro.cards.set15.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.PutRandomCardFromHandOnBottomOfDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Event • Fellowship
 * Game Text: Spot Smeagol to play a Free Peoples condition from your draw deck. If that condition was a [GOLLUM]
 * condition, you may make an opponent place a random card from his or her hand beneath his or her draw deck.
 * Shuffle your draw deck.
 */
public class Card15_050 extends AbstractEvent {
    public Card15_050() {
        super(Side.FREE_PEOPLE, 2, Culture.GOLLUM, "Something Slimy", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Filters.smeagol);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromDeckEffect(playerId, Side.FREE_PEOPLE, CardType.CONDITION) {
                    @Override
                    protected void afterCardPlayed(PhysicalCard cardPlayed) {
                        if (Filters.and(Culture.GOLLUM, CardType.CONDITION).accepts(game.getGameState(), game.getModifiersQuerying(), cardPlayed))
                            action.appendEffect(
                                    new OptionalEffect(action, playerId,
                                            new ChooseOpponentEffect(playerId) {
                                                @Override
                                                protected void opponentChosen(String opponentId) {
                                                    action.appendEffect(
                                                            new PutRandomCardFromHandOnBottomOfDeckEffect(opponentId));
                                                }

                                                @Override
                                                public String getText(LotroGame game) {
                                                    return "Make an opponent place a random card from hand beneath draw deck";
                                                }
                                            }));
                    }
                });
        return action;
    }
}
