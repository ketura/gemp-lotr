package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.cards.modifiers.conditions.InitiativeCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 6
 * Type: Minion • Orc
 * Strength: 15
 * Vitality: 4
 * Site: 6
 * Game Text: Archer. If you have initiative, the minion archery total is +1 and Orc Archer Troop is fierce.
 */
public class Card7_294 extends AbstractMinion {
    public Card7_294() {
        super(6, 15, 4, 6, Race.ORC, Culture.SAURON, "Orc Archer Troop", true);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new ArcheryTotalModifier(self, Side.SHADOW, new InitiativeCondition(Side.SHADOW), 1));
        modifiers.add(
                new KeywordModifier(self, self, new InitiativeCondition(Side.SHADOW), Keyword.FIERCE, 1));
        return modifiers;
    }
}
