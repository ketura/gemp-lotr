package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. Assignment: Spot a site you control and remove (2) to assign a [DUNLAND] Man
 * to an unbound companion.
 */
public class Card4_031 extends AbstractPermanent {
    public Card4_031() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.DUNLAND, Zone.SUPPORT, "Over the Isen", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.ASSIGNMENT, self, 2)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlled(playerId))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(2));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose DUNLAND Man", Filters.culture(Culture.DUNLAND), Filters.race(Race.MAN), Filters.canBeAssignedToSkirmishByEffect(Side.SHADOW)) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard dunlandMan) {
                            action.insertEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose unbound companion", Filters.unboundCompanion(), Filters.canBeAssignedToSkirmishByEffect(Side.SHADOW)) {
                                        @Override
                                        protected void cardSelected(LotroGame game, PhysicalCard unboundCompanion) {
                                            action.insertEffect(
                                                    new AssignmentEffect(playerId, unboundCompanion, dunlandMan));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
