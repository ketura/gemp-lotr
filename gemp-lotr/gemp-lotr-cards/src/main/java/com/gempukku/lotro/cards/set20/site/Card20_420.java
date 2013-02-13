package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Trollshaw Forest
 * 1	0
 * Forest.
 * Each time you play an artifact or possession on your companion, you may draw a card.
 */
public class Card20_420 extends AbstractSite {
    public Card20_420() {
        super("Trollshaw Forest", Block.SECOND_ED, 1, 0, null);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.playedOn(game, effectResult, Filters.and(CardType.COMPANION, Filters.owner(playerId)), Filters.or(CardType.POSSESSION, CardType.ARTIFACT))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
