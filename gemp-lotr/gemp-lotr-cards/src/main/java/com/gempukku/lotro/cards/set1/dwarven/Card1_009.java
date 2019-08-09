package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterLostSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Possession � Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be a Dwarf. Each time a player's minion loses a skirmish to bearer, that player discards the
 * top card from his draw deck.
 */
public class Card1_009 extends AbstractAttachableFPPossession {
	public Card1_009() {
		super(0, 2, 0, Culture.DWARVEN, PossessionClass.HAND_WEAPON, "Dwarven Axe");
	}

	@Override
	protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
		return Race.DWARF;
	}

	@Override
	public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
		if (TriggerConditions.losesSkirmishInvolving(game, effectResult, CardType.MINION, Filters.hasAttached(self))) {
			CharacterLostSkirmishResult skirmishResult = ((CharacterLostSkirmishResult) effectResult);
			PhysicalCard loser = skirmishResult.getLoser();
			RequiredTriggerAction action = new RequiredTriggerAction(self);
			action.appendEffect(new DiscardTopCardFromDeckEffect(self, loser.getOwner(), 1, true));
			return Collections.singletonList(action);
		}

		return null;
	}
}
