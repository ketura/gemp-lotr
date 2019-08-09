package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 *
 * Mount Doom
 * 9	9
 * Mountain.
 * When the fellowship moves to Mount Doom, the Shadow player may remove 2 threats to add 2 burdens.
 */
public class Card20_466 extends AbstractSite {
    public Card20_466() {
        super("Mount Doom", SitesBlock.SECOND_ED, 9, 9, null);
        addKeyword(Keyword.MOUNTAIN);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)
                && GameUtils.isShadow(game, playerId)
                && PlayConditions.canRemoveThreat(game, self, 2)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 2));
            action.appendEffect(
                    new AddBurdenEffect(playerId, self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
