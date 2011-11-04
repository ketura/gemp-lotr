package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.MinThreatCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 10
 * Vitality: 3
 * Site: 4
 * Game Text: Southron. While you can spot 2 threats, this minion is an archer. While you can spot 3 threats, this
 * minion is fierce. While you can spot 4 threats, this minion is damage +1.
 */
public class Card7_172 extends AbstractMinion {
    public Card7_172() {
        super(5, 10, 3, 4, Race.MAN, Culture.RAIDER, "Troop of Haradrim");
        addKeyword(Keyword.SOUTHRON);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, self, new MinThreatCondition(2), Keyword.ARCHER, 1));
        modifiers.add(
                new KeywordModifier(self, self, new MinThreatCondition(3), Keyword.FIERCE, 1));
        modifiers.add(
                new KeywordModifier(self, self, new MinThreatCondition(4), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
