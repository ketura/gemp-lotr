package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a Dwarf wins a skirmish, make an opponent discard 3 cards from the top of his or her draw
 * deck.
 */
public class Card1_023 extends AbstractResponseEvent {
    public Card1_023() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.DWARVEN, "Nobody Tosses a Dwarf");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.keyword(Keyword.DWARF))) {
            final PlayEventAction action = new PlayEventAction(self);
            action.addEffect(new PlayoutDecisionEffect(
                    game.getUserFeedback(), playerId,
                    new MultipleChoiceAwaitingDecision(1, "Choose an opponent", GameUtils.getOpponents(game, playerId)) {
                        @Override
                        protected void validDecisionMade(int index, String result) {
                            action.addEffect(new DiscardTopCardFromDeckEffect(result));
                            action.addEffect(new DiscardTopCardFromDeckEffect(result));
                            action.addEffect(new DiscardTopCardFromDeckEffect(result));
                        }
                    }
            ));
            return Collections.<Action>singletonList(action);
        }
        return null;
    }
}
