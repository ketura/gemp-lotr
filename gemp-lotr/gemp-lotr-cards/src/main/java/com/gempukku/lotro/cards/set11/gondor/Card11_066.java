package com.gempukku.lotro.cards.set11.gondor;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event • Fellowship or Regroup
 * Game Text: To play, exert a ranger. If the fellowship is in region 1, play the fellowship's next 2 sites. Otherwise,
 * play the fellowship's next site.
 */
public class Card11_066 extends AbstractEvent {
    public Card11_066() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Well-traveled", Phase.FELLOWSHIP, Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Keyword.RANGER);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self, true);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Keyword.RANGER));
        action.appendEffect(
                new PlaySiteEffect(action, playerId, null, game.getGameState().getCurrentSiteNumber() + 1));
        if (PlayConditions.location(game, Filters.region(1)))
            action.appendEffect(
                    new PlaySiteEffect(action, playerId, null, game.getGameState().getCurrentSiteNumber() + 2));
        return action;
    }
}
