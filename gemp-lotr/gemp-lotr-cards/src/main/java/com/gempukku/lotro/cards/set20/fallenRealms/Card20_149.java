package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

/**
 * 3
 * Whirling Strike
 * Fallen Realms	Event • Skirmish
 * Wound a companion skirmishing a Southron.
 */
public class Card20_149 extends AbstractEvent{
    public Card20_149() {
        super(Side.SHADOW, 3, Culture.FALLEN_REALMS, "Whirling Strike", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.inSkirmishAgainst(Keyword.SOUTHRON)));
        return action;
    }
}
