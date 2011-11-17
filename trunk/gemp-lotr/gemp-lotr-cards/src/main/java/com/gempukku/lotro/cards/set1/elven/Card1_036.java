package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RevealAndChooseCardsFromOpponentHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Exert an Elf to reveal an opponent's hand. That player discards a card from hand for each Orc
 * revealed.
 */
public class Card1_036 extends AbstractOldEvent {
    public Card1_036() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Curse Their Foul Feet!", Phase.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, Race.ELF);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF));
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(String opponentId) {
                        List<? extends PhysicalCard> hand = game.getGameState().getHand(opponentId);
                        int orcsCount = Filters.filter(hand, game.getGameState(), game.getModifiersQuerying(), Race.ORC).size();
                        action.appendEffect(new RevealAndChooseCardsFromOpponentHandEffect(action, playerId, opponentId, self, "Opponent's hand", Filters.none, 0, 0) {
                            @Override
                            protected void cardsSelected(List<PhysicalCard> selectedCards) {
                                // Do nothing
                            }
                        });
                        action.appendEffect(new ChooseAndDiscardCardsFromHandEffect(action, opponentId, true, orcsCount));
                    }
                });
        return action;
    }
}
