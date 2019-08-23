package com.gempukku.lotro.cards.set11.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.RevealHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.GANDALF, Race.WIZARD);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new RevealHandEffect(self, playerId, playerId) {
                    @Override
                    protected void cardsRevealed(Collection<? extends PhysicalCard> cards) {
                        int companionCount = Filters.filter(cards, game, CardType.COMPANION).size();
                        int penalty = companionCount * (PlayConditions.location(game, Keyword.BATTLEGROUND) ? -4 : -3);
                        action.appendEffect(
                                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, penalty, CardType.MINION));
                    }
                });
        return action;
    }
}
