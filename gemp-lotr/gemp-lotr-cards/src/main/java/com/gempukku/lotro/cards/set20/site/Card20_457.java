package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Osgiliath Ruins
 * 7	6
 * Battleground.
 * When the fellowship moves to Osgiliath Ruins, the Shadow player may draw a card for each burden.
 */
public class Card20_457 extends AbstractSite {
    public Card20_457() {
        super("Osgiliath Ruins", SitesBlock.SECOND_ED, 7, 6, null);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)
                && GameUtils.isShadow(game, playerId)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, game.getGameState().getBurdens()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
