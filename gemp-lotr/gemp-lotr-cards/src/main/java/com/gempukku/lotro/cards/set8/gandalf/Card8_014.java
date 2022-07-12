package com.gempukku.lotro.cards.set8.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.OverwhelmedByMultiplierModifier;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event • Skirmish
 * Game Text: At sites 1K to 5K, heal Gandalf. At any other site, prevent a Hobbit from being overwhelmed unless his or
 * her strength is tripled.
 */
public class Card8_014 extends AbstractEvent {
    public Card8_014() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "A Fool", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        final PhysicalCard currentSite = game.getGameState().getCurrentSite();
        if (currentSite.getBlueprint().getSiteBlock() == SitesBlock.KING
                && currentSite.getSiteNumber() >= 1 && currentSite.getSiteNumber() <= 5) {
            action.appendEffect(
                    new HealCharactersEffect(self, self.getOwner(), Filters.gandalf));
        } else {
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Hobbit", Race.HOBBIT) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new OverwhelmedByMultiplierModifier(self, card, 3)));
                        }
                    });
        }
        return action;
    }
}
