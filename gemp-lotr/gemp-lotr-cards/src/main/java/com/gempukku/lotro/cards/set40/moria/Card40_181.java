package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Troll's Keyward, Ruthless Taskmaster
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 3
 * Type: Minion - Goblin
 * Strength: 8
 * Vitality: 3
 * Home: 4
 * Card Number: 1R181
 * Game Text: If this minion is at an underground site, the Cave Troll of Moria's twilight cost is -3.
 * Skirmish: Exert this minion to make the Cave Troll of Moria strength +2.
 */
public class Card40_181 extends AbstractMinion {
    public Card40_181() {
        super(3, 8, 3, 4, Race.GOBLIN, Culture.MORIA, "Troll's Keyward", "Ruthless Taskmaster", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        TwilightCostModifier modifier = new TwilightCostModifier(self, Filters.name("Cave Troll of Moria"),
                new LocationCondition(Keyword.UNDERGROUND), -3);
        return Collections.singletonList(modifier);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Filters.name("Cave Troll of Moria")));
            return Collections.singletonList(action);
        }
        return null;
    }
}
