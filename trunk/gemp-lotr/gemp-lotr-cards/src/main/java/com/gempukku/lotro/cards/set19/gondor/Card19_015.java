package com.gempukku.lotro.cards.set19.gondor;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ages End
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition
 * Strength: +1
 * Game Text: Bearer must be a fellowship companion. While bearer is a [GONDOR] companion, he or she is damage +1.
 */
public class Card19_015 extends AbstractAttachable {
    public Card19_015() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.GONDOR, null, "Strength In My Blood");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(CardType.COMPANION, Keyword.FELLOWSHIP);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.hasAttached(self), Culture.GONDOR, CardType.COMPANION), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
