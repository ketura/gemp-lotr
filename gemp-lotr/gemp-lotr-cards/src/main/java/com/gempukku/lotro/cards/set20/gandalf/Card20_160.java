package com.gempukku.lotro.cards.set20.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 2
 * •Glamdring, Turgon's Blade
 * Gandalf	Possession • Hand Weapon
 * 2
 * Bearer must be Gandalf.
 * While skirmishing an Orc or Goblin, Gandalf is strength +2 and damage +1.
 */
public class Card20_160 extends AbstractAttachableFPPossession {
    public Card20_160() {
        super(2, 2, 0, Culture.GANDALF, PossessionClass.HAND_WEAPON, "Glamdring", "Turgon's Blade", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.gandalf;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.hasAttached(self), Filters.inSkirmishAgainst(Filters.or(Race.ORC, Race.GOBLIN))), Keyword.DAMAGE, 1));
        modifiers.add(
                new StrengthModifier(self, Filters.and(Filters.hasAttached(self), Filters.inSkirmishAgainst(Filters.or(Race.ORC, Race.GOBLIN))), 2));
        return modifiers;
    }
}
