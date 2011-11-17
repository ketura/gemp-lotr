package com.gempukku.lotro.cards.set7.elven;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.AddActionToCardModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.actions.PlayerReconcilesAction;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Bearer must be an [ELVEN] companion. Each minion gains this ability: 'Assignment: Assign this minion
 * to bearer of Ancient Blade.' Regroup: Discard this condition to discard a minion and reconcile your hand.
 */
public class Card7_015 extends AbstractAttachable {
    public Card7_015() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 2, Culture.ELVEN, null, "Ancient Blade", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ELVEN, CardType.COMPANION);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(final LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new AddActionToCardModifier(self, null, CardType.MINION) {
                    @Override
                    protected ActivateCardAction createExtraPhaseAction(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard matchingCard) {
                        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ASSIGNMENT, matchingCard)) {
                            ActivateCardAction action = new ActivateCardAction(matchingCard);
                            action.setText("Assign to " + self.getAttachedTo().getBlueprint().getName());
                            action.appendEffect(
                                    new AssignmentEffect(matchingCard.getOwner(), self.getAttachedTo(), matchingCard));
                            return action;
                        }
                        return null;
                    }
                });
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.MINION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard a minion";
                        }
                    });
            possibleEffects.add(
                    new AbstractSuccessfulEffect() {
                        @Override
                        public String getText(LotroGame game) {
                            return "Reconcile your hand";
                        }

                        @Override
                        public Effect.Type getType() {
                            return null;
                        }

                        @Override
                        public Collection<? extends EffectResult> playEffect(LotroGame game) {
                            game.getActionsEnvironment().addActionToStack(
                                    new PlayerReconcilesAction(game, playerId));
                            return null;
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(
                            action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
