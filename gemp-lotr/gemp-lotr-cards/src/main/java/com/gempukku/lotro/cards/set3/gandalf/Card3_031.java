package com.gempukku.lotro.cards.set3.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.CantBeAssignedToSkirmishModifier;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Stealth. Maneuver: At sites 1 to 5, spot Gandalf to prevent Hobbits from being assigned to skirmishes
 * until the regroup phase. At any other site, spot Gandalf to make a Hobbit strength +3 until the regroup phase.
 */
public class Card3_031 extends AbstractEvent {
    public Card3_031() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Depart Silently", Phase.MANEUVER);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"));
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        if (game.getGameState().getCurrentSiteNumber() <= 5) {
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new CantBeAssignedToSkirmishModifier(self, Filters.race(Race.HOBBIT)), Phase.REGROUP));
        } else {
            action.appendEffect(
                    new ChooseActiveCardEffect(playerId, "Choose a Hobbit", Filters.race(Race.HOBBIT)) {
                        @Override
                        protected void cardSelected(PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(card), 3), Phase.REGROUP));
                        }
                    });
        }
        return action;
    }
}
