package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.PutRandomCardFromHandOnBottomOfDeckEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 0
 * Type: Event • Maneuver
 * Game Text: Choose an opponent. For each mounted companion you spot, that opponent must remove (1) or place a random
 * card from hand beneath his or her draw deck.
 */
public class Card7_238 extends AbstractEvent {
    public Card7_238() {
        super(Side.FREE_PEOPLE, 0, Culture.ROHAN, "Knights of His House", Phase.MANEUVER);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(String opponentId) {
                        int cardCount = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION, Filters.mounted);
                        for (int i = 0; i < cardCount; i++) {
                            List<Effect> possibleEffects = new LinkedList<Effect>();
                            possibleEffects.add(
                                    new RemoveTwilightEffect(1));
                            possibleEffects.add(
                                    new PutRandomCardFromHandOnBottomOfDeckEffect(opponentId));
                            action.appendEffect(
                                    new ChoiceEffect(action, opponentId, possibleEffects));
                        }
                    }
                });
        return action;
    }
}
