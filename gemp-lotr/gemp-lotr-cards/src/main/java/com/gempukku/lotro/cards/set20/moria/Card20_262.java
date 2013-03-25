package com.gempukku.lotro.cards.set20.moria;

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
 * 2
 * •Goblin Messenger
 * Minion • Goblin
 * 5	1	4
 * When you play this minion, you may stack a Goblin on a [Moria] condition.
 * http://www.lotrtcg.org/coreset/moria/goblinmessenger(r2).jpg
 */
public class Card20_262 extends AbstractMinion {
    public Card20_262() {
        super(2, 5, 1, 4, Race.GOBLIN, Culture.MORIA, "Goblin Messenger", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndStackCardFromPlayEffect(action, playerId, Filters.and(Culture.MORIA, CardType.CONDITION), Race.GOBLIN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
