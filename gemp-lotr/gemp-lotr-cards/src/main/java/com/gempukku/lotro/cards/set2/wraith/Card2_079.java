package com.gempukku.lotro.cards.set2.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.effects.PutOnTheOneRingEffect;
import com.gempukku.lotro.logic.effects.TakeOffTheOneRingEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Exert a twilight Nazgul to exert the Ring-bearer. If the Ring-bearer is then exhausted, he puts
 * on The One Ring until the regroup phase.
 */
public class Card2_079 extends AbstractEvent {
    public Card2_079() {
        super(Side.SHADOW, 1, Culture.WRAITH, "Resistance Becomes Unbearable", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.NAZGUL, Keyword.TWILIGHT);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.NAZGUL, Keyword.TWILIGHT));
        action.appendEffect(
                new ExertCharactersEffect(action, self, Filters.ringBearer));
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        if (Filters.canSpot(game, Filters.ringBearer, Filters.exhausted)) {
                            action.insertEffect(
                                    new PutOnTheOneRingEffect());
                            game.getActionsEnvironment().addUntilEndOfPhaseActionProxy(
                                    new AbstractActionProxy() {
                                        @Override
                                        public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResult) {
                                            if (TriggerConditions.startOfPhase(lotroGame, effectResult, Phase.REGROUP)) {
                                                RequiredTriggerAction action = new RequiredTriggerAction(self);
                                                action.appendEffect(
                                                        new TakeOffTheOneRingEffect());
                                                return Collections.singletonList(action);
                                            }
                                            return null;
                                        }
                                    }, Phase.REGROUP);
                        }
                    }
                });
        return action;
    }
}
