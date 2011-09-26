package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Each time a companion or ally loses a skirmish that involves a [SAURON] Orc,
 * each [SAURON] Orc is strength +1 until the regroup phase.
 */
public class Card1_263 extends AbstractPermanent {
    public Card1_263() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, Zone.SHADOW_SUPPORT, "Orc Banner");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.losesSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.type(CardType.ALLY), Filters.type(CardType.COMPANION)))
                && PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.culture(Culture.SAURON), Filters.race(Race.ORC)))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self, null, "Each [SAURON] Orc is strength +1 until the regroup phase.");
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.and(Filters.culture(Culture.SAURON), Filters.race(Race.ORC)), 1), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
