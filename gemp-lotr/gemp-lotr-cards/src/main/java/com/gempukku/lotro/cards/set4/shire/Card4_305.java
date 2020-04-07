package com.gempukku.lotro.cards.set4.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddTokenEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each time a Hobbit wins a skirmish, you may place a [SHIRE] token here.
 * Fellowship: Remove a burden for each [SHIRE] token here (limit 3). Discard this condition.
 */
public class Card4_305 extends AbstractPermanent {
    public Card4_305() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.SHIRE, "Good Work", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Race.HOBBIT)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.SHIRE));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            int tokenCount = Math.min(3, game.getGameState().getTokenCount(self, Token.SHIRE));
            ActivateCardAction action = new ActivateCardAction(self);
            if (tokenCount > 0)
                action.appendEffect(
                        new RemoveBurdenEffect(playerId, self, tokenCount));
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
