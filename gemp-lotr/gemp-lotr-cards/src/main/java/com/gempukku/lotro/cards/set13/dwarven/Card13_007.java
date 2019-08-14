package com.gempukku.lotro.cards.set13.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event • Maneuver
 * Game Text: Remove a [DWARVEN] token to heal a Dwarf.
 */
public class Card13_007 extends AbstractEvent {
    public Card13_007() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Sorrow Shared", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canRemoveTokens(game, Token.DWARVEN, 1, Filters.any);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.DWARVEN, 1, Filters.any));
        action.appendEffect(
                new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Race.DWARF));
        return action;
    }
}
