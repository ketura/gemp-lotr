package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Shadow: Spot a Nazgul and remove a threat to play a Nazgul. Its twilight cost is -2.
 */
public class Card7_179 extends AbstractPermanent {
    public Card7_179() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "Ghastly Host");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSpot(game, self, Race.NAZGUL)
                && PlayConditions.canRemoveThreat(game, self, 1)
                && PlayConditions.canPlayFromHand(playerId, game, -2, Race.NAZGUL)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, -2, Race.NAZGUL));
            return Collections.singletonList(action);
        }
        return null;
    }
}
