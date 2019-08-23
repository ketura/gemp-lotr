package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.PlayNextSiteEffect;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 0
 * Well-Traveled
 * Gondor	Event • Fellowship or Regroup
 * To play, exert a [Gondor] ranger.
 * If the fellowship is in region 1, play the fellowship's next 2 sites. Otherwise, play the fellowship's next site.
 */
public class Card20_208 extends AbstractEvent {
    public Card20_208() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Well-Traveled", Phase.FELLOWSHIP, Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.GONDOR, Keyword.RANGER);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GONDOR, Keyword.RANGER));
        int region = GameUtils.getRegion(game);
        if (region == 1) {
            action.appendEffect(
                    new PlaySiteEffect(action, playerId, null, game.getGameState().getCurrentSiteNumber() + 1));
            action.appendEffect(
                    new PlaySiteEffect(action, playerId, null, game.getGameState().getCurrentSiteNumber() + 2));
        } else
            action.appendEffect(
                    new PlayNextSiteEffect(action, playerId));

        return action;
    }
}
