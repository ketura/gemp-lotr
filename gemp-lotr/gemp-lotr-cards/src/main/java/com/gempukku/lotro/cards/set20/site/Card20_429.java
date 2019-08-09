package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * Ford of Bruinen
 * 3	0
 * River. Sanctuary.
 * The twilight cost of the first Nazgul played at Ford of Bruinen each turn is -2.
 */
public class Card20_429 extends AbstractSite {
    public Card20_429() {
        super("Ford of Bruinen", SitesBlock.SECOND_ED, 3, 0, null);
        addKeyword(Keyword.RIVER);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, final PhysicalCard self) {
        return new TwilightCostModifier(self,
                Filters.and(
                        Race.NAZGUL,
                        new Filter() {
                            @Override
                            public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                return game.getModifiersQuerying().getUntilEndOfTurnLimitCounter(self).getUsedLimit() < 1;
                            }
                        }), -2);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Race.NAZGUL)
                && PlayConditions.location(game, self))
            game.getModifiersQuerying().getUntilEndOfTurnLimitCounter(self).incrementToLimit(1, 1);
        return null;
    }
}
