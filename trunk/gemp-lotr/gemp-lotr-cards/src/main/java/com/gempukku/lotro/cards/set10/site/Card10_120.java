package com.gempukku.lotro.cards.set10.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Twilight Cost: 8
 * Type: Site
 * Site: 8K
 * Game Text: Skirmish: Spot your [WRAITH] Orc and remove 2 threats to make that [WRAITH] Orc strength +3.
 */
public class Card10_120 extends AbstractSite {
    public Card10_120() {
        super("Watchers of Cirith Ungol", Block.KING, 8, 8, Direction.RIGHT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSpot(game, Filters.owner(playerId), Culture.WRAITH, Race.ORC)
                && PlayConditions.canRemoveThreat(game, self, 2)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 2));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 3, Filters.owner(playerId), Culture.WRAITH, Race.ORC));
            return Collections.singletonList(action);
        }
        return null;
    }
}
