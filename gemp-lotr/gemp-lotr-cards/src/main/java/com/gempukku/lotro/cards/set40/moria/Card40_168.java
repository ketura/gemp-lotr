package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Goblin Reinforcements
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 4
 * Type: Minion - Goblin
 * Strength: 10
 * Vitality: 2
 * Home: 4
 * Card Number: 1U168
 * Game Text: When you play this minion, you may play a [MORIA] Goblin from your discard pile for each companion over 5.
 */
public class Card40_168 extends AbstractMinion {
    public Card40_168() {
        super(4, 10, 2, 4, Race.GOBLIN, Culture.MORIA, "Goblin Reinforcements", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        final int companionCount = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION);
        if (TriggerConditions.played(game, effectResult, self)
                && companionCount > 5
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.MORIA, Race.GOBLIN)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            for (int i = 5; i < companionCount; i++)
                action.appendEffect(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.MORIA, Race.GOBLIN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
