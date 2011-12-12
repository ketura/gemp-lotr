package com.gempukku.lotro.cards.set12.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.CantExertWithCardModifier;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 12
 * Type: Minion • Balrog
 * Strength: 17
 * Vitality: 5
 * Site: 4
 * Game Text: Damage +1. While The Balrog is at an underground site, it is fierce and cannot take wounds or be exerted.
 */
public class Card12_079 extends AbstractMinion {
    public Card12_079() {
        super(12, 17, 5, 4, Race.BALROG, Culture.MORIA, "The Balrog", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, self, new LocationCondition(Keyword.UNDERGROUND), Keyword.FIERCE, 1));
        modifiers.add(
                new CantTakeWoundsModifier(self, new LocationCondition(Keyword.UNDERGROUND), self));
        modifiers.add(
                new CantExertWithCardModifier(self, self, new LocationCondition(Keyword.UNDERGROUND), Filters.any));
        return modifiers;
    }
}
