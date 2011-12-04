package com.gempukku.lotro.cards.set11.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Possession • Armor
 * Game Text: Bearer must be a [GONDOR] Man. Each minion skirmishing bearer loses all damage bonuses. While bearer is
 * at a battleground site and unwounded, he or she is defender +1.
 */
public class Card11_055 extends AbstractAttachableFPPossession {
    public Card11_055() {
        super(2, 0, 0, Culture.GONDOR, PossessionClass.ARMOR, "Armor of the Citadel");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new RemoveKeywordModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(Filters.hasAttached(self))), Keyword.DAMAGE));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.hasAttached(self), Filters.not(Filters.wounded)), new LocationCondition(Keyword.BATTLEGROUND), Keyword.DEFENDER, 1));
        return modifiers;
    }
}
