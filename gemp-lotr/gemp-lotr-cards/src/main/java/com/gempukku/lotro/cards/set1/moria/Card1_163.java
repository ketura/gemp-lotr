package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 4
 * Type: Minion o Orc
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: For each other [MORIA] Orc you can spot, Ancient Chieftain is strength +1.
 */
public class Card1_163 extends AbstractMinion {
    public Card1_163() {
        super(4, 9, 2, 4, Race.ORC, Culture.MORIA, "Ancient Chieftain", null, true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, null, new CountActiveEvaluator(Culture.MORIA, Race.ORC, Filters.not(self))));
}
}
