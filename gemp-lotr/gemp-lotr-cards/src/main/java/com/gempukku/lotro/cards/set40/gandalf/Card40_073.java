package com.gempukku.lotro.cards.set40.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Title: *Glamdring, Turgon's Blade
 * Set: Second Edition
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Possession - Hand Weapon
 * Strength: +2
 * Card Number: 1R73
 * Game Text: Bearer must be Gandalf. While skirmishing an Orc or Goblin, Gandalf is strength +2 and damage +1.
 */
public class Card40_073 extends AbstractAttachableFPPossession{
    public Card40_073() {
        super(2, 2, 0, Culture.GANDALF, PossessionClass.HAND_WEAPON, "Glamdring",
                "Turgon's Blade", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.gandalf;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        Filter skirmishingOrcOrGoblin = Filters.and(Filters.hasAttached(self), Filters.inSkirmishAgainst(Filters.or(Race.ORC, Race.GOBLIN)));
        StrengthModifier modifier1 = new StrengthModifier(self,skirmishingOrcOrGoblin, 2);
        KeywordModifier modifier2 = new KeywordModifier(self, skirmishingOrcOrGoblin, Keyword.DAMAGE, 1);
        return Arrays.asList(modifier1, modifier2);
    }
}
