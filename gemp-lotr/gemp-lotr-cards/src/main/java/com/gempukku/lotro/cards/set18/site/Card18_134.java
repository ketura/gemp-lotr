package com.gempukku.lotro.cards.set18.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Twilight Cost: 1
 * Type: Site
 * Game Text: Mountain. When the fellowship moves to this site, each player wounds two of his characters.
 */
public class Card18_134 extends AbstractNewSite {
    public Card18_134() {
        super("Doorway to Doom", 1, Direction.RIGHT);
        addKeyword(Keyword.MOUNTAIN);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            final PlayOrder counterClockwisePlayOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(game.getGameState().getCurrentPlayerId(), false);
            String playerId;
            while ((playerId = counterClockwisePlayOrder.getNextPlayer()) != null) {
                action.appendEffect(
                        new ChooseAndWoundCharactersEffect(action, playerId, 2, 2, Filters.owner(playerId), Filters.character));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
