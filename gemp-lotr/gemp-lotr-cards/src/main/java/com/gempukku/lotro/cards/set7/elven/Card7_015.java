package com.gempukku.lotro.cards.set7.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ReconcileHandEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.AddActionToCardModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
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
        super(Side.FREE_PEOPLE, CardType.CONDITION, 2, Culture.ELVEN, null, "Ancient Blade", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ELVEN, CardType.COMPANION);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(final LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new AddActionToCardModifier(self, null, CardType.MINION) {
                    @Override
                    protected ActivateCardAction createExtraPhaseAction(LotroGame game, PhysicalCard matchingCard) {
                        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, matchingCard, 0)) {
                            ActivateCardAction action = new ActivateCardAction(matchingCard);
                            action.setText("Assign to " + GameUtils.getFullName(self.getAttachedTo()));
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
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.MINION));
            action.appendEffect(
                    new ReconcileHandEffect(self.getOwner()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
