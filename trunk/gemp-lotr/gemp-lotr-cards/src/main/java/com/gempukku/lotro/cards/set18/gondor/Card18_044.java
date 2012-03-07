package com.gempukku.lotro.cards.set18.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a ranger strength +2 and you may reinforce a [GONDOR] token.
 */
public class Card18_044 extends AbstractEvent {
    public Card18_044() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "Defenses Long Held", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Keyword.RANGER));
        action.appendEffect(
                new OptionalEffect(action, playerId,
                        new ReinforceTokenEffect(self, playerId, Token.GONDOR)));
        return action;
    }
}
