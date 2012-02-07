package com.gempukku.lotro.cards.set15.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Shadow: Exert this minion to exert X companions and add X threats, where X is the number
 * of Free Peoples cultures you spot over 2.
 */
public class Card15_165 extends AbstractMinion {
    public Card15_165() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Merciless Berserker");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            int count = new CountCulturesEvaluator(2, Side.FREE_PEOPLE).evaluateExpression(game.getGameState(), game.getModifiersQuerying(), self);
            if (count > 0)
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, playerId, count, count, CardType.COMPANION));
            action.appendEffect(
                    new AddThreatsEffect(playerId, self, count));
            return Collections.singletonList(action);
        }
        return null;
    }
}
