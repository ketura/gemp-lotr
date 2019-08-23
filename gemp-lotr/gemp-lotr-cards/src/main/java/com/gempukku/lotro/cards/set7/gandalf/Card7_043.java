package com.gempukku.lotro.cards.set7.gandalf;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event • Maneuver
 * Game Text: Exert Gandalf three times (or twice if you have initiative) to make all unbound companions of one culture
 * (except [GANDALF]) strength +3 until the regroup phase.
 */
public class Card7_043 extends AbstractEvent {
    public Card7_043() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Light the Beacons", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return (PlayConditions.canExert(self, game, 3, Filters.gandalf)
                        || (PlayConditions.hasInitiative(game, Side.FREE_PEOPLE) && PlayConditions.canExert(self, game, 2, Filters.gandalf)));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        int exertCount = (PlayConditions.hasInitiative(game, Side.FREE_PEOPLE)) ? 2 : 3;
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, exertCount, Filters.gandalf));
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose companion whose culture should get the strength bonus", Filters.unboundCompanion, Filters.not(Culture.GANDALF)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        game.getModifiersEnvironment().addUntilStartOfPhaseModifier(
                                new StrengthModifier(self, Filters.and(card.getBlueprint().getCulture(), Filters.unboundCompanion), 3), Phase.REGROUP);
                    }
                });
        return action;
    }
}
