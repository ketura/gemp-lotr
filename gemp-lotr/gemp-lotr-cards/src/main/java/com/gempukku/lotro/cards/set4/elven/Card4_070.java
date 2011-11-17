package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
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
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Exert an Elf to make a minion skirmishing that Elf strength -3.
 */
public class Card4_070 extends AbstractOldEvent {
    public Card4_070() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Flashing Steel", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF));
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.ELF)) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        action.appendEffect(
                                new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION, Filters.inSkirmishAgainst(Filters.sameCard(character))) {
                                    @Override
                                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                                        action.insertEffect(
                                                new AddUntilEndOfPhaseModifierEffect(
                                                        new StrengthModifier(self, Filters.sameCard(card), -3), Phase.SKIRMISH));
                                    }
                                });
                    }
                });
        return action;
    }
}
