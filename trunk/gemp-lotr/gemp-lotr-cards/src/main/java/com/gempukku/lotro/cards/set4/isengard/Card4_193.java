package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Tracker. Fierce. During a fierce skirmish involving this minion, it is strength +2.
 */
public class Card4_193 extends AbstractMinion {
    public Card4_193() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Runner");
        addKeyword(Keyword.TRACKER);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(Filters.sameCard(self), Filters.inSkirmish()),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return gameState.isFierceSkirmishes();
                            }
                        }, 2));
    }
}
