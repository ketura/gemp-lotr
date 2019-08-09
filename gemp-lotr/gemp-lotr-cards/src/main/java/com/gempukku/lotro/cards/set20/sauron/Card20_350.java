package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountFPCulturesEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.NegativeEvaluator;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * Black Gate Olog-hai
 * Minion • Troll
 * 14	4	6
 * Fierce.
 * This minion is strength -1 for each Free Peoples culture you can spot.
 * Assignment: Exert this minion to assign it to an ally. The Free Peoples player may add a threat to prevent this.
 * http://lotrtcg.org/coreset/sauron/blackgateologhai(r1).png
 */
public class Card20_350 extends AbstractMinion {
    public Card20_350() {
        super(4, 14, 4, 6, Race.TROLL, Culture.SAURON, "Black Gate Olog-hai");
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, null,
new NegativeEvaluator(new CountFPCulturesEvaluator(self.getOwner()))));
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
