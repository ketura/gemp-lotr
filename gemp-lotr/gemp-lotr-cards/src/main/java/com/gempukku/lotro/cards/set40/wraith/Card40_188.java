package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.CorruptRingBearerEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.TakeOffTheOneRingEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Title: Its Master's Call
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 8
 * Type: Event - Maneuver
 * Card Number: 1R188
 * Game Text: The twilight cost of this event is -1 for each burden you can spot.
 * To play, spot a Nazgul and the Ring-bearer wearing The One Ring.
 * Corrupt the Ring-bearer; the Free Peoples player may add 3 burden to prevent this and take off The One Ring.
 */
public class Card40_188 extends AbstractEvent {
    public Card40_188() {
        super(Side.SHADOW, 8, Culture.WRAITH, "Its Master's Call", Phase.MANEUVER);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        return -game.getGameState().getBurdens();
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.NAZGUL)
                && game.getGameState().isWearingRing();
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, final LotroGame game, final PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PreventableEffect(action,
                        new CorruptRingBearerEffect(),
                        GameUtils.getFreePeoplePlayer(game),
                        new PreventableEffect.PreventionCost() {
                            @Override
                            public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                return new AddBurdenEffect(GameUtils.getFreePeoplePlayer(game), self, 3);
                            }
                        }));
        action.appendEffect(new TakeOffTheOneRingEffect());
        return action;
    }
}
