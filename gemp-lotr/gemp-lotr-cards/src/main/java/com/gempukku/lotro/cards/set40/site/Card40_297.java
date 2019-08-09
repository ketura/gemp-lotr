package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.modifiers.evaluator.ForEachThreatEvaluator;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Minas Tirith Balcony
 * Set: Second Edition
 * Side: None
 * Site Number: 6
 * Shadow Number: 3
 * Card Number: 1U297
 * Game Text: Battleground. Sanctuary. When the fellowship moves to this site, spot Gandalf to draw a card for each threat.
 */
public class Card40_297 extends AbstractSite {
    public Card40_297() {
        super("Minas Tirith Balcony", Block.SECOND_ED, 6, 3, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)
                && PlayConditions.canSpot(game, Filters.gandalf)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, GameUtils.getFreePeoplePlayer(game), new ForEachThreatEvaluator()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
