package com.gempukku.lotro.cards.set12.shire;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: While you can spot a Hobbit, each unbound companion is resistance +1.
 */
public class Card12_125 extends AbstractPermanent {
    public Card12_125() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.SHIRE, "Measure of Comfort");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new ResistanceModifier(self, Filters.unboundCompanion, new SpotCondition(Race.HOBBIT), 1));
}
}
