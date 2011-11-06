package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractResponseOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a Dwarf wins a skirmish, make an opponent discard 3 cards from the top of his or her draw
 * deck.
 */
public class Card1_023 extends AbstractResponseOldEvent {
    public Card1_023() {
        super(Side.FREE_PEOPLE, Culture.DWARVEN, "Nobody Tosses a Dwarf");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.race(Race.DWARF))
                && checkPlayRequirements(playerId, game, self, 0)) {
            final PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(String opponentId) {
                            action.appendEffect(new DiscardTopCardFromDeckEffect(self, opponentId, true));
                            action.appendEffect(new DiscardTopCardFromDeckEffect(self, opponentId, true));
                            action.appendEffect(new DiscardTopCardFromDeckEffect(self, opponentId, true));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
