package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a GONDOR companion", Culture.GONDOR, CardType.COMPANION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                                new StrengthModifier(self, card, 2), Phase.SKIRMISH);
                        if (PlayConditions.canSpot(game, 2, Culture.GONDOR, Keyword.FORTIFICATION)) {
                            action.appendEffect(
                                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.inSkirmishAgainst(card)));
                        }
                    }
                });
        return action;
    }
}
