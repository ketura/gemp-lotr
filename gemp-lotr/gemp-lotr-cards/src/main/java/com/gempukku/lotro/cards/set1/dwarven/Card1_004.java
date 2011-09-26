package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.CompositeModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Exert a Dwarf to make that Dwarf strength +3 and damage +1.
 */
public class Card1_004 extends AbstractEvent {
    public Card1_004() {
        super(Side.FREE_PEOPLE, Culture.DWARVEN, "Battle Fury", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.race(Race.DWARF)) {
                    @Override
                    protected void cardsSelected(Collection<PhysicalCard> dwarf, boolean success) {
                        super.cardsSelected(dwarf, success);
                        if (success) {
                            List<Modifier> modifiers = new LinkedList<Modifier>();
                            modifiers.add(new StrengthModifier(null, null, 3));
                            modifiers.add(new KeywordModifier(null, null, Keyword.DAMAGE));

                            action.appendEffect(new CardAffectsCardEffect(self, dwarf));
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new CompositeModifier(self, Filters.in(dwarf), modifiers), Phase.SKIRMISH));
                        }
                    }
                }
        );
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
