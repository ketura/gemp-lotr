package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Ally • Home 3 • Man
 * Strength: 2
 * Vitality: 2
 * Site: 3
 * Game Text: To play, spot Gandalf. Fellowship: Exert Ottar to discard up to 3 cards from hand and draw an equal
 * number of cards.
 */
public class Card1_080 extends AbstractAlly {
    public Card1_080() {
        super(1, 3, 2, 2, Race.MAN, Culture.GANDALF, "Ottar", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.FELLOWSHIP);
            action.appendCost(new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card(s) to discard", game.getGameState().getHand(playerId), 0, 3) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            action.appendEffect(new DiscardCardsFromHandEffect(self, selectedCards));
                            action.appendEffect(new DrawCardEffect(playerId, selectedCards.size()));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
