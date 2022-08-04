package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Minion • Uruk-Hai
 * Strength: 5
 * Vitality: 1
 * Site: 5
 * Game Text: Tracker. Fierce. During a fierce skirmish involving this minion, it is strength +3 and damage +1.
 */
public class Card4_187 extends AbstractMinion {
    public Card4_187() {
        super(1, 5, 1, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Foot Soldier");
        addKeyword(Keyword.TRACKER);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new StrengthModifier(self, Filters.and(self, Filters.inSkirmish),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                return game.getGameState().isFierceSkirmishes();
                            }
                        }, 3));
        modifiers.add(
                new KeywordModifier(self, Filters.and(self, Filters.inSkirmish),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                return game.getGameState().isFierceSkirmishes();
                            }
                        }, Keyword.DAMAGE, 1));

        return modifiers;
    }
}
