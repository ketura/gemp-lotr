package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardCardAtRandomFromHandEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Tale. Bearer must be an Elf companion. Archery: If bearer is an archer, exert bearer to make an opponent
 * discard 2 cards at random from hand.
 */
public class Card1_068 extends AbstractAttachable {
    public Card1_068() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.ELVEN, "The White Arrows of Lorien", "1_68");
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        Filter validTargetFilter = Filters.and(Filters.keyword(Keyword.ELF), Filters.type(CardType.COMPANION));

        appendAttachCardAction(actions, game, self, validTargetFilter);

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.ARCHERY, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())
                && game.getModifiersQuerying().hasKeyword(game.getGameState(), self.getAttachedTo(), Keyword.ARCHER)) {
            final CostToEffectAction action = new CostToEffectAction(self, "Exert bearer to make an opponent discard 2 cards at random from hand");
            action.addCost(new ExertCharacterEffect(self.getAttachedTo()));
            action.addEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new MultipleChoiceAwaitingDecision(1, "Choose an opponent to discard 2 cards at random from hand", GameUtils.getOpponents(game, playerId)) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    action.addEffect(new DiscardCardAtRandomFromHandEffect(result));
                                    action.addEffect(new DiscardCardAtRandomFromHandEffect(result));
                                }
                            })
            );

            actions.add(action);
        }

        return actions;
    }
}
