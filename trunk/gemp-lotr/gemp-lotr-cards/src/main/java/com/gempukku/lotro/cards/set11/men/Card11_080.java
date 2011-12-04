package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 6
 * Type: Minion • Man
 * Strength: 12
 * Vitality: 3
 * Site: 4
 * Game Text: Archer. Each time a Free Peoples character is killed, each of your [MEN] minions is strength +3 until
 * the regroup phase.
 */
public class Card11_080 extends AbstractMinion {
    public Card11_080() {
        super(6, 12, 3, 4, Race.MAN, Culture.MEN, "Ferocious Haradrim");
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachKilled(game, effectResult, Side.FREE_PEOPLE, Filters.character)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.and(Filters.owner(self.getOwner()), Culture.MEN, CardType.MINION), 3), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
