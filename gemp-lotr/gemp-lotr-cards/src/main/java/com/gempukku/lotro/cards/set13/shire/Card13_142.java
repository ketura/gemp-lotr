package com.gempukku.lotro.cards.set13.shire;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 8
 * Game Text: Ring-bound. While Bilbo is not assigned to a skirmish, each Ring-bound Hobbit is strength +1. At the start
 * of each skirmish involving Bilbo, you may remove a burden for each other Ring-bound Hobbit you can spot.
 */
public class Card13_142 extends AbstractCompanion {
    public Card13_142() {
        super(2, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Bilbo", "Aged Ring-bearer", true);
        addKeyword(Keyword.RING_BOUND);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, Filters.and(Race.HOBBIT, Keyword.RING_BOUND), new SpotCondition(self, Filters.notAssignedToSkirmish), 1));
}

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && PlayConditions.canSpot(game, self, Filters.inSkirmish)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int otherRingBoundHobbitCount = Filters.countActive(game, Filters.not(self), Race.HOBBIT, Keyword.RING_BOUND);
            if (otherRingBoundHobbitCount > 0)
                action.appendEffect(
                        new RemoveBurdenEffect(playerId, self, otherRingBoundHobbitCount));
            return Collections.singletonList(action);
        }
        return null;
    }
}
