package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Title: Relentless Pursuit
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 2
 * Type: Condition - Support Area
 * Card Number: 1R196
 * Game Text: To play, exert a Nazgul. Each site in the current region is a forest.
 * Shadow: Spot a Nazgul and discad this condition to play a Nazgul from your discard pile.
 */
public class Card40_196 extends AbstractPermanent {
    public Card40_196() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.WRAITH, "Relentless Pursuit");
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, Race.NAZGUL));
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new KeywordModifier(self, Filters.and(CardType.SITE, Filters.siteInCurrentRegion), Keyword.FOREST));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSpot(game, Race.NAZGUL)
                && PlayConditions.canSelfDiscard(self, game)
                && PlayConditions.canPlayFromDiscard(playerId, game, Race.NAZGUL)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Race.NAZGUL));
            return Collections.singletonList(action);
        }
        return null;
    }
}
