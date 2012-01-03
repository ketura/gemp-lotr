package com.gempukku.lotro.cards.set13.gandalf;

import com.gempukku.lotro.cards.AbstractFollower;
import com.gempukku.lotro.cards.modifiers.CancelStrengthBonusModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Effect;

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
        super(Side.FREE_PEOPLE, 2, 0, 0, 2, Culture.GANDALF, "Radagast", true);
    }

    @Override
    protected boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    protected Effect getAidCost(LotroGame game, PhysicalCard self) {
        return new AddTwilightEffect(self, 2);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new CancelStrengthBonusModifier(self, new SpotCondition(2, Culture.GANDALF, CardType.COMPANION),
                        Filters.and(CardType.POSSESSION, Filters.attachedTo(CardType.MINION, Filters.inSkirmishAgainst(Filters.hasAttached(self))))));
    }
}
