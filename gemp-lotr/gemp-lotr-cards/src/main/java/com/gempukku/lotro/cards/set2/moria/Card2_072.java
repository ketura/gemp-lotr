package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.effects.AddTwilightEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Shadow: Exert a [MORIA] minion and spot X burdens to add (X) (limit (5)).
 */
public class Card2_072 extends AbstractEvent {
    public Card2_072() {
        super(Side.SHADOW, Culture.MORIA, "Troubled Mountains", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.type(CardType.MINION));
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.culture(Culture.MORIA), Filters.type(CardType.MINION)));
        action.appendEffect(
                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                        new IntegerAwaitingDecision(1, "Choose number of burdens you wish to spot", 0, game.getGameState().getBurdens()) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int spotBurdens = getValidatedResult(result);
                                int addTwilight = Math.min(5, spotBurdens);
                                action.appendEffect(
                                        new AddTwilightEffect(addTwilight));
                            }
                        }));
        return action;
    }
}
