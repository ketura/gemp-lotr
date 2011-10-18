package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractResponseOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.SpecialFlagModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.Assignment;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
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
public class Card1_224 extends AbstractResponseOldEvent {
    public Card1_224() {
        super(Side.SHADOW, Culture.WRAITH, "Return to Its Master");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.END_OF_PHASE
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && game.getGameState().isWearingRing()
                && PlayConditions.canPayForShadowCard(game, self, 0)) {
            final PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        public void doPlayEffect(final LotroGame game) {
                            List<Assignment> assignments = new LinkedList<Assignment>(game.getGameState().getAssignments());
                            for (Assignment assignment : assignments)
                                game.getGameState().removeAssignment(assignment);

                            if (Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER), Filters.canBeAssignedToSkirmishByEffect(Side.SHADOW)) > 0) {
                                action.appendEffect(
                                        new ChooseActiveCardEffect(self, playerId, "Choose a Nazgul to skirmish the Ring-Bearer", Filters.race(Race.NAZGUL), Filters.canBeAssignedToSkirmishByEffect(Side.SHADOW)) {
                                            @Override
                                            protected void cardSelected(LotroGame game, PhysicalCard nazgul) {
                                                PhysicalCard ringBearer = game.getGameState().getRingBearer(game.getGameState().getCurrentPlayerId());
                                                action.appendEffect(new AssignmentEffect(playerId, ringBearer, nazgul));
                                                action.appendEffect(
                                                        new AddUntilEndOfPhaseModifierEffect(
                                                                new SpecialFlagModifier(self, ModifierFlag.RING_TEXT_INACTIVE), Phase.SKIRMISH));
                                            }
                                        });
                            }
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
