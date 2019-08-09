package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * •Pippin, Frodo's Cousin
 * Companion • Hobbit
 * 3	4	8
 * Each companion Bearing a pipe is strength + 1.
 * While Pippin bears a pipe, he is strength + 1.
 * http://lotrtcg.org/coreset/shire/pippinfc(r2).jpg
 */
public class Card20_401 extends AbstractCompanion {
    public Card20_401() {
        super(1, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Pippin", "Frodo's Cousin", true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.and(CardType.COMPANION, Filters.hasAttached(PossessionClass.PIPE)), 1));
        modifiers.add(
                new StrengthModifier(self, Filters.and(self, Filters.hasAttached(PossessionClass.PIPE)), 1));
        return modifiers;
    }
}
