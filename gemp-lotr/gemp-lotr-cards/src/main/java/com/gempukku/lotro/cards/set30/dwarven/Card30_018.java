package com.gempukku.lotro.cards.set30.dwarven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Follower • Dwarf
 * Strength: +1
 * Game Text: Aid - Discard a [DWARVEN] card from hand. (At the start of the maneuver phase, you may discard a [DWARVEN]
 * card from hand to transfer this to a companion.) When you transfer Ori to Balin, you may draw 3 cards.
 */
public class Card30_018 extends AbstractFollower {
    public Card30_018() {
        super(Side.FREE_PEOPLE, 1, 1, 0, 0, Culture.DWARVEN, "Ori", "Dwarven Chronicler", true);
    }
	
	@Override
	public Race getRace() {
		return Race.DWARF;
	}
	
    @Override
    public boolean canPayAidCost(LotroGame game, PhysicalCard self) {
		if (PlayConditions.canDiscardFromHand(game, self.getOwner(), 1, Culture.DWARVEN)) {
			return true;
		} else {
			return false;
		}
    }

    @Override
    public void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self) {
        action.appendCost(new ChooseAndDiscardCardsFromHandEffect(action, self.getOwner(), false, 1, 1, Culture.DWARVEN));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.transferredCard(game, effectResult, self, null, Filters.name("Balin"))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 3));
            return Collections.singletonList(action);
        }
        return null;
    }
}