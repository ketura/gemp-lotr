package com.gempukku.lotro.cards.set18.orc;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

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
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ORC, CardType.MINION, Keyword.ARCHER);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilEndOfPhaseModifierEffect(
                        new ArcheryTotalModifier(self, Side.SHADOW, 2), Phase.ARCHERY));
        final int count = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.FOLLOWER);
        if (count > 0)
            action.appendEffect(
                    new OptionalEffect(action, playerId,
                            new AddUntilEndOfPhaseModifierEffect(
                                    new ArcheryTotalModifier(self, Side.SHADOW, count * 2), Phase.ARCHERY) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Add additional " + (count * 2) + " to the minion archery total";
                                }
                            }));
        return action;
    }
}
