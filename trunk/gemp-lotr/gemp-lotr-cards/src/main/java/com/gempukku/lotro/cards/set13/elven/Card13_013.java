package com.gempukku.lotro.cards.set13.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Assignment: Discard this from play and assign a minion to an [ELVEN] companion. Those characters cannot
 * use skirmish special abilities until the start of the regroup phase.
 */
public class Card13_013 extends AbstractPermanent {
    public Card13_013() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Crashing Cavalry");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ASSIGNMENT, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION, Filters.assignableToSkirmishAgainst(Side.FREE_PEOPLE, Filters.and(Culture.ELVEN, CardType.COMPANION))) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard minion) {
                            action.insertEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose an ELVEN companion", Culture.ELVEN, CardType.COMPANION, Filters.assignableToSkirmishAgainst(Side.FREE_PEOPLE, minion)) {
                                        @Override
                                        protected void cardSelected(LotroGame game, final PhysicalCard companion) {
                                            action.insertEffect(
                                                    new AssignmentEffect(playerId, companion, minion));
                                            action.appendEffect(
                                                    new AddUntilStartOfPhaseModifierEffect(
                                                            new AbstractModifier(self, "Can't use skirmish special abilities", null, ModifierEffect.ACTION_MODIFIER) {
                                                                @Override
                                                                public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
                                                                    if (action.getActionTimeword() == Phase.SKIRMISH && action.getType() == Action.Type.SPECIAL_ABILITY
                                                                            && (action.getActionSource() == minion || action.getActionSource() == companion))
                                                                        return false;
                                                                    return true;
                                                                }
                                                            }, Phase.REGROUP));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
