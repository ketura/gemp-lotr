package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndStackCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Possession
 * Game Text: Plays to your support area. Fellowship: Stack a Free Peoples artifact (or possession) from hand on this
 * card, or play a card stacked here as if played from hand.
 */
public class Card1_073 extends AbstractPermanent {
    public Card1_073() {
        super(Side.FREE_PEOPLE, 1, CardType.POSSESSION, Culture.GANDALF, "Gandalf's Cart", null, true);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && (
                Filters.filter(game.getGameState().getStackedCards(self), game, Filters.playable(game)).size() > 0
                        || Filters.filter(game.getGameState().getHand(playerId), game, Side.FREE_PEOPLE, Filters.or(CardType.ARTIFACT, CardType.POSSESSION)).size() > 0)) {
            final ActivateCardAction action = new ActivateCardAction(self);

            List<Effect> possibleChoices = new LinkedList<Effect>();
            possibleChoices.add(
                    new ChooseAndStackCardsFromHandEffect(action, playerId, 1, 1, self, Side.FREE_PEOPLE, Filters.or(CardType.ARTIFACT, CardType.POSSESSION)));
            possibleChoices.add(
                    new ChooseAndPlayCardFromStackedEffect(playerId, self, Filters.any));
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleChoices));

            return Collections.singletonList(action);
        }
        return null;
    }
}
