package com.gempukku.lotro.cards.set32.gundabad;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Mirkwood
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Each time the fellowship moves, add (1) for each ally you can spot (limit (3)). Maneuver:
 * Discard this condition to wound a [DWARVEN] ally (or discard a [DWARVEN] possession instead if you cannot
 * spot a [DWARVEN] ally).
 */
public class Card32_026 extends AbstractPermanent {
    public Card32_026() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.GUNDABAD, "War for Gold", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int allies = Filters.countActive(game, CardType.ALLY);
            int twilight = Math.min(3, allies);
            action.appendEffect(
                    new AddTwilightEffect(self, twilight));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            if (Filters.canSpot(game, Culture.DWARVEN, CardType.ALLY)) {
                action.appendEffect(
                        new ChooseAndWoundCharactersEffect(action, self.getOwner(), 1, 1, 1, Culture.DWARVEN, CardType.ALLY));
            } else {
                action.appendEffect(
                        new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.DWARVEN, CardType.POSSESSION));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
