package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

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
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Valor", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose an Elf", Race.ELF) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        final boolean againstWoundedMinion = Filters.inSkirmishAgainst(Filters.and(CardType.MINION, Filters.wounded)).accepts(game, card);
                        int bonus = againstWoundedMinion ? 3 : 2;
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), bonus)));
                    }
                });
        return action;
    }
}
