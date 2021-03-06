package com.gempukku.lotro.cards.set8.sauron;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.ReturnCardsToHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.SAURON, "Called Away");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canDiscardFromPlay(self, game, Culture.SAURON, CardType.MINION)
                && PlayConditions.canSpot(game, CardType.COMPANION, Filters.not(Filters.ringBearer), Filters.lessVitalityThan(game.getGameState().getBurdens() + 1))
                && PlayConditions.canRemoveBurdens(game, self, 1)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.SAURON, CardType.MINION));
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose a companion", CardType.COMPANION, Filters.not(Filters.ringBearer), Filters.lessVitalityThan(game.getGameState().getBurdens() + 1)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            int vitality = game.getModifiersQuerying().getVitality(game, card);
                            if (vitality > 0)
                                action.insertCost(
                                        new RemoveBurdenEffect(playerId, self, vitality));
                            action.appendEffect(
                                    new ReturnCardsToHandEffect(self, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
