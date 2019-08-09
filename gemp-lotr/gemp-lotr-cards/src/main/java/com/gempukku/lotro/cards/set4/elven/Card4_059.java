package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

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
        super(Side.FREE_PEOPLE, 0, Culture.ELVEN, "Arrow and Blade", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        final AtomicInteger counter = new AtomicInteger();
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 0, Integer.MAX_VALUE, CardType.COMPANION, Race.ELF) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        counter.incrementAndGet();
                    }
                });
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose minion skirmishing unbound companion", CardType.MINION, Filters.inSkirmishAgainst(Filters.unboundCompanion)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), -counter.get())));
                    }
                });
        return action;
    }
}
