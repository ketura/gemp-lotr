package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Goblin Howler
 * Moria	Minion • Goblin
 * 5	2	4
 * Each time you play a [Moria] minion from a [Moria] condition, you may make this minion strength +1 until the regroup phase.
 */
public class Card20_261 extends AbstractMinion{
    public Card20_261() {
        super(2, 5, 2, 4, Race.GOBLIN, Culture.MORIA, "Goblin Howler");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.playedFromZone(game, effectResult, Zone.STACKED, Culture.MORIA, CardType.MINION)
                && Filters.and(Culture.MORIA, CardType.CONDITION).accepts(game.getGameState(), game.getModifiersQuerying(), ((PlayCardResult) effectResult).getAttachedOrStackedPlayedFrom())) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new StrengthModifier(self, self, 1), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
