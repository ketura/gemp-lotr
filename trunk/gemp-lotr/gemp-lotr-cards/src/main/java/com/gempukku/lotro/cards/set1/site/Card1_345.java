package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 3
 * Type: Site
 * Site: 4
 * Game Text: Underground. Shadow: Remove (1) to play a Shadow weapon from your discard pile.
 */
public class Card1_345 extends AbstractSite {
    public Card1_345() {
        super("Mithril Mine", 4, 3, Direction.RIGHT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.SHADOW, self)
                && game.getGameState().getTwilightPool() >= 1) {
            CostToEffectAction action = new CostToEffectAction(self, Keyword.SHADOW, "Remove (1) to play a Shadow weapon from your discard pile.");
            action.addCost(new RemoveTwilightEffect(1));
            action.addEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose Shadow weapon to play", Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(),
                            Filters.or(
                                    Filters.keyword(Keyword.HAND_WEAPON),
                                    Filters.keyword(Keyword.RANGED_WEAPON)),
                            new Filter() {
                                @Override
                                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                    List<? extends Action> playableActions = physicalCard.getBlueprint().getPlayablePhaseActions(playerId, game, physicalCard);
                                    return (playableActions != null && playableActions.size() > 0);
                                }
                            }), 1, 1) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            PhysicalCard selectedCard = selectedCards.get(0);
                            game.getActionsEnvironment().addActionToStack(selectedCard.getBlueprint().getPlayablePhaseActions(playerId, game, selectedCard).get(0));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
