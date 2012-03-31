package com.gempukku.lotro.cards.set18.orc;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.cards.modifiers.conditions.FierceSkirmishCondition;
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
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 1
 * Type: Possession • Mount
 * Strength: +1
 * Vitality: +1
 * Game Text: Bearer must be an [ORC] Orc with strength 9 or less (or bearer must be Gothmog). Bearer is fierce.
 * While bearer is Gothmog, he is damage +1 in fierce skirmishes.
 */
public class Card18_081 extends AbstractAttachable {
    public Card18_081() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.ORC, PossessionClass.MOUNT, "Gothmog's Warg", "Leader's Mount", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.or(Filters.name("Gothmog"), Filters.and(Culture.ORC, Race.ORC, Filters.lessStrengthThan(10)));
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.FIERCE));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.name("Gothmog"), Filters.hasAttached(self)), new FierceSkirmishCondition(), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
