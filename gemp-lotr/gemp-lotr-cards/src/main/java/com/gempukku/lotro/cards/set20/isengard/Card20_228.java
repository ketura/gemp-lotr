package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.URUK_HAI);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.URUK_HAI));
        int count = Filters.countActive(game, CardType.SITE, Keyword.BATTLEGROUND, Filters.region(GameUtils.getRegion(game)));
        if (count > 0)
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, count, count, Side.FREE_PEOPLE, CardType.CONDITION));
        return action;
    }
}
