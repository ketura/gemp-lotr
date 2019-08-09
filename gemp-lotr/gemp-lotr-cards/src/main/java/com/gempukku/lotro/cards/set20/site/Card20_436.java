package com.gempukku.lotro.cards.set20.site;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;

/**
 * Hollin
 * 4	3
 * Plains.
 * Uruk-hai are not roaming.
 */
public class Card20_436 extends AbstractSite {
    public Card20_436() {
        super("Hollin", SitesBlock.SECOND_ED, 4, 3, null);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new RemoveKeywordModifier(self, Race.URUK_HAI, Keyword.ROAMING));
}
}
