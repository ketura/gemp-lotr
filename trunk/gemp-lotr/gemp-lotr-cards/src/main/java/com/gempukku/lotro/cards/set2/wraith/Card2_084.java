package com.gempukku.lotro.cards.set2.wraith;

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
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 5
 * Type: Minion • Nazgul
 * Strength: 10
 * Vitality: 3
 * Site: 2
 * Game Text: Twilight. While you can spot 2 burdens or 2 wounds on the Ring-bearer, Ulaire Nelya is fierce and
 * damage +1.
 */
public class Card2_084 extends AbstractMinion {
    public Card2_084() {
        super(5, 10, 3, 2, Race.NAZGUL, Culture.WRAITH, "Ulaire Nelya", true);
        addKeyword(Keyword.TWILIGHT);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.sameCard(self),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return gameState.getBurdens() >= 2 || gameState.getWounds(Filters.findFirstActive(gameState, modifiersQuerying, Keyword.RING_BEARER)) >= 2;
                            }
                        }, Keyword.DAMAGE, 1));
        modifiers.add(
                new KeywordModifier(self, Filters.sameCard(self),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return gameState.getBurdens() >= 2 || gameState.getWounds(Filters.findFirstActive(gameState, modifiersQuerying, Keyword.RING_BEARER)) >= 2;
                            }
                        }, Keyword.FIERCE, 1));
        return modifiers;
    }
}
