package com.gempukku.lotro.cards.set4.elven;

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
 * Set: The Two Towers
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make a minion skirmishing an Elf strength -2 for each wound on that minion.
 */
public class Card4_066 extends AbstractEvent {
    public Card4_066() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Feathered", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a minion", Filters.type(CardType.MINION), Filters.inSkirmishAgainst(Filters.race(Race.ELF))) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int wounds = game.getGameState().getWounds(card);
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), -2 * wounds), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
