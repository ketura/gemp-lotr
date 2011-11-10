package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.PutRandomCardFromHandOnBottomOfDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a [ROHAN] Man strength +2. If that Man is mounted, choose an opponent who must place a random card
 * from hand beneath his or her draw deck.
 */
public class Card7_259 extends AbstractEvent {
    public Card7_259() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Wind in His Face", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a ROHAN Man", Culture.ROHAN, Race.MAN) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, 2), Phase.SKIRMISH));
                        boolean mounted = Filters.mounted.accepts(game.getGameState(), game.getModifiersQuerying(), card);
                        if (mounted)
                            action.appendEffect(
                                    new ChooseOpponentEffect(playerId) {
                                        @Override
                                        protected void opponentChosen(String opponentId) {
                                            action.appendEffect(
                                                    new PutRandomCardFromHandOnBottomOfDeckEffect(opponentId));
                                        }
                                    });
                    }
                });
        return action;
    }
}
