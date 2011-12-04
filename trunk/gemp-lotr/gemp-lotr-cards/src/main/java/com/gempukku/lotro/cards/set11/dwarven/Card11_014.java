package com.gempukku.lotro.cards.set11.dwarven;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.ExtraFilters;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;

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
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canDiscardCardsFromHandToPlay(self, game, playerId, 1, Culture.DWARVEN);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.DWARVEN));
        action.appendEffect(
                new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDeck(playerId), Filters.and(Culture.DWARVEN, CardType.POSSESSION, ExtraFilters.attachableTo(game, Culture.DWARVEN, CardType.COMPANION, Filters.minResistance(5))), 0, 1) {
                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                        if (selectedCards.size() > 0) {
                            PhysicalCard selectedCard = selectedCards.iterator().next();
                            AttachPermanentAction attachPermanentAction = ((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, Filters.and(Culture.DWARVEN, CardType.COMPANION, Filters.minResistance(5)), 0);
                            game.getActionsEnvironment().addActionToStack(attachPermanentAction);
                        }
                    }
                });
        return action;
    }
}
