package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event • Response
 * Game Text: If a burden is added, spot a [SAURON] minion to add an additional burden.
 */
public class Card10_097 extends AbstractResponseEvent {
    public Card10_097() {
        super(Side.SHADOW, 0, Culture.SAURON, "The Ring is Mine!");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.addedBurden(game, effectResult, Filters.any)
                && checkPlayRequirements(playerId, game, self, 0, false)
                && PlayConditions.canSpot(game, Culture.SAURON, CardType.MINION)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new AddBurdenEffect(self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
