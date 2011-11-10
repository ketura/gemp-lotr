package com.gempukku.lotro.cards.set6.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Spell. Skirmish: Make a minion skirmishing Gandalf strength -1 for each wound on each character
 * in the skirmish.
 */
public class Card6_036 extends AbstractEvent {
    public Card6_036() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Threw Down My Enemy", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose minion", CardType.MINION, Filters.inSkirmishAgainst(Filters.gandalf)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int wounds = 0;
                        for (PhysicalCard character : Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.inSkirmish, Filters.wounded))
                            wounds += game.getGameState().getWounds(character);

                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, -wounds), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
