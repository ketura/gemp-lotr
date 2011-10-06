package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Exert X Elf companions to make a minion skirmishing an unbound companion strength -X.
 */
public class Card4_059 extends AbstractEvent {
    public Card4_059() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Arrow and Blade", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        final AtomicInteger counter = new AtomicInteger();
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 0, Integer.MAX_VALUE, Filters.type(CardType.COMPANION), Filters.race(Race.ELF)) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        counter.incrementAndGet();
                    }
                });
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose minion skirmishing unbound companion", Filters.type(CardType.MINION), Filters.inSkirmishAgainst(Filters.unboundCompanion())) {
                    @Override
                    protected void cardSelected(PhysicalCard card) {
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), -counter.get()), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
