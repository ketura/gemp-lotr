package com.gempukku.lotro.cards.set8.gondor;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.AddThreatExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, add a threat. Each time your [GONDOR] Wraith wins a skirmish, you may remove a threat.
 */
public class Card8_047 extends AbstractPermanent {
    public Card8_047() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, "Stronger and More Terrible");
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new AddThreatExtraPlayCostModifier(self, 1, null, self));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.owner(playerId), Culture.GONDOR, Race.WRAITH)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new RemoveThreatsEffect(self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
