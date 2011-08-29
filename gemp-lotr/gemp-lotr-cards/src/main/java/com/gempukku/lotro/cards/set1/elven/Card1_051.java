package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Signet;
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
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion � Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Archer. While skirmishing a Nazgul, Legolas is strength +3.
 */
public class Card1_051 extends AbstractCompanion {
    public Card1_051() {
        super(2, 6, 3, Culture.ELVEN, "Legolas", "1_51", true);
        addKeyword(Keyword.ELF);
        addKeyword(Keyword.ARCHER);
        setSignet(Signet.GANDALF);
    }

    @Override
    public Modifier getAlwaysOnEffect(final PhysicalCard self) {
        return new StrengthModifier(self,
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        Skirmish activeSkirmish = gameState.getSkirmish();
                        return (activeSkirmish != null
                                && activeSkirmish.getFellowshipCharacter().equals(self)
                                && Filters.filter(activeSkirmish.getShadowCharacters(), gameState, modifiersQuerying, Filters.keyword(Keyword.NAZGUL)).size() > 0);
                    }
                }, 3);
    }
}
