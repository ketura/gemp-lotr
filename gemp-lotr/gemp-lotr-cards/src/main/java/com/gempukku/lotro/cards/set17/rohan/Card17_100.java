package com.gempukku.lotro.cards.set17.rohan;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAssignMinionToCompanionEffect;
import com.gempukku.lotro.logic.modifiers.LoseAllKeywordsModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ROHAN, "Into the Caves");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 3, Culture.ROHAN, CardType.COMPANION);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ASSIGNMENT, self)
                && PlayConditions.canSpot(game, CardType.MINION, Filters.assignableToSkirmishAgainst(Side.FREE_PEOPLE, Filters.frodo))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            PhysicalCard frodo = Filters.findFirstActive(game, Filters.frodo);
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
