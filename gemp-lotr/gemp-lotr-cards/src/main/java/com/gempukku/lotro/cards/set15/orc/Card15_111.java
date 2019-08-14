package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.ShuffleDeckEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 2
 * Site: 4
 * Game Text: Skirmish: Exert this minion to make the Free Peoples player shuffle his or her draw deck and reveal
 * the top card of that draw deck. Make this minion strength +X, where X is the twilight cost of the revealed card.
 */
public class Card15_111 extends AbstractMinion {
    public Card15_111() {
        super(3, 7, 2, 4, Race.ORC, Culture.ORC, "Moria Menace");
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ShuffleDeckEffect(game.getGameState().getCurrentPlayerId()));
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, game.getGameState().getCurrentPlayerId(), 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                            for (PhysicalCard card : revealedCards) {
                                int twilightCost = card.getBlueprint().getTwilightCost();

                                action.appendEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new StrengthModifier(self, self, twilightCost)));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
