package com.gempukku.lotro.cards.set18.gondor;

import com.gempukku.lotro.cards.AbstractFollower;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Follower
 * Strength: +2
 * Game Text: Aid - Exert a [GONDOR] companion. (At the start of the maneuver phase, you may exert a [GONDOR] companion
 * to transfer this to a companion.)
 */
public class Card18_047 extends AbstractFollower {
    public Card18_047() {
        super(Side.FREE_PEOPLE, 0, 2, 0, 0, Culture.GONDOR, "Elendil's Army", true);
    }

    @Override
    protected boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.GONDOR, CardType.COMPANION);
    }

    @Override
    protected Effect getAidCost(LotroGame game, Action action, PhysicalCard self) {
        return new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, Culture.GONDOR, CardType.COMPANION);
    }
}
