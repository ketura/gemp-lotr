package com.gempukku.lotro.cards.set15.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: While you control a site, this minion is strength +3 and fierce.
 */
public class Card15_082 extends AbstractMinion {
    public Card15_082() {
        super(4, 9, 2, 4, Race.MAN, Culture.MEN, "Grousing Hillman");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, new SpotCondition(Filters.siteControlled(self.getOwner())), 2));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(Filters.siteControlled(self.getOwner())), Keyword.FIERCE, 1));
        return modifiers;
    }
}
