package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 10
 * Vitality: 2
 * Site: 5
 * Game Text: Besieger. While this minion is stacked on a site you control, besiegers are fierce.
 */
public class Card7_275 extends AbstractMinion {
    public Card7_275() {
        super(4, 10, 2, 5, Race.ORC, Culture.SAURON, "Gorgoroth Pillager");
        addKeyword(Keyword.BESIEGER);
    }

    @Override
    public List<? extends Modifier> getStackedOnModifiers(LotroGame game, PhysicalCard self) {
        if (self.getStackedOn() != null
                && Filters.siteControlled(self.getOwner()).accepts(game.getGameState(), game.getModifiersQuerying(), self.getStackedOn())) {
            return Collections.singletonList(
                    new KeywordModifier(self, Keyword.BESIEGER, Keyword.FIERCE));
        }
        return null;
    }
}
