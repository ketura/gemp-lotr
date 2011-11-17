package com.gempukku.lotro.cards.set10.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Twilight Cost: 0
 * Type: Site
 * Site: 3K
 * Game Text: Sanctuary. Fellowship: If you cannot spot 2 threats, add a threat to play a fortification from your
 * draw deck.
 */
public class Card10_117 extends AbstractSite {
    public Card10_117() {
        super("Base of Mindolluin", Block.KING, 3, 0, Direction.RIGHT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                && !PlayConditions.canSpotThreat(game, 2)
                && PlayConditions.canAddThreat(game, self, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddThreatsEffect(playerId, self, 1));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Keyword.FORTIFICATION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
