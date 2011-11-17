package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 1
 * Site: 3
 * Game Text: Each time this minion wins a skirmish, you may make him strength +2 and fierce until the regroup phase.
 */
public class Card4_016 extends AbstractMinion {
    public Card4_016() {
        super(3, 9, 1, 3, Race.MAN, Culture.DUNLAND, "Dunlending Robber");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.sameCard(self), 2), Phase.REGROUP));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, Filters.sameCard(self), Keyword.FIERCE), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
