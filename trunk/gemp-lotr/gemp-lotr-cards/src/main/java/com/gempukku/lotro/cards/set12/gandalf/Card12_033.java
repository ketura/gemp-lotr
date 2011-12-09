package com.gempukku.lotro.cards.set12.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Make a [GANDALF] companion strength +2. Then, if that companion is at a battleground or dwelling site,
 * you may take a [GANDALF] condition from your discard pile into hand.
 */
public class Card12_033 extends AbstractEvent {
    public Card12_033() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "The Terror of His Coming", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a GANDALF companion", Culture.GANDALF, CardType.COMPANION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, 2), Phase.SKIRMISH));
                        if (PlayConditions.location(game, Filters.or(Keyword.BATTLEGROUND, Keyword.DWELLING))) {
                            action.appendEffect(
                                    new OptionalEffect(action, playerId,
                                            new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.GANDALF, CardType.CONDITION) {
                                                @Override
                                                public String getText(LotroGame game) {
                                                    return "Take a GANDALF condition from your discard pile into hand";
                                                }
                                            }));
                        }
                    }
                });
        return action;
    }
}
