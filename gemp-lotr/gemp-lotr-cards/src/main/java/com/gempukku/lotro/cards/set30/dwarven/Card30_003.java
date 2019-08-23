package com.gempukku.lotro.cards.set30.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CardMatchesEvaluator;


/**
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a Dwarf strength +3 (or +4 if he bears a [DWARVEN] follower).
 */
public class Card30_003 extends AbstractEvent {
    public Card30_003() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Battle Fury", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
           action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new CardMatchesEvaluator(3, 4, Filters.hasAttached(Filters.and(CardType.FOLLOWER, Culture.DWARVEN))), Race.DWARF, Filters.or(CardType.COMPANION, CardType.ALLY)));
return action;
    }
}
