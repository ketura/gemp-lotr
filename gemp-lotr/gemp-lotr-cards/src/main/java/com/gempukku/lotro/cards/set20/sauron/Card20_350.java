package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * Black Gate Olog-hai
 * Sauron	Minion • Troll
 * 13	4	6
 * Fierce.
 * This minion's twilight cost is +1 for each Free Peoples culture you can spot.
 * Assignment: Exert this minion to assign it to skirmish an ally. The Free Peoples player may add a threat to prevent this.
 */
public class Card20_350 extends AbstractMinion {
    public Card20_350() {
        super(4, 13, 4, 6, Race.TROLL, Culture.SAURON, "Black Gate Olog-hai");
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return new CountCulturesEvaluator(Side.FREE_PEOPLE).evaluateExpression(gameState, modifiersQuerying, null);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new PreventableEffect(action,
                            new ChooseAndAssignCharacterToMinionEffect(action, playerId, self, true, CardType.ALLY) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Assign "+ getName()+" to an ally";
                                }
                            }, game.getGameState().getCurrentPlayerId(),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                    return new AddThreatsEffect(playerId, self, 1);
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
