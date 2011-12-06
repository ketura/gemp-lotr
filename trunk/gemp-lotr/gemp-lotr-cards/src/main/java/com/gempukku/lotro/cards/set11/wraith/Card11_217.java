package com.gempukku.lotro.cards.set11.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.CorruptRingBearerEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Regroup: Discard your Nazgul from play to add a [WRAITH] token here. The Free Peoples player may return
 * a companion to hand to prevent the placement of that token. When you can spot 4 [WRAITH] tokens on this condition,
 * the Ring-bearer is corrupted.
 */
public class Card11_217 extends AbstractPermanent {
    public Card11_217() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "Shapes Slowly Advancing");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canDiscardFromPlay(self, game, Filters.owner(playerId), Race.NAZGUL)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.owner(playerId), Race.NAZGUL));
            action.appendEffect(
                    new PreventableEffect(action,
                            new AddTokenEffect(self, self, Token.WRAITH) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Add WRAITH token to " + GameUtils.getCardLink(self);
                                }
                            }, game.getGameState().getCurrentPlayerId(),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                    return new ChooseAndReturnCardsToHandEffect(subAction, playerId, 1, 1, CardType.COMPANION, Filters.not(Filters.ringBearer)) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Return a companion to hand";
                                        }
                                    };
                                }
                            }
                    ));
            action.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            if (game.getGameState().getTokenCount(self, Token.WRAITH) >= 4)
                                action.appendEffect(
                                        new CorruptRingBearerEffect());
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
