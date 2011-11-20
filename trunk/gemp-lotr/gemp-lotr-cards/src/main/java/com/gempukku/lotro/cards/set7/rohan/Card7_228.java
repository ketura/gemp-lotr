package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Theoden
 * Game Text: Valiant. While skirmishing a mounted minion, Eowyn is strength +2. While skirmishing a fierce minion,
 * Eowyn is strength +2.
 */
public class Card7_228 extends AbstractCompanion {
    public Card7_228() {
        super(2, 6, 3, 6, Culture.ROHAN, Race.MAN, Signet.THÉODEN, "Eowyn", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.and(self, Filters.inSkirmishAgainst(CardType.MINION, Filters.mounted)), 2));
        modifiers.add(
                new StrengthModifier(self, Filters.and(self, Filters.inSkirmishAgainst(CardType.MINION, Keyword.FIERCE)), 2));
        return modifiers;
    }
}
