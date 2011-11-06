package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: While you can spot a Nazgul, Gorbag is strength +2 and damage +1.
 */
public class Card7_180 extends AbstractMinion {
    public Card7_180() {
        super(5, 11, 3, 4, Race.ORC, Culture.WRAITH, "Gorbag", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, new SpotCondition(Race.NAZGUL), 2));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(Race.NAZGUL), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
