package com.gempukku.lotro.cards.set2.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PutOnTheOneRingEffect;
import com.gempukku.lotro.cards.effects.TakeOffTheOneRingEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
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
        super(Side.SHADOW, Culture.WRAITH, "Resistance Becomes Unbearable");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.NAZGUL), Filters.keyword(Keyword.TWILIGHT));
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.NAZGUL), Filters.keyword(Keyword.TWILIGHT)));
        action.appendEffect(
                new ExertCharactersEffect(self, Filters.keyword(Keyword.RING_BEARER)));
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER), Filters.exhausted())) {
                            action.insertEffect(
                                    new PutOnTheOneRingEffect());
                            game.getActionsEnvironment().addUntilEndOfPhaseActionProxy(
                                    new AbstractActionProxy() {
                                        @Override
                                        public List<? extends Action> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResults) {
                                            if (effectResults.getType() == EffectResult.Type.START_OF_PHASE
                                                    && lotroGame.getGameState().getCurrentPhase() == Phase.REGROUP) {
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
