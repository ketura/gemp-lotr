package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 3
 * Site: 5
 * Game Text: Fierce. This minion is damage +1 for each [URUK-HAI] hunter assigned to a skirmish.
 */
public class Card17_134 extends AbstractMinion {
    public Card17_134() {
        super(4, 8, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Vanquisher", null, true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new KeywordModifier(self, self, null, Keyword.DAMAGE, new CountActiveEvaluator(Culture.URUK_HAI, Keyword.HUNTER, Filters.assignedToSkirmish)));
}
}
