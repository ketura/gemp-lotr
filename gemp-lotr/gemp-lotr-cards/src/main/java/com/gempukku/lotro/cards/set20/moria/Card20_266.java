package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Goblin Reclaimer
 * Moria	Minion • Goblin
 * 6	2	4
 * When you play this minion at an underground site, you may play a [Moria] condition from your discard pile.
 */
public class Card20_266 extends AbstractMinion {
    public Card20_266() {
        super(2, 6, 2, 4, Race.GOBLIN, Culture.MORIA, "Goblin Reclaimer");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.location(game, Keyword.UNDERGROUND)
            && PlayConditions.canPlayFromDiscard(playerId, game, Culture.MORIA, CardType.CONDITION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.MORIA, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
