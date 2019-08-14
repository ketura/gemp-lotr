package com.gempukku.lotro.cards.set6.gollum;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If Smeagol wins a skirmish, heal him and remove 2 burdens.
 */
public class Card6_043 extends AbstractResponseEvent {
    public Card6_043() {
        super(Side.FREE_PEOPLE, 0, Culture.GOLLUM, "Not Listening");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.smeagol)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Filters.smeagol));
            action.appendEffect(
                    new RemoveBurdenEffect(playerId, self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
