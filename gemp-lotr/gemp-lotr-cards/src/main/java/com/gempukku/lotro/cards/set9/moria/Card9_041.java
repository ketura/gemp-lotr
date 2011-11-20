package com.gempukku.lotro.cards.set9.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 7
 * Type: Minion • Orc
 * Strength: 15
 * Vitality: 3
 * Site: 4
 * Game Text: When you play Host of Moria, you may play a [MORIA] card from your discard pile.
 */
public class Card9_041 extends AbstractMinion {
    public Card9_041() {
        super(7, 15, 3, 4, Race.ORC, Culture.MORIA, "Host of Moria", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.MORIA, Filters.not(CardType.EVENT, Keyword.RESPONSE))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.MORIA, Filters.not(CardType.EVENT, Keyword.RESPONSE)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
