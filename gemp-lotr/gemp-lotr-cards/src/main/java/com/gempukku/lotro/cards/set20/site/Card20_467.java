package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.PutOnTheOneRingEffect;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Orodruin Chamber
 * 9	9
 * Mountain. Underground.
 * When the fellowship moves to Oroduin Chamber, the Ring-bearer wears The One Ring until the regroup phase.
 */
public class Card20_467 extends AbstractSite {
    public Card20_467() {
        super("Orodruin Chamber", SitesBlock.SECOND_ED, 9, 9, null);
        addKeyword(Keyword.MOUNTAIN);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)) {
            RequiredTriggerAction action= new RequiredTriggerAction(self);
            action.appendEffect(
                    new PutOnTheOneRingEffect());
            return Collections.singletonList(action);
        }
        return null;
    }
}
