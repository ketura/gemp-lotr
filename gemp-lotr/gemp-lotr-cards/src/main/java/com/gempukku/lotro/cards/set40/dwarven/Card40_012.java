package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.PutCardFromStackedIntoHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseStackedCardsEffect;

import java.util.Collection;

/**
 * Title: Dwarven Frenzy
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event - Maneuver
 * Card Number: 1R12
 * Game Text: Exert X Dwarves twice to take X [DWARVEN] cards stacked on [DWARVEN] conditions into hand (and draw
 * X cards if the fellowship is at a mountain or underground site).
 */
public class Card40_012 extends AbstractEvent {
    public Card40_012() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Dwarven Frenzy", Phase.MANEUVER);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(
                        action, playerId, 0, Integer.MAX_VALUE, 2, Race.DWARF) {
                    @Override
                    protected void cardsToBeExertedCallback(Collection<PhysicalCard> characters) {
                        int exertedDwarves = characters.size();
                        action.appendEffect(
                                new ChooseStackedCardsEffect(action, playerId, exertedDwarves, exertedDwarves, Filters.and(Culture.DWARVEN, CardType.CONDITION), Culture.DWARVEN) {
                                    @Override
                                    protected void cardsChosen(LotroGame game, Collection<PhysicalCard> stackedCards) {
                                        for (PhysicalCard stackedCard : stackedCards) {
                                            action.appendEffect(new PutCardFromStackedIntoHandEffect(stackedCard));
                                        }
                                    }
                                });
                        if (Filters.or(Keyword.MOUNTAIN, Keyword.UNDERGROUND).accepts(game, game.getGameState().getCurrentSite())) {
                            action.appendEffect(
                                    new DrawCardsEffect(action, playerId, exertedDwarves));
                        }
                    }
                });
        return action;
    }
}
