package com.gempukku.lotro.cards.set11.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RevealCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.HashSet;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Spot a [GANDALF] Wizard to reveal your hand and make a minion strength -3 for each companion in your hand
 * (or -4 for each if the fellowship is at a battleground site).
 */
public class Card11_036 extends AbstractEvent {
    public Card11_036() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Inspiration", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.GANDALF, Race.WIZARD);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new RevealCardsFromHandEffect(self, playerId, new HashSet<PhysicalCard>(game.getGameState().getHand(playerId))));
        int companionCount = Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION).size();
        int penalty = companionCount * ((Filters.and(Keyword.BATTLEGROUND).accepts(game.getGameState(), game.getModifiersQuerying(), game.getGameState().getCurrentSite())) ? -4 : -3);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, penalty, CardType.MINION));
        return action;
    }
}
