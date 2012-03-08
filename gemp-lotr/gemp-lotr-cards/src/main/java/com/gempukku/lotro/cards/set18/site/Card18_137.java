package com.gempukku.lotro.cards.set18.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Twilight Cost: 1
 * Type: Site
 * Game Text: Battleground. Plains. When the fellowship moves to this site, each player may reinforce a culture token.
 */
public class Card18_137 extends AbstractNewSite {
    public Card18_137() {
        super("Morannon Plains", 1, Direction.RIGHT);
        addKeyword(Keyword.BATTLEGROUND);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            final PlayOrder counterClockwisePlayOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(game.getGameState().getCurrentPlayerId(), false);
            String playerId;
            while ((playerId = counterClockwisePlayOrder.getNextPlayer()) != null) {
                action.appendEffect(
                        new OptionalEffect(
                                action, playerId,
                                new ReinforceTokenEffect(self, playerId, null)));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
