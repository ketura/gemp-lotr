package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.*;

import java.util.LinkedList;
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
 * Game Text: Damage +1. While an ally is in the dead pile, this minion is strength +3 and fierce.
 */
public class Card4_197 extends AbstractMinion {
    public Card4_197() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Stalker");
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.sameCard(self),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return Filters.filter(gameState.getDeadPile(gameState.getCurrentPlayerId()), gameState, modifiersQuerying, CardType.ALLY).size() > 0;
                            }
                        }, 3));
        modifiers.add(
                new KeywordModifier(self, Filters.sameCard(self),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return Filters.filter(gameState.getDeadPile(gameState.getCurrentPlayerId()), gameState, modifiersQuerying, CardType.ALLY).size() > 0;
                            }
                        }, Keyword.FIERCE, 1));
        return modifiers;
    }
}
