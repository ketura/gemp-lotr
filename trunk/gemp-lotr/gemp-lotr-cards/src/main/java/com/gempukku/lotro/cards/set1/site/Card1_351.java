package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.cards.effects.ChooseOpponentEffect;
import com.gempukku.lotro.cards.effects.RevealAndChooseCardsFromOpponentHandEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 3
 * Type: Site
 * Site: 6
 * Game Text: Sanctuary. Fellowship: Exert an Elf to look at an opponent's hand.
 */
public class Card1_351 extends AbstractSite {
    public Card1_351() {
        super("Galadriel's Glade", 6, 3, Direction.LEFT);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF), Filters.canExert())) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Exert an Elf to look at an opponent's hand.");
            action.addCost(
                    new ChooseAndExertCharacterEffect(action, playerId, "Choose an Elf", true, Filters.race(Race.ELF), Filters.canExert()));

            action.addEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(String opponentId) {
                            action.addEffect(
                                    new RevealAndChooseCardsFromOpponentHandEffect(playerId, opponentId, "Opponent's hand", Filters.none(), 0, 0) {
                                        @Override
                                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                                            // Do nothing, it's just to look at hand
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
