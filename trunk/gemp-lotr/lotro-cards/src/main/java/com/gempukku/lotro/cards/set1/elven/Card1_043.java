package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentFromHandAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseAnyCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromHandEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each time you play an Elf, choose an opponent to discard a card from hand.
 */
public class Card1_043 extends AbstractLotroCardBlueprint {
    public Card1_043() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, Culture.ELVEN, "Far-seeing Eyes", "1_43", true);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFromHandDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && !Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Far-seeing Eyes"))) {
            PlayPermanentFromHandAction action = new PlayPermanentFromHandAction(self, Zone.FREE_SUPPORT);
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getRequiredWhenActions(final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.keyword(Keyword.ELF))) {
            final CostToEffectAction action = new CostToEffectAction(self, "Choose an opponent to discard a card from hand");
            action.addCost(
                    new PlayoutDecisionEffect(game.getUserFeedback(), self.getOwner(),
                            new MultipleChoiceAwaitingDecision(1, "Choose opponent", getOpponents(game.getGameState().getPlayerOrder().getAllPlayers(), self.getOwner())) {
                                @Override
                                protected void validDecisionMade(final String opponentId) {
                                    action.addEffect(
                                            new ChooseAnyCardEffect(opponentId, "Choose card to discard", Filters.zone(Zone.HAND), Filters.owner(opponentId)) {
                                                @Override
                                                protected void cardSelected(PhysicalCard card) {
                                                    action.addEffect(new DiscardCardFromHandEffect(card));
                                                }
                                            });
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }

    private String[] getOpponents(List<String> players, String self) {
        List<String> shadowPlayers = new LinkedList<String>(players);
        shadowPlayers.remove(self);
        return shadowPlayers.toArray(new String[shadowPlayers.size()]);
    }
}
