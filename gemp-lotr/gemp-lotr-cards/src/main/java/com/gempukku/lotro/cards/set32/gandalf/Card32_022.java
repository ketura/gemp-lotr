package com.gempukku.lotro.cards.set32.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collection;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event • Maneuver
 * Game Text: Make X minions strength -X until the regroup phase, where X is the number of the following characters you can spot: Gandalf, Elrond or Galadriel.
 */
public class Card32_022 extends AbstractEvent {
    public Card32_022() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Gathering of the Three Rings", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        final int count = Filters.countActive(game, Filters.or(Filters.name("Galadriel"), Filters.name("Elrond"), Filters.name("Gandalf")));
        action.appendEffect(
                new ChooseActiveCardsEffect(self, game.getGameState().getCurrentPlayerId(), "Select minions", count, count, CardType.MINION) {
            @Override
            public void cardsSelected(LotroGame game, Collection<PhysicalCard> chosenMinions) {
                action.appendEffect(
                        new AddUntilEndOfPhaseModifierEffect(new StrengthModifier(self, Filters.in(chosenMinions), -count), Phase.REGROUP));
            }
        });
        return action;
    }
}
