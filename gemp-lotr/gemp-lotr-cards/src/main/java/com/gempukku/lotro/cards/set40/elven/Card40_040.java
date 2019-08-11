package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

/**
 * Title: Crippling Arrows
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event - Maneuver
 * Card Number: 1C40
 * Game Text: To play, spot an Elf. Each wounded minion is prevented from being fierce until the regroup phase.
 */
public class Card40_040 extends AbstractEvent {
    public Card40_040() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "Crippling Arrows", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new RemoveKeywordModifier(self, Filters.and(CardType.MINION, Filters.wounded), Keyword.FIERCE), Phase.REGROUP));
                    }
                });
        return action;
    }
}
