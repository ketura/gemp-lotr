package com.gempukku.lotro.cards.set6.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 2
 * Type: Minion • Man
 * Strength: 7
 * Vitality: 1
 * Site: 3
 * Game Text: While you control 2 sites, this minion is strength +5, fierce and damage +1.
 */
public class Card6_002 extends AbstractMinion {
    public Card6_002() {
        super(2, 7, 1, 3, Race.MAN, Culture.DUNLAND, "Dunlending Elder");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, new SpotCondition(2, Filters.siteControlled(self.getOwner())), 5));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(2, Filters.siteControlled(self.getOwner())), Keyword.FIERCE, 1));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(2, Filters.siteControlled(self.getOwner())), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
