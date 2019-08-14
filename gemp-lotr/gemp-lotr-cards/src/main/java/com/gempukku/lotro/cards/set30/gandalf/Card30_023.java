package com.gempukku.lotro.cards.set30.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Play Gandalf or an ally (except a Dwarf) from your draw deck or discard pile.
 */
public class Card30_023 extends AbstractEvent {
    public Card30_023() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "A Wizard Is Never Late", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        Filter filter = Filters.or(Filters.gandalf, Filters.and(CardType.ALLY, Filters.not(Race.DWARF)));

        PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseAndPlayCardFromDeckEffect(playerId, filter) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play character from draw deck";
                    }
                });
        possibleEffects.add(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, filter) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play character from discard pile";
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
