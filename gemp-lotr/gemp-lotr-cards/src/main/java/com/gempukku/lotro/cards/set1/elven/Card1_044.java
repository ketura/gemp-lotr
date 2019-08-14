package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.RevealAndChooseCardsFromOpponentHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Exert an Elf to reveal an opponent's hand. You may discard a [ISENGARD] minion revealed to
 * draw 2 cards.
 */
public class Card1_044 extends AbstractEvent {
    public Card1_044() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Foul Creation", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.ELF);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF));
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(final String opponentId) {
                        action.appendEffect(
                                new RevealAndChooseCardsFromOpponentHandEffect(action, playerId, opponentId, self, "Choose an ISENGARD minion", Filters.and(Culture.ISENGARD, CardType.MINION), 0, 1) {
                                    @Override
                                    protected void cardsSelected(List<PhysicalCard> selectedCards) {
                                        if (selectedCards.size() > 0) {
                                            action.appendEffect(new DiscardCardsFromHandEffect(self, opponentId, selectedCards, true));
                                            action.appendEffect(new DrawCardsEffect(action, playerId, 1));
                                            action.appendEffect(new DrawCardsEffect(action, playerId, 1));
                                        }
                                    }
                                }
                        );
                    }
                });
        return action;
    }
}
