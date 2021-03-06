package com.gempukku.lotro.cards.set5.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CardMatchesEvaluator;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make a [GONDOR] Man strength +2 (or +4 if skirmishing a minion bearing a [GONDOR] fortification).
 */
public class Card5_043 extends AbstractEvent {
    public Card5_043() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "War Must Be", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new CardMatchesEvaluator(2, 4, Filters.inSkirmishAgainst(CardType.MINION, Filters.hasAttached(Culture.GONDOR, Keyword.FORTIFICATION))), Culture.GONDOR, Race.MAN));
        return action;
    }
}
