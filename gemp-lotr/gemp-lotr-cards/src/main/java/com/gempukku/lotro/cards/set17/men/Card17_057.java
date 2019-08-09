package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 2
 * Type: Minion • Man
 * Strength: 6
 * Vitality: 1
 * Site: 4
 * Game Text: This minion is strength +1 for each [MEN] minion stacked on each [MEN] possession. At the start of
 * the maneuver phase, you may play a minion stacked on a [MEN] possession as if from hand.
 */
public class Card17_057 extends AbstractMinion {
    public Card17_057() {
        super(2, 6, 1, 4, Race.MAN, Culture.MEN, "Sunland Skirmisher");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new StrengthModifier(self, self, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                        int count = 0;
                        for (PhysicalCard possession : Filters.filterActive(game, Culture.MEN, CardType.POSSESSION, Filters.hasStacked(Culture.MEN, CardType.MINION))) {
                            count += Filters.filter(game.getGameState().getStackedCards(possession), game, Culture.MEN, CardType.MINION).size();
                        }
                        return count;
                    }
                }));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, Filters.and(Culture.MEN, CardType.POSSESSION), CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
