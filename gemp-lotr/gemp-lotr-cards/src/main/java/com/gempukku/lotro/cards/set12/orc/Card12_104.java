package com.gempukku.lotro.cards.set12.orc;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Reveal the top 5 cards of your draw deck (or, if the fellowship is at a battleground site, the top 7
 * cards) to make an [ORC] minion strength +1 for each [ORC] card revealed.
 */
public class Card12_104 extends AbstractEvent {
    public Card12_104() {
        super(Side.SHADOW, 1, Culture.ORC, "Taunt", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && ((PlayConditions.location(game, Keyword.BATTLEGROUND) && game.getGameState().getDeck(playerId).size() >= 7)
                || (!PlayConditions.location(game, Keyword.BATTLEGROUND) && game.getGameState().getDeck(playerId).size() >= 5));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        int count = PlayConditions.location(game, Keyword.BATTLEGROUND) ? 7 : 5;
        action.appendCost(
                new RevealTopCardsOfDrawDeckEffect(self, playerId, count) {
                    @Override
                    protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                        int bonus = Filters.filter(revealedCards, game.getGameState(), game.getModifiersQuerying(), Culture.ORC).size();
                        action.appendEffect(
                                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, bonus, Culture.ORC, CardType.MINION));
                    }
                });
        return action;
    }
}
