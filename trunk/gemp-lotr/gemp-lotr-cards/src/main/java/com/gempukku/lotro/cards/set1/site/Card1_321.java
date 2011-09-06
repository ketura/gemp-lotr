package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
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
 * Game Text: Fellowship: Exert a Hobbit to play Merry or Pippin from your draw deck.
 */
public class Card1_321 extends AbstractSite {
    public Card1_321() {
        super("Farmer Maggot's Fields", 1, 0, Direction.LEFT);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.HOBBIT), Filters.canExert())) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Exert a Hobbit to play Merry or Pippin from your draw deck.");
            action.addCost(
                    new ChooseAndExertCharacterEffect(action, playerId, "Choose a Hobbit", true, Filters.keyword(Keyword.HOBBIT), Filters.canExert()));

            action.addEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.or(Filters.name("Merry"), Filters.name("Pippin"))));

            return Collections.singletonList(action);
        }
        return null;
    }

}
