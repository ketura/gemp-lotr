package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;

import java.util.Collection;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 0
 * Type: Event • Maneuver
 * Game Text: To play, spot 2 [RAIDER] Men. Discard any number of cards from hand. The Free Peoples player then discards
 * any number of cards from hand. For each card you discarded more than the Free Peoples player, add (1).
 */
public class Card7_151 extends AbstractEvent {
    public Card7_151() {
        super(Side.SHADOW, 0, Culture.RAIDER, "Hosts Still Unfought", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, 2, Culture.RAIDER, Race.MAN);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 0, Integer.MAX_VALUE, Filters.any) {
                    @Override
                    protected void cardsBeingDiscardedCallback(Collection<PhysicalCard> cardsBeingDiscarded) {
                        final int shadowDiscardCount = cardsBeingDiscarded.size();

                        action.appendEffect(
                                new ChooseAndDiscardCardsFromHandEffect(action, game.getGameState().getCurrentPlayerId(), false, 0, Integer.MAX_VALUE, Filters.any) {
                                    @Override
                                    protected void cardsBeingDiscardedCallback(Collection<PhysicalCard> cardsBeingDiscarded) {
                                        int fpDiscardCount = cardsBeingDiscarded.size();

                                        int diff = shadowDiscardCount - fpDiscardCount;
                                        if (diff > 0) {
                                            action.appendEffect(
                                                    new AddTwilightEffect(self, diff));
                                        }
                                    }
                                });
                    }
                });
        return action;
    }
}
