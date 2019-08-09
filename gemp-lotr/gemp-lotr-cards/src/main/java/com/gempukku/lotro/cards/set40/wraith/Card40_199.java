package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseActionProxyEffect;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AssignAgainstResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Shire! Baggins!
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 1
 * Type: Event - Response
 * Card Number: 1U199
 * Game Text: If the Free Peoples player assigns a Nazgul to skirmish a Hobbit, exert that Nazgul.
 * If the Free Peoples player cancels this skirmish, add a burden.
 */
public class Card40_199 extends AbstractResponseEvent {
    public Card40_199() {
        super(Side.SHADOW, 1, Culture.WRAITH, "Shire! Baggins!");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.assignedAgainst(game, effectResult, Side.FREE_PEOPLE, Race.NAZGUL, Race.HOBBIT)
        && checkPlayRequirements(playerId, game, self, 0, 0, false, false)) {
            final AssignAgainstResult assignResult = (AssignAgainstResult) effectResult;
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(action, self, Race.NAZGUL, Filters.in(assignResult.getAgainst())));
            action.appendEffect(
                    new AddUntilStartOfPhaseActionProxyEffect(
                            new AbstractActionProxy() {
                                @Override
                                public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                                    if (TriggerConditions.skirmishCancelled(game, effectResult, assignResult.getAssignedCard())) {
                                        RequiredTriggerAction action = new RequiredTriggerAction(self);
                                        action.setVirtualCardAction(true);
                                        action.appendEffect(
                                                new AddBurdenEffect(self.getOwner(), self, 1));
                                        return Collections.singletonList(action);
                                    }
                                    return null;
                                }
                            }, Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
