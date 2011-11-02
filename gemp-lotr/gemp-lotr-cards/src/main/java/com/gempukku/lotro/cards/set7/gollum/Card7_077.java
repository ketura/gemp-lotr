package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
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
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Bearer must be Smeagol. Each minion gains this ability: 'Assignment: Assign this minion to Smeagol.'
 * Regroup: Discard this condition to discard a minion and play a companion from your discard pile.
 */
public class Card7_077 extends AbstractAttachable {
    public Card7_077() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 2, Culture.GOLLUM, null, "We Hates Them", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.smeagol;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(final LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new AddActionToCardModifier(self, null, CardType.MINION) {
                    @Override
                    protected ActivateCardAction createExtraPhaseAction(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard matchingCard) {
                        if (PlayConditions.canUseFPCardDuringPhase(gameState, Phase.ASSIGNMENT, matchingCard)) {
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
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.REGROUP, self)
                && PlayConditions.canSelfDiscard(self, game)
                && PlayConditions.canPlayFromDiscard(playerId, game, CardType.COMPANION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.MINION));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId), CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
