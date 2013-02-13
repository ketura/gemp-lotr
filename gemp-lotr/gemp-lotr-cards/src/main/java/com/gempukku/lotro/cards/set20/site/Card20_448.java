package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Minas Tirith Balcony
 * 6	3
 * Battleground. Sanctuary.
 * When the fellowship moves to Minas Tirith Balcony, the Free Peoples player may spot Gandalf to draw a card for each threat.
 */
public class Card20_448 extends AbstractSite {
    public Card20_448() {
        super("Minas Tirith Balcony", Block.SECOND_ED, 6, 3, null);
        addKeyword(Keyword.BATTLEGROUND);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)
                && GameUtils.isFP(game, playerId)
                && PlayConditions.canSpot(game, Filters.gandalf)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, game.getGameState().getThreats()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
