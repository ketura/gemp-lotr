package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.PreventCardEffect;
import com.gempukku.lotro.logic.effects.PreventableCardEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If the Ring-bearer is about to heal, remove a burden instead.
 */
public class Card1_287 extends AbstractResponseEvent {
    public Card1_287() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Extraordinary Resilience");
    }

    @Override
    public List<PlayEventAction> getPlayResponseEventBeforeActions(String playerId, LotroGame game, Effect effect, final PhysicalCard self) {
        if (TriggerConditions.isGettingHealed(effect, game, Filters.ringBearer)) {
            final PlayEventAction action = new PlayEventAction(self);
            action.appendCost(
                    new PreventCardEffect((PreventableCardEffect) effect, Filters.findFirstActive(game, Filters.ringBearer)));
            action.appendEffect(
                    new RemoveBurdenEffect(playerId, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
