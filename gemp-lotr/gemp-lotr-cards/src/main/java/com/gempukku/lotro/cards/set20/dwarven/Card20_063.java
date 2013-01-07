package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.StackPlayedEventOnACardEffect;
import com.gempukku.lotro.cards.results.PlayEventResult;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Resourcefulness
 * Dwarven	Event • Manuever
 * Until the start of the regroup phase, each time you play a [Dwarven] event, stack that event on
 * a [Dwarven] support area condition.
 */
public class Card20_063 extends AbstractEvent {
    public Card20_063() {
        super(Side.FREE_PEOPLE, 2, Culture.DWARVEN, "Resourcefulness", Phase.MANEUVER);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilStartOfPhaseActionProxyEffect(
                        new AbstractActionProxy() {
                            @Override
                            public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                                if (TriggerConditions.played(game, effectResult, Filters.owner(playerId), Culture.DWARVEN, CardType.EVENT)) {
                                    final PlayEventResult playEventResult = (PlayEventResult) effectResult;
                                    final RequiredTriggerAction action = new RequiredTriggerAction(self);
                                    action.setVirtualCardAction(true);
                                    action.appendEffect(
                                            new ChooseActiveCardEffect(self, playerId, "Choose DWARVEN condition in your support area", Culture.DWARVEN, CardType.CONDITION, Keyword.SUPPORT_AREA) {
                                                @Override
                                                protected void cardSelected(LotroGame game, PhysicalCard card) {
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
