package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Gathering Evil
 * Sauron	Condition • Support Area
 * Shadow: Remove a threat and exert a [Sauron] Orc to play a [Sauron] minion from your discard pile; it's twilight
 * cost is -1 for each Free Peoples culture less than 3 that you can spot.
 */
public class Card20_357 extends AbstractPermanent {
    public Card20_357() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "Gathering Evil");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canRemoveThreat(game, self, 1)
                && PlayConditions.canExert(self, game, Culture.SAURON, Race.ORC)
                && PlayConditions.canPlayFromDiscard(playerId, game,
                Math.max(0, GameUtils.getSpottableFPCulturesCount(game.getGameState(), game.getModifiersQuerying(), playerId)-3), Culture.SAURON, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.SAURON, Race.ORC));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Math.max(0, GameUtils.getSpottableFPCulturesCount(game.getGameState(), game.getModifiersQuerying(), playerId)-3),
                            Culture.SAURON, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
