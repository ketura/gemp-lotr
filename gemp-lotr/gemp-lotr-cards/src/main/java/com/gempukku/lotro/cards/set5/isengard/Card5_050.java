package com.gempukku.lotro.cards.set5.isengard;

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

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 4
 * Game Text: Warg-rider. While Foul Horde is not exhausted, it is fierce. While Foul Horde has at least 3 vitality,
 * it is ambush (1). While Foul Horde has at least 4 vitality, it is damage + 1.
 */
public class Card5_050 extends AbstractMinion {
    public Card5_050() {
        super(4, 9, 3, 4, Race.ORC, Culture.ISENGARD, "Foul Horde", true);
        addKeyword(Keyword.WARG_RIDER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.sameCard(self), Filters.not(Filters.exhausted)), Keyword.FIERCE));
        modifiers.add(
                new KeywordModifier(self, Filters.sameCard(self),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return modifiersQuerying.getVitality(gameState, self) >= 3;
                            }
                        }, Keyword.AMBUSH, 1));
        modifiers.add(
                new KeywordModifier(self, Filters.sameCard(self),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return modifiersQuerying.getVitality(gameState, self) >= 4;
                            }
                        }, Keyword.DAMAGE, 1));
        return modifiers;
    }
}
