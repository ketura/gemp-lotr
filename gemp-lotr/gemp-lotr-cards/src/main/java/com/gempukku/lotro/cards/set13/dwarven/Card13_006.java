package com.gempukku.lotro.cards.set13.dwarven;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.EffectResult;

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
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Race.DWARF)
                && checkPlayRequirements(playerId, game, self, 0, 0, false, false)
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
