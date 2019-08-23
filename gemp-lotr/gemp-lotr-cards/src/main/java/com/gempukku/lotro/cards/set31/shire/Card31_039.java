package com.gempukku.lotro.cards.set31.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.RemoveGameTextModifier;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make Bilbo strength +2. If he wears the One Ring, until the regroup phase,
 * remove the game text from a minion (except Sauron) skirmishing him and make it unable to
 * gain game text.
 */
public class Card31_039 extends AbstractEvent {
    public Card31_039() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "An Invisible Ring", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, final LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
				new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Filters.name("Bilbo")));
		if (game.getGameState().isWearingRing()) {
			action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION, Filters.inSkirmishAgainst(Filters.name("Bilbo"))) {
                @Override
                protected void cardSelected(LotroGame game, PhysicalCard card) {
                    action.appendEffect(
                            new AddUntilStartOfPhaseModifierEffect(
                                    new RemoveGameTextModifier(self, card), Phase.REGROUP));
                }
			});
		}
        return action;
    }
}
