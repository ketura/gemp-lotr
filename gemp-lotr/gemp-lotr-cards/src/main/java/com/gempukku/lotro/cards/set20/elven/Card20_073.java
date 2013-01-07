package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Arwen, Bane of the Black Riders
 * Elven    Companion • Elf
 * 6	3	8
 * Ranger.
 * At the start of the regroup phase, you may exert Arwen to reveal the top card of your draw deck. If it is
 * an [Elven] card, you may wound a minion (or a Nazgul twice).
 */
public class Card20_073 extends AbstractCompanion {
    public Card20_073() {
        super(2, 6, 3, 8, Culture.ELVEN, Race.ELF, null, "Arwen", "Bane of the Black Riders", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)
                && PlayConditions.canSelfExert(self, game)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                            for (final PhysicalCard revealedCard : revealedCards) {
                                if (Filters.and(Culture.ELVEN).accepts(game.getGameState(), game.getModifiersQuerying(), revealedCard)) {
                                    action.appendEffect(
                                            new ChooseActiveCardEffect(self, playerId, "Choose a minion to wound", Filters.canTakeWound, CardType.MINION) {
                                                @Override
                                                protected void cardSelected(LotroGame game, PhysicalCard minion) {
                                                    action.appendEffect(
                                                            new WoundCharactersEffect(self, revealedCard));
                                                    if (Filters.and(Race.NAZGUL).accepts(game.getGameState(), game.getModifiersQuerying(), minion))
                                                        action.appendEffect(
                                                                new WoundCharactersEffect(self, revealedCard));
                                                }
                                            });
                                }
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
