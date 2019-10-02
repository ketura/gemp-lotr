package com.gempukku.lotro.cards.set32.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.ForEachYouSpotDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Event • Shadow
 * Game Text: Spot X Wise characters to take into hand X minions from your discard pile.
 */
public class Card32_065 extends AbstractEvent {
    public Card32_065() {
        super(Side.SHADOW, 1, Culture.WRAITH, "The Great Enemy", Phase.SHADOW);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PlayoutDecisionEffect(playerId,
                        new ForEachYouSpotDecision(1, "How many Wise characters do you wish to spot?", game, Keyword.WISE) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int count = getValidatedResult(result);
                                for (int i = 0; i < count; i++)
                                    action.appendEffect(
                                            new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, CardType.MINION));
                            }
                        }));
        return action;
    }
}
