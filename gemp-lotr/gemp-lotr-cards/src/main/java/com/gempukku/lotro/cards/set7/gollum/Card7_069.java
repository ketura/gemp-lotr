package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.MoveLimitModifier;
import com.gempukku.lotro.logic.modifiers.cost.AddThreatExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.SpotExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: To play, spot Smeagol and add 2 threats. The move limit for this turn is +1. Discard this condition
 * at the end of the turn.
 */
public class Card7_069 extends AbstractPermanent {
    public Card7_069() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GOLLUM, "Secret Paths");
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new SpotExtraPlayCostModifier(self,self, null, Filters.smeagol),
                new AddThreatExtraPlayCostModifier(self, 2, null, self));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (game.getModifiersQuerying().getUntilEndOfTurnLimitCounter(self).getUsedLimit() < 1) {
                game.getModifiersEnvironment().addUntilEndOfTurnModifier(
                        new MoveLimitModifier(self,1));
                game.getModifiersQuerying().getUntilEndOfTurnLimitCounter(self).incrementToLimit(1, 1);
        }

        if (TriggerConditions.endOfTurn(game, effectResult)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
