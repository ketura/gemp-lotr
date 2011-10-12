package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotBurdensCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: Easterling. While you can spot 3 burdens, this minion is fierce and damage +1.
 */
public class Card4_227 extends AbstractMinion {
    public Card4_227() {
        super(3, 8, 2, 4, Race.MAN, Culture.RAIDER, "Easterling Infantry");
        addKeyword(Keyword.EASTERLING);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.sameCard(self), new SpotBurdensCondition(3), Keyword.FIERCE, 1));
        modifiers.add(
                new KeywordModifier(self, Filters.sameCard(self), new SpotBurdensCondition(3), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
