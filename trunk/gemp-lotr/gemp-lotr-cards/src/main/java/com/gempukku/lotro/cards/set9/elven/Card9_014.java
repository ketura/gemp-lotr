package com.gempukku.lotro.cards.set9.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Companion • Elf
 * Strength: 3
 * Vitality: 3
 * Resistance: 3
 * Game Text: While Galadriel bears an artifact or The One Ring, she is resistance +1 for each [ELVEN] companion you
 * can spot.
 */
public class Card9_014 extends AbstractCompanion {
    public Card9_014() {
        super(3, 3, 3, 3, Culture.ELVEN, Race.ELF, null, "Galadriel", true);
        addKeyword(Keyword.CAN_START_WITH_RING);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ResistanceModifier(self, Filters.and(self, Filters.hasAttached(Filters.or(CardType.THE_ONE_RING, CardType.ARTIFACT))), new CountSpottableEvaluator(Culture.ELVEN, CardType.COMPANION)));
    }
}
