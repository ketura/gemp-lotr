package com.gempukku.lotro.cards.set7.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
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
 * Game Text: Shadow: Remove 2 threats to play a Nazgul from your discard pile. His twilight cost is -2.
 */
public class Card7_358 extends AbstractSite {
    public Card7_358() {
        super("Morgulduin", Block.KING, 8, 8, Direction.RIGHT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && PlayConditions.canRemoveThreat(game, self, 2)
                && PlayConditions.canPlayFromDiscard(playerId, game, -2, Race.NAZGUL)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 2));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, -2, Race.NAZGUL));
            return Collections.singletonList(action);
        }
        return null;
    }
}
