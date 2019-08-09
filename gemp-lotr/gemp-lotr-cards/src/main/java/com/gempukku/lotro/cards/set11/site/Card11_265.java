package com.gempukku.lotro.cards.set11.site;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Shadows
 * Twilight Cost: 2
 * Type: Site
 * Game Text: Underground. Each unwounded [GONDOR] Man is defender +1.
 */
public class Card11_265 extends AbstractShadowsSite {
    public Card11_265() {
        super("Window on the West", 2, Direction.LEFT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new KeywordModifier(self, Filters.and(Culture.GONDOR, Race.MAN, Filters.unwounded), Keyword.DEFENDER, 1));
}
}
