package com.gempukku.lotro.cards.set14.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Expanded Middle-earth
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Each of your Dwarves and other [GANDALF] Men is strength +1. While you can spot more minions
 * than companions, the fellowship archery total is +X, where X is the number of minions minus the number of companions.
 */
public class Card14_005 extends AbstractCompanion {
    public Card14_005() {
        super(2, 6, 3, 6, Culture.GANDALF, Race.MAN, null, "Brand", "King of Dale", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self,
                        Filters.and(Filters.owner(self.getOwner()), Filters.not(self),
                                Filters.or(Race.DWARF, Filters.and(Culture.GANDALF, Race.MAN))), 1));
        modifiers.add(
                new ArcheryTotalModifier(self, Side.FREE_PEOPLE,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return Filters.countActive(gameState, modifiersQuerying, CardType.MINION)
                                        > Filters.countActive(gameState, modifiersQuerying, CardType.COMPANION);
                            }
                        }, new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                return Filters.countActive(gameState, modifiersQuerying, CardType.MINION)
                                        - Filters.countActive(gameState, modifiersQuerying, CardType.COMPANION);
                            }
                        }
                ));
        return modifiers;
    }
}
