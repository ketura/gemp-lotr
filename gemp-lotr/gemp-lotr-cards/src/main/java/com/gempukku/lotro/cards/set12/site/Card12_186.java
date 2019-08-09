package com.gempukku.lotro.cards.set12.site;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

/**
 * Set: Black Rider
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Battleground. Underground. The Balrog is twilight cost -3.
 */
public class Card12_186 extends AbstractShadowsSite {
    public Card12_186() {
        super("The Bridge of Khazad-dum", 0, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new TwilightCostModifier(self, Filters.balrog, -3));
}
}
