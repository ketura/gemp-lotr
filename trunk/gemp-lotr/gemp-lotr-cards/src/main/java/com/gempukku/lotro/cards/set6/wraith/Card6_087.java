package com.gempukku.lotro.cards.set6.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 2
 * Site: 3
 * Game Text: When you play Ulaire Nertea, spot another Nazgul to make the Free Peoples player exert a Ring-bound
 * companion for each Free Peoples culture you can spot over 2.
 */
public class Card6_087 extends AbstractMinion {
    public Card6_087() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, "Ulaire Nertea", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Race.NAZGUL, Filters.not(self))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int count = new CountCulturesEvaluator(2, 1, Side.FREE_PEOPLE).evaluateExpression(game.getGameState(), game.getModifiersQuerying(), null);
            for (int i = 0; i < count; i++)
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION, Keyword.RING_BOUND));
            return Collections.singletonList(action);
        }
        return null;
    }
}
