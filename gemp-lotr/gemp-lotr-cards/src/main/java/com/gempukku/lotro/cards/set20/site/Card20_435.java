package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.CantReplaceSiteModifier;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * Eastern Emyn Muil
 * 4	4
 * Mountain.
 * Until the end of the game, sites in this region may not be replaced.
 */
public class Card20_435 extends AbstractSite {
    public Card20_435() {
        super("Eastern Emyn Muil", Block.SECOND_ED, 4, 4, null);
        addKeyword(Keyword.MOUNTAIN);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (self.getWhileInZoneData() == null) {
            int region = GameUtils.getRegion(self.getSiteNumber());
            self.setWhileInZoneData(new Object());
            game.getModifiersEnvironment().addAlwaysOnModifier(
                    new CantReplaceSiteModifier(self, null, Filters.and(CardType.SITE, Zone.ADVENTURE_PATH, Filters.region(region))));
        }
        return null;
    }
}
