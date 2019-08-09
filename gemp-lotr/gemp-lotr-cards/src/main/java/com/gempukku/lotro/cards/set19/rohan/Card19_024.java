package com.gempukku.lotro.cards.set19.rohan;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.ReturnCardsToHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndTransferAttachableEffect;
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
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Condition
 * Strength: +1
 * Game Text: Bearer must be a [ROHAN] Man. At the start of each skirmish you may transfer this condition to
 * a [ROHAN] Man. At the end of each turn, if bearer did not win a skirmish this turn, return this condition to hand.
 */
public class Card19_024 extends AbstractAttachable {
    public Card19_024() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 2, Culture.ROHAN, null, "Brought Down From Inside");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, Filters.hasAttached(self), 1));
}

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndTransferAttachableEffect(action, playerId, self, Filters.any, Filters.and(Culture.ROHAN, Race.MAN)));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.endOfTurn(game, effectResult)
                && !game.getActionsEnvironment().hasWonSkirmishThisTurn(game, self.getAttachedTo())) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ReturnCardsToHandEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
