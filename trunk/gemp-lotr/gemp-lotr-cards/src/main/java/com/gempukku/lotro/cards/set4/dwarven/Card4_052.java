package com.gempukku.lotro.cards.set4.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Each time Gimli wins a skirmish, place a [DWARVEN] token on this card. While
 * you can spot X [DWARVEN] tokens on this card and the same number of [ELVEN] tokens on Final Count, Gimli
 * is strength +X (limit +3).
 */
public class Card4_052 extends AbstractPermanent {
    public Card4_052() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "My Axe Is Notched", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.gimli, null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
                                PhysicalCard finalCount = Filters.findFirstActive(gameState, modifiersQuerying, Filters.name("Final Count"));
                                if (finalCount != null)
                                    return Math.min(3, Math.min(gameState.getTokenCount(self, Token.DWARVEN), gameState.getTokenCount(finalCount, Token.ELVEN)));
                                return 0;
                            }
                        }));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game, effectResult, Filters.gimli)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.DWARVEN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
