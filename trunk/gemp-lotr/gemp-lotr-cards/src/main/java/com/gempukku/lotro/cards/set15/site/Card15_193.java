package com.gempukku.lotro.cards.set15.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.modifiers.CantReplaceSiteModifier;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * Set: The Hunters
 * Twilight Cost: 3
 * Type: Site
 * Game Text: Battleground. Mountain. Underground. Until the end of the game, sites in this region cannot be replaced.
 */
public class Card15_193 extends AbstractNewSite {
    public Card15_193() {
        super("Mount Doom", 3, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
        addKeyword(Keyword.MOUNTAIN);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (self.getWhileInZoneData() == null) {
            int region = 1 + ((self.getSiteNumber() - 1) / 3);
            self.setWhileInZoneData(new Object());
            game.getModifiersEnvironment().addAlwaysOnModifier(
                    new CantReplaceSiteModifier(self, null, Filters.region(region)));
        }
        return null;
    }
}
