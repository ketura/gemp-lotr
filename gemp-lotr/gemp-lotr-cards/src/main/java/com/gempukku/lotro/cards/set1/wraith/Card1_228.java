package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If Ring-bearer puts on The One Ring, exert a Nazgul to add 2 burdens.
 */
public class Card1_228 extends AbstractResponseEvent {
    public Card1_228() {
        super(Side.SHADOW, 0, Culture.WRAITH, "The Twilight World");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.PUT_ON_THE_ONE_RING
                && checkPlayRequirements(playerId, game, self, 0, 0, false, false)
                && PlayConditions.canExert(self, game, Race.NAZGUL)) {
            PlayEventAction action = new PlayEventAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.NAZGUL));
            action.appendEffect(
                    new AddBurdenEffect(self.getOwner(), self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
