package com.gempukku.lotro.cards.set6.rohan;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.MoveLimitModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Fellowship: Spot 3 mounted [ROHAN] Men to make the move limit for this turn +2. Each Shadow player may
 * draw 6 cards.
 */
public class Card6_096 extends AbstractEvent {
    public Card6_096() {
        super(Side.FREE_PEOPLE, 2, Culture.ROHAN, "News From the Mark", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 3, Culture.ROHAN, Race.MAN, Filters.mounted);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilEndOfTurnModifierEffect(
                        new MoveLimitModifier(self, 2)));
        for (final String opponentId : GameUtils.getShadowPlayers(game)) {
            action.appendEffect(
                    new PlayoutDecisionEffect(opponentId,
                            new MultipleChoiceAwaitingDecision(1, "Do you want to draw 6 cards?", new String[]{"Yes", "No"}) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    if (index == 0)
                                        action.insertEffect(
                                                new DrawCardsEffect(action, opponentId, 6));
                                }
                            }));
        }
        return action;
    }
}
