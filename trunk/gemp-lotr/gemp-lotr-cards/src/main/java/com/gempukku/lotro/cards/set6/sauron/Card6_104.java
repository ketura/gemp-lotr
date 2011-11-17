package com.gempukku.lotro.cards.set6.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 11
 * Vitality: 3
 * Site: 6
 * Game Text: Shadow: Exert this minion to draw X cards and add (X), where X is the number of Free Peoples cultures
 * you spot over 2.
 */
public class Card6_104 extends AbstractMinion {
    public Card6_104() {
        super(4, 11, 3, 6, Race.ORC, Culture.SAURON, "Orc Insurgent", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            int x = new CountCulturesEvaluator(2, 1, Side.FREE_PEOPLE).evaluateExpression(game.getGameState(), game.getModifiersQuerying(), null);
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new DrawCardEffect(playerId, x));
            action.appendEffect(
                    new AddTwilightEffect(self, x));
            return Collections.singletonList(action);
        }
        return null;
    }
}
