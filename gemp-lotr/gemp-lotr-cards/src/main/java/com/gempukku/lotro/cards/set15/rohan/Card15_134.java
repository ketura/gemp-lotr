package com.gempukku.lotro.cards.set15.rohan;

import com.gempukku.lotro.cards.AbstractFollower;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Follower
 * Strength: +1
 * Game Text: Aid - (1). (At the start of the maneuver phase, you may add (1) to transfer this to a companion.)
 * To play, spot a [ROHAN] Man. Bearer is considered to be mounted.
 */
public class Card15_134 extends AbstractFollower {
    public Card15_134() {
        super(Side.FREE_PEOPLE, 2, 1, 0, 0, Culture.ROHAN, "Rohan Stable Master");
    }

    @Override
    protected boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    protected Effect getAidCost(LotroGame game, PhysicalCard self) {
        return new AddTwilightEffect(self, 1);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.MOUNTED));
    }
}
