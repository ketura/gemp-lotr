package com.gempukku.lotro.cards.set12.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Faramir. He is damage +1. Each minion skirmishing Faramir at a battleground or forest site
 * is roaming.
 */
public class Card12_048 extends AbstractAttachableFPPossession {
    public Card12_048() {
        super(2, 2, 0, Culture.GONDOR, PossessionClass.HAND_WEAPON, "Faramir's Sword", null, true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Faramir");
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE, 1));
        modifiers.add(
                new KeywordModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(Filters.hasAttached(self))),
                        new LocationCondition(Filters.or(Keyword.BATTLEGROUND, Keyword.FOREST)), Keyword.ROAMING, 1));
        return modifiers;
    }
}
