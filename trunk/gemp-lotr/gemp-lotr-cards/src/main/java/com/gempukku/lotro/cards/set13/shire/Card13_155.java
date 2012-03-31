package com.gempukku.lotro.cards.set13.shire;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
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
 * Set: Bloodlines
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Artifact • Phial
 * Resistance: +1
 * Game Text: Bearer must be a Ring-bound Hobbit. While bearer is in region 1, bearer gains muster. While bearer is in
 * region 2, each of your [SHIRE] companions gains muster. While bearer is in region 3, each character gains muster.
 */
public class Card13_155 extends AbstractAttachableFPPossession {
    public Card13_155() {
        super(0, 0, 0, Culture.SHIRE, CardType.ARTIFACT, PossessionClass.PHIAL, "Phial of Galadriel", "The Light of Earendil", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Race.HOBBIT, Keyword.RING_BOUND);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new ResistanceModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), new LocationCondition(Filters.region(1)), Keyword.MUSTER, 1));
        modifiers.add(
                new KeywordModifier(self,
                        Filters.and(Filters.owner(self.getOwner()), Culture.SHIRE, CardType.COMPANION),
                        new LocationCondition(Filters.region(2)), Keyword.MUSTER, 1));
        modifiers.add(
                new KeywordModifier(self, Filters.character, new LocationCondition(Filters.region(3)), Keyword.MUSTER, 1));
        return modifiers;
    }
}
