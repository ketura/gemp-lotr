package com.gempukku.lotro.cards.set11.site;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Shadows
 * Twilight Cost: 2
 * Type: Site
 * Game Text: Battleground. Each character bearing a hand weapon is strength +2.
 */
public class Card11_245 extends AbstractShadowsSite {
    public Card11_245() {
        super("Helm's Gate", 2, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, Filters.and(Filters.character, Filters.hasAttached(PossessionClass.HAND_WEAPON)), 2));
}
}
