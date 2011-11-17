package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.modifiers.SpecialFlagModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Skirmishes are resolved in an order decided by the first Shadow player. Each time a [ROHAN] Man wins
 * a skirmish, place a [ROHAN] token here. Each [ROHAN] Man is strength +1 for each token here. Discard this condition
 * during the regroup phase.
 */
public class Card7_249 extends AbstractPermanent {
    public Card7_249() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Seeking New Foes");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new SpecialFlagModifier(self, ModifierFlag.SKIRMISH_ORDER_BY_FIRST_SHADOW_PLAYER));
        modifiers.add(
                new StrengthModifier(self, Filters.and(Culture.ROHAN, Race.MAN), null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                return gameState.getTokenCount(self, Token.ROHAN);
                            }
                        }));
        return modifiers;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Culture.ROHAN, Race.MAN)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.ROHAN));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
