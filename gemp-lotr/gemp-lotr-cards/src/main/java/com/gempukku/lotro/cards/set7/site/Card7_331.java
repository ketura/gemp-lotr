package com.gempukku.lotro.cards.set7.site;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
        super("Isengard Ruined", SitesBlock.KING, 1, 0, Direction.LEFT);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSpot(game, Filters.gandalf)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new AddTwilightEffect(self, 2));
            for (String nextPlayerId : GameUtils.getAllPlayers(game)) {
                final String drawingPlayerId = nextPlayerId;
                action.appendEffect(
                        new PlayoutDecisionEffect(drawingPlayerId,
                                new MultipleChoiceAwaitingDecision(1, "Do you wish to draw a card?", new String[]{"Yes", "No"}) {
                                    @Override
                                    protected void validDecisionMade(int index, String result) {
                                        if (index == 0) {
                                            action.insertEffect(
                                                    new DrawCardsEffect(action, drawingPlayerId, 1));
                                        }
                                    }
                                }));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
