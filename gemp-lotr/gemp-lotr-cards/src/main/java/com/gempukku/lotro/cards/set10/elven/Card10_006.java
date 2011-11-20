package com.gempukku.lotro.cards.set10.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Frodo
 * Game Text: Each minion skirmishing Arwen is strength -2 for each wounded minion you can spot.
 */
public class Card10_006 extends AbstractCompanion {
    public Card10_006() {
        super(2, 6, 3, 6, Culture.ELVEN, Race.ELF, Signet.FRODO, "Arwen", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self,
                        Filters.and(CardType.MINION, Filters.inSkirmishAgainst(self)), null,
                        new MultiplyEvaluator(-2, new CountSpottableEvaluator(CardType.MINION, Filters.wounded))));
    }
}
