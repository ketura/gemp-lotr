package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Culture;
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
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Minion • Orc
 * Strength: 5
 * Vitality: 1
 * Site: 4
 * Game Text: When you play this minion, you may add (2).
 */
public class Card1_178 extends AbstractMinion {
    public Card1_178() {
        super(1, 5, 1, 4, Race.ORC, Culture.MORIA, "Goblin Runner");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.sameCard(self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(new AddTwilightEffect(self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
