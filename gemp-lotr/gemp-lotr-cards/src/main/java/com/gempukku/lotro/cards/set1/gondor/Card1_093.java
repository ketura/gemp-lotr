package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Exert Aragorn to make Arwen strength +3, or exert Arwen to make Aragorn strength +3.
 */
public class Card1_093 extends AbstractEvent {
    public Card1_093() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Arwen's Fate", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.name("Aragorn"), Filters.name("Arwen")));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);

        List<Effect> possibleEffects = new LinkedList<Effect>();

        final PhysicalCard arwen = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Arwern"));
        final PhysicalCard aragorn = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Aragorn"));

        if (arwen != null) {
            action.addCost(
                    new ExertCharacterEffect(arwen) {
                        @Override
                        public void playEffect(LotroGame game) {
                            super.playEffect(game);
                            action.addEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.name("Aragorn"), 3), Phase.SKIRMISH));
                        }
                    });
        }
        if (aragorn != null) {
            action.addCost(
                    new ExertCharacterEffect(aragorn) {
                        @Override
                        public void playEffect(LotroGame game) {
                            super.playEffect(game);
                            action.addEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.name("Arwen"), 3), Phase.SKIRMISH));
                        }
                    });
        }

        action.addCost(
                new ChoiceEffect(action, playerId, possibleEffects, true));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
