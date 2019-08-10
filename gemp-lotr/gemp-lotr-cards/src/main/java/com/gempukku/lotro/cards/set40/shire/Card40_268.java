package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.MakeRingBearerEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Sam, Humble Halfling
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Companion - Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 9
 * Card Number: 1R268
 * Game Text: Ring-bound. While bearing The One Ring, Sam is resistance -4.
 * At the start of each of your turns, you may exert Sam to remove a burden.
 * Response: If Frodo dies, make Sam the Ring-bearer.
 */
public class Card40_268 extends AbstractCompanion {
    public Card40_268() {
        super(2, 3, 4, 9, Culture.SHIRE, Race.HOBBIT, null, "Sam",
                "Humble Halfling", true);
        addKeyword(Keyword.RING_BOUND);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        ResistanceModifier modifier = new ResistanceModifier(self, Filters.and(self, Filters.hasAttached(CardType.THE_ONE_RING)), -4);
        return Collections.singletonList(modifier);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachKilled(game, effectResult, Filters.frodo, Filters.ringBearer)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(new MakeRingBearerEffect(self));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.startOfTurn(game, effectResult)
                && PlayConditions.canSelfExert(self, game)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new RemoveBurdenEffect(playerId, self));
            return Collections.singletonList(action);
        }

        return null;
    }
}
