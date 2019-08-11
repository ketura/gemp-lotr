package com.gempukku.lotro.cards.set30.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
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
 * Game Text: Aid - Exert a companion. (At the start of the maneuver phase, you may exert a companion
 * to transfer this to a companion.)
 * Each time bearer wins a fierce skirmish, you may discard a condition from play.
 */
public class Card30_005 extends AbstractFollower {
    public Card30_005() {
        super(Side.FREE_PEOPLE, 1, 1, 0, 0, Culture.DWARVEN, "Bifur", "Inarticulate", true);
    }
	
	@Override
	public Race getRace() {
		return Race.DWARF;
	}
	
    @Override
    public boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, CardType.COMPANION);
    }

    @Override
    public void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self) {
        action.appendCost(new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, CardType.COMPANION));
	}


    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))//
				&& game.getGameState().isFierceSkirmishes()) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}