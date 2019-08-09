package com.gempukku.lotro.cards.set11.wraith;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 2
 * Site: 3
 * Game Text: Each character skirmishing Úlairë Lemenya who has resistance 5 or less is strength -3.
 */
public class Card11_221 extends AbstractMinion {
    public Card11_221() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, Names.lemenya, "Fifth of the Nine Riders", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, Filters.and(Filters.inSkirmishAgainst(self), Filters.maxResistance(5)), -3));
}
}
