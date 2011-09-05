package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
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
    public List<? extends Action> getInPlayPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF), Filters.canExert())) {
            final CostToEffectAction action = new CostToEffectAction(self, Keyword.FELLOWSHIP, "Exert an Elf to look at an opponent's hand.");
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose an Elf", Filters.keyword(Keyword.ELF), Filters.canExert()) {
                        @Override
                        protected void cardSelected(PhysicalCard elf) {
                            action.addCost(new ExertCharacterEffect(elf));
                        }
                    });

            action.addEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new MultipleChoiceAwaitingDecision(1, "Choose an opponent", GameUtils.getOpponents(game, playerId)) {
                                @Override
                                protected void validDecisionMade(int index, String opponentId) {
                                    action.addEffect(
                                            new ChooseArbitraryCardsEffect(playerId, "Opponent's hand", new LinkedList<PhysicalCard>(game.getGameState().getHand(opponentId)), 0, 0) {
                                                @Override
                                                protected void cardsSelected(List<PhysicalCard> selectedCards) {

                                                }
                                            });
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
