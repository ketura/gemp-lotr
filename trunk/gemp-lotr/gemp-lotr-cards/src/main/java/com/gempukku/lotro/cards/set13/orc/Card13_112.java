package com.gempukku.lotro.cards.set13.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.RevealCardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: To play, spot an [ORC] Orc. This minion is twilight cost -1 for each burden. Each time a Ring-bound
 * companion loses a skirmish involving an [ORC] minion, you may reveal this card from hand to add a burden.
 */
public class Card13_112 extends AbstractMinion {
    public Card13_112() {
        super(5, 11, 3, 4, Race.ORC, Culture.ORC, "Orc Crusher", null, true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ORC, Race.ORC);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return -gameState.getBurdens();
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggersFromHand(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.losesSkirmishInvolving(game, effectResult,
                Filters.and(CardType.COMPANION, Keyword.RING_BOUND),
                Filters.and(Culture.ORC, CardType.MINION))
                && !playerId.equals(game.getGameState().getCurrentPlayerId())) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RevealCardEffect(self, self));
            action.appendEffect(
                    new AddBurdenEffect(self.getOwner(), self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
