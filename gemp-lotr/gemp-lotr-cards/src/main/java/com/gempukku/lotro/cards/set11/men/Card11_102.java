package com.gempukku.lotro.cards.set11.men;

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
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: Each time this minion is assigned to skirmish a character who has resistance 5 or less, it is fierce until
 * the regroup phase.
 */
public class Card11_102 extends AbstractMinion {
    public Card11_102() {
        super(4, 11, 3, 4, Race.MAN, Culture.MEN, "Throng of Harad");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.assignedAgainst(game, effectResult, null, Filters.and(Filters.character, Filters.maxResistance(5)), self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, self, Keyword.FIERCE), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
