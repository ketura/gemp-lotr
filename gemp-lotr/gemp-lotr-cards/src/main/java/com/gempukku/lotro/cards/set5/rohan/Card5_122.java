package com.gempukku.lotro.cards.set5.rohan;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.ExtraFilters;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Valiant. Maneuver: Play a [ROHAN] possession on Eowyn to heal her.
 */
public class Card5_122 extends AbstractCompanion {
    public Card5_122() {
        super(2, 6, 3, Culture.ROHAN, Race.MAN, Signet.GANDALF, "Eowyn", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && PlayConditions.canPlayFromHand(playerId, game, Culture.ROHAN, CardType.POSSESSION, ExtraFilters.attachableTo(game, self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDiscard(playerId), Filters.and(Culture.ROHAN, CardType.POSSESSION, ExtraFilters.attachableTo(game, self)), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.iterator().next();
                                AttachPermanentAction attachPermanentAction = ((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, Filters.and(self), 0);
                                game.getActionsEnvironment().addActionToStack(attachPermanentAction);
                            }
                        }
                    });
            action.appendEffect(
                    new HealCharactersEffect(playerId, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
