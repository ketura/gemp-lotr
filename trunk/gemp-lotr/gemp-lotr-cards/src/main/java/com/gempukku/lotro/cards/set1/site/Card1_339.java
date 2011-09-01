package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
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
import com.gempukku.lotro.logic.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 0
 * Type: Site
 * Site: 3
 * Game Text: Sanctuary. Fellowship: Play a Hobbit to draw a card.
 */
public class Card1_339 extends AbstractSite {
    public Card1_339() {
        super("Frodo's Bedroom", 3, 0, Direction.RIGHT);
        addKeyword(Keyword.SANCTUARY);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)) {
            CostToEffectAction action = new CostToEffectAction(self, Keyword.FELLOWSHIP, "Play a Hobbit to draw a card");
            action.addCost(
                    new ChooseCardsFromHandEffect(playerId, "Choose a Hobbit to play", 1, 1, Filters.keyword(Keyword.HOBBIT),
                            new Filter() {
                                @Override
                                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                    List<? extends Action> playableActions = physicalCard.getBlueprint().getPhaseActions(playerId, game, physicalCard);
                                    return (playableActions != null && playableActions.size() > 0);
                                }
                            }) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            LotroCardBlueprint blueprint = selectedCards.get(0).getBlueprint();
                            if (!blueprint.isUnique() || !Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name(blueprint.getName()))) {
                                Zone zone = (blueprint.getCardType() == CardType.COMPANION) ? Zone.FREE_CHARACTERS : Zone.FREE_SUPPORT;
                                game.getActionsEnvironment().addActionToStack(new PlayPermanentAction(selectedCards.get(0), zone));
                            }
                        }
                    });
            action.addEffect(new DrawCardEffect(playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
