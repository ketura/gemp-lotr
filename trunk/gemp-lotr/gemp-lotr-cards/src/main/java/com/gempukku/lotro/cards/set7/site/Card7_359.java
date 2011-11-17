package com.gempukku.lotro.cards.set7.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Twilight Cost: 8
 * Type: Site
 * Site: 8K
 * Game Text: Shadow: Remove 2 threats and play Gollum from your discard pile to add 2 burdens.
 */
public class Card7_359 extends AbstractSite {
    public Card7_359() {
        super("Northern Ithilien", Block.KING, 8, 8, Direction.RIGHT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && PlayConditions.canRemoveThreat(game, self, 2)
                && PlayConditions.canPlayFromDiscard(playerId, game, Filters.gollum)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 2));
            action.appendCost(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.gollum));
            action.appendEffect(
                    new AddBurdenEffect(self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
