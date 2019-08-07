package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Orc Miscreant
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion - Orc
 * Strength: 8
 * Vitality: 3
 * Home: 6
 * Card Number: 1C228
 * Game Text: At the start of each skirmish involving this minion, you may remove a threat and spot another [SAURON] Orc
 * to wound a companion this minion is skirmishing.
 */
public class Card40_228 extends AbstractMinion {
    public Card40_228() {
        super(3, 8, 3, 6, Race.ORC, Culture.SAURON, "Orc Miscreant");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && PlayConditions.canSpot(game, self, Filters.inSkirmish)
                && PlayConditions.canRemoveThreat(game, self, 1)
                && PlayConditions.canSpot(game, self, Culture.SAURON, Race.ORC, Filters.not(self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.inSkirmishAgainst(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
