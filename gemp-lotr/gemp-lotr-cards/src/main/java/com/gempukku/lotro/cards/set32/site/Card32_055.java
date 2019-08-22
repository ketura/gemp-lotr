package com.gempukku.lotro.cards.set32.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Twilight Cost: 8
 * Type: Site
 * Site: 7
 * Game Text: Mountain. While you can spot Thrór's Key, the Shadow number of Secret Door is -3.
 */
public class Card32_055 extends AbstractSite {
    public Card32_055() {
        super("Secret Door", SitesBlock.HOBBIT, 7, 8, Direction.RIGHT);
        addKeyword(Keyword.MOUNTAIN);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new TwilightCostModifier(self, self, new SpotCondition(Filters.name("Thror's Key")), -3));
    }
}
