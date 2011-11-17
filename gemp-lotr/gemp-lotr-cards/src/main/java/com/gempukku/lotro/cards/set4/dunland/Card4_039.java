package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Spot 3 [DUNLAND] Men to discard a Free Peoples possession or condition.
 */
public class Card4_039 extends AbstractOldEvent {
    public Card4_039() {
        super(Side.SHADOW, Culture.DUNLAND, "Wild Man Raid", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Culture.DUNLAND, Race.MAN) >= 3;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.side(Side.FREE_PEOPLE), Filters.or(CardType.POSSESSION, CardType.CONDITION)));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
