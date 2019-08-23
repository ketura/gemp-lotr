package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndStackCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Nine for Mortal Men
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 0
 * Type: Artifact - Support Area
 * Card Number: 1R193
 * Game Text: Shadow: Spot a Nazgul to stack a [RINGWRAITH] card here.
 * Response: If a Nazgul is about to take a wound, discard 2 cards stacked here to prevent that wound.
 * Regroup: Discard 3 cards stacked here to take a [RINGWRAITH] card from your discard pile into hand.
 */
public class Card40_193 extends AbstractPermanent {
    public Card40_193() {
        super(Side.SHADOW, 0, CardType.ARTIFACT, Culture.WRAITH, "Nine for Mortal Men", null, true);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSpot(game, Race.NAZGUL)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndStackCardsFromHandEffect(action, playerId, 1, 1, self, Culture.WRAITH));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canDiscardFromStacked(self, game, playerId, 3, self, Filters.any)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 3, 3, self, Filters.any));
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.WRAITH));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Race.NAZGUL)
                && PlayConditions.canDiscardFromStacked(self, game, playerId, 2, self, Filters.any)) {
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;

            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 2, 2, self, Filters.any));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, woundEffect, playerId, "Choose Nazgul to prevent wound to", Race.NAZGUL));
            return Collections.singletonList(action);
        }
        return null;
    }
}
