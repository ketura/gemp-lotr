package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 5
 * Vitality: 3
 * Resistance: 6
 * Game Text: While you have only 4 or 5 cards in hand, Cirion is strength +2.
 */
public class Card7_082 extends AbstractCompanion {
    public Card7_082() {
        super(2, 5, 3, 6, Culture.GONDOR, Race.MAN, null, "Cirion", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(final LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, self, new Condition() {
                    @Override
                    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                        int handSize = gameState.getHand(self.getOwner()).size();
                        return (handSize >= 4 && handSize <= 5);
                    }
                }, 2));
    }
}
