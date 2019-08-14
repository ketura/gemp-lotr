package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Title: It Will Destroy Them All
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition - Companion
 * Card Number: 1R221
 * Game Text: To play, exert a [SAURON] Orc. Limit 1 per companion.
 * Each time bearer is assigned to a skirmish, the Free Peoples player must add a burden or make bearer strength -2 until the regroup phase.
 */
public class Card40_221 extends AbstractAttachable {
    public Card40_221() {
        super(Side.SHADOW, CardType.CONDITION, 1, Culture.SAURON, null, "It Will Destroy Them All");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(CardType.COMPANION, Filters.not(Filters.hasAttached(Filters.name("It Will Destroy Them All"))));
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.SAURON, Race.ORC);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, Culture.SAURON, Race.ORC));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.assignedToSkirmish(game, effectResult, null, Filters.hasAttached(self))) {
            final String freePeoplePlayer = GameUtils.getFreePeoplePlayer(game);
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new ArrayList<Effect>(2);
            possibleEffects.add(
                    new AddBurdenEffect(freePeoplePlayer, self, 1));
            possibleEffects.add(
                    new AddUntilStartOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.hasAttached(self), -2), Phase.REGROUP) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Make bearer strength -2 until the regroun phase";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, freePeoplePlayer, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
