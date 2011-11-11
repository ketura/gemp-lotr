package com.gempukku.lotro.cards.set8.gollum;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 6
 * Type: Minion • Spider
 * Strength: 8
 * Vitality: 8
 * Site: 8
 * Game Text: Enduring. Fierce. While in a fierce skirmish, Shelob is damage +1.
 */
public class Card8_026 extends AbstractMinion {
    public Card8_026() {
        super(6, 8, 8, 8, Race.SPIDER, Culture.GOLLUM, "Shelob", true);
        addKeyword(Keyword.ENDURING);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.and(self, Filters.inSkirmish),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return gameState.isFierceSkirmishes();
                            }
                        }, Keyword.DAMAGE, 1));
    }
}
