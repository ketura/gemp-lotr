package com.gempukku.lotro.cards.set19.wraith;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.AndCondition;
import com.gempukku.lotro.logic.modifiers.condition.CanSpotTwilightCondition;

/**
 * Set: Ages End
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 6
 * Type: Minion • Nazgul
 * Strength: 12
 * Vitality: 3
 * Site: 3
 * Game Text: Fierce. While you can spot 5 twilight tokens in the twilight pool and another [WRAITH] card, Ulaire Attea
 * is strength +3.
 */
public class Card19_035 extends AbstractMinion {
    public Card19_035() {
        super(6, 12, 3, 3, Race.NAZGUL, Culture.WRAITH, Names.attea, "Dark Predator", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public java.util.List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return java.util.Collections.singletonList(new StrengthModifier(self, self,
new AndCondition(
new CanSpotTwilightCondition(5),
new SpotCondition(Filters.not(self), Culture.WRAITH)
), 3));
}
}
