package com.gempukku.lotro.cards.set7.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Make Gandalf strength +2 (or +4 and damage +1 if you have initiative).
 */
public class Card7_031 extends AbstractEvent {
    public Card7_031() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "All Save One", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose Gandalf", Filters.gandalf) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        if (PlayConditions.hasInitiative(game, Side.FREE_PEOPLE)) {
                            game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                                    new StrengthModifier(self, card, 4), Phase.SKIRMISH);
                            game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                                    new KeywordModifier(self, card, Keyword.DAMAGE, 1), Phase.SKIRMISH);
                        } else {
                            game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                                    new StrengthModifier(self, card, 2), Phase.SKIRMISH);
                        }
                    }
                });
        return action;
    }
}
