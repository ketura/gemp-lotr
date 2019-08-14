package com.gempukku.lotro.cards.set31.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AllyParticipatesInArcheryFireAndSkirmishesModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Archery: Spot an [ELVEN] archer ally to exert a minion. Until the regroup phase, that ally is
 * strength +2 and participates in archery fire and skirmishes.
 */
public class Card31_011 extends AbstractEvent {
    public Card31_011() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "The Evil Becomes Stronger", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.ELF, CardType.ALLY);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
		if (Filters.canSpot(game, Culture.ELVEN, Keyword.ARCHER, CardType.ALLY)) {
			final PlayEventAction action = new PlayEventAction(self);
			action.appendEffect(
					new ChooseActiveCardEffect(self, playerId, "Choose an Ally", Culture.ELVEN, Keyword.ARCHER, CardType.ALLY) {
				@Override
				protected void cardSelected(LotroGame game, PhysicalCard card) {
					action.insertEffect(
							new AddUntilStartOfPhaseModifierEffect(
									new StrengthModifier(self, Filters.sameCard(card), 2), Phase.REGROUP));
					action.insertEffect(
							new AddUntilStartOfPhaseModifierEffect(
									new AllyParticipatesInArcheryFireAndSkirmishesModifier(self, card), Phase.REGROUP));
				}
			});
			action.appendEffect(
					new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.MINION));
			return action;
		}
        return null;
	}
}