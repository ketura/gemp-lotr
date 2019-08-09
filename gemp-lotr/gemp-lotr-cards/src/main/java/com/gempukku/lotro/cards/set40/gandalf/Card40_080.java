package com.gempukku.lotro.cards.set40.gandalf;

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
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.modifiers.condition.AndCondition;
import com.gempukku.lotro.logic.modifiers.condition.InitiativeCondition;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Title: *Keep it Secret, Keep it Safe
 * Set: Second Edition
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Condition - Support Area
 * Card Number: 1U80
 * Game Text: To play, exert Gandalf.
 * While you can spot Gandalf and have initiative, the twilight cost of each Free Peoples artifact, condition,
 * and possession is -1. Discard this condition if you lose initiative.
 */
public class Card40_080 extends AbstractPermanent {
    public Card40_080() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GANDALF, "Keep it Secret, Keep it Safe", null, true);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, Filters.gandalf));
    }

        @Override
        public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new TwilightCostModifier(
                        self, Filters.and(Side.FREE_PEOPLE, Filters.or(CardType.ARTIFACT, CardType.CONDITION, CardType.POSSESSION)),
                        new AndCondition(new SpotCondition(Filters.gandalf), new InitiativeCondition(Side.FREE_PEOPLE)),
                        -1));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.losesInitiative(effectResult, Side.FREE_PEOPLE)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
