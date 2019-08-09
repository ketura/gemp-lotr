package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each time a Dwarf wins a skirmish against an Orc, discard that Orc. Discard
 * this condition if a Dwarf loses a skirmish.
 */
public class Card1_020 extends AbstractPermanent {
	public Card1_020() {
		super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Let Them Come!");
	}

	@Override
	public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
		if (TriggerConditions.winsSkirmish(game, effectResult, Race.DWARF) && PlayConditions.isActive(game, Filters.inSkirmish, Race.ORC)) {
			RequiredTriggerAction action = new RequiredTriggerAction(self);
			action.appendEffect(
							new DiscardCardsFromPlayEffect(self.getOwner(), self, Filters.inSkirmish, Race.ORC));
			return Collections.singletonList(action);
		}
		if (TriggerConditions.losesSkirmish(game, effectResult, Race.DWARF)) {
			RequiredTriggerAction action = new RequiredTriggerAction(self);
			action.appendEffect(new SelfDiscardEffect(self));
			return Collections.singletonList(action);
		}
		return null;
	}
}
