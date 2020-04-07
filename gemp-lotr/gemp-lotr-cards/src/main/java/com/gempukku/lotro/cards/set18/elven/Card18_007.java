package com.gempukku.lotro.cards.set18.elven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Follower
 * Strength: +2
 * Game Text: Aid - (2). (At the start of the maneuver phase, you may add (2) to transfer this to a companion.) To play,
 * spot 2 [ELVEN] companions. When you play Celebring, you may play an [ELVEN] artifact from your deck.
 */
public class Card18_007 extends AbstractFollower {
    public Card18_007() {
        super(Side.FREE_PEOPLE, 3, 2, 0, 0, Culture.ELVEN, "Celebring", "Elven-smith", true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Culture.ELVEN, CardType.COMPANION);
    }

    @Override
    public boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    public void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self) {
        action.appendCost(new AddTwilightEffect(self, 2));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Culture.ELVEN, CardType.ARTIFACT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
