package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Orthanc Breeder
 * Isengard	Minion • Orc
 * 5	3	4
 * Lurker.
 * Shadow: Exert this minion and play a Uruk-hai to add (2).
 */
public class Card20_213 extends AbstractMinion {
    public Card20_213() {
        super(2, 5, 3, 4, Race.ORC, Culture.ISENGARD, "Orthanc Breeder");
        addKeyword(Keyword.LURKER);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canPlayFromHand(playerId, game, Race.URUK_HAI)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, Race.URUK_HAI));
            action.appendEffect(
                    new AddTwilightEffect(self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
