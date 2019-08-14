package com.gempukku.lotro.cards.set13.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.OptionalEffect;
import com.gempukku.lotro.logic.effects.ReinforceTokenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event • Response
 * Game Text: If a Dwarf wins a fierce skirmish, play a [DWARVEN] possession from your discard pile and you may
 * reinforce a [DWARVEN] token.
 */
public class Card13_006 extends AbstractResponseEvent {
    public Card13_006() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Honoring His Kinfolk");
    }

    @Override
    public List<PlayEventAction> getOptionalInHandAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Race.DWARF)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)
                && game.getGameState().isFierceSkirmishes()
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.DWARVEN, CardType.POSSESSION)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.DWARVEN, CardType.POSSESSION));
            action.appendEffect(
                    new OptionalEffect(action, playerId,
                            new ReinforceTokenEffect(self, playerId, Token.DWARVEN)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
