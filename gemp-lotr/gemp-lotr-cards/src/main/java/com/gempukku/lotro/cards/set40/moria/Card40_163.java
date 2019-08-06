package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndStackCardFromPlayEffect;
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
 * Title: *Goblin Messenger
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Minion - Goblin
 * Strength: 5
 * Vitality: 2
 * Home: 4
 * Card Number: 1U163
 * Game Text: When you play this minion, you may stack a Goblin on a [MORIA] condition.
 */
public class Card40_163 extends AbstractMinion {
    public Card40_163() {
        super(2, 5, 2, 4, Race.GOBLIN, Culture.MORIA, "Goblin Messenger", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndStackCardFromPlayEffect(action, playerId, Filters.and(Culture.MORIA, CardType.CONDITION), Race.GOBLIN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
