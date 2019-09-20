package com.gempukku.lotro.cards.set32.moria;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Minion • Creature
 * Strength: 4
 * Vitality: 2
 * Site: 4
 * Game Text: Shadow: Exert this minion to draw 3 cards. If you cannot spot 3 Orcs, the Free Peoples
 * player may add 2 doubts to prevent this.
 */
public class Card32_030 extends AbstractMinion {
    public Card32_030() {
        super(1, 4, 2, 4, Race.CREATURE, Culture.MORIA, "Dark Bats");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfExertEffect(action, self));
            if (PlayConditions.canSpot(game, 3, Race.ORC)) {
                action.appendEffect(new DrawCardsEffect(action, playerId, 3));
            } else {
                action.appendEffect(
                        new PreventableEffect(action,
                                new DrawCardsEffect(action, playerId, 3),
                                game.getGameState().getCurrentPlayerId(),
                                new PreventableEffect.PreventionCost() {
                                    @Override
                                    public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                        return new AddBurdenEffect(game.getGameState().getCurrentPlayerId(), self, 2);
                                    }
                                }));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
