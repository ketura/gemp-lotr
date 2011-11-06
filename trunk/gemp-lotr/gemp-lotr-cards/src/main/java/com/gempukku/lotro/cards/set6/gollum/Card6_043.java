package com.gempukku.lotro.cards.set6.gollum;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

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
        if (PlayConditions.winsSkirmish(game, effectResult, Filters.smeagol)
                && checkPlayRequirements(playerId, game, self, 0)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Filters.smeagol));
            action.appendEffect(
                    new RemoveBurdenEffect(self));
            action.appendEffect(
                    new RemoveBurdenEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
