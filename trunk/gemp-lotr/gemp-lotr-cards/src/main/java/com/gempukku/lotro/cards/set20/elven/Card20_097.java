package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.SubCostToEffectAction;
import com.gempukku.lotro.cards.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.YesNoDecision;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 0
 * •Narya, Elven Ring of Power
 * Elven	Artifact • Ring
 * 1
 * Bearer must be Cirdan or Gandalf.
 * Maneuver: Exert bearer to reveal the top card of your draw deck. If it is a Free Peoples card, you may discard it
 * to heal 2 companions of that card's culture.
 */
public class Card20_097 extends AbstractAttachableFPPossession {
    public Card20_097() {
        super(0, 0, 1, Culture.ELVEN, PossessionClass.RING, "Narya", "Elven Ring of Power", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.or(Filters.name("Cirdan"), Filters.gandalf);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game, Filters.hasAttached(self))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.hasAttached(self)));
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                            for (final PhysicalCard revealedCard : revealedCards) {
                                if (Filters.and(Side.FREE_PEOPLE).accepts(game.getGameState(), game.getModifiersQuerying(), revealedCard)) {
                                    action.appendEffect(
                                            new PlayoutDecisionEffect(playerId,
                                                    new YesNoDecision("Do you want to discard that card to heal 2 companions of the card's culture?") {
                                                        @Override
                                                        protected void yes() {
                                                            SubCostToEffectAction subAction = new SubCostToEffectAction(action);
                                                            subAction.appendCost(
                                                                    new DiscardCardFromDeckEffect(revealedCard));
                                                            subAction.appendEffect(
                                                                    new ChooseAndHealCharactersEffect(action, playerId, 2, 2, CardType.COMPANION, revealedCard.getBlueprint().getCulture()));
                                                            game.getActionsEnvironment().addActionToStack(subAction);
                                                        }
                                                    }));
                                }
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
