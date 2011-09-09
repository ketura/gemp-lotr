package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
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
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +3
 * Game Text: Bearer must be Cave Troll of Moria. Cave Troll of Moria is damage +1.
 */
public class Card1_166 extends AbstractAttachable {
    public Card1_166() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.MORIA, Keyword.HAND_WEAPON, "Cave Troll's Hammer", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.name("Cave Troll of Moria"), Filters.not(Filters.hasAttached(Filters.keyword(Keyword.HAND_WEAPON))));
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new StrengthModifier(null, null, 3));
        modifiers.add(new KeywordModifier(null, null, Keyword.DAMAGE));
        return new CompositeModifier(self, Filters.attachedTo(self), modifiers);
    }
}
