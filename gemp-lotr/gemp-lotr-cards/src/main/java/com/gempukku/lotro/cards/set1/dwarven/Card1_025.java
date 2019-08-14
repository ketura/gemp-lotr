package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a Dwarf wins a skirmish, heal that Dwarf.
 */
public class Card1_025 extends AbstractResponseEvent {
    public Card1_025() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Still Draws Breath");
    }

    @Override
    public List<PlayEventAction> getOptionalInHandAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (self.getZone() == Zone.HAND && TriggerConditions.winsSkirmish(game, effectResult, Race.DWARF)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            CharacterWonSkirmishResult skirmishResult = (CharacterWonSkirmishResult) effectResult;
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, skirmishResult.getWinner()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
