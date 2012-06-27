package com.gempukku.lotro.cards.set13.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: Shadow: Exert this minion, spot another [MEN] minion, and remove a culture token to reinforce
 * a [MEN] token.
 */
public class Card13_090 extends AbstractMinion {
    public Card13_090() {
        super(3, 8, 2, 4, Race.MAN, Culture.MEN, "Easterling Runner");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canSpot(game, Filters.not(self), Culture.MEN, CardType.MINION)
                && PlayConditions.canRemoveAnyCultureTokens(game, 1, Filters.any)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, null, 1, Filters.any));
            action.appendEffect(
                    new ReinforceTokenEffect(self, playerId, Token.MEN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
