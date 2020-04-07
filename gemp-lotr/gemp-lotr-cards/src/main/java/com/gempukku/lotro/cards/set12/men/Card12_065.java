package com.gempukku.lotro.cards.set12.men;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 12
 * Vitality: 2
 * Site: 4
 * Game Text: While this minion is bearing a possession, it is fierce.
 */
public class Card12_065 extends AbstractMinion {
    public Card12_065() {
        super(4, 12, 2, 4, Race.MAN, Culture.MEN, "Frenzied Dunlending");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new KeywordModifier(self, Filters.and(self, Filters.hasAttached(CardType.POSSESSION)), Keyword.FIERCE));
}
}
