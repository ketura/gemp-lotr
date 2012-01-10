package com.gempukku.lotro.cards.set13.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 14
 * Vitality: 3
 * Site: 4
 * Game Text: At the end of each skirmish involving this minion, if you can spot a Free Peoples culture token, this
 * minion is fierce until the regroup phase.
 */
public class Card13_113 extends AbstractMinion {
    public Card13_113() {
        super(5, 14, 3, 4, Race.ORC, Culture.ORC, "Orc Line-breaker");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.endOfPhase(game, effectResult, Phase.SKIRMISH)
                && PlayConditions.canSpot(game, self, Filters.inSkirmish)
                && PlayConditions.canSpot(game, Side.FREE_PEOPLE, Filters.hasAnyCultureTokens(1))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, self, Keyword.FIERCE), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
