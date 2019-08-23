package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Ten Thousand Strong
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Condition - Site
 * Card Number: 1R140
 * Game Text: To play, exert an Uruk-hai. Uruk-hai are fierce while at this site.
 * Shadow: Spot an Uruk-hai and discard this condition to play an Uruk-hai from your discard pile.
 */
public class Card40_140 extends AbstractAttachable {
    public Card40_140() {
        super(Side.SHADOW, CardType.CONDITION, 0, Culture.ISENGARD, null, "Ten Thousand Strong", null, true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.URUK_HAI);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return CardType.SITE;
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, Race.URUK_HAI));
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        KeywordModifier modifier = new KeywordModifier(self, Race.URUK_HAI, new LocationCondition(Filters.hasAttached(self)), Keyword.FIERCE, 1);
        return Collections.singletonList(modifier);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSpot(game, Race.URUK_HAI)
                && PlayConditions.canSelfDiscard(self, game)
                && PlayConditions.canPlayFromDiscard(playerId, game, Race.URUK_HAI)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Race.URUK_HAI));
            return Collections.singletonList(action);
        }
        return null;
    }
}
