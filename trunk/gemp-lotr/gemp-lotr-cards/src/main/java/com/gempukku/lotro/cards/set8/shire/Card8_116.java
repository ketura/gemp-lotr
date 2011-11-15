package com.gempukku.lotro.cards.set8.shire;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardOnTopOfDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event • Response
 * Game Text: If the fellowship moves during the regroup phase, exert an unbound Hobbit to place a Shadow card from your
 * discard pile on top of your draw deck.
 */
public class Card8_116 extends AbstractResponseEvent {
    public Card8_116() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "We Shall Meet Again Soon");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_FELLOWSHIP_MOVES
                && checkPlayRequirements(playerId, game, self, 0, false)
                && game.getGameState().getCurrentPhase() == Phase.REGROUP
                && PlayConditions.canExert(self, game, Filters.unboundCompanion, Race.HOBBIT)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.unboundCompanion, Race.HOBBIT));
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardOnTopOfDeckEffect(action, playerId, 1, 1, Side.SHADOW));
            return Collections.singletonList(action);
        }
        return null;
    }
}
