package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Tale. Bearer must be a [GONDOR] Man. Limit 1 per bearer. Bearer is strength +1 for each Elf you can spot
 * (limit +3).
 */
public class Card1_049 extends AbstractAttachable {
    public Card1_049() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.ELVEN, null, "The Last Alliance of Elves and Men");
        addKeyword(Keyword.TALE);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Filters.race(Race.MAN), Filters.not(Filters.hasAttached(Filters.name(getName()))));
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.hasAttached(self), null, new CountSpottableEvaluator(3, Race.ELF));
    }
}
