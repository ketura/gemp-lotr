package com.gempukku.lotro.cards.set11.dwarven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.PreventableCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event • Response
 * Game Text: If a Dwarf who has resistance 3 or more is about to take a wound, prevent that.
 */
public class Card11_013 extends AbstractResponseEvent {
    public Card11_013() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "On Guard");
    }

    @Override
    public List<PlayEventAction> getPlayResponseEventBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Race.DWARF, Filters.minResistance(3))
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            final PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (PreventableCardEffect) effect, playerId, "Choose a Dwarf", Race.DWARF, Filters.minResistance(3)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
