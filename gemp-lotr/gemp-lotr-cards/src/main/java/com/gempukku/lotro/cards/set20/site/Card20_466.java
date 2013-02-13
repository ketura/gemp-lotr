package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Mount Doom
 * 9	9
 * Mountain.
 * When the fellowship moves to Mount Doom, add a burden for each threat you can spot.
 */
public class Card20_466 extends AbstractSite {
    public Card20_466() {
        super("Mount Doom", Block.SECOND_ED, 9, 9, null);
        addKeyword(Keyword.MOUNTAIN);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddThreatsEffect(game.getGameState().getCurrentPlayerId(), self, game.getGameState().getThreats()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
