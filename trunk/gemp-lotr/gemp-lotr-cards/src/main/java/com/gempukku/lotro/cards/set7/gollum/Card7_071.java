package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ExhaustCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Companion
 * Strength: 3
 * Vitality: 4
 * Resistance: 6
 * Signet: Frodo
 * Game Text: Ring-bound. To play, add a burden. Assignment: Assign a minion to Smeagol and add 2 threats
 * to exhaust that minion.
 */
public class Card7_071 extends AbstractCompanion {
    public Card7_071() {
        super(0, 3, 4, 6, Culture.GOLLUM, null, Signet.FRODO, "Smeagol", true);
        addKeyword(Keyword.RING_BOUND);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayPermanentAction playCardAction = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        playCardAction.appendCost(
                new AddBurdenEffect(self, 1));
        return playCardAction;
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ASSIGNMENT, self)
                && PlayConditions.canCardAssignToSkirmish(self, game, self)
                && PlayConditions.canAddThreat(game, self, 2)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose minion to assign to Smeagol", CardType.MINION, Filters.canBeAssignedToSkirmishByEffectAgainst(Side.FREE_PEOPLE, self)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertCost(
                                    new AssignmentEffect(playerId, self, card));
                            action.appendEffect(
                                    new ExhaustCharacterEffect(self, action, card));
                        }
                    });
            action.appendCost(
                    new AddThreatsEffect(playerId, self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
