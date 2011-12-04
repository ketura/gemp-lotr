package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractResponseOldEvent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Response: If a [SAURON] Orc kills a companion, add 1 burden (or 3 if the Ring-bearer wears The One Ring).
 */
public class Card1_273 extends AbstractResponseOldEvent {
    public Card1_273() {
        super(Side.SHADOW, Culture.SAURON, "The Ring's Oppression");
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachKilledBy(game, effectResult, Filters.and(Culture.SAURON, Race.ORC), CardType.COMPANION)
                && checkPlayRequirements(playerId, game, self, 0, 0, false, false)) {
            PlayEventAction action = new PlayEventAction(self);
            int burdens = (game.getGameState().isWearingRing()) ? 3 : 1;
            action.appendEffect(
                    new AddBurdenEffect(self, burdens));
            return Collections.singletonList(action);
        }
        return null;
    }
}
