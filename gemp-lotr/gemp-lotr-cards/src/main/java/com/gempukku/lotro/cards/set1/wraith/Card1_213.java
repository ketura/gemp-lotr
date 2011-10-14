package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make a Nazgul strength +2 (or +3 if the Ring-bearer is assigned to a skirmish that has not
 * resolved).
 */
public class Card1_213 extends AbstractEvent {
    public Card1_213() {
        super(Side.SHADOW, Culture.WRAITH, "Frozen by Fear", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a Nazgul", Filters.race(Race.NAZGUL)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard nazgul) {
                        boolean notAssigned = Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER), Filters.notAssignedToSkirmish());
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(nazgul), notAssigned ? 2 : 3), Phase.SKIRMISH));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
