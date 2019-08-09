package com.gempukku.lotro.cards.set40.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: Narrow Escape
 * Set: Second Edition
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event - Skirmish
 * Card Number: 1C85
 * Game Text: Spot Saruman to cancel a skirmish involving Gandalf or spot Gandalf to heal him once for each threat.
 */
public class Card40_085 extends AbstractEvent {
    public Card40_085() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "Narrow Escape", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Filters.or(Filters.gandalf, Filters.saruman));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleEffects = new ArrayList<Effect>(2);
        if (PlayConditions.canSpot(game, Filters.saruman)) {
            possibleEffects.add(
                    new CancelSkirmishEffect(Filters.gandalf) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Cancel skirmish involving Gandalf";
                        }
                    });
        }
        if (PlayConditions.canSpot(game, Filters.gandalf)) {
            possibleEffects.add(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, game.getGameState().getThreats(), Filters.gandalf) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Heal Gandalf once for each threat";
                        }
                    });
        }
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
