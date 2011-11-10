package com.gempukku.lotro.cards.set7.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Type: Site
 * Site: 1K
 * Game Text: Fellowship: Spot Gandalf to add (2). Each player may draw a card.
 */
public class Card7_331 extends AbstractSite {
    public Card7_331() {
        super("Isengard Ruined", Block.KING, 1, 0, Direction.LEFT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSpot(game, Filters.gandalf)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new AddTwilightEffect(self, 2));
            PlayOrder drawOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(game.getGameState().getCurrentPlayerId(), false);
            String nextPlayerId;
            while ((nextPlayerId = drawOrder.getNextPlayer()) != null) {
                final String drawingPlayerId = nextPlayerId;
                action.appendEffect(
                        new PlayoutDecisionEffect(game.getUserFeedback(), drawingPlayerId,
                                new MultipleChoiceAwaitingDecision(1, "Do you wish to draw a card?", new String[]{"Yes", "No"}) {
                                    @Override
                                    protected void validDecisionMade(int index, String result) {
                                        if (index == 0) {
                                            action.insertEffect(
                                                    new DrawCardEffect(drawingPlayerId, 1));
                                        }
                                    }
                                }));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
