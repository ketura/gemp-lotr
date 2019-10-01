package com.gempukku.lotro.cards.set2.isengard;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Response: If the Ring-bearer puts on The One Ring, spot an [ISENGARD] minion to make the Free Peoples
 * player discard the top 5 cards of his or her draw deck.
 */
public class Card2_045 extends AbstractResponseEvent {
    public Card2_045() {
        super(Side.SHADOW, 1, Culture.ISENGARD, "Too Much Attention");
    }

    @Override
    public List<PlayEventAction> getPlayResponseEventAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.PUT_ON_THE_ONE_RING
                && Filters.canSpot(game, Culture.ISENGARD, CardType.MINION)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new DiscardTopCardFromDeckEffect(self, game.getGameState().getCurrentPlayerId(), 5, true));

            return Collections.singletonList(action);
        }
        return null;
    }
}
