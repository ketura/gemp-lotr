package com.gempukku.lotro.cards.set4.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Regroup: Heal an unbound Hobbit once (or twice if you spot Gandalf).
 */
public class Card4_320 extends AbstractEvent {
    public Card4_320() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "Store-room", Phase.REGROUP);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        boolean spotGandalf = Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"));
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndHealCharactersEffect(action, playerId, 1, 1, spotGandalf ? 2 : 1, Race.HOBBIT, Filters.unboundCompanion));
        return action;
    }
}
