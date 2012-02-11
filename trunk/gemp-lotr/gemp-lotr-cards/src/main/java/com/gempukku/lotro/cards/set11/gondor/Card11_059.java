package com.gempukku.lotro.cards.set11.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be a [GONDOR] Man. While bearer is a ranger or at a river site, he or she is damage +1.
 */
public class Card11_059 extends AbstractAttachableFPPossession {
    public Card11_059() {
        super(1, 2, 0, Culture.GONDOR, PossessionClass.HAND_WEAPON, "Gondorian Blade");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.hasAttached(self), Keyword.RANGER), new NotCondition(new LocationCondition(Keyword.RIVER)), Keyword.DAMAGE, 1));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), new LocationCondition(Keyword.RIVER), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
