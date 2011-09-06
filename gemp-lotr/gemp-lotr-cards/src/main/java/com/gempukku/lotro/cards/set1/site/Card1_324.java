package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Type: Site
 * Site: 1
 * Game Text: Fellowship: Add a burden to play Aragorn from your draw deck.
 */
public class Card1_324 extends AbstractSite {
    public Card1_324() {
        super("The Prancing Pony", 1, 0, Direction.LEFT);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Add a burder to play Aragorn from your draw deck.");
            action.addCost(new AddBurdenEffect(playerId));

            action.addEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.name("Aragorn")));

            return Collections.singletonList(action);
        }
        return null;
    }
}
