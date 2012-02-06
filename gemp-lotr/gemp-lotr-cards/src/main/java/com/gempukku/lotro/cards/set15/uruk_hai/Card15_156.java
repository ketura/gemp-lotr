package com.gempukku.lotro.cards.set15.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.FierceSkirmishCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 7
 * Vitality: 3
 * Site: 5
 * Game Text: Fierce. During a fierce skirmish involving this minion, it is strength +3 and gains hunter 1
 * (While skirmishing a non-hunter character, this character is strength +1.)
 */
public class Card15_156 extends AbstractMinion {
    public Card15_156() {
        super(3, 7, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Charging Uruk");
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.and(self, Filters.inSkirmish), new FierceSkirmishCondition(), 3));
        modifiers.add(
                new KeywordModifier(self, Filters.and(self, Filters.inSkirmish), new FierceSkirmishCondition(), Keyword.HUNTER, 1));
        return modifiers;
    }
}
