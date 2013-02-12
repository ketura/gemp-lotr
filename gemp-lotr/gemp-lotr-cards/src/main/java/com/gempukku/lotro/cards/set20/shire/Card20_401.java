package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * •Pippin, Frodo's Cousin
 * Shire	Companion • Hobbit
 * 3	4	8
 * The twilight cost of each pipeweed possession is -1.
 * While Pippin bears a pipe, he is strength + 2
 */
public class Card20_401 extends AbstractCompanion {
    public Card20_401() {
        super(1, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Pippin", "Frodo's Cousin", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new TwilightCostModifier(self, Filters.and(CardType.POSSESSION, Keyword.PIPEWEED), -1));
        modifiers.add(
                new StrengthModifier(self, Filters.and(self, Filters.hasAttached(PossessionClass.PIPE)), 2));
        return modifiers;
    }
}
