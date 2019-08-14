package com.gempukku.lotro.cards.set13.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CardMatchesEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Spell. Make a [GANDALF] companion strength +2 (or +4 if that companion bears a follower).
 */
public class Card13_032 extends AbstractEvent {
    public Card13_032() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "For a While Less Dark", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new CardMatchesEvaluator(2, 4, Filters.hasAttached(CardType.FOLLOWER)), Culture.GANDALF, CardType.COMPANION));
        return action;
    }
}
