package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 7
 * Vitality: 1
 * Site: 4
 * Game Text: Archer. While you can spot a companion who has resistance 4 or less, the minion archery total is +1.
 */
public class Card11_067 extends AbstractMinion {
    public Card11_067() {
        super(3, 7, 1, 4, Race.MAN, Culture.MEN, "Archer of Harad");
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new ArcheryTotalModifier(self, Side.SHADOW, new SpotCondition(CardType.COMPANION, Filters.maxResistance(4)), 1));
}
}
