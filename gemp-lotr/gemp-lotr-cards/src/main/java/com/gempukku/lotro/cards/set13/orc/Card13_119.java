package com.gempukku.lotro.cards.set13.orc;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 6
 * Vitality: 1
 * Site: 4
 * Game Text: While this minion is at an underground site, it cannot take wounds.
 */
public class Card13_119 extends AbstractMinion {
    public Card13_119() {
        super(2, 6, 1, 4, Race.ORC, Culture.ORC, "Underdeeps Denizen");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new CantTakeWoundsModifier(self, new LocationCondition(Keyword.UNDERGROUND), self));
}
}
