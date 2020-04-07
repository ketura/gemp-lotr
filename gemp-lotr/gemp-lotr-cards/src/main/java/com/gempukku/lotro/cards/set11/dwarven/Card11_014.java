package com.gempukku.lotro.cards.set11.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.timing.ExtraFilters;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event • Fellowship
 * Game Text: Discard a [DWARVEN] card from hand to play a [DWARVEN] possession from your draw deck on a [DWARVEN]
 * companion who has resistance 5 or more.
 */
public class Card11_014 extends AbstractEvent {
    public Card11_014() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Well-equipped", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canDiscardCardsFromHandToPlay(self, game, self.getOwner(), 1, Culture.DWARVEN);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.DWARVEN));
        action.appendEffect(
                new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDeck(playerId), Filters.and(Culture.DWARVEN, CardType.POSSESSION, ExtraFilters.attachableTo(game, Culture.DWARVEN, CardType.COMPANION, Filters.minResistance(5))), 0, 1) {
                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                        if (selectedCards.size() > 0) {
                            PhysicalCard selectedCard = selectedCards.iterator().next();
                            game.getActionsEnvironment().addActionToStack(PlayUtils.getPlayCardAction(game, selectedCard, 0, Filters.and(Culture.DWARVEN, CardType.COMPANION, Filters.minResistance(5)), false));
                        }
                    }
                });
        return action;
    }
}
