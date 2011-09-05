package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardFromHandEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Exert an Elf to reveal an opponent's hand. That player discards a card from hand for each Orc
 * revealed.
 */
public class Card1_036 extends AbstractEvent {
    public Card1_036() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Curse Their Foul Feet!", Phase.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseActiveCardEffect(playerId, "Choose an Elf", Filters.keyword(Keyword.ELF), Filters.canExert()) {
                    @Override
                    protected void cardSelected(PhysicalCard elf) {
                        action.addCost(new ExertCharacterEffect(elf));
                    }
                });
        action.addEffect(new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                new MultipleChoiceAwaitingDecision(1, "Choose an opponent", GameUtils.getOpponents(game, playerId)) {
                    @Override
                    protected void validDecisionMade(int index, String chosenOpponent) {
                        List<? extends PhysicalCard> hand = game.getGameState().getHand(chosenOpponent);
                        int orcsCount = Filters.filter(hand, game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ORC)).size();
                        for (int i = 0; i < orcsCount; i++)
                            action.addEffect(new ChooseAndDiscardCardFromHandEffect(action, chosenOpponent, false));
                    }
                })
        );
        return action;
    }
}
