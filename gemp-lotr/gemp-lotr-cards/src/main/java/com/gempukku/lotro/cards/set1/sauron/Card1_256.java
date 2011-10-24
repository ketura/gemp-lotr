package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 4
 * Site: 6
 * Game Text: For each companion you can spot, this minion is strength +1.
 */
public class Card1_256 extends AbstractMinion {
    public Card1_256() {
        super(5, 9, 4, 6, Race.ORC, Culture.SAURON, "Morgul Hunter");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null, new CountSpottableEvaluator(CardType.COMPANION));
    }
}
