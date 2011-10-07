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
 * Game Text: Skirmish: Make an Elf strength +2 (or +3 if skirmishing a wounded minion).
 */
public class Card4_087 extends AbstractEvent {
    public Card4_087() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Valor", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose an Elf", Filters.race(Race.ELF)) {
                    @Override
                    protected void cardSelected(PhysicalCard card) {
                        final boolean againstWoundedMinion = Filters.inSkirmishAgainst(Filters.and(Filters.type(CardType.MINION), Filters.wounded())).accepts(game.getGameState(), game.getModifiersQuerying(), card);
                        int bonus = againstWoundedMinion ? 3 : 2;
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), bonus), Phase.SKIRMISH));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
