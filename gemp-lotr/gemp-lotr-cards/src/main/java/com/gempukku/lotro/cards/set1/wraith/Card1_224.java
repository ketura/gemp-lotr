package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If the Ring-bearer wears The One Ring at the end of a skirmish phase, cancel all remaining
 * assignments and assign a Nazgul to skirmish the Ring-bearer; The One Ring's game text does not apply during this skirmish.
 */
public class Card1_224 extends AbstractResponseEvent {
    public Card1_224() {
        super(Side.SHADOW, Culture.WRAITH, "Return to Its Master");
    }

    @Override
    public List<? extends Action> getOptionalAfterActions(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.END_OF_PHASE
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && game.getGameState().isWearingRing()
                && PlayConditions.canPayForShadowCard(game, self, 0)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Cancel all remaining " +
                    "assignments and assign a Nazgul to skirmish the Ring-bearer; The One Ring's game text does not " +
                    "apply during this skirmish.");
            action.addEffect(
                    new UnrespondableEffect() {
                        @Override
                        public void playEffect(final LotroGame game) {
                            List<Skirmish> assignments = new LinkedList<Skirmish>(game.getGameState().getAssignments());
                            for (Skirmish assignment : assignments)
                                game.getGameState().removeAssignment(assignment);

                            action.addEffect(
                                    new ChooseActiveCardEffect(playerId, "Choose a Nazgul to skirmish the Ring-Bearer", Filters.keyword(Keyword.NAZGUL)) {
                                        @Override
                                        protected void cardSelected(PhysicalCard nazgul) {
                                            PhysicalCard ringBearer = game.getGameState().getRingBearer(game.getGameState().getCurrentPlayerId());
                                            game.getGameState().assignToSkirmishes(ringBearer, Collections.singletonList(nazgul));
                                            game.getGameState().setCancelRingText(true);
                                            game.getActionsEnvironment().addUntilStartOfPhaseActionProxy(
                                                    new AbstractActionProxy() {
                                                        @Override
                                                        public List<? extends Action> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResult) {
                                                            if (effectResult.getType() == EffectResult.Type.END_OF_PHASE
                                                                    && lotroGame.getGameState().getCurrentPhase() == Phase.SKIRMISH)
                                                                game.getGameState().setCancelRingText(false);
                                                            return null;
                                                        }
                                                    }, Phase.REGROUP);
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
