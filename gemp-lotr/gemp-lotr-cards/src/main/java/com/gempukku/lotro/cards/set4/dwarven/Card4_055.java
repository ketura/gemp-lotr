package com.gempukku.lotro.cards.set4.dwarven;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ShuffleCardsFromPlayAndStackedOnItIntoDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

import java.util.Collections;
import java.util.Set;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Regroup: Exert a Dwarf to shuffle a [DWARVEN] condition (and all cards stacked on it) into your draw deck.
 * Exert a minion for each card shuffled into your draw deck.
 */
public class Card4_055 extends AbstractOldEvent {
    public Card4_055() {
        super(Side.FREE_PEOPLE, Culture.DWARVEN, "Restless Axe", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF));
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.DWARF)));
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a DWARVEN condition", Filters.culture(Culture.DWARVEN), Filters.type(CardType.CONDITION)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new ShuffleCardsFromPlayAndStackedOnItIntoDeckEffect(self, playerId, Collections.singleton(card)) {
                                    @Override
                                    protected void cardsShuffledCallback(Set<PhysicalCard> cardsShuffled) {
                                        action.appendEffect(
                                                new ChooseAndExertCharactersEffect(action, playerId, cardsShuffled.size(), cardsShuffled.size(), Filters.type(CardType.MINION)));
                                    }
                                });

                    }
                });
        return action;
    }
}
