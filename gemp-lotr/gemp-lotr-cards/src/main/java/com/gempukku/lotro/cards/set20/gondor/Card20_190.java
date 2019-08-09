package com.gempukku.lotro.cards.set20.gondor;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 3
 * •Faramir, Cunning Ranger
 * Gondor	Companion • Man
 * 7	3	7
 * Ring-bound. Ranger.
 * Faramir is strength +2 while at a site from your adventure deck.
 */
public class Card20_190 extends AbstractCompanion {
    public Card20_190() {
        super(3, 7, 3, 7, Culture.GONDOR, Race.MAN, null, "Faramir", "Cunning Ranger", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, new LocationCondition(Filters.owner(self.getOwner())), 2));
}
}
