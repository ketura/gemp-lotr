package com.gempukku.lotro.cards.set30.dwarven;

import com.gempukku.lotro.cards.AbstractFollower;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Follower • Dwarf
 * Strength: +1
 * Game Text: Aid - Exert Bilbo. (At the start of the maneuver phase, you may exert Bilbo to transfer this to a companion.)
 * Bearer may not be overwhelmed unless his or her strength is tripled.
 */
public class Card30_008 extends AbstractFollower {
    public Card30_008() {
        super(Side.FREE_PEOPLE, 2, 1, 0, 0, Culture.DWARVEN, "Dori", "Really a Decent Fellow", true);
    }
	
	@Override
	public Race getRace() {
		return Race.DWARF;
	}
		
    @Override
    protected boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.name("Bilbo"));
    }

    @Override
    protected Effect getAidCost(LotroGame game, Action action, PhysicalCard self) {
        return new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, Filters.name("Bilbo"));
	}

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new OverwhelmedByMultiplierModifier(self, Filters.hasAttached(self), 3));
    }
}