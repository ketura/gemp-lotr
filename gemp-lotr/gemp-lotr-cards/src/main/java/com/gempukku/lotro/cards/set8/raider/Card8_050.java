package com.gempukku.lotro.cards.set8.raider;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 2
 * Type: Possession • Support Area
 * Game Text: When you play this possession, you may add a [RAIDER] token here. Shadow: Remove X [RAIDER] tokens here
 * to play a corsair from your discard pile; its twilight cost is -X. Discard this possession.
 */
public class Card8_050 extends AbstractPermanent {
    public Card8_050() {
        super(Side.SHADOW, 2, CardType.POSSESSION, Culture.RAIDER, Zone.SUPPORT, "Black Sails of Umbar");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.RAIDER));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)) {
            int tokens = game.getGameState().getTokenCount(self, Token.RAIDER);
            if (PlayConditions.canPlayFromDiscard(playerId, game, -tokens, Keyword.CORSAIR)) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new PlayoutDecisionEffect(playerId,
                                new IntegerAwaitingDecision(1, "How many tokens you wish to remove?", 0, tokens) {
                                    @Override
                                    public void decisionMade(String result) throws DecisionResultInvalidException {
                                        int tokens = getValidatedResult(result);
                                        if (tokens > 0)
                                            action.insertCost(
                                                    new RemoveTokenEffect(self, self, Token.RAIDER, tokens));
                                        action.appendEffect(
                                                new ChooseAndPlayCardFromDiscardEffect(playerId, game, -tokens, Keyword.CORSAIR));
                                        action.appendEffect(
                                                new DiscardCardsFromPlayEffect(self, self));
                                    }
                                }));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
