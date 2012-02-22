package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.cards.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 10
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. At the start of the archery phase, if you control a site, you may add 2 to the minion archery
 * total for each Free Peoples culture you can spot over 2.
 */
public class Card17_121 extends AbstractMinion {
    public Card17_121() {
        super(4, 10, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Berserker");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.ARCHERY)
                && PlayConditions.canSpot(game, Filters.siteControlled(playerId))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int count = new CountCulturesEvaluator(2, Side.FREE_PEOPLE).evaluateExpression(game.getGameState(), game.getModifiersQuerying(), null);
            if (count > 0)
                action.appendEffect(
                        new AddUntilEndOfPhaseModifierEffect(
                                new ArcheryTotalModifier(self, Side.SHADOW, null, count), Phase.ARCHERY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
