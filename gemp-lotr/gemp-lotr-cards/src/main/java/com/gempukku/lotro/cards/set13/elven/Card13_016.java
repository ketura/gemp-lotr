package com.gempukku.lotro.cards.set13.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.OptionalEffect;
import com.gempukku.lotro.logic.effects.ReinforceTokenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.ELF);
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
