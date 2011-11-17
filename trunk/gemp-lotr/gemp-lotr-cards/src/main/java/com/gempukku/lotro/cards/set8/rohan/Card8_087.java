package com.gempukku.lotro.cards.set8.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
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
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 3
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Valiant. When you play Eomer (except in your starting fellowship), you may reveal the top 10 cards
 * of your draw deck. You may play each [ROHAN] possession revealed. Shuffle your draw deck.
 */
public class Card8_087 extends AbstractCompanion {
    public Card8_087() {
        super(3, 8, 3, Culture.ROHAN, Race.MAN, Signet.GANDALF, "Eomer", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && game.getGameState().getCurrentPhase() != Phase.PLAY_STARTING_FELLOWSHIP) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 10) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> cards) {
                            action.insertEffect(
                                    new PlayAnyNumberOfCardsFromDeckEffect(action, playerId, cards));
                        }
                    });
            action.appendEffect(
                    new ShuffleDeckEffect(playerId));
            return Collections.singletonList(action);
        }
        return null;
    }

    private static class PlayAnyNumberOfCardsFromDeckEffect extends ChooseAndPlayCardFromDeckEffect {
        private CostToEffectAction _action;
        private String _playerId;
        private Collection<PhysicalCard> _cards;

        public PlayAnyNumberOfCardsFromDeckEffect(CostToEffectAction action, String playerId, Collection<PhysicalCard> cards) {
            super(playerId, Filters.in(cards));
            _action = action;
            _playerId = playerId;
            _cards = cards;
        }

        @Override
        protected void afterCardPlayed(PhysicalCard cardPlayed) {
            _action.insertEffect(
                    new PlayAnyNumberOfCardsFromDeckEffect(_action, _playerId, _cards));
        }
    }
}
