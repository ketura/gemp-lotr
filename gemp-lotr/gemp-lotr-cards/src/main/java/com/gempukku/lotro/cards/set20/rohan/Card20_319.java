package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 2
 * •Eowyn, Lady of the Shield-arm
 * Rohan	Companion • Man
 * 6	3	7
 * Valiant. While in your starting fellowship, Eowyn's twilight cost is -1.
 * While you can spot an exhausted [Rohan] Man, Eowyn is strength +3.
 */
public class Card20_319 extends AbstractCompanion {
    public Card20_319() {
        super(2, 6, 3, 7, Culture.ROHAN, Race.MAN, null, Names.eowyn, "Lady of the Shield-arm", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return (gameState.getCurrentPhase() == Phase.PLAY_STARTING_FELLOWSHIP)?-1:0;
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, self,
                new SpotCondition(Culture.ROHAN, Race.MAN, Filters.exhausted), 3);
    }
}
