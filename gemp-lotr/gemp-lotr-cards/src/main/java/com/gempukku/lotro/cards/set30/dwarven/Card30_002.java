package com.gempukku.lotro.cards.set30.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.ShuffleCardsFromDiscardIntoDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Companion • Dwarf
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Game Text: Fellowship: Exert Balin to shuffle a Free Peoples event from your discard pile into your draw deck.
 */
public class Card30_002 extends AbstractCompanion{
    public Card30_002() {
        super(3, 7, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Balin", "Brother of Dwalin", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfExertEffect(action, self));

            Collection<PhysicalCard> discardedFreePeoplesEvents = Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Side.FREE_PEOPLE, CardType.EVENT);

            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to shuffle into draw deck", new LinkedList<PhysicalCard>(discardedFreePeoplesEvents), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            action.insertEffect(
                                    new ShuffleCardsFromDiscardIntoDeckEffect(self, playerId, selectedCards));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}

