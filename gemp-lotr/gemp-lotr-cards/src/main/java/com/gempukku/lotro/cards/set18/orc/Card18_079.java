package com.gempukku.lotro.cards.set18.orc;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.OptionalEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Event • Archery
 * Game Text: Spot an [ORC] archer minion to add 2 to the minion archery total. If you do, you may add an additional 2
 * to the minion archery total for each follower you can spot.
 */
public class Card18_079 extends AbstractEvent {
    public Card18_079() {
        super(Side.SHADOW, 2, Culture.ORC, "Frenzy of Arrows", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.ORC, CardType.MINION, Keyword.ARCHER);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilEndOfPhaseModifierEffect(
                        new ArcheryTotalModifier(self, Side.SHADOW, 2)));
        final int count = Filters.countActive(game, CardType.FOLLOWER);
        if (count > 0)
            action.appendEffect(
                    new OptionalEffect(action, playerId,
                            new AddUntilEndOfPhaseModifierEffect(
                                    new ArcheryTotalModifier(self, Side.SHADOW, count * 2)) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Add additional " + (count * 2) + " to the minion archery total";
                                }
                            }));
        return action;
    }
}
