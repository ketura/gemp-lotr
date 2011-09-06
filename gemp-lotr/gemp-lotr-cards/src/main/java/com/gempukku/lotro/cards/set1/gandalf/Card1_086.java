package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Spell. Fellowship: Spot Gandalf to reveal an opponent's hand.
 */
public class Card1_086 extends AbstractEvent {
    public Card1_086() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Treachery Deeper Than You Know", Phase.FELLOWSHIP);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                        new MultipleChoiceAwaitingDecision(1, "Choose an opponent", GameUtils.getOpponents(game, playerId)) {
                            @Override
                            protected void validDecisionMade(int index, String result) {
                                // TODO - Reveal
                            }
                        })
        );
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
