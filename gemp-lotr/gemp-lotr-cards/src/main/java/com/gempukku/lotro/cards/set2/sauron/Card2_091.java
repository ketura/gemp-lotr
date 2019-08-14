package com.gempukku.lotro.cards.set2.sauron;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If the Ring-bearer puts on The One Ring, spot a [SAURON] minion to make the Free Peoples player
 * discard his or her hand.
 */
public class Card2_091 extends AbstractResponseEvent {
    public Card2_091() {
        super(Side.SHADOW, 0, Culture.SAURON, "Southern Spies");
    }

    @Override
    public List<PlayEventAction> getOptionalInHandAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.PUT_ON_THE_ONE_RING
                && PlayConditions.canPlayCardDuringPhase(game, (Phase) null, self)
                && Filters.canSpot(game, Culture.SAURON, CardType.MINION)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new DiscardCardsFromHandEffect(self, game.getGameState().getCurrentPlayerId(), new HashSet<PhysicalCard>(game.getGameState().getHand(game.getGameState().getCurrentPlayerId())), true));
            return Collections.singletonList(action);
        }
        return null;
    }
}
