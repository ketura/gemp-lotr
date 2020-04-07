package com.gempukku.lotro.cards.set6.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make a [GONDOR] Man strength +2 (and exert a minion if you spot a [ROHAN] token).
 */
public class Card6_053 extends AbstractEvent {
    public Card6_053() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Mortal Men", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Culture.GONDOR, Race.MAN));
        if (Filters.canSpot(game, Filters.hasToken(Token.ROHAN)))
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.MINION));
        return action;
    }
}
