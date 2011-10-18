package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

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
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.race(Race.ELF), Filters.canExert(self));
    }

    @Override
    public AttachPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        AttachPermanentAction permanentAction = super.getPlayCardAction(playerId, game, self, additionalAttachmentFilter, twilightModifier);
        permanentAction.setExertTarget(true);
        return permanentAction;
    }

    @Override
    public List<? extends Action> getOptionalInPlayAfterActions(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.KILL) {
            KillResult killResult = (KillResult) effectResult;
            if (Filters.filter(killResult.getKilledCards(), game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.MINION), Filters.culture(Culture.ISENGARD)).size() > 0) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new DiscardCardsFromPlayEffect(self, self));
                action.appendEffect(
                        new ChooseOpponentEffect(playerId) {
                            @Override
                            protected void opponentChosen(final String opponentId) {
                                action.insertEffect(
                                        new RevealTopCardsOfDrawDeckEffect(self, opponentId, 10) {
                                            @Override
                                            protected void cardsRevealed(final List<PhysicalCard> cards) {
                                                action.appendEffect(
                                                        new ChooseArbitraryCardsEffect(playerId, "Choose shadow card", cards, Filters.side(Side.SHADOW), 1, 1) {
                                                            @Override
                                                            protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                                                for (PhysicalCard selectedCard : selectedCards) {
                                                                    action.insertEffect(
                                                                            new DiscardCardFromDeckEffect(selectedCard));
                                                                }
                                                            }
                                                        });
                                                action.appendEffect(
                                                        new ChooseArbitraryCardsEffect(playerId, "Choose free people card", cards, Filters.side(Side.FREE_PEOPLE), 1, 1) {
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
        }
        return null;
    }
}
