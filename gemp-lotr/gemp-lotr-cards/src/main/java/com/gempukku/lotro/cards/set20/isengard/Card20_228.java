package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;

/**
 * 1
 * My Fighting Uruk-hai
 * Isengard	Event • Maneuver
 * Exert an Uruk-hai to discard a Free Peoples' condition for each battleground in the current region.
 */
public class Card20_228 extends AbstractEvent {
    public Card20_228() {
        super(Side.SHADOW, 1, Culture.ISENGARD, "My Fighting Uruk-hai", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Race.URUK_HAI);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.URUK_HAI));
        int count = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.SITE, Keyword.BATTLEGROUND, Filters.region(GameUtils.getRegion(game.getGameState())));
        if (count > 0)
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, count, count, Side.FREE_PEOPLE, CardType.CONDITION));
        return action;
    }
}
