package com.gempukku.lotro.cards.set13.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event • Maneuver
 * Game Text: Exert an Elf to take an [ELVEN] skirmish event into hand from your discard pile and you may reinforce
 * an [ELVEN] token.
 */
public class Card13_016 extends AbstractEvent {
    public Card13_016() {
        super(Side.FREE_PEOPLE, 0, Culture.ELVEN, "Inside a Song", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Race.ELF);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF));
        action.appendEffect(
                new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.ELVEN, CardType.EVENT, Keyword.SKIRMISH));
        action.appendEffect(
                new OptionalEffect(action, playerId,
                        new ReinforceTokenEffect(self, playerId, Token.ELVEN)));
        return action;
    }
}
