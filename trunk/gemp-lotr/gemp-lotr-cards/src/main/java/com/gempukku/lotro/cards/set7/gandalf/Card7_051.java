package com.gempukku.lotro.cards.set7.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ForEachTwilightTokenYouSpotEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Exert Gandalf to make him strength +1 for each twilight token you spot (limit +8).
 */
public class Card7_051 extends AbstractEvent {
    public Card7_051() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "Undaunted", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, final int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gandalf));
        action.appendEffect(
                new ForEachTwilightTokenYouSpotEffect(playerId) {
                    @Override
                    protected void twilightTokensSpotted(int twilightTokens) {
                        twilightTokens = Math.min(8, twilightTokens);
                        action.insertEffect(
                                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, twilightTokens, Filters.gandalf));
                    }
                });
        return action;
    }
}
