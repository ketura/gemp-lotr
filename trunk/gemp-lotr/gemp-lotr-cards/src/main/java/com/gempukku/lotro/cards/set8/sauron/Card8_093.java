package com.gempukku.lotro.cards.set8.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.ReturnCardsToHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Regroup: Discard a [SAURON] minion, spot a companion (except the Ring-bearer), and remove X burdens,
 * where X is that companion’s vitality, to return that companion to its owner’s hand.
 */
public class Card8_093 extends AbstractPermanent {
    public Card8_093() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "Called Away");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canDiscardFromPlay(self, game, Culture.SAURON, CardType.MINION)
                && PlayConditions.canSpot(game, CardType.COMPANION, Filters.not(Keyword.RING_BEARER), Filters.lessVitalityThan(game.getGameState().getBurdens() + 1))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.SAURON, CardType.MINION));
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose a companion", CardType.COMPANION, Filters.not(Keyword.RING_BEARER), Filters.lessVitalityThan(game.getGameState().getBurdens() + 1)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            int vitality = game.getModifiersQuerying().getVitality(game.getGameState(), card);
                            for (int i = 0; i < vitality; i++)
                                action.insertCost(
                                        new RemoveBurdenEffect(self));
                            action.appendEffect(
                                    new ReturnCardsToHandEffect(self, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
