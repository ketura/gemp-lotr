package com.gempukku.lotro.cards.set30.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
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
    public boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.DWARVEN, CardType.COMPANION);
    }

    @Override
    public void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self) {
        action.appendCost(new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, Culture.DWARVEN, CardType.COMPANION));
    }
	
	@Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE));
	}
}