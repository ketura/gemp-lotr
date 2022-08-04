package com.gempukku.lotro.cards.set15.men;

import com.gempukku.lotro.common.CardType;
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
 * Set: The Hunters
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 12
 * Vitality: 3
 * Site: 4
 * Game Text: While you can spot more companions than minions, each [MEN] hunter is strength +2 and is an archer.
 */
public class Card15_079 extends AbstractMinion {
    public Card15_079() {
        super(5, 12, 3, 4, Race.MAN, Culture.MEN, "Enraged Herdsman");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new StrengthModifier(self, Filters.and(Culture.MEN, Keyword.HUNTER),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                return Filters.countActive(game, CardType.COMPANION)
                                        > Filters.countActive(game, CardType.MINION);
                            }
                        }, 2));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Culture.MEN, Keyword.HUNTER),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                return Filters.countActive(game, CardType.COMPANION)
                                        > Filters.countActive(game, CardType.MINION);
                            }
                        }, Keyword.ARCHER, 1));
        return modifiers;
    }
}
