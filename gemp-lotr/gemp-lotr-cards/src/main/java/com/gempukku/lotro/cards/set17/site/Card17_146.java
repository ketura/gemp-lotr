package com.gempukku.lotro.cards.set17.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromDeadPileEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Twilight Cost: 3
 * Type: Site
 * Game Text: River. When the fellowship moves to this site in region 2, the Free Peoples player places a card from
 * the dead pile out of play.
 */
public class Card17_146 extends AbstractNewSite {
    public Card17_146() {
        super("Falls of Rauros", 3, Direction.LEFT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self, Filters.region(2))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseCardsFromDeadPileEffect(game.getGameState().getCurrentPlayerId(), 1, 1, Filters.owner(game.getGameState().getCurrentPlayerId())) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            for (PhysicalCard card : cards) {
                                game.getGameState().removeCardsFromZone(game.getGameState().getCurrentPlayerId(), Collections.singleton(card));
                                game.getGameState().addCardToZone(game, card, Zone.REMOVED);
                                game.getGameState().sendMessage(game.getGameState().getCurrentPlayerId() + " placed " + GameUtils.getCardLink(card) + " from the dead pile out of play");
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
