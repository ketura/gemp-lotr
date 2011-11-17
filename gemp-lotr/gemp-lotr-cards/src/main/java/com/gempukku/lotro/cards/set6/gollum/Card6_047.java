package com.gempukku.lotro.cards.set6.gollum;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If Gollum wins a skirmish, wound a companion (except the Ring-bearer).
 */
public class Card6_047 extends AbstractResponseEvent {
    public Card6_047() {
        super(Side.SHADOW, 0, Culture.GOLLUM, "You're a Lier and a Thief");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.gollum)
                && checkPlayRequirements(playerId, game, self, 0, false)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Keyword.RING_BEARER)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
