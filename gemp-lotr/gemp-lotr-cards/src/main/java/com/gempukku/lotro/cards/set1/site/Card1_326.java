package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Type: Site
 * Site: 1
 * Game Text: Fellowship: Exert a Hobbit to play a companion or ally; that character's twilight cost is -1.
 */
public class Card1_326 extends AbstractSite {
    public Card1_326() {
        super("Westfarthing", 1, 0, Direction.LEFT);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.HOBBIT), Filters.canExert())) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Exert a Hobbit to play a companion or ally; that character's twilight cost is -1.");
            action.addCost(
                    new ChooseAndExertCharacterEffect(action, playerId, "Choose a Hobbit", true, Filters.race(Race.HOBBIT), Filters.canExert()));
            action.addEffect(
                    new ChooseCardsFromHandEffect(playerId, "Choose companion or ally to play", 1, 1, Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY)),
                            new Filter() {
                                @Override
                                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                    return physicalCard.getBlueprint().checkPlayRequirements(playerId, game, physicalCard, -1);
                                }
                            }) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            PhysicalCard selectedCard = selectedCards.get(0);
                            game.getActionsEnvironment().addActionToStack(selectedCard.getBlueprint().getPlayCardAction(playerId, game, selectedCard, -1));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
