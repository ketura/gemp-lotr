package com.gempukku.lotro.cards.set12.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 12
 * Vitality: 3
 * Site: 4
 * Game Text: Each time you play an [ORC] possession on an [ORC] Orc, you may add (1) (or (2) if that Orc is at a
 * battleground or underground site).
 */
public class Card12_091 extends AbstractMinion {
    public Card12_091() {
        super(5, 12, 3, 4, Race.ORC, Culture.ORC, "Orc Artisan");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.playedOn(game, effectResult, Filters.and(Culture.ORC, Race.ORC), Culture.ORC, CardType.POSSESSION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int count = PlayConditions.location(game, Filters.or(Keyword.BATTLEGROUND, Keyword.UNDERGROUND)) ? 2 : 1;
            action.appendEffect(
                    new AddTwilightEffect(self, count));
            return Collections.singletonList(action);
        }
        return null;
    }
}
