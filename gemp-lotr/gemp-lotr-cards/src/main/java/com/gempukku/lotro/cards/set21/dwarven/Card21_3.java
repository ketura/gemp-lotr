package com.gempukku.lotro.cards.set21.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CardMatchesEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;


/**
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a Dwarf strength +3 (or +4 if he bears a [DWARVEN] follower).
 */
public class Card21_3 extends AbstractEvent {
    public Card21_3() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Battle Fury", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
           action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new CardMatchesEvaluator(3, 4, Filters.hasAttached(CardType.FOLLOWER)), Race.DWARF, Filters.or(CardType.COMPANION, CardType.ALLY)));
return action;
    }
}