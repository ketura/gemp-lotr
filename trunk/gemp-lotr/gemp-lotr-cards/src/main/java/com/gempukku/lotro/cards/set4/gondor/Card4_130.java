package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
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
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Ring-bound. Ranger. When this companion is in your starting fellowship, his twilight cost is -1.
 * While skirmishing a roaming minion, this companion is strength +2.
 */
public class Card4_130 extends AbstractCompanion {
    public Card4_130() {
        super(3, 6, 3, Culture.GONDOR, Race.MAN, null, "Ranger of Ithilien");
        addKeyword(Keyword.RING_BOUND);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public int getCompanionStartingFellowshipModifier() {
        return -1;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self,
                        Filters.and(
                                Filters.sameCard(self),
                                Filters.inSkirmishAgainst(
                                        Filters.and(
                                                Filters.type(CardType.MINION),
                                                Filters.keyword(Keyword.ROAMING)))), 2));
    }
}
