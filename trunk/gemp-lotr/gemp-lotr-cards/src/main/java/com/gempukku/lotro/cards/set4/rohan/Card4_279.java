package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.LiberateASiteEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Regroup: Spot 2 mounted [ROHAN] Men to liberate a site or draw 3 cards.
 */
public class Card4_279 extends AbstractEvent {
    public Card4_279() {
        super(Side.FREE_PEOPLE, Culture.ROHAN, "Helm! Helm!", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canSpot(game, 2, Filters.hasAttached(Filters.possessionClass(PossessionClass.MOUNT)), Filters.culture(Culture.ROHAN), Filters.race(Race.MAN));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);

        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new LiberateASiteEffect(self));
        possibleEffects.add(
                new DrawCardEffect(playerId, 3));

        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }
}
