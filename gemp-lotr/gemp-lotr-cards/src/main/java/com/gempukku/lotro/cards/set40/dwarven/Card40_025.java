package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseActionProxyEffect;
import com.gempukku.lotro.logic.effects.StackPlayedEventOnACardEffect;
import com.gempukku.lotro.logic.timing.results.PlayEventResult;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Resourcefulness
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Event - Maneuver
 * Card Number: 1U25
 * Game Text: Until the start of the regroup phase, each time you play a [DWARVEN] skirmish event,
 * stack that event on a [DWARVEN] support area condition.
 */
public class Card40_025 extends AbstractEvent {
    public Card40_025() {
        super(Side.FREE_PEOPLE, 2, Culture.DWARVEN, "Resourcefulness", Phase.MANEUVER);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilStartOfPhaseActionProxyEffect(
                        new AbstractActionProxy() {
                            public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame lotroGame, final EffectResult effectResults) {
                                if (TriggerConditions.played(lotroGame, effectResults, Culture.DWARVEN, CardType.EVENT, Keyword.SKIRMISH)
                                        && PlayConditions.canSpot(lotroGame, 1, Culture.DWARVEN, CardType.CONDITION, Zone.SUPPORT)) {
                                    final RequiredTriggerAction action = new RequiredTriggerAction(self);
                                    action.setVirtualCardAction(true);
                                    action.appendEffect(
                                            new ChooseActiveCardEffect(self, playerId, "Choose condition to stack the played event on", Culture.DWARVEN, CardType.CONDITION, Zone.SUPPORT) {
                                                @Override
                                                protected void cardSelected(LotroGame game, PhysicalCard card) {
                                                    final PlayEventResult playEventResult = (PlayEventResult) effectResults;
                                                    action.appendEffect(
                                                            new StackPlayedEventOnACardEffect(playEventResult.getPlayEventAction(), card));
                                                }
                                            });
                                    return Collections.singletonList(action);
                                }
                                return null;
                            }
                        }, Phase.REGROUP));
        return action;
    }
}
