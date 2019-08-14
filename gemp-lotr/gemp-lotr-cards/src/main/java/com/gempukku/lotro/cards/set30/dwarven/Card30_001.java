package com.gempukku.lotro.cards.set30.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Event • Maneuver
 * Game Text: Exert a Dwarf character to discard a condition (or 2 conditions if you
 * can spot 4 Shadow conditions).
 */
public class Card30_001 extends AbstractEvent {
    public Card30_001() {
        super(Side.FREE_PEOPLE, 2, Culture.DWARVEN, "Ancestral Knowledge", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.DWARF, Filters.character);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        int count = PlayConditions.canSpot(game, 4, Side.SHADOW, CardType.CONDITION) ? 2 : 1;
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.DWARF, Filters.character));
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, count, count, CardType.CONDITION));
        return action;
    }
}
