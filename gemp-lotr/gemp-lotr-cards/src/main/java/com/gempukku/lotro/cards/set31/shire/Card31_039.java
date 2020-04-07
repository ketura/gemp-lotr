package com.gempukku.lotro.cards.set31.shire;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.RemovePlayedEventFromGameEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: If Bilbo wears The One Ring, wound each minion he is skirmishing. Then remove this
 * event from the game.
 */
public class Card31_039 extends AbstractEvent {
    public Card31_039() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "An Invisible Ring", Phase.SKIRMISH);
    }
    
    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return game.getGameState().isWearingRing();
    }
    
    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new WoundCharactersEffect(self, Filters.inSkirmishAgainst(Filters.name("Bilbo"))));
        action.appendEffect(
                new RemovePlayedEventFromGameEffect(action));
        return action;
    }
}
