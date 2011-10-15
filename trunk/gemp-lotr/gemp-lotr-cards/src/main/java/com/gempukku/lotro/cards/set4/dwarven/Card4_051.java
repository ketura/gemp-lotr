package com.gempukku.lotro.cards.set4.dwarven;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make a Dwarf strength +2 (or strength +3 and damage +1 if you spot Legolas).
 */
public class Card4_051 extends AbstractOldEvent {
    public Card4_051() {
        super(Side.FREE_PEOPLE, Culture.DWARVEN, "Khazad Ai-menu", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a Dwarf", Filters.race(Race.DWARF)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        boolean spotsLegolas = Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Legolas"));
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), spotsLegolas ? 3 : 2), Phase.SKIRMISH));
                        if (spotsLegolas)
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, Filters.sameCard(card), Keyword.DAMAGE), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
