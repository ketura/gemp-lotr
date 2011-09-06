package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Minion � Uruk-Hai
 * Strength: 6
 * Vitality: 1
 * Site: 5
 * Game Text: Damage +1. While you can spot a weather condition, this minion is strength +3.
 */
public class Card1_149 extends AbstractMinion {
    public Card1_149() {
        super(2, 6, 1, 5, Keyword.URUK_HAI, Culture.ISENGARD, "Uruk Messenger");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new StrengthModifier(self,
                Filters.and(
                        Filters.sameCard(self),
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return Filters.canSpot(gameState, modifiersQuerying, Filters.keyword(Keyword.WEATHER), Filters.type(CardType.CONDITION));
                            }
                        }), 3);
    }
}
