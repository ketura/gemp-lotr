package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromHandEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
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
        super(Side.FREE_PEOPLE, CardType.CONDITION, Culture.ELVEN, "Far-seeing Eyes", true);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return PlayConditions.checkUniqueness(game.getGameState(), game.getModifiersQuerying(), self);
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.FREE_SUPPORT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && checkPlayRequirements(playerId, game, self)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));
        }
        return null;
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.keyword(Keyword.ELF))) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Choose an opponent to discard a card from hand");
            action.addCost(
                    new PlayoutDecisionEffect(game.getUserFeedback(), self.getOwner(),
                            new MultipleChoiceAwaitingDecision(1, "Choose opponent", GameUtils.getOpponents(game, self.getOwner())) {
                                @Override
                                protected void validDecisionMade(int index, final String opponentId) {
                                    action.addEffect(
                                            new ChooseCardsFromHandEffect(opponentId, "Choose card to discard", 1, 1, Filters.any()) {
                                                @Override
                                                protected void cardsSelected(List<PhysicalCard> selectedCards) {
                                                    action.addEffect(new DiscardCardFromHandEffect(selectedCards.get(0)));
                                                }
                                            });
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
