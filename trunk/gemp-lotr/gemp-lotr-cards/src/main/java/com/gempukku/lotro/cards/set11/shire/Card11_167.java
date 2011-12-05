package com.gempukku.lotro.cards.set11.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Stealth. If the fellowship is at a dwelling site, exert 2 Hobbits to cancel a skirmish involving a Hobbit.
 * At any other site, exert 2 Hobbits to make a Hobbit strength +3.
 */
public class Card11_167 extends AbstractEvent {
    public Card11_167() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "Incognito", Phase.SKIRMISH);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, 1, 2, Race.HOBBIT);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Race.HOBBIT));
        if (PlayConditions.location(game, Keyword.DWELLING))
            action.appendEffect(
                    new CancelSkirmishEffect(Race.HOBBIT));
        else
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 3, Race.HOBBIT));
        return action;
    }
}
