package com.gempukku.lotro.cards.set15.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
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
 * Set: The Hunters
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 7
 * Type: Companion • Ent
 * Strength: 8
 * Vitality: 4
 * Resistance: 6
 * Game Text: Skinbark is twilight cost -1 for each [GANDALF] condition you can spot. Each time Skinbark wins
 * a skirmish, you may place a [GANDALF] card from your discard pile beneath your draw deck.
 */
public class Card15_037 extends AbstractCompanion {
    public Card15_037() {
        super(7, 8, 4, 6, Culture.GANDALF, Race.ENT, null, "Skinbark", "Elder Ent", true);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return -Filters.countActive(gameState, modifiersQuerying, Culture.GANDALF, CardType.CONDITION);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardOnBottomOfDeckEffect(action, playerId, 1, 1, Culture.GANDALF));
            return Collections.singletonList(action);
        }
        return null;
    }
}
