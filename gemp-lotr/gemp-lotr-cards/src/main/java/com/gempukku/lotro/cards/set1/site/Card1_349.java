package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 6
 * Type: Site
 * Site: 5
 * Game Text: Underground. Shadow: Play The Balrog from your draw deck or hand; The Balrog's twilight cost is -6.
 */
public class Card1_349 extends AbstractSite {
    public Card1_349() {
        super("The Bridge of Khazad-dum", 5, 6, Direction.LEFT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.SHADOW, self)) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.SHADOW, "Play The Balrog from your draw deck or hand; The Balrog's twilight cost is -6.");

            List<Effect> possibleEffects = new LinkedList<Effect>();
            // Play from hand
            possibleEffects.add(
                    new ChooseCardsFromHandEffect(playerId, "Choose Balrog to play", 1, 1, Filters.name("Balrog"),
                            new Filter() {
                                @Override
                                public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                    return
                                            !Filters.canSpot(gameState, modifiersQuerying, Filters.name("Balrog"))
                                                    && gameState.getTwilightPool() >= (modifiersQuerying.getTwilightCost(gameState, physicalCard) - 6);
                                }
                            }) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            PhysicalCard balrog = selectedCards.get(0);
                            game.getActionsEnvironment().addActionToStack(
                                    new PlayPermanentAction(balrog, Zone.SHADOW_CHARACTERS, -6));
                        }
                    });

            // Play from deck
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.name("Balrog"), -6));

            action.addEffect(
                    new ChoiceEffect(action, playerId, possibleEffects, false));

            return Collections.singletonList(action);
        }
        return null;
    }
}
