package com.gempukku.lotro.cards.set17.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignMinionToCompanionEffect;
import com.gempukku.lotro.cards.modifiers.LoseAllKeywordsModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot 3 [ROHAN] companions. Assignment: Assign a minion to Frodo to make that minion lose all
 * keywords of your choice and unable to gain keywords until the regroup phase.
 */
public class Card17_100 extends AbstractPermanent {
    public Card17_100() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Into the Caves");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 3, Culture.ROHAN, CardType.COMPANION);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canSpot(game, CardType.MINION, Filters.assignableToSkirmishAgainst(Side.FREE_PEOPLE, Filters.frodo))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            PhysicalCard frodo = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.frodo);
            action.appendCost(
                    new ChooseAndAssignMinionToCompanionEffect(action, playerId, frodo, CardType.MINION) {
                        @Override
                        protected void assignmentMadeCallback(PhysicalCard fpChar, PhysicalCard minion) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new LoseAllKeywordsModifier(self, minion), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
