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
 * ❻ •Mûmak Commander, Scourge of the Sands [Fal]
 * Minion • Man
 * Strength: 11   Vitality: 3   Roaming: 4
 * Southron.
 * Regroup: Remove ❸ to make the Free Peoples player wound a companion (or 2 companions if you can spot 6 companions)
 * <p/>
 * http://lotrtcg.org/coreset/fallenrealms/mumakcommandersots(r3).jpg
 */
public class Card20_131 extends AbstractMinion {
    public Card20_131() {
        super(6, 11, 3, 4, Race.MAN, Culture.FALLEN_REALMS, "Mumak Commander", "Scourge of the Sands", true);
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
                    new ChooseAndWoundCharactersEffect(action, game.getGameState().getCurrentPlayerId(), count, count, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
