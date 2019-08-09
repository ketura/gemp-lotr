package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.RevealHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CardPhaseLimitEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Frodo. Fellowship or Regroup: Exert Frodo to reveal an opponent's hand. Remove (1) for
 * each Orc revealed (limit (4)).
 */
public class Card1_313 extends AbstractAttachableFPPossession {
    public Card1_313() {
        super(1, 2, 0, Culture.SHIRE, PossessionClass.HAND_WEAPON, "Sting", null, true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.frodo;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if ((PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                || PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self))
                && PlayConditions.canExert(self, game, self.getAttachedTo())) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new ExertCharactersEffect(action, self, self.getAttachedTo()));
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(final String opponentId) {
                            action.appendEffect(
                                    new RevealHandEffect(self, playerId, opponentId) {
                                        @Override
                                        protected void cardsRevealed(Collection<? extends PhysicalCard> cards) {
                                            Collection<PhysicalCard> orcs = Filters.filter(game.getGameState().getHand(opponentId), game, Race.ORC);
                                            int toRemove = Math.min(orcs.size(), game.getGameState().getTwilightPool());
                                            action.appendEffect(
                                                    new RemoveTwilightEffect(
                                                            new CardPhaseLimitEvaluator(game, self, game.getGameState().getCurrentPhase(), 4,
                                                                    new ConstantEvaluator(toRemove))));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
