package com.gempukku.lotro.cards.set13.orc;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Make an [ORC] Orc strength +2 (or +6 if you remove 2 burdens).
 */
public class Card13_121 extends AbstractEvent {
    public Card13_121() {
        super(Side.SHADOW, 0, Culture.ORC, "Whatever Means", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final AtomicInteger bonus = new AtomicInteger(2);
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new OptionalEffect(action, playerId,
                        new RemoveBurdenEffect(playerId, self, 2) {
                            @Override
                            protected FullEffectResult playEffectReturningResult(LotroGame game) {
                                final FullEffectResult fullEffectResult = super.playEffectReturningResult(game);
                                if (fullEffectResult.isSuccessful())
                                    bonus.set(6);
                                return fullEffectResult;
                            }
                        }));
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                return bonus.get();
                            }
                        }, Culture.ORC, Race.ORC));
        return action;
    }
}
