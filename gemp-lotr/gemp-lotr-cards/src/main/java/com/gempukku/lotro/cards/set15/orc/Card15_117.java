package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 6
 * Type: Minion • Troll
 * Strength: 15
 * Vitality: 3
 * Site: 4
 * Game Text: For each companion over 5, this minion is damage +1. Assignment: Exert this minion twice to assign it
 * to a companion (except the Ring-bearer). If you cannot spot 6 companions, the Free Peoples player may exert
 * a companion to prevent this.
 */
public class Card15_117 extends AbstractMinion {
    public Card15_117() {
        super(6, 15, 3, 4, Race.TROLL, Culture.ORC, "Tower Troll", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new KeywordModifier(self, self, null, Keyword.DAMAGE, new CountActiveEvaluator(5, null, CardType.COMPANION));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSelfExert(self, 2, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose companion", CardType.COMPANION, Filters.not(Filters.ringBearer), Filters.assignableToSkirmishAgainst(Side.SHADOW, self)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard companion) {
                            if (PlayConditions.canSpot(game, 6, CardType.COMPANION))
                                action.appendEffect(
                                        new AssignmentEffect(playerId, companion, self));
                            else
                                action.appendEffect(
                                        new PreventableEffect(action,
                                                new AssignmentEffect(playerId, companion, self), game.getGameState().getCurrentPlayerId(),
                                                new PreventableEffect.PreventionCost() {
                                                    @Override
                                                    public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                                        return new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION);
                                                    }
                                                }));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
