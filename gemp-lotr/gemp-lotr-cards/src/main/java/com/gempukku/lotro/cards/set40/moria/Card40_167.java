package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
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
 * Title: *Goblin Reclaimer
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Minion - Goblin
 * Strength: 6
 * Vitality: 2
 * Home: 4
 * Card Number: 1R167
 * Game Text: When you play this minion, you may spot another [MORIA] Goblin to play a [MORIA] condition from your discard pile.
 */
public class Card40_167 extends AbstractMinion {
    public Card40_167() {
        super(2, 6, 2, 4, Race.GOBLIN, Culture.MORIA, "Goblin Reclaimer", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Culture.MORIA, Race.GOBLIN, Filters.not(self))
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.MORIA, CardType.CONDITION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.MORIA, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
