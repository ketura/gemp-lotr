package com.gempukku.lotro.cards.set30.dwarven;

import com.gempukku.lotro.cards.AbstractFollower;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Follower • Dwarf
 * Strength: +2
 * Game Text: Aid - Exert a [DWARVEN] companion. (At the start of the maneuver phase, you may exert a [DWARVEN] companion
 * to transfer this to a companion.)
 */
public class Card30_007 extends AbstractFollower {
    public Card30_007() {
        super(Side.FREE_PEOPLE, 3, 2, 0, 0, Culture.DWARVEN, "Bombur", "Chief Cook", true);
    }
	
	@Override
	public Race getRace() {
		return Race.DWARF;
	}
	
    @Override
    protected boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.DWARVEN, CardType.COMPANION);
    }

    @Override
    protected Effect getAidCost(LotroGame game, Action action, PhysicalCard self) {
        return new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, Culture.DWARVEN, CardType.COMPANION);
    }
	
	@Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE));
	}
}