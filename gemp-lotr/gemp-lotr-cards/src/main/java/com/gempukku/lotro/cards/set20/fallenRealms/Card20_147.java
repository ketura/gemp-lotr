package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 12
 * Waylayed
 * Fallen Realms	Event • Regroup
 * Exert a Southron to wound an unbound companion 4 times.
 */
public class Card20_147 extends AbstractEvent {
    public Card20_147() {
        super(Side.SHADOW, 12, Culture.FALLEN_REALMS, "Waylayed", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Keyword.SOUTHRON);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Keyword.SOUTHRON));
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, 4, Filters.unboundCompanion));
        return action;
    }
}
