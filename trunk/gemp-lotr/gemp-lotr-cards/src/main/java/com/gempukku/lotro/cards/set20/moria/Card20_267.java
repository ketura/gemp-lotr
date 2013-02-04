package com.gempukku.lotro.cards.set20.moria;

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
 * 4
 * •Goblin Reinforcer
 * Moria	Minion • Goblin
 * 10	3	4
 * When you play this minion, you may play a [Moria] minion from your discard pile for each companion over 5.
 */
public class Card20_267 extends AbstractMinion {
    public Card20_267() {
        super(4, 10, 3, 4, Race.GOBLIN, Culture.MORIA, "Goblin Reinforcer", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.MORIA, CardType.MINION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int companions = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION);
            int minions = Math.max(0, companions - 5);
            for (int i=0; i<minions; i++) {
                action.appendEffect(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.MORIA, CardType.MINION));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
