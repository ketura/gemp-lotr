package com.gempukku.lotro.cards.set7.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
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
 * Game Text: Skirmish: Spot your [SAURON] minion and remove 2 threats to make your [SAURON] minion strength +3.
 */
public class Card7_357 extends AbstractSite {
    public Card7_357() {
        super("Morgul Vale", Block.KING, 8, 8, Direction.RIGHT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSpot(game, Filters.owner(playerId), Culture.SAURON, CardType.MINION)
                && PlayConditions.canRemoveThreat(game, self, 2)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 2));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 3, Filters.owner(playerId), Culture.SAURON, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
