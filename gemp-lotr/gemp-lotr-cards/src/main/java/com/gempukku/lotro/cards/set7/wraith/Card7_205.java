package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.CorruptRingBearerEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot a Nazgul. Shadow: If you have initiative and there are 3 characters in the dead pile, spot
 * 3 burdens, 3 threats and a Nazgul to corrupt the Ring-bearer.
 */
public class Card7_205 extends AbstractPermanent {
    public Card7_205() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, "Put Forth His Strength");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.NAZGUL);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.hasInitiative(game, Side.SHADOW)
                && game.getGameState().getDeadPile(game.getGameState().getCurrentPlayerId()).size() >= 3
                && PlayConditions.canSpotThreat(game, 3)
                && PlayConditions.canSpotBurdens(game, 3)
                && PlayConditions.canSpot(game, Race.NAZGUL)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new CorruptRingBearerEffect());
            return Collections.singletonList(action);
        }
        return null;
    }
}
