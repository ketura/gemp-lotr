package com.gempukku.lotro.cards.set6.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDeckIntoHandOrDiscardEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Spot an Elf to reveal the top card of your draw deck. You may discard it, return it to
 * the top of your draw deck, or take it into hand.
 */
public class Card6_019 extends AbstractEvent {
    public Card6_019() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Gift of Foresight", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                    @Override
                    protected void cardsRevealed(List<PhysicalCard> cards) {
                        for (final PhysicalCard card : cards) {
                            action.appendEffect(
                                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                                            new MultipleChoiceAwaitingDecision(1, "Choose what do you want to do with " + GameUtils.getCardLink(card), new String[]{"Discard it", "Return to top of deck", "Take into hand"}) {
                                                @Override
                                                protected void validDecisionMade(int index, String result) {
                                                    if (index == 0)
                                                        action.appendEffect(
                                                                new DiscardCardFromDeckEffect(card));
                                                    else if (index == 2)
                                                        action.appendEffect(
                                                                new PutCardFromDeckIntoHandOrDiscardEffect(card));
                                                }
                                            }));
                        }
                    }
                });
        return action;
    }
}
