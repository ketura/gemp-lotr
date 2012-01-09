package com.gempukku.lotro.cards.set13.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Map;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 12
 * Vitality: 3
 * Site: 4
 * Game Text: To play, spot a [MEN] minion. While you can spot 5 or more culture tokens, the minion archery total is +2.
 */
public class Card13_086 extends AbstractMinion {
    public Card13_086() {
        super(5, 12, 3, 4, Race.MAN, Culture.MEN, "Desert Wind");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.MEN, CardType.MINION);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new ArcheryTotalModifier(self, Side.SHADOW,
                new Condition() {
                    @Override
                    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                        int count = 0;
                        for (PhysicalCard physicalCard : Filters.filterActive(gameState, modifiersQuerying, Filters.hasAnyCultureTokens(1))) {
                            for (Map.Entry<Token, Integer> tokenCountEntry : gameState.getTokens(physicalCard).entrySet()) {
                                if (tokenCountEntry.getKey().getCulture() != null) {
                                    count += tokenCountEntry.getValue();
                                    if (count >= 5)
                                        return true;
                                }
                            }
                        }
                        return false;
                    }
                }, 2);
    }
}
