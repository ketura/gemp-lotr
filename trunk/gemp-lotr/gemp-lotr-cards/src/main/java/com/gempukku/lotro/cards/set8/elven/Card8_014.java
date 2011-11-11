package com.gempukku.lotro.cards.set8.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;

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
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        final PhysicalCard currentSite = game.getGameState().getCurrentSite();
        if (currentSite.getBlueprint().getSiteBlock() == Block.KING
                && currentSite.getBlueprint().getSiteNumber() >= 1 && currentSite.getBlueprint().getSiteNumber() <= 5) {
            action.appendEffect(
                    new HealCharactersEffect(self, Filters.gandalf));
        } else {
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Hobbit", Race.HOBBIT) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new OverwhelmedByMultiplierModifier(self, card, 3), Phase.SKIRMISH));
                        }
                    });
        }
        return action;
    }
}
