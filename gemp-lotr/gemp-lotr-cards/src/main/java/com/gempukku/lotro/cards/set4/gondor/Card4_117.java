package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.OpponentsCantPlayPhaseEventsOrPhaseSpecialAbilitiesModifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Signet: Frodo
 * Game Text: Ring-bound. Ranger. An opponent may not play skirmish events or use skirmish special abilities during
 * skirmishes involving Faramir.
 */
public class Card4_117 extends AbstractCompanion {
    public Card4_117() {
        super(3, 7, 3, 6, Culture.GONDOR, Race.MAN, Signet.FRODO, "Faramir", "Son of Denethor", true);
        addKeyword(Keyword.RING_BOUND);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new OpponentsCantPlayPhaseEventsOrPhaseSpecialAbilitiesModifier(self,
                        new SpotCondition(self, Filters.inSkirmish), self.getOwner(), Phase.SKIRMISH));
    }
}
