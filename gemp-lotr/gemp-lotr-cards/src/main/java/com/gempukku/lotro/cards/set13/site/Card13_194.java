package com.gempukku.lotro.cards.set13.site;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.SpecialFlagModifier;

/**
 * Set: Bloodlines
 * Twilight Cost: 2
 * Type: Site
 * Game Text: Mountain. Culture tokens cannot be added, removed, or reinforced.
 */
public class Card13_194 extends AbstractShadowsSite {
    public Card13_194() {
        super("Redhorn Pass", 2, Direction.LEFT);
        addKeyword(Keyword.MOUNTAIN);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new SpecialFlagModifier(self, ModifierFlag.CANT_TOUCH_CULTURE_TOKENS));
}
}
