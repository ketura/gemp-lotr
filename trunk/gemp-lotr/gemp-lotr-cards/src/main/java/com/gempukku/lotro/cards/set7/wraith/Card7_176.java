package com.gempukku.lotro.cards.set7.wraith;

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
import com.gempukku.lotro.logic.effects.AddThreatsEffect;

import java.util.Collection;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Event • Regroup
 * Game Text: Spot a Nazgul to discard any number of cards from your hand. The Free Peoples player then discards any
 * number of cards from his or her hand. For each card you discarded more than the Free Peoples player, add a threat.
 */
public class Card7_176 extends AbstractEvent {
    public Card7_176() {
        super(Side.SHADOW, 1, Culture.WRAITH, "Disposable Servants", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canSpot(game, self, Race.NAZGUL);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
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
                                                    new AddThreatsEffect(playerId, self, diff));
                                        }
                                    }
                                });
                    }
                });
        return action;
    }
}
