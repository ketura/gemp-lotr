package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
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
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: When this companion is in your starting fellowship, his twilight cost is -1. While skirmishing a wounded
 * minion, this companion is strength +2.
 */
public class Card4_265 extends AbstractCompanion {
    public Card4_265() {
        super(3, 6, 3, Culture.ROHAN, Race.MAN, null, "Elite Rider");
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
                                Filters.inSkirmishAgainst(Filters.type(CardType.MINION), Filters.wounded)), 2));
    }
}
