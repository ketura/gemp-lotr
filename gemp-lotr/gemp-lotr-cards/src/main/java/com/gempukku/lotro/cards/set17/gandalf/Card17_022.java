package com.gempukku.lotro.cards.set17.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Follower
 * Game Text: Aid - (1). (At the start of the maneuver phase, you may add (1) to transfer this to a companion.) To play,
 * spot a [GANDALF] follower. Bearer is strength +1 for each other [GANDALF] follower you can spot.
 */
public class Card17_022 extends AbstractFollower {
    public Card17_022() {
        super(Side.FREE_PEOPLE, 3, 0, 0, 0, Culture.GANDALF, "Meneldor", "Misty Mountain Eagle", true);
    }

    @Override
    public boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    public void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self) {
        action.appendCost(new AddTwilightEffect(self, 1));
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.GANDALF, CardType.FOLLOWER);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.hasAttached(self), null, new CountActiveEvaluator(Filters.not(self), Culture.GANDALF, CardType.FOLLOWER)));
    }
}
