package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Make a roaming minion skirmishing a [GONDOR] Man strength -3.
 */
public class Card7_128 extends AbstractEvent {
    public Card7_128() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "While We Yet Live", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -3, CardType.MINION, Keyword.ROAMING, Filters.inSkirmishAgainst(Culture.GONDOR, Race.MAN)));
        return action;
    }
}
