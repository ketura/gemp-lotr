package com.gempukku.lotro.cards.set11.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10
 * Game Text: Ring-bearer. Ring-bound. At the start of the regroup phase, you may exert Frodo to make him gain muster
 * until the end of the regroup phase. (At the start of the regroup phase, you may discard a card from hand to draw
 * a card.)
 */
public class Card11_164 extends AbstractCompanion {
    public Card11_164() {
        super(0, 3, 4, 10, Culture.SHIRE, Race.HOBBIT, null, "Frodo", true);
        addKeyword(Keyword.CAN_START_WITH_RING);
        addKeyword(Keyword.RING_BOUND);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)
                && PlayConditions.canSelfExert(self, game)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new KeywordModifier(self, self, Keyword.MUSTER), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
