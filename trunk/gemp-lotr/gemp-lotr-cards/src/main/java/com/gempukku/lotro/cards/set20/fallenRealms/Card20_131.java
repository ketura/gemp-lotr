package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 6
 * •Mumak Commander
 * Fallen Realms	Minion • Man
 * 11	4	4
 * Southron.
 * Regroup: Remove (3) to make the free people's player wound an unbound companion (or 2 companions if you can spot 6 companions).
 */
public class Card20_131 extends AbstractMinion {
    public Card20_131() {
        super(6, 11, 4, 4, Race.MAN, Culture.FALLEN_REALMS, "Mumak Commander", null, true);
        addKeyword(Keyword.SOUTHRON);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 3)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            int count = (Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION) >= 6) ? 2 : 1;
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, game.getGameState().getCurrentPlayerId(), count, count, Filters.unboundCompanion));
            return Collections.singletonList(action);
        }
        return null;
    }
}
