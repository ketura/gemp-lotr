package com.gempukku.lotro.cards.set31.spider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Spider
 * Twilight Cost: 4
 * Type: Minion • Spider
 * Strength: 8
 * Vitality: 3
 * Site: 5
 * Game Text: Fierce. When you play this minion, you may remove (1) to play a Spider or an Orc from your 
 * discard pile.
 */
public class Card31_061 extends AbstractMinion {
    public Card31_061() {
        super(4, 8, 3, 5, Race.SPIDER, Culture.GUNDABAD, "Old Tomnoddy");
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canPlayFromDiscard(playerId, game, 1, 0, Filters.or(Race.SPIDER, Race.ORC))
				&& (game.getGameState().getTwilightPool() > 0)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(1));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.or(Race.SPIDER, Race.ORC)));
            return Collections.singletonList(action);
        }
        return null;
    }
}