package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.cards.modifiers.conditions.InitiativeCondition;
import com.gempukku.lotro.cards.modifiers.conditions.PhaseCondition;
import com.gempukku.lotro.common.*;
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
 * Twilight Cost: 6
 * Type: Minion • Man
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: Southron. Archer. While you have initiative, the Ring-bearer cannot take wounds during the archery phase
 * and this minion is ambush (8).
 */
public class Card7_170 extends AbstractMinion {
    public Card7_170() {
        super(6, 11, 3, 4, Race.MAN, Culture.RAIDER, "Suzerain of Harad", true);
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantTakeWoundsModifier(self,
                        new AndCondition(new InitiativeCondition(Side.SHADOW), new PhaseCondition(Phase.ARCHERY)),
                        Keyword.RING_BEARER));
        modifiers.add(
                new KeywordModifier(self, self, new InitiativeCondition(Side.SHADOW), Keyword.AMBUSH, 8));
        return modifiers;
    }
}
