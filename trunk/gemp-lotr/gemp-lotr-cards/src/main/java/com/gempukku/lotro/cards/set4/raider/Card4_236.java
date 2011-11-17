package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Each time a companion or ally loses a skirmish involving a Southron, you may
 * remove (4) to make the Free Peoples Player wound a Ring-bound companion.
 */
public class Card4_236 extends AbstractPermanent {
    public Card4_236() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.RAIDER, Zone.SUPPORT, "Howl of Harad");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (game.getGameState().getTwilightPool() >= 4
                && TriggerConditions.losesSkirmishAgainst(game, effectResult, Filters.or(CardType.COMPANION, CardType.ALLY), Keyword.SOUTHRON)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(4));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION, Keyword.RING_BOUND));
            return Collections.singletonList(action);
        }
        return null;
    }
}
