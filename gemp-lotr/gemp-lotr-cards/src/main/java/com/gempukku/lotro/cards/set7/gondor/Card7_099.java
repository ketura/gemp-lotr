package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a [GONDOR] companion strength +2 and, if you can spot 2 [GONDOR] fortifications, exert a minion
 * that companion is skirmishing.
 */
public class Card7_099 extends AbstractEvent {
    public Card7_099() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "Great Gate", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a GONDOR companion", Culture.GONDOR, CardType.COMPANION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                                new StrengthModifier(self, card, 2), Phase.SKIRMISH);
                        if (Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Culture.GONDOR, Keyword.FORTIFICATION) >= 2) {
                            action.appendEffect(
                                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.inSkirmishAgainst(card)));
                        }
                    }
                });
        return action;
    }
}
