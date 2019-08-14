package com.gempukku.lotro.cards.set5.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.ExtraFilters;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Fellowship: Exert Aragorn twice to play any number of possessions from your discard pile onto companions
 * with the Aragorn signet.
 */
public class Card5_041 extends AbstractEvent {
    public Card5_041() {
        super(Side.FREE_PEOPLE, 3, Culture.GONDOR, "These Are My People", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, 2, Filters.aragorn);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.aragorn));

        action.appendEffect(
                new PlayAnyNumberOfPossessionsOnCharacters(action, playerId));

        return action;
    }

    private class PlayAnyNumberOfPossessionsOnCharacters extends AbstractSuccessfulEffect {
        private PlayEventAction _action;
        private String _playerId;

        private PlayAnyNumberOfPossessionsOnCharacters(PlayEventAction action, String playerId) {
            _action = action;
            _playerId = playerId;
        }

        @Override
        public String getText(LotroGame game) {
            return null;
        }

        @Override
        public Effect.Type getType() {
            return null;
        }

        @Override
        public void playEffect(final LotroGame game) {
            final Filter additionalAttachmentFilter = Filters.and(CardType.COMPANION, Signet.ARAGORN);

            _action.appendEffect(
                    new ChooseArbitraryCardsEffect(_playerId, "Choose card to play", game.getGameState().getDiscard(_playerId),
                            Filters.and(
                                    CardType.POSSESSION,
                                    ExtraFilters.attachableTo(game, additionalAttachmentFilter)), 0, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.iterator().next();
                                game.getActionsEnvironment().addActionToStack(PlayUtils.getPlayCardAction(game, selectedCard, 0, additionalAttachmentFilter, false));
                                _action.appendEffect(new PlayAnyNumberOfPossessionsOnCharacters(_action, _playerId));
                            }
                        }
                    });
        }
    }
}
