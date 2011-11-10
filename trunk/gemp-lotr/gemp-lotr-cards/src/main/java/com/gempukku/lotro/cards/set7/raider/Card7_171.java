package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 3
 * Type: Event • Regroup
 * Game Text: Spot a threat and X [RAIDER] Men to make the Free Peoples player exert X companions (limit 3).
 */
public class Card7_171 extends AbstractEvent {
    public Card7_171() {
        super(Side.SHADOW, 3, Culture.RAIDER, "Thrice Outnumbered", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpotThreat(game, 1);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ForEachYouSpotEffect(playerId, Culture.RAIDER, Race.MAN) {
                    @Override
                    protected void spottedCards(int spotCount) {
                        spotCount = Math.min(3, spotCount);
                        if (spotCount > 0) {
                            action.appendEffect(
                                    new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), spotCount, spotCount, CardType.COMPANION));
                        }
                    }
                });
        return action;
    }
}
