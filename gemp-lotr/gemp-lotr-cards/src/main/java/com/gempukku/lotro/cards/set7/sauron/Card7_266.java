package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PutCharacterFromPlayInDeadPileEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Event • Regroup
 * Game Text: Remove 4 threats and spot 2 [SAURON] Orcs to make the Free Peoples player spot a companion and place that
 * companion in the dead pile.
 */
public class Card7_266 extends AbstractEvent {
    public Card7_266() {
        super(Side.SHADOW, 1, Culture.SAURON, "Breached", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canRemoveThreat(game, self, 4)
                && PlayConditions.canSpot(game, 2, Culture.SAURON, Race.ORC);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new RemoveThreatsEffect(self, 4));
        action.appendEffect(
                new ChooseActiveCardEffect(self, game.getGameState().getCurrentPlayerId(), "Choose companion to place in the dead pile", CardType.COMPANION) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard card) {
                        action.insertEffect(
                                new PutCharacterFromPlayInDeadPileEffect(card));
                    }
                });
        return action;
    }
}
