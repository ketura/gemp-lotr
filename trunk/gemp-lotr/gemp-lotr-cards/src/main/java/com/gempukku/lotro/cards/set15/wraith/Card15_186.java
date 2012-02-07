package com.gempukku.lotro.cards.set15.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.cards.modifiers.conditions.PhaseCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 5
 * Type: Minion • Nazgul
 * Strength: 10
 * Vitality: 3
 * Site: 3
 * Game Text: Fierce. While you can spot 6 companions, [WRAITH] minions cannot take wounds
 * (except during skirmish phases).
 * Skirmish: If this minion is mounted, exert him twice to wound a companion bearing a [WRAITH] condition.
 */
public class Card15_186 extends AbstractMinion {
    public Card15_186() {
        super(5, 10, 3, 3, Race.NAZGUL, Culture.WRAITH, "Úlairë Nelya", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantTakeWoundsModifier(self,
                new AndCondition(
                        new NotCondition(new PhaseCondition(Phase.SKIRMISH)),
                        new SpotCondition(6, CardType.COMPANION)), Filters.and(Culture.WRAITH, CardType.MINION));
    }
}
