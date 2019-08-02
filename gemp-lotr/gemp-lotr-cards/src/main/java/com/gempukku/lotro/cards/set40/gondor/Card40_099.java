package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.AddNoTwilightForCompanionMoveModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Title: Avoid Being Seen
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event - Fellowship
 * Card Number: 1R99
 * Game Text: To play, exert X Rangers. Until the end of turn, add no twilight for each exerted Ranger when the fellowship moves.
 */
public class Card40_099 extends AbstractEvent {
    public Card40_099() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Aboid Being Seen", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 0, Integer.MAX_VALUE, Filters.character, Keyword.RANGER) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        action.appendEffect(
                                new AddUntilEndOfTurnModifierEffect(
                                        new AddNoTwilightForCompanionMoveModifier(self, character)));
                    }
                });
        return action;
    }
}
