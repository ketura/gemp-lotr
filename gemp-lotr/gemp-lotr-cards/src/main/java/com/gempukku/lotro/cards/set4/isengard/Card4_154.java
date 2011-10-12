package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.ReturnCardsToHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Minion • Man
 * Strength: 4
 * Vitality: 3
 * Site: 3
 * Game Text: Maneuver: Exert Grima and spot an unbound companion bearing 3 or more cards to return each Free Peoples
 * card that companion bears to its owner's hand.
 */
public class Card4_154 extends AbstractMinion {
    public Card4_154() {
        super(2, 4, 3, 3, Race.MAN, Culture.ISENGARD, "Grima", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.MANEUVER, self, 0)
                && PlayConditions.canExert(self, game, Filters.sameCard(self))
                && PlayConditions.canSpot(game, Filters.unboundCompanion(),
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return gameState.getAttachedCards(physicalCard).size() >= 3;
                    }
                })) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose unbound companion", Filters.unboundCompanion(), new Filter() {
                        @Override
                        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                            return gameState.getAttachedCards(physicalCard).size() >= 3;
                        }
                    }) {
                        @Override
                        protected void cardSelected(PhysicalCard card) {
                            action.insertEffect(
                                    new ReturnCardsToHandEffect(self, Filters.and(Filters.attachedTo(Filters.sameCard(card)), Filters.side(Side.FREE_PEOPLE))));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
