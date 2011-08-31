package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Exert an Elf to reveal an opponent's hand. You may discard a [ISENGARD] minion revealed to
 * draw 2 cards.
 */
public class Card1_044 extends AbstractLotroCardBlueprint {
    public Card1_044() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.ELVEN, "Foul Creation", "1_44");
        addKeyword(Keyword.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF), Filters.canExert())) {
            final PlayEventAction action = new PlayEventAction(self);
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose an Elf", Filters.keyword(Keyword.ELF), Filters.canExert()) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard elf) {
                            action.addCost(new ExertCharacterEffect(elf));
                        }
                    });
            action.addCost(new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                    new MultipleChoiceAwaitingDecision(1, "Choose an opponent", GameUtils.getOpponents(game, playerId)) {
                        @Override
                        protected void validDecisionMade(int index, String chosenOpponent) {
                            List<? extends PhysicalCard> hand = game.getGameState().getHand(chosenOpponent);
                            List<PhysicalCard> isengardMinions = Filters.filter(hand, game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION));
                            action.addCost(
                                    new ChooseArbitraryCardEffect(playerId, "Choose ISENGARD minion to discard", isengardMinions) {
                                        @Override
                                        protected void cardSelected(PhysicalCard card) {
                                            action.addEffect(new DiscardCardFromHandEffect(card));
                                            action.addEffect(new DrawCardEffect(playerId));
                                            action.addEffect(new DrawCardEffect(playerId));
                                        }
                                    }
                            );
                        }
                    })
            );

            return Collections.singletonList(action);
        }
        return null;
    }
}
