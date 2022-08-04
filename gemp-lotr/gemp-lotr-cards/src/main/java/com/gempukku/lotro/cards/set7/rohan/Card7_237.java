package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.PutRandomCardFromHandOnBottomOfDeckEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 0
 * Type: Event • Regroup
 * Game Text: Spot 3 [ROHAN] Men to make an opponent remove (1) or place a random card from hand beneath his or her draw
 * deck. Do this once for each card in that player's hand when you play this event.
 */
public class Card7_237 extends AbstractEvent {
    public Card7_237() {
        super(Side.FREE_PEOPLE, 0, Culture.ROHAN, "His Golden Shield", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 3, Culture.ROHAN, Race.MAN);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, final LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(String opponentId) {
                        int cardCount = game.getGameState().getHand(opponentId).size();
                        for (int i = 0; i < cardCount; i++) {
                            List<Effect> possibleEffects = new LinkedList<>();
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
