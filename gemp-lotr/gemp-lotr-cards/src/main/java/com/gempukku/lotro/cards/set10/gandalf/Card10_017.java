package com.gempukku.lotro.cards.set10.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.owner(self.getOwner()), Race.WIZARD);
    }

    @Override
    public List<PlayEventAction> getOptionalInHandAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)
                && game.getGameState().getCurrentPhase() == Phase.REGROUP) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.owner(playerId), Race.WIZARD));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self.getOwner(), self, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
