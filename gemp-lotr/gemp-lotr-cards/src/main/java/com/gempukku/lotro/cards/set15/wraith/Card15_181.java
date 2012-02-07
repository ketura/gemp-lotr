package com.gempukku.lotro.cards.set15.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event • Shadow
 * Game Text: Remove 2 threats to play a Nazgul. That Nazgul is twilight cost -2 and gains hunter 2
 * (While skirmishing a non-hunter character, this character is strength +2.) until the start of the regroup phase.
 */
public class Card15_181 extends AbstractEvent {
    public Card15_181() {
        super(Side.SHADOW, 0, Culture.WRAITH, "Later Than You Think", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canRemoveThreat(game, self, 2)
                && PlayConditions.canPlayFromHand(playerId, game, -2, Race.NAZGUL);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new RemoveThreatsEffect(self, 2));
        action.appendEffect(
                new ChooseAndPlayCardFromHandEffect(playerId, game, -2, Race.NAZGUL) {
                    @Override
                    protected void afterCardPlayed(PhysicalCard cardPlayed) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new KeywordModifier(self, cardPlayed, Keyword.HUNTER, 2), Phase.REGROUP));
                    }
                });
        return action;
    }
}
