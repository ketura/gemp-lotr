package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Arrays;
import java.util.List;

/**
 * Title: *Gimli, Stalwart Protector
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Companion - Dwarf
 * Strength: 6
 * Vitality: 4
 * Resistance: 7
 * Card Number: 1C18
 * Game Text: Damage +1. While in your starting fellowship, Gimli's twilight cost is -1.
 * While at a mountain or underground site, Gimli is strength +2 and takes no more than one wound in a skirmish.
 */
public class Card40_018 extends AbstractCompanion {
    public Card40_018() {
        super(3, 6, 4, 7, Culture.DWARVEN, Race.DWARF, null, "Gimli", "Stalwart Protector", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        if (game.getGameState().getCurrentPhase() == Phase.PLAY_STARTING_FELLOWSHIP)
            return -1;
        return 0;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        LocationCondition condition = new LocationCondition(Filters.or(Keyword.MOUNTAIN, Keyword.UNDERGROUND));

        StrengthModifier modifier1 = new StrengthModifier(self, self,
                condition, 2);
        CantTakeMoreThanXWoundsModifier modifier2 = new CantTakeMoreThanXWoundsModifier(
                self, Phase.SKIRMISH, 1, condition, self);

        return Arrays.asList(modifier1, modifier2);
    }
}
