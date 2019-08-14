package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * •The Mouth of Sauron, Black Gate Messenger
 * Sauron	Minion • Man
 * 9	3	6
 * Assignment: Exert the Mouth of Sauron to assign it to a companion (except the Ring-bearer).
 * The Free Peoples player may exert a companion of another culture to prevent this.
 */
public class Card20_362 extends AbstractMinion {
    public Card20_362() {
        super(3, 9, 3, 6, Race.MAN, Culture.SAURON, "The Mouth of Sauron", "Black Gate Messenger", true);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a companion to assign to", CardType.COMPANION, Filters.not(Filters.ringBearer),
                            Filters.assignableToSkirmishAgainst(Side.SHADOW, self)) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard companionToAssignTo) {
                            action.appendEffect(
                                    new PreventableEffect(action,
                                            new AssignmentEffect(playerId, companionToAssignTo, self),
                                            game.getGameState().getCurrentPlayerId(),
                                            new PreventableEffect.PreventionCost() {
                                                @Override
                                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                                    return new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(companionToAssignTo.getBlueprint().getCulture())) {
                                                        @Override
                                                        public String getText(LotroGame game) {
                                                            return "Exert a companion of another culture";
                                                        }
                                                    };
                                                }
                                            }));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
