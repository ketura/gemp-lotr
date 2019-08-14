package com.gempukku.lotro.cards.set6.dunland;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Response: If a [DUNLAND] Man wins a skirmish, discard all conditions.
 */
public class Card6_008 extends AbstractResponseEvent {
    public Card6_008() {
        super(Side.SHADOW, 2, Culture.DUNLAND, "Too Long Have These Peasants Stood");
    }

    @Override
    public List<PlayEventAction> getOptionalInHandAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Culture.DUNLAND, Race.MAN)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self.getOwner(), self, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
