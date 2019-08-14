package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Saruman, The White Wizard
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion - Wizard
 * Strength: 8
 * Vitality: 4
 * Home: 4
 * Card Number: 1P310
 * Game Text: Skirmish: Exert Saruman and discard a spell stacked on an [ISENGARD] artifact to wound a companion
 * skirmishing Saruman. The Free Peoples player may add a threat to prevent this.
 */
public class Card40_310 extends AbstractMinion {
    public Card40_310() {
        super(4, 8, 4, 4, Race.WIZARD, Culture.ISENGARD, "Saruman",
                "The White Wizard", true);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canDiscardFromStacked(self, game, playerId, 1, Filters.and(Culture.ISENGARD, CardType.ARTIFACT),
                Keyword.SPELL)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, Filters.and(Culture.ISENGARD, CardType.ARTIFACT),
                            Keyword.SPELL));
            action.appendEffect(
                    new PreventableEffect(
                            action,
                            new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION,
                                    Filters.inSkirmishAgainst(self)) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Wound a companion skirmishing Saruman";
                                }
                            },
                            GameUtils.getFreePeoplePlayer(game),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                    return new AddThreatsEffect(playerId, self, 1);
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
