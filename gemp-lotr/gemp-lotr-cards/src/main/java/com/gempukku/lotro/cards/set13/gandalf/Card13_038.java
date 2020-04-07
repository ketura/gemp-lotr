package com.gempukku.lotro.cards.set13.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.modifiers.CancelStrengthBonusTargetModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Follower
 * Resistance: +2
 * Game Text: Aid - (2). (At the start of the maneuver phase, you may add (2) to transfer this to a companion.) While
 * you can spot 2 [GANDALF] companions, minions skirmishing bearer do not gain strength bonuses from possessions.
 */
public class Card13_038 extends AbstractFollower {
    public Card13_038() {
        super(Side.FREE_PEOPLE, 2, 0, 0, 2, Culture.GANDALF, "Radagast", "Tender of Beasts", true);
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
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new CancelStrengthBonusTargetModifier(self, new SpotCondition(2, Culture.GANDALF, CardType.COMPANION),
                        Filters.and(CardType.MINION, Filters.inSkirmishAgainst(Filters.hasAttached(self))),
                        CardType.POSSESSION));
    }
}
