package com.gempukku.lotro.cards.set32.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Twilight Cost: 9
 * Type: Site
 * Site: 9
 * Game Text: Mountain. Battleground. Shadow: Exert a Dwarven character for each Shadow culture you can spot
 * (limit once per turn).
 */
public class Card32_056 extends AbstractSite {
    public Card32_056() {
        super("Northern Slopes", SitesBlock.HOBBIT, 9, 9, Direction.RIGHT);
        addKeyword(Keyword.MOUNTAIN);
        addKeyword(Keyword.BATTLEGROUND);
    }
    
    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && game.getModifiersQuerying().getUntilEndOfTurnLimitCounter(self).getUsedLimit() < 1) {
            ActivateCardAction action = new ActivateCardAction(self);
            int shadowCultures = new CountCulturesEvaluator(Side.SHADOW).evaluateExpression(game, self);
            for (int i = 0; i < shadowCultures; i++) {
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1,
                                Filters.and(Culture.DWARVEN, Filters.or(CardType.COMPANION, CardType.ALLY))));
            }
            action.appendEffect(
                    new UnrespondableEffect() {
                @Override
                public void doPlayEffect(LotroGame game) {
                    game.getModifiersQuerying().getUntilEndOfTurnLimitCounter(self).incrementToLimit(1, 1);
                }
            });
            return Collections.singletonList(action);
        }
        return null;
    }
}
