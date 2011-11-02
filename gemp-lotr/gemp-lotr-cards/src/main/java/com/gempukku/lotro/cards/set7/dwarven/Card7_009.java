package com.gempukku.lotro.cards.set7.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.cards.modifiers.conditions.MinThreatCondition;
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
 * Set: The Return of the King
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be a dwarf. While you can spot a threat, bearer is damage +1. While you can spot 2 threats,
 * bearer is strength +1. While you can spot 3 threats, the fellowship archery total is +1.
 */
public class Card7_009 extends AbstractAttachableFPPossession {
    public Card7_009() {
        super(2, 2, 0, Culture.DWARVEN, PossessionClass.HAND_WEAPON, "Gimli's Battle Axe", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), new MinThreatCondition(1), Keyword.DAMAGE, 1));
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), new MinThreatCondition(2), 1));
        modifiers.add(
                new ArcheryTotalModifier(self, Side.FREE_PEOPLE, new MinThreatCondition(3), 1));
        return modifiers;
    }
}
