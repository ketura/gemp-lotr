package com.gempukku.lotro.cards.set13.site;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.NotCondition;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Set: Bloodlines
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Marsh. Each companion of a race that has the most companions is strength -1 until the regroup phase.
 */
public class Card13_190 extends AbstractShadowsSite {
    public Card13_190() {
        super("Doors of Durin", 0, Direction.LEFT);
        addKeyword(Keyword.MARSH);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new StrengthModifier(self,
                Filters.and(CardType.COMPANION,
                        new Filter() {
                            @Override
                            public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                final Race race = physicalCard.getBlueprint().getRace();
                                if (race != null && isRaceMostCommonRaceAmongCompanions(game, race))
                                    return true;
                                return false;
                            }
                        }), new NotCondition(new PhaseCondition(Phase.REGROUP)), -1));
    }

    private boolean isRaceMostCommonRaceAmongCompanions(LotroGame game, Race race) {
        if (race == null)
            return false;

        Map<Race, Integer> counts = new HashMap<>();
        for (PhysicalCard companion : Filters.filterActive(game, CardType.COMPANION)) {
            final Race companionRace = companion.getBlueprint().getRace();
            if (companionRace != null) {
                Integer count = counts.get(companionRace);
                if (count == null)
                    count = 0;
                counts.put(companionRace, count + 1);
            }
        }

        final Integer thisRaceCount = counts.get(race);
        if (thisRaceCount == null)
            return false;
        for (Map.Entry<Race, Integer> raceCount : counts.entrySet()) {
            if (raceCount.getValue() > thisRaceCount)
                return false;
        }
        return true;
    }
}
