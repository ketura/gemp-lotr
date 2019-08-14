package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpotThreat(game, 1);
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
