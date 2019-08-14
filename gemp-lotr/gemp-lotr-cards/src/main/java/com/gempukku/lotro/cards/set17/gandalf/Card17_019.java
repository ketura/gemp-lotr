package com.gempukku.lotro.cards.set17.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a [GANDALF] companion strength +1 for each [GANDALF] Man you can spot.
 */
public class Card17_019 extends AbstractEvent {
    public Card17_019() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Guidance of the Istari", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, new CountActiveEvaluator(Culture.GANDALF, Race.MAN), Culture.GANDALF, CardType.COMPANION));
        return action;
    }
}
