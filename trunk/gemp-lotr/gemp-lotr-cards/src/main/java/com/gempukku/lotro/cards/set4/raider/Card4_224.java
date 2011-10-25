package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotBurdensCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 2
 * Type: Minion • Man
 * Strength: 6
 * Vitality: 2
 * Site: 4
 * Game Text: Easterling. While you can spot 2 burdens, this minion is strength +3 and fierce.
 */
public class Card4_224 extends AbstractMinion {
    public Card4_224() {
        super(2, 6, 2, 4, Race.MAN, Culture.RAIDER, "Easterling Axeman");
        addKeyword(Keyword.EASTERLING);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.sameCard(self), new SpotBurdensCondition(2), 3));
        modifiers.add(
                new KeywordModifier(self, Filters.sameCard(self), new SpotBurdensCondition(2), Keyword.FIERCE, 1));
        return modifiers;
    }
}
