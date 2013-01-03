package com.gempukku.lotro.cards.set13.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 2
 * Type: Minion • Uruk-Hai
 * Strength: 6
 * Vitality: 1
 * Site: 5
 * Game Text: Damage +1. While you can spot a Free Peoples culture token, this minion cannot take wounds except during
 * skirmishes.
 */
public class Card13_160 extends AbstractMinion {
    public Card13_160() {
        super(2, 6, 1, 5, Race.URUK_HAI, Culture.URUK_HAI, "Cavern Denizen");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantTakeWoundsModifier(self,
                new AndCondition(
                        new NotCondition(new PhaseCondition(Phase.SKIRMISH)),
                        new SpotCondition(Side.FREE_PEOPLE, Filters.hasAnyCultureTokens(1))),
                self);
    }
}
