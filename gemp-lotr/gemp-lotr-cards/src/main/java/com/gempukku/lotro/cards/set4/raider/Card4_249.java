package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 10
 * Vitality: 3
 * Site: 4
 * Game Text: Southron. Ambush (1). Assignment: Spot 6 companions to assign this minion to the Ring-bearer. The Free
 * Peoples player may discard an unbound companion to prevent this.
 */
public class Card4_249 extends AbstractMinion {
    public Card4_249() {
        super(5, 10, 3, 4, Race.MAN, Culture.RAIDER, "Southron Commander");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.AMBUSH, 1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSpot(game, 6, CardType.COMPANION)
                && PlayConditions.canCardAssignToSkirmish(self, game, self)
                && PlayConditions.canCardAssignToSkirmish(self, game, Keyword.RING_BEARER)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose Ring-bearer", Keyword.RING_BEARER) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(new PreventableEffect(action,
                                    new AssignmentEffect(playerId, card, self),
                                    game.getGameState().getCurrentPlayerId(),
                                    new PreventableEffect.PreventionCost() {
                                        @Override
                                        public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                            return new ChooseAndDiscardCardsFromPlayEffect(subAction, playerId, 1, 1, Filters.unboundCompanion);
                                        }
                                    }
                            ));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
