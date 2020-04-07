package com.gempukku.lotro.cards.set7.elven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.ShuffleDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make an Elf strength +2 and you may shuffle your draw deck.
 */
public class Card7_030 extends AbstractEvent {
    public Card7_030() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Uncertain Paths", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Race.ELF));
        action.appendEffect(
                new PlayoutDecisionEffect(playerId,
                        new MultipleChoiceAwaitingDecision(1, "Do you want to shuffle your draw deck?", new String[]{"Yes", "No"}) {
                            @Override
                            protected void validDecisionMade(int index, String result) {
                                if (index == 0) {
                                    action.insertEffect(
                                            new ShuffleDeckEffect(playerId));
                                }
                            }
                        }));
        return action;
    }
}