package com.gempukku.lotro.cards.set1;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.CancelEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Type: The One Ring
 * Strength: +1
 * Game Text: Response: If bearer is about to take a wound in a skirmish, he wears The One Ring until the regroup phase.
 * While wearing The One Ring, each time the Ring-bearer is about to take a wound during a skirmish, add a burden
 * instead.
 */
public class Card1_002 extends AbstractLotroCardBlueprint {
    public Card1_002() {
        super(Side.RING, CardType.THE_ONE_RING, null, "The One Ring", "1_2", true);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "Adds +1 Str, bearer is Ring-Bound and Ring-Bearer", Filters.sameCard(self.getAttachedTo())) {
            @Override
            public boolean hasKeyword(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, boolean result) {
                return (result || (keyword == Keyword.RING_BOUND || keyword == Keyword.RING_BEARER));
            }

            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                return result + 1;
            }
        };
    }

    @Override
    public List<? extends Action> getPlayableIsAboutToActions(final String playerId, LotroGame lotroGame, Effect effect, EffectResult effectResult, final PhysicalCard self) {
        if (lotroGame.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && effectResult.getType() == EffectResult.Type.WOUND
                && ((WoundResult) effectResult).getWoundedCard() == self.getAttachedTo()) {
            List<Action> actions = new LinkedList<Action>();

            CostToEffectAction action = new CostToEffectAction(self, "Put on The One Ring until the Regroup phase");
            action.addCost(new CancelEffect(effect));
            action.addEffect(new AddBurdenEffect(playerId));
            action.addEffect(new AddUntilStartOfPhaseActionProxyEffect(
                    new AbstractActionProxy() {
                        @Override
                        public List<Action> getRequiredIsAboutToActions(LotroGame lotroGame, Effect effect, EffectResult effectResult) {
                            if (effectResult.getType() == EffectResult.Type.WOUND
                                    && ((WoundResult) effectResult).getWoundedCard() == self.getAttachedTo()) {
                                List<Action> actions = new LinkedList<Action>();
                                CostToEffectAction action = new CostToEffectAction(self, "Add a burden instead of taking a wound");
                                action.addCost(new CancelEffect(effect));
                                action.addEffect(new AddBurdenEffect(playerId));
                                return actions;
                            } else {
                                return null;
                            }
                        }
                    }, Phase.REGROUP));

            actions.add(action);
            return actions;
        } else {
            return null;
        }
    }

}
