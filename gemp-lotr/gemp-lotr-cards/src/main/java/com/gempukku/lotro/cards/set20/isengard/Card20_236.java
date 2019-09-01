package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * •Saruman, The White Wizard
 * Minion • Wizard
 * 8	4	4
 * Skirmish: Exert Saruman and discard a spell stacked on an artifact to wound a companion skirmishing Saruman.
 * The Free Peoples Player may add a threat to prevent this.
 * http://lotrtcg.org/coreset/isengard/sarumantww(r1).png
 */
public class Card20_236 extends AbstractMinion {
    public Card20_236() {
        super(4, 8, 4, 4, Race.WIZARD, Culture.ISENGARD, "Saruman", "The White Wizard", true);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.isActive(game, CardType.ARTIFACT, Filters.hasStacked(Keyword.SPELL))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, CardType.ARTIFACT, Keyword.SPELL));
            action.appendEffect(
                    new PreventableEffect(action,
                            new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.inSkirmishAgainst(self)) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Wound companion skirmishing Saruman";
                                }
                            }, game.getGameState().getCurrentPlayerId(),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                    return new AddThreatsEffect(playerId, self, 1);
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
