package com.gempukku.lotro.cards.set7.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.modifiers.condition.InitiativeCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Twilight Cost: 3
 * Type: Site
 * Site: 2K
 * Game Text: Plains. If the Shadow has initiative, the Shadow number of this site is +3.
 */
public class Card7_337 extends AbstractSite {
    public Card7_337() {
        super("West Road", SitesBlock.KING, 2, 3, Direction.LEFT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new TwilightCostModifier(self, self, new InitiativeCondition(Side.SHADOW), 3));
    }
}
