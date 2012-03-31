package com.gempukku.lotro.cards.set11.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PutCardFromDeckIntoHandEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 3
 * Site: 3
 * Game Text: When you play Úlairë Otsëa, you may spot a companion who has X wounds to reveal the top X cards of your
 * draw deck. Take each [WRAITH] card revealed this way into hand.
 */
public class Card11_224 extends AbstractMinion {
    public Card11_224() {
        super(4, 9, 3, 3, Race.NAZGUL, Culture.WRAITH, "Úlairë Otsëa", "Seventh of the Nine Riders", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, CardType.COMPANION, Filters.wounded)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose a wounded companion to spot", CardType.COMPANION, Filters.wounded) {
                        @Override
                        protected void cardSelected(final LotroGame game, PhysicalCard card) {
                            int wounds = game.getGameState().getWounds(card);
                            action.appendEffect(
                                    new RevealTopCardsOfDrawDeckEffect(self, playerId, wounds) {
                                        @Override
                                        protected void cardsRevealed(List<PhysicalCard> cards) {
                                            for (PhysicalCard wraithCard : Filters.filter(cards, game.getGameState(), game.getModifiersQuerying(), Culture.WRAITH)) {
                                                action.appendEffect(
                                                        new PutCardFromDeckIntoHandEffect(wraithCard));
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
