package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Archer. While Legolas is at a river or forest, add 1 to the fellowship archery total.
 */
public class Card3_121 extends AbstractCompanion {
    public Card3_121() {
        super(2, 6, 3, Culture.ELVEN, Race.ELF, Signet.ARAGORN, "Legolas", true);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ArcheryTotalModifier(self, Side.FREE_PEOPLE,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                PhysicalCard currentSite = gameState.getCurrentSite();
                                return modifiersQuerying.hasKeyword(gameState, currentSite, Keyword.RIVER)
                                        || modifiersQuerying.hasKeyword(gameState, currentSite, Keyword.FOREST);
                            }
                        }, 1));
    }
}
