package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.LocationEvaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Title: We Could Have Shot Him in the Dark
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event - Archery
 * Card Number: 1C62
 * Game Text: Spot 2 Elf archers to make the fellowship archery total +2 (or +3 if at a forest).
 */
public class Card40_062 extends AbstractEvent {
    public Card40_062() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "We Could Have Shot Him in the Dark", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Race.ELF, Keyword.ARCHER);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilEndOfPhaseModifierEffect(
                        new ArcheryTotalModifier(self, Side.FREE_PEOPLE, null,
                                new LocationEvaluator(2, 3, Keyword.FOREST))));
        return action;
    }
}
