package com.gempukku.lotro.cards.set11.site;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Shadows
 * Twilight Cost: 1
 * Type: Site
 * Game Text: Plains. Each [MEN] minion gains ambush 1.
 */
public class Card11_253 extends AbstractShadowsSite {
    public Card11_253() {
        super("Pelennor Fields", 1, Direction.RIGHT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new KeywordModifier(self, Filters.and(Culture.MEN, CardType.MINION), Keyword.AMBUSH, 1));
}
}
