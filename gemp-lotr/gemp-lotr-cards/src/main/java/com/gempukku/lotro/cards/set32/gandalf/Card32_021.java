package com.gempukku.lotro.cards.set32.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AllyParticipatesInArcheryFireAndSkirmishesModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event • Maneuver
 * Game Text: Exert an Wise ally to make that ally strength +3 and participate in archery fire
 * and skirmishes until the regroup phase.
 */
public class Card32_021 extends AbstractEvent {
    public Card32_021() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Former Herald", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, CardType.ALLY, Keyword.WISE);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.ALLY, Keyword.WISE) {
            @Override
            public void forEachCardExertedCallback(PhysicalCard wiseAlly) {
                action.appendEffect(
                        new AddUntilStartOfPhaseModifierEffect(
                                new StrengthModifier(self, Filters.sameCard(wiseAlly), 3), Phase.REGROUP));
                action.appendEffect(
                        new AddUntilStartOfPhaseModifierEffect(
                                new AllyParticipatesInArcheryFireAndSkirmishesModifier(self, Filters.sameCard(wiseAlly)), Phase.REGROUP));
            }
        });
        return action;
    }
}
