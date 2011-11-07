package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collection;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Event • Maneuver
 * Game Text: Choose an opponent to discard the top card of his or her draw deck. If the twilight cost of that card is
 * less than the number of besiegers you spot, take control of a site and all besiegers are strength +3 until
 * the regroup phase.
 */
public class Card7_267 extends AbstractEvent {
    public Card7_267() {
        super(Side.SHADOW, 2, Culture.SAURON, "Din of Arms", Phase.MANEUVER);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(String opponentId) {
                        action.insertEffect(
                                new DiscardTopCardFromDeckEffect(self, opponentId, 1, true) {
                                    @Override
                                    protected void cardsDiscardedCallback(Collection<PhysicalCard> cards) {
                                        final int besiegerCount = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Keyword.BESIEGER);
                                        for (PhysicalCard card : cards) {
                                            if (card.getBlueprint().getTwilightCost() < besiegerCount) {
                                                action.appendEffect(
                                                        new TakeControlOfASiteEffect(self, playerId));
                                                action.appendEffect(
                                                        new AddUntilStartOfPhaseModifierEffect(
                                                                new StrengthModifier(self, Keyword.BESIEGER, 3), Phase.REGROUP));
                                            }
                                        }
                                    }
                                });
                    }
                });
        return action;
    }
}
