package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Tale. Plays to your support area. While a Dwarf skirmishes a [MORIA] minion, that Dwarf is strength +1.
 */
public class Card1_024 extends AbstractPermanent {
    public Card1_024() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.DWARVEN, Zone.FREE_SUPPORT, "Stairs of Khazad-dum");
        addKeyword(Keyword.TALE);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new StrengthModifier(self,
                Filters.and(
                        Filters.race(Race.DWARF),
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                Skirmish skirmish = gameState.getSkirmish();
                                return (skirmish != null)
                                        && (skirmish.getFellowshipCharacter() == physicalCard)
                                        && (Filters.filter(skirmish.getShadowCharacters(), gameState, modifiersQuerying, Filters.culture(Culture.MORIA)).size() > 0);
                            }
                        }), 1);
    }
}
