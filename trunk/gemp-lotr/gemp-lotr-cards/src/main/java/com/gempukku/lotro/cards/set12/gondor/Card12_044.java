package com.gempukku.lotro.cards.set12.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.OpponentsCantUseSpecialAbilitiesModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Make a [GONDOR] Man strength +1. If he or she is skirmishing a roaming minion, your opponents cannot use
 * special abilities.
 */
public class Card12_044 extends AbstractEvent {
    public Card12_044() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Concealment", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a GONDOR Man", Culture.GONDOR, Race.MAN) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, 1), Phase.SKIRMISH));
                        if (Filters.inSkirmishAgainst(Filters.roamingMinion).accepts(game.getGameState(), game.getModifiersQuerying(), card)) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new OpponentsCantUseSpecialAbilitiesModifier(self, playerId), Phase.SKIRMISH));
                        }
                    }
                });
        return action;
    }
}
