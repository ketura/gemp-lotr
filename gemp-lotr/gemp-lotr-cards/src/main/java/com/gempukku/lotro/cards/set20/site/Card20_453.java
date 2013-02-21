package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Dimholt Road
 * 7	6
 * Mountain.
 * If you cannot spot Aragorn when the fellowship moves to Dimholt Road, add two threats.
 */
public class Card20_453 extends AbstractSite {
    public Card20_453() {
        super("Dimholt Road", Block.SECOND_ED, 7, 6, null);
        addKeyword(Keyword.MOUNTAIN);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)
                && !PlayConditions.canSpot(game, Filters.aragorn)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddThreatsEffect(game.getGameState().getCurrentPlayerId(), self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
