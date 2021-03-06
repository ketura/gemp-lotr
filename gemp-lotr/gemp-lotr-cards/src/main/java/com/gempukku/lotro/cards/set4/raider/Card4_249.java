package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 10
 * Vitality: 3
 * Site: 4
 * Game Text: Southron. Ambush (1). Assignment: Spot 6 companions to assign this minion to the Ring-bearer. The Free
 * Peoples player may discard an unbound companion to prevent this.
 */
public class Card4_249 extends AbstractMinion {
    public Card4_249() {
        super(5, 10, 3, 4, Race.MAN, Culture.RAIDER, "Southron Commander");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.AMBUSH, 1);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSpot(game, 6, CardType.COMPANION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose Ring-bearer", Filters.ringBearer) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(new PreventableEffect(action,
                                    new AssignmentEffect(playerId, card, self),
                                    game.getGameState().getCurrentPlayerId(),
                                    new PreventableEffect.PreventionCost() {
                                        @Override
                                        public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                            return new ChooseAndDiscardCardsFromPlayEffect(subAction, playerId, 1, 1, Filters.unboundCompanion) {
                                                @Override
                                                public String getText(LotroGame game) {
                                                    return "Discard an unbound companion";
                                                }
                                            };
                                        }
                                    }
                            ));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
