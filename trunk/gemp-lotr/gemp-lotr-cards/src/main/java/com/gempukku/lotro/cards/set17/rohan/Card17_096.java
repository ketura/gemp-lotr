package com.gempukku.lotro.cards.set17.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignMinionToCompanionEffect;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.cards.modifiers.LoseAllKeywordsModifier;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 7
 * Game Text: While the Ringbearer is assigned to a skirmish, Eowyn cannot take wounds while skirmishing.
 * Assignment: Exert Eowyn and assign a minion to the Ringbearer to make that minion lose all game text keywords
 * and unable to gain game text keywords until the regroup phase.
 */
public class Card17_096 extends AbstractCompanion {
    public Card17_096() {
        super(2, 6, 3, 7, Culture.ROHAN, Race.MAN, null, Names.eowyn, "Northwoman", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantTakeWoundsModifier(self,
                new AndCondition(
                        new SpotCondition(Filters.ringBearer, Filters.assignedToSkirmish),
                        new SpotCondition(self, Filters.inSkirmish)), self);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ASSIGNMENT, self)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canSpot(game, CardType.MINION, Filters.assignableToSkirmishAgainst(Side.FREE_PEOPLE, Filters.ringBearer))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendCost(
                    new ChooseAndAssignMinionToCompanionEffect(action, playerId, game.getGameState().getRingBearer(playerId), CardType.MINION) {
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
