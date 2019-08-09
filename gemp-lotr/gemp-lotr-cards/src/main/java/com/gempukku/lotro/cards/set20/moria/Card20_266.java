package com.gempukku.lotro.cards.set20.moria;

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
 * ❷ Goblin Reclaimer [Mor]
 * Minion • Goblin
 * Strength: 6   Vitality: 2   Roaming: 4
 * When you play this minion, you may spot another [Mor] Goblin to play a [Mor] condition from your discard pile.
 * <p/>
 * http://lotrtcg.org/coreset/moria/goblinreclaimer(r3).jpg
 */
public class Card20_266 extends AbstractMinion {
    public Card20_266() {
        super(2, 6, 2, 4, Race.GOBLIN, Culture.MORIA, "Goblin Reclaimer");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Filters.not(self), Culture.MORIA, Race.GOBLIN)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.MORIA, CardType.CONDITION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.MORIA, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
