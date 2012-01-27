package com.gempukku.lotro.cards.set15.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: Hunter 1. (While skirmishing a non-hunter character, this character is strength +1.)
 * This minion is strength +3 for each follower you can spot.
 */
public class Card15_083 extends AbstractMinion {
    public Card15_083() {
        super(3, 8, 2, 4, Race.MAN, Culture.MEN, "Hunting Herdsman");
        addKeyword(Keyword.HUNTER, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null, new MultiplyEvaluator(3, new CountSpottableEvaluator(CardType.FOLLOWER)));
    }
}
