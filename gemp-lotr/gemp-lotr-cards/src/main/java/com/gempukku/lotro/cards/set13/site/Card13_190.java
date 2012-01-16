package com.gempukku.lotro.cards.set13.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.cards.modifiers.conditions.PhaseCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Set: Bloodlines
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Marsh. Each companion of a race that has the most companions is strength -1 until the regroup phase.
 */
public class Card13_190 extends AbstractNewSite {
    public Card13_190() {
        super("Doors of Durin", 0, Direction.LEFT);
        addKeyword(Keyword.MARSH);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self,
                Filters.and(CardType.COMPANION,
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                final Race race = physicalCard.getBlueprint().getRace();
                                if (race != null && isRaceMostCommonRaceAmongCompanions(gameState, modifiersQuerying, race))
                                    return true;
                                return false;
                            }
                        }), new NotCondition(new PhaseCondition(Phase.REGROUP)), -1);
    }

    private boolean isRaceMostCommonRaceAmongCompanions(GameState gameState, ModifiersQuerying modifiersQuerying, Race race) {
        Map<Race, Integer> counts = new HashMap<Race, Integer>();
        for (PhysicalCard companion : Filters.filterActive(gameState, modifiersQuerying, CardType.COMPANION)) {
            final Race companionRace = companion.getBlueprint().getRace();
            if (companionRace != null) {
                Integer count = counts.get(companionRace);
                if (count == null)
                    count = 0;
                counts.put(companionRace, count + 1);
            }
        }

        final Integer thisRaceCount = counts.get(race);
        for (Map.Entry<Race, Integer> raceCount : counts.entrySet()) {
            if (raceCount.getValue() > thisRaceCount)
                return false;
        }
        return true;
    }
}
