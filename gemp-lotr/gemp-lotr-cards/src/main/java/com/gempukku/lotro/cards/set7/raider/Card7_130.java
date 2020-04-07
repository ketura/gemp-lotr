package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.IncrementPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Regroup: Spot 2 [RAIDER] Men and remove (3) to add a threat (limit 1).
 */
public class Card7_130 extends AbstractPermanent {
    public Card7_130() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.RAIDER, "Dark Tidings");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 3)
                && PlayConditions.canSpot(game, 2, Culture.RAIDER, Race.MAN)
                && PlayConditions.checkPhaseLimit(game, self, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new IncrementPhaseLimitEffect(self, 1));
            action.appendCost(
                    new RemoveTwilightEffect(3));
            action.appendEffect(
                    new AddThreatsEffect(playerId, self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
