package com.gempukku.lotro.cards.set10.gandalf;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event • Response
 * Game Text: If the fellowship moves during the regroup phase, exert your Wizard to discard each minion.
 */
public class Card10_017 extends AbstractResponseEvent {
    public Card10_017() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Out of the High Airs");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, Filters.owner(playerId), Race.WIZARD);
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_FELLOWSHIP_MOVES
                && checkPlayRequirements(playerId, game, self, 0, false)
                && game.getGameState().getCurrentPhase() == Phase.REGROUP) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.owner(playerId), Race.WIZARD));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
