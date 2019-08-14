package com.gempukku.lotro.cards.set11.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event • Fellowship
 * Game Text: Exert a [GANDALF] Wizard to remove a burden (or 2 burdens, if the fellowship is at a dwelling site).
 */
public class Card11_029 extends AbstractEvent {
    public Card11_029() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "Ease the Burden", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.GANDALF, Race.WIZARD);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GANDALF, Race.WIZARD));
        int count = PlayConditions.location(game, Keyword.DWELLING) ? 2 : 1;
        action.appendEffect(
                new RemoveBurdenEffect(playerId, self, count));
        return action;
    }
}
