package com.gempukku.lotro.cards.set7.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Twilight Cost: 9
 * Type: Site
 * Site: 9K
 * Game Text: Shadow: Remove a threat and spot your minion to draw a card.
 */
public class Card7_361 extends AbstractSite {
    public Card7_361() {
        super("Haunted Pass", Block.KING, 9, 9, Direction.LEFT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && PlayConditions.canRemoveThreat(game, self, 1)
                && PlayConditions.canSpot(game, Filters.owner(playerId), CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new DrawCardEffect(playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
