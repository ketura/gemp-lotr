package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 3
 * Gimli, Stalwart Protector
 * Dwarven	Companion • Dwarf
 * 6	4	7
 * Damage +1.
 * While in your starting fellowship, Gimli's twilight cost is -1.
 * While at a mountain or underground site, Gimli is strength +2 and takes no more than one wound in a skirmish.
 */
public class Card20_054 extends AbstractCompanion {
    public Card20_054() {
        super(3, 6, 4, 7, Culture.DWARVEN, Race.DWARF, null, "Gimli", "Stalwart Protector", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (gameState.getCurrentPhase() == Phase.PLAY_STARTING_FELLOWSHIP)
            return -1;
        return 0;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        LocationCondition locationCondition = new LocationCondition(Filters.or(Keyword.MOUNTAIN, Keyword.UNDERGROUND));
        modifiers.add(
                new StrengthModifier(self, self, locationCondition, 2));
        modifiers.add(
                new CantTakeMoreThanXWoundsModifier(self, Phase.SKIRMISH, 1, locationCondition, self));
        return modifiers;
    }
}
