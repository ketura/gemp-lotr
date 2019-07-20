package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.List;

/**
 * Title: Galadriel's Wisdom
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event - Regroup
 * Card Number: 1C46
 * Game Text: Exert Galadriel and reveal the top card of your draw deck to discard a Shadow condition (or 2 Shadow conditions if you reveal an [ELVEN] card).
 */
public class Card40_046 extends AbstractEvent {
    public Card40_046() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Galadriel's Wisdom", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Filters.name("Galadriel"))
                && game.getGameState().getDeck(playerId).size() > 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Galadriel")));
        action.appendCost(
                new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                    @Override
                    protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                        if (revealedCards.size() > 0) {
                            PhysicalCard revealedCard = revealedCards.get(0);
                            int discardCount = (revealedCard.getBlueprint().getCulture() == Culture.ELVEN) ? 2 : 1;
                            action.appendEffect(
                                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, discardCount, discardCount, Side.SHADOW, CardType.CONDITION));
                        }
                    }
                });
        return action;
    }
}
