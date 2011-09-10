package com.gempukku.lotro.cards.set1;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.modifiers.CompositeModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.StartOfPhaseResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Type: The One Ring
 * Strength: +1
 * Vitality: +1
 * Game Text: Response: If bearer is about to take a wound, he wears The One Ring until the regroup phase. While wearing
 * The One Ring, each time the Ring-bearer is about to take a wound, add 2 burdens instead.
 */
public class Card1_001 extends AbstractLotroCardBlueprint {
    public Card1_001() {
        super(Side.RING, CardType.THE_ONE_RING, null, "The One Ring", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return false;
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return null;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new StrengthModifier(null, null, 1));
        modifiers.add(new VitalityModifier(null, null, 1));
        modifiers.add(new KeywordModifier(null, null, Keyword.RING_BEARER));
        modifiers.add(new KeywordModifier(null, null, Keyword.RING_BOUND));

        return new CompositeModifier(self, Filters.attachedTo(self), modifiers);
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(final String playerId, LotroGame lotroGame, Effect effect, EffectResult effectResult, final PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WOUND
                && ((WoundResult) effectResult).getWoundedCard() == self.getAttachedTo()) {
            List<Action> actions = new LinkedList<Action>();

            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.RESPONSE, "Put on The One Ring until the Regroup phase");
            action.addCost(new CancelEffect(effect));
            action.addEffect(new AddBurdenEffect(playerId));
            action.addEffect(new AddBurdenEffect(playerId));
            action.addEffect(new PutOnTheOneRingEffect());
            action.addEffect(new AddUntilStartOfPhaseActionProxyEffect(
                    new AbstractActionProxy() {
                        @Override
                        public List<? extends Action> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResult) {
                            if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                                    && ((StartOfPhaseResult) effectResult).getPhase() == Phase.REGROUP) {
                                DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Take off The One Ring");
                                action.addEffect(new TakeOffTheOneRingEffect());
                                return Collections.singletonList(action);
                            }
                            return null;
                        }
                    }
                    , Phase.REGROUP));

            actions.add(action);
            return actions;
        } else {
            return null;
        }
    }

    @Override
    public List<? extends Action> getRequiredBeforeTriggers(LotroGame game, Effect effect, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WOUND
                && game.getGameState().isWearingRing()
                && !game.getGameState().isCancelRingText()
                && ((WoundResult) effectResult).getWoundedCard() == self.getAttachedTo()) {
            List<Action> actions = new LinkedList<Action>();
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Add 2 burdens instead of taking a wound");
            action.addCost(new CancelEffect(effect));
            action.addEffect(new AddBurdenEffect(self.getOwner()));
            action.addEffect(new AddBurdenEffect(self.getOwner()));
            return actions;
        } else {
            return null;
        }
    }

    @Override
    public int getTwilightCost() {
        throw new UnsupportedOperationException("This method should not be called on this card");
    }
}
