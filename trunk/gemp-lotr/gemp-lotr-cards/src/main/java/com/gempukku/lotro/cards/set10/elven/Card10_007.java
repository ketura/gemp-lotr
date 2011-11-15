package com.gempukku.lotro.cards.set10.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: At the start of each skirmish involving Celeborn, you may reveal the top card of your draw deck. If it is
 * an [ELVEN] card, you may discard it to wound each minion in that skirmish.
 */
public class Card10_007 extends AbstractCompanion {
    public Card10_007() {
        super(2, 6, 3, Culture.ELVEN, Race.ELF, null, "Celeborn", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && Filters.inSkirmish.accepts(game.getGameState(), game.getModifiersQuerying(), self)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> cards) {
                            for (final PhysicalCard card : cards) {
                                if (card.getBlueprint().getCulture() == Culture.ELVEN) {
                                    action.insertCost(
                                            new OptionalEffect(action, playerId,
                                                    new UnrespondableEffect() {
                                                        @Override
                                                        public String getText(LotroGame game) {
                                                            return "Discard revealed card";
                                                        }

                                                        @Override
                                                        protected void doPlayEffect(LotroGame game) {
                                                            action.insertCost(
                                                                    new DiscardCardFromDeckEffect(card));
                                                            action.appendEffect(
                                                                    new WoundCharactersEffect(self, CardType.MINION, Filters.inSkirmish));
                                                        }
                                                    }));
                                }
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
