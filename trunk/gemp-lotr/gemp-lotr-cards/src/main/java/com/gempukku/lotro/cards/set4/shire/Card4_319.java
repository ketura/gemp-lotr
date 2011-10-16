package com.gempukku.lotro.cards.set4.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.cards.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Stealth. Skirmish: At sites 1T to 4T, cancel a skirmish involving a Hobbit. At any other site, prevent
 * a Hobbit from being overwhelmed unless his or her strength is tripled.
 */
public class Card4_319 extends AbstractEvent {
    public Card4_319() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "Severed His Bonds", Phase.SKIRMISH);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        boolean firstOption = (game.getGameState().getCurrentSiteNumber() <= 4 && game.getGameState().getCurrentSiteBlock() == Block.TWO_TOWERS);
        if (firstOption) {
            action.appendEffect(
                    new CancelSkirmishEffect(Race.HOBBIT));
        } else {
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Hobbit", Race.HOBBIT) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new OverwhelmedByMultiplierModifier(self, Filters.sameCard(card), 3), Phase.SKIRMISH));
                        }
                    });
        }
        return action;
    }
}
