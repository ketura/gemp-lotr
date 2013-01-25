package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.AddNoTwilightForCompanionMoveModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 0
 * Avoid Being Seen
 * Gondor	Event • Fellowship
 * To play, exert X [Gondor] rangers.
 * Until the end of turn, add no twilight for each exerted ranger when the fellowship moves.
 */
public class Card20_181 extends AbstractEvent {
    public Card20_181() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Avoid Being Seen", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 0, Integer.MAX_VALUE, Culture.GONDOR, Keyword.RANGER){
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
