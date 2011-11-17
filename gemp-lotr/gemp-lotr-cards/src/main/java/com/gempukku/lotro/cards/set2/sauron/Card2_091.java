package com.gempukku.lotro.cards.set2.sauron;

import com.gempukku.lotro.cards.AbstractResponseOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

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
public class Card2_091 extends AbstractResponseOldEvent {
    public Card2_091() {
        super(Side.SHADOW, Culture.SAURON, "Southern Spies");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.PUT_ON_THE_ONE_RING
                && PlayConditions.canPlayCardDuringPhase(game, (Phase) null, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), CardType.MINION)
                && checkPlayRequirements(playerId, game, self, 0, false)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new DiscardCardsFromHandEffect(self, game.getGameState().getCurrentPlayerId(), new HashSet<PhysicalCard>(game.getGameState().getHand(game.getGameState().getCurrentPlayerId())), true));
            return Collections.singletonList(action);
        }
        return null;
    }
}
