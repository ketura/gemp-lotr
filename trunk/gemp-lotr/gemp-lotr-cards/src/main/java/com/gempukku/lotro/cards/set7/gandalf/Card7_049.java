package com.gempukku.lotro.cards.set7.gandalf;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.AddActionToCardModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Bearer must be Gandalf. Each minion gains this ability: 'Assignment: Assign this minion to Gandalf.'
 * Regroup: Discard this condition to discard a minion and heal a companion 3 times.
 */
public class Card7_049 extends AbstractAttachable {
    public Card7_049() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 2, Culture.GANDALF, null, "Steadfast Champion", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.gandalf;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(final LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new AddActionToCardModifier(self, null, CardType.MINION) {
                    @Override
                    protected ActivateCardAction createExtraPhaseAction(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard matchingCard) {
                        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ASSIGNMENT, matchingCard)) {
                            ActivateCardAction action = new ActivateCardAction(matchingCard);
                            action.setText("Assign to Gandalf");
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
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.MINION));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, 3, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
