package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertTargetExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, exert an Ent. Plays on that Ent.
 * Response: If an [ISENGARD] minion is killed, discard this condition to reveal the top 10 cards of an opponent's
 * draw deck. Discard 1 Shadow card and 1 Free Peoples card revealed. Your opponent reshuffles that deck.
 */
public class Card4_107 extends AbstractAttachable {
    public Card4_107() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.GANDALF, null, "Windows in a Stone Wall");
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Race.ENT, Filters.canExert(self));
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertTargetExtraPlayCostModifier(self, self, null));
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.forEachKilled(game, effectResult, Culture.ISENGARD, CardType.MINION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(final String opponentId) {
                            action.insertEffect(
                                    new RevealTopCardsOfDrawDeckEffect(self, opponentId, 10) {
                                        @Override
                                        protected void cardsRevealed(final List<PhysicalCard> revealedCards) {
                                            action.appendEffect(
                                                    new ChooseArbitraryCardsEffect(playerId, "Choose shadow card", revealedCards, Side.SHADOW, 1, 1) {
                                                        @Override
                                                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                                            for (PhysicalCard selectedCard : selectedCards) {
                                                                action.insertEffect(
                                                                        new DiscardCardFromDeckEffect(selectedCard));
                                                            }
                                                        }
                                                    });
                                            action.appendEffect(
                                                    new ChooseArbitraryCardsEffect(playerId, "Choose free people card", revealedCards, Side.FREE_PEOPLE, 1, 1) {
                                                        @Override
                                                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                                            for (PhysicalCard selectedCard : selectedCards) {
                                                                action.insertEffect(
                                                                        new DiscardCardFromDeckEffect(selectedCard));
                                                            }
                                                        }
                                                    });
                                            action.appendEffect(
                                                    new ShuffleDeckEffect(opponentId));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
