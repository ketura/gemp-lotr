package com.gempukku.lotro.cards.set19.gondor;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ReturnCardsToHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndTransferAttachableEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ages End
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Condition
 * Strength: +1
 * Game Text: Bearer must be a [GONDOR] Man. Each time bearer wins a skirmish, you may transfer this condition
 * to another [GONDOR] Man. At the end of each turn, if bearer is not Aragorn, return this condition to hand.
 */
public class Card19_014 extends AbstractAttachable {
    public Card19_014() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 2, Culture.GONDOR, null, "Not Bound To His Fate");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.hasAttached(self), 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self.getAttachedTo())) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndTransferAttachableEffect(action, playerId, self, Filters.any, Filters.and(Culture.GONDOR, Race.MAN)));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.END_OF_TURN
                && !PlayConditions.canSpot(game, Filters.aragorn, Filters.hasAttached(self))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ReturnCardsToHandEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
