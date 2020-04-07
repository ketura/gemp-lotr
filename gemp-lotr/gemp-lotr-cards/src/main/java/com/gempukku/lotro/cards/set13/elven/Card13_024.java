package com.gempukku.lotro.cards.set13.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Make an Elf strength +1 for each card that has an [ELVEN] token on it.
 */
public class Card13_024 extends AbstractEvent {
    public Card13_024() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "Spring Forth Nimbly", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, new CountActiveEvaluator(Filters.hasToken(Token.ELVEN)), Race.ELF));
        return action;
    }
}
