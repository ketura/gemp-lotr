package com.gempukku.lotro.cards.set15.gollum;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.OptionalEffect;
import com.gempukku.lotro.logic.effects.PutRandomCardFromHandOnBottomOfDeckEffect;
import com.gempukku.lotro.logic.effects.ShuffleDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.smeagol);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromDeckEffect(playerId, Side.FREE_PEOPLE, CardType.CONDITION) {
                    @Override
                    protected void afterCardPlayed(PhysicalCard cardPlayed) {
                        if (Filters.and(Culture.GOLLUM, CardType.CONDITION).accepts(game, cardPlayed))
                            action.insertEffect(
                                    new OptionalEffect(action, playerId,
                                            new ChooseOpponentEffect(playerId) {
                                                @Override
                                                protected void opponentChosen(String opponentId) {
                                                    action.insertEffect(
                                                            new PutRandomCardFromHandOnBottomOfDeckEffect(opponentId));
                                                }

                                                @Override
                                                public String getText(LotroGame game) {
                                                    return "Make an opponent place a random card from hand beneath draw deck";
                                                }
                                            }));
                    }
                });
        action.appendEffect(
                new ShuffleDeckEffect(playerId));
        return action;
    }
}
