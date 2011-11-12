package com.gempukku.lotro.cards.set8.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Knight. When you play Faramir (except in your starting fellowship), you may reveal the top 10 cards of
 * your draw deck. You may play a knight revealed. Shuffle your draw deck.
 */
public class Card8_034 extends AbstractCompanion {
    public Card8_034() {
        super(3, 7, 3, Culture.GONDOR, Race.MAN, Signet.GANDALF, "Faramir", true);
        addKeyword(Keyword.KNIGHT);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, self)
                && game.getGameState().getCurrentPhase() != Phase.PLAY_STARTING_FELLOWSHIP) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 10) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> cards) {
                            action.appendEffect(
                                    new ChooseArbitraryCardsEffect(playerId, "Choose a knight to play", cards, Filters.and(Keyword.KNIGHT, Filters.playable(game)), 0, 1) {
                                        @Override
                                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                            if (selectedCards.size() == 1) {
                                                PhysicalCard selectedCard = selectedCards.iterator().next();
                                                CostToEffectAction playCardAction = selectedCard.getBlueprint().getPlayCardAction(playerId, game, selectedCard, 0, false);
                                                game.getActionsEnvironment().addActionToStack(playCardAction);
                                            }
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
