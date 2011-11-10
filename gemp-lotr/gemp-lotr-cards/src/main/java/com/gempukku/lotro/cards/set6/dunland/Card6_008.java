package com.gempukku.lotro.cards.set6.dunland;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

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
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game, effectResult, Culture.DUNLAND, Race.MAN)
                && checkPlayRequirements(playerId, game, self, 0, false)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
