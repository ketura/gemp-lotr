package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.CompositeModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Aragorn. He is damage +1.
 */
public class Card1_112 extends AbstractAttachableFPPossession {
    public Card1_112() {
        super(1, Culture.GONDOR, "Ranger's Sword", true);
        addKeyword(Keyword.HAND_WEAPON);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.name("Aragorn"), Filters.not(Filters.hasAttached(Filters.keyword(Keyword.HAND_WEAPON))));
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new StrengthModifier(null, null, 2));
        modifiers.add(new KeywordModifier(null, null, Keyword.DAMAGE));

        return new CompositeModifier(self, Filters.attachedTo(self), modifiers);
    }
}
