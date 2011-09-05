package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
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
    public List<? extends Action> getInPlayPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.HOBBIT), Filters.canExert())) {
            final CostToEffectAction action = new CostToEffectAction(self, Keyword.FELLOWSHIP, "Exert a Hobbit to play a companion or ally; that character's twilight cost is -1.");
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose a Hobbit", Filters.keyword(Keyword.HOBBIT), Filters.canExert()) {
                        @Override
                        protected void cardSelected(PhysicalCard hobbit) {
                            action.addCost(new ExertCharacterEffect(hobbit));
                        }
                    }
            );
            action.addEffect(
                    new ChooseCardsFromHandEffect(playerId, "Choose companion or ally to play", 1, 1, Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY)),
                            new Filter() {
                                @Override
                                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                    List<? extends Action> playableActions = physicalCard.getBlueprint().getInPlayPhaseActions(playerId, game, physicalCard);
                                    return (playableActions != null && playableActions.size() > 0);
                                }
                            }) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            LotroCardBlueprint blueprint = selectedCards.get(0).getBlueprint();
                            if (!blueprint.isUnique() || !Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name(blueprint.getName()))) {
                                Zone zone = (blueprint.getCardType() == CardType.COMPANION) ? Zone.FREE_CHARACTERS : Zone.FREE_SUPPORT;
                                game.getActionsEnvironment().addActionToStack(new PlayPermanentAction(selectedCards.get(0), zone, -1));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
