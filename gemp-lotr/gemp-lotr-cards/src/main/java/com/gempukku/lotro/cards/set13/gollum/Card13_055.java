package com.gempukku.lotro.cards.set13.gollum;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDeckIntoDiscardEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Companion
 * Strength: 3
 * Vitality: 4
 * Resistance: 5
 * Game Text: Ring-bound. To play, add a burden. For each card titled Deagol in your discard pile, Smeagol is
 * strength +2 and resistance -1. Each time Smeagol wins a skirmish, you may take a [GOLLUM] card from your draw deck
 * and place it in your discard pile.
 */
public class Card13_055 extends AbstractCompanion {
    public Card13_055() {
        super(0, 3, 4, 5, Culture.GOLLUM, null, null, "Smeagol", true);
        addKeyword(Keyword.RING_BOUND);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayPermanentAction playCardAction = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        playCardAction.appendCost(
                new AddBurdenEffect(self, 1));
        return playCardAction;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                return Filters.filter(gameState.getDiscard(self.getOwner()), gameState, modifiersQuerying, Filters.name("Deagol")).size() * 2;
                            }
                        }));
        modifiers.add(
                new ResistanceModifier(self, self, null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                return -Filters.filter(gameState.getDiscard(self.getOwner()), gameState, modifiersQuerying, Filters.name("Deagol")).size();
                            }
                        }));
        return modifiers;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPutCardFromDeckIntoDiscardEffect(action, playerId, 1, 1, Culture.GOLLUM));
            action.appendEffect(
                    new ShuffleDeckEffect(playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
