package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Search. To play, spot an [ISENGARD] tracker. Plays on a companion. Limit 1 per companion. If bearer
 * is killed, reveal the top 10 cards of opponent's draw deck and discard 1 Shadow card and 1 Free Peoples card.
 * Your opponent reshuffles that deck.
 */
public class Card4_171 extends AbstractAttachable {
    public Card4_171() {
        super(Side.SHADOW, CardType.CONDITION, 1, Culture.ISENGARD, null, "Rest While You Can");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.ISENGARD, Keyword.TRACKER);
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(CardType.COMPANION, Filters.not(Filters.hasAttached(Filters.name("Rest While You Can"))));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredBeforeTriggers(final LotroGame game, Effect effect, final PhysicalCard self) {
        if (TriggerConditions.isGettingKilled(effect, game, self.getAttachedTo())) {
            KillEffect killEffect = (KillEffect) effect;
            if (killEffect.getCharactersToBeKilled().contains(self.getAttachedTo())) {
                final RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new RevealTopCardsOfDrawDeckEffect(self, game.getGameState().getCurrentPlayerId(), 10) {
                            @Override
                            protected void cardsRevealed(final List<PhysicalCard> revealedCards) {
                                action.appendEffect(
                                        new ChooseArbitraryCardsEffect(self.getOwner(), "Choose shadow card", revealedCards, Side.SHADOW, 1, 1) {
                                            @Override
                                            protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                                for (PhysicalCard selectedCard : selectedCards) {
                                                    action.insertEffect(
                                                            new DiscardCardFromDeckEffect(selectedCard));
                                                }
                                            }
                                        });
                                action.appendEffect(
                                        new ChooseArbitraryCardsEffect(self.getOwner(), "Choose free people card", revealedCards, Side.FREE_PEOPLE, 1, 1) {
                                            @Override
                                            protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                                for (PhysicalCard selectedCard : selectedCards) {
                                                    action.insertEffect(
                                                            new DiscardCardFromDeckEffect(selectedCard));
                                                }
                                            }
                                        });
                                action.appendEffect(
                                        new ShuffleDeckEffect(game.getGameState().getCurrentPlayerId()));
                            }
                        });
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
