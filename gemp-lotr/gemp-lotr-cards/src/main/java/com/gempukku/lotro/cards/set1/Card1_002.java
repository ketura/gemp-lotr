package com.gempukku.lotro.cards.set1;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.StartOfPhaseResult;

import java.util.Collections;
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
public class Card1_002 extends AbstractAttachable {
    public Card1_002() {
        super(Side.RING, CardType.THE_ONE_RING, 0, null, null, "The One Ring", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.none();
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new StrengthModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(new KeywordModifier(self, Filters.hasAttached(self), Keyword.RING_BEARER));
        modifiers.add(new KeywordModifier(self, Filters.hasAttached(self), Keyword.RING_BOUND));
        return modifiers;
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(final String playerId, LotroGame game, Effect effect, final PhysicalCard self) {
        if (game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && effect.getType() == EffectResult.Type.WOUND
                && !game.getGameState().isCancelRingText()) {
            WoundCharacterEffect woundEffect = (WoundCharacterEffect) effect;
            if (woundEffect.getCardsToBeAffected(game).contains(self.getAttachedTo())) {
                List<Action> actions = new LinkedList<Action>();

                ActivateCardAction action = new ActivateCardAction(self, Keyword.RESPONSE);
                action.appendEffect(new PreventEffect(woundEffect, self.getAttachedTo()));
                action.appendEffect(new CardAffectsCardEffect(self, self.getAttachedTo()));
                action.appendEffect(new AddBurdenEffect(self));
                action.appendEffect(new PutOnTheOneRingEffect());
                action.appendEffect(new AddUntilStartOfPhaseActionProxyEffect(
                        new AbstractActionProxy() {
                            @Override
                            public List<? extends Action> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResult) {
                                if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                                        && ((StartOfPhaseResult) effectResult).getPhase() == Phase.REGROUP) {
                                    ActivateCardAction action = new ActivateCardAction(self, null);
                                    action.appendEffect(new TakeOffTheOneRingEffect());
                                    return Collections.singletonList(action);
                                }
                                return null;
                            }
                        }
                        , Phase.REGROUP));

                actions.add(action);
                return actions;
            }
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.WOUND
                && game.getGameState().isWearingRing()
                && !game.getGameState().isCancelRingText()) {
            WoundCharacterEffect woundEffect = (WoundCharacterEffect) effect;
            if (woundEffect.getCardsToBeAffected(game).contains(self.getAttachedTo())) {
                List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(new PreventEffect(woundEffect, self.getAttachedTo()));
                action.appendEffect(new CardAffectsCardEffect(self, self.getAttachedTo()));
                action.appendEffect(new AddBurdenEffect(self));
                return actions;
            }
        }
        return null;
    }
}
