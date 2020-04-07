package com.gempukku.lotro.cards.set15.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canRemoveThreat(game, self, 2)
                && PlayConditions.canPlayFromHand(self.getOwner(), game, -2, Race.NAZGUL);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
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
