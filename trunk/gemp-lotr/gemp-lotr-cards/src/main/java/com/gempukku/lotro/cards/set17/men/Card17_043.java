package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.CardType;
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
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 6
 * Type: Minion • Man
 * Strength: 15
 * Vitality: 3
 * Site: 4
 * Game Text: While you control 2 sites, Easterling Sneak is fierce. While you control 4 sites, each [MEN] minion
 * is strength +1 and fierce.
 */
public class Card17_043 extends AbstractMinion {
    public Card17_043() {
        super(6, 15, 3, 4, Race.MAN, Culture.MEN, "Easterling Sneak", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(2, Filters.siteControlled(self.getOwner())), Keyword.FIERCE, 1));
        modifiers.add(
                new StrengthModifier(self, Filters.and(CardType.MINION, Culture.MEN), new SpotCondition(4, Filters.siteControlled(self.getOwner())), 1));
        modifiers.add(
                new KeywordModifier(self, Filters.and(CardType.MINION, Culture.MEN), new SpotCondition(4, Filters.siteControlled(self.getOwner())), Keyword.FIERCE, 1));
        return modifiers;
    }
}
