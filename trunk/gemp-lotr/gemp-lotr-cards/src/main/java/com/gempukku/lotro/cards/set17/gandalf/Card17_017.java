package com.gempukku.lotro.cards.set17.gandalf;

import com.gempukku.lotro.cards.*;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.*;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Wizard
 * Strength: 7
 * Vitality: 4
 * Resistance: 7
 * Game Text: When you play Gandalf (except in your starting fellowship), you may play a [GANDALF] possession on him
 * from your draw deck or discard pile. Each time Gandalf wins a skirmish, you may reinforce a Free Peoples token.
 */
public class Card17_017 extends AbstractCompanion {
    public Card17_017() {
        super(4, 7, 4, 7, Culture.GANDALF, Race.WIZARD, null, "Gandalf", "Returned", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && !PlayConditions.isPhase(game, Phase.PLAY_STARTING_FELLOWSHIP)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDeck(playerId),
                            Filters.and(
                                    Culture.GANDALF, CardType.POSSESSION,
                                    ExtraFilters.attachableTo(game, self)), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.iterator().next();
                                game.getActionsEnvironment().addActionToStack(((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, self, 0));
                            }
                        }

                        @Override
                        public String getText(LotroGame game) {
                            return "Play GANDALF possession on him from draw deck";
                        }
                    });
            possibleEffects.add(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDiscard(playerId),
                            Filters.and(
                                    Culture.GANDALF, CardType.POSSESSION,
                                    ExtraFilters.attachableTo(game, self)), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.iterator().next();
                                game.getActionsEnvironment().addActionToStack(((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, self, 0));
                            }
                        }

                        @Override
                        public String getText(LotroGame game) {
                            return "Play GANDALF possession on him from discard pile";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose card to reinforce", Side.FREE_PEOPLE, Filters.hasAnyCultureTokens(1)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            final Map<Token, Integer> tokens = game.getGameState().getTokens(card);
                            action.appendEffect(
                                    new AddTokenEffect(self, card, tokens.keySet().iterator().next()));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
