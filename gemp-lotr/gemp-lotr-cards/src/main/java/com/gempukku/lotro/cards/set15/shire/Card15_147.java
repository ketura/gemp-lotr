package com.gempukku.lotro.cards.set15.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Follower
 * Strength: +1
 * Resistance: +1
 * Game Text: Aid - Add a burden. (At the start of the maneuver phase, you may add a burden to transfer this
 * to a companion.) To play, spot 2 Hobbits. Bearer is strength +1 for each fierce minion you can spot.
 */
public class Card15_147 extends AbstractFollower {
    public Card15_147() {
        super(Side.FREE_PEOPLE, 2, 1, 0, 1, Culture.SHIRE, "Hobbiton Farmer", "Lover of Pipeweed", true);
    }

    @Override
    public boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    public void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self) {
        action.appendCost(new AddBurdenEffect(self.getOwner(), self, 1));
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Race.HOBBIT);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.hasAttached(self), null, new CountActiveEvaluator(CardType.MINION, Keyword.FIERCE)));
    }
}
